package uge4;

import uge1.Modulo;
import uge2.Signature;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;

/**
 * Based on code from dDist.
 * Start the server first, then connect by running the client with the IP of the server to connect to.
 *
 * @author Christoffer
 * @since 2017-05-06
 *
 */
public class Client extends Base {
    public static void main(String[] args) {
        String servername = args[0];
        int portNumber = Integer.parseInt(args[1]);

        System.out.println("Client starting, connecting to " + servername + ":" + portNumber);
        Client client = new Client();
        Socket socket = client.connectToServer(servername, portNumber);

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Generate public+private key
            client.generatePublicKey();

            // Choose random number a and send g^a mod p + public key
            client.chooseA();
            client.sendInitialMessage(objectOutputStream);

            // Receive servers g^b mod p + public key
            BigInteger toReceive = (BigInteger) objectInputStream.readObject();
            PublicKey serverPublicKey = (PublicKey) objectInputStream.readObject();

            // Compute common key (g^b mod p)^a mod p based on the number from the server
            client.computeKey(toReceive);

            // Sign all messages seen so far, and send signature to Server
            BigInteger clientSignature = client.createSignature(toReceive, serverPublicKey);
            objectOutputStream.writeObject(clientSignature);

            // Receive signature from server
            BigInteger serverSignature = (BigInteger) objectInputStream.readObject();

            // Check signature
            if (!client.verifyServerSignature(clientSignature, serverSignature, serverPublicKey)) {
                throw new RuntimeException("Server signature could not be verified! Aborting");
            }

            System.out.println("Everything went well, common key is " + client.getKey());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private BigInteger a;

    /**
     * Verifies the signature from the server of the messages seen so far.
     * @param clientSignature
     * @param serverSignature
     * @param serverPublicKey
     * @return accept
     */
    private boolean verifyServerSignature(BigInteger clientSignature, BigInteger serverSignature, PublicKey serverPublicKey) {
        Modulo serverKey = new Modulo();
        serverKey.n = serverPublicKey.n;
        return Signature.verifySignature(messageSoFar.add(clientSignature), serverSignature, serverKey);
    }

    private void sendInitialMessage(ObjectOutputStream objectOutputStream) throws IOException {
        toSend = KeyExchangeCommons.G.modPow(a, KeyExchangeCommons.P);
        objectOutputStream.writeObject(toSend);
        objectOutputStream.writeObject(publicKey);
    }

    /**
     * Compute the common key
     * @param fromServer g^b mod p from server
     */
    private void computeKey(BigInteger fromServer) {
        key = fromServer.modPow(a, KeyExchangeCommons.P);
    }

    /**
     * Creates a signature of the messages seen so far.
     * We simply add all the seen messages for now.
     *
     * @param toReceive g^b mod p (from server)
     * @param serverPublicKey the servers public key
     * @return The signature of the hash of the message
     */
    private BigInteger createSignature(BigInteger toReceive, PublicKey serverPublicKey) {
        messageSoFar = toSend.add(publicKey.n.add(publicKey.e)).add(toReceive).add(serverPublicKey.n.add(serverPublicKey.e));
        return Signature.createSignature(messageSoFar, keyPair);
    }

    /**
     * Choose a random value A
     */
    private void chooseA() {
        a = KeyExchangeCommons.randomNumber();
    }

    /**
     * Connects to the server on IP address serverName and port number portNumber.
     */
    private Socket connectToServer(String serverName, int portNumber) {
        Socket res = null;
        try {
            res = new Socket(serverName, portNumber);
        } catch (IOException e) {
            // We return null on IOExceptions
        }
        return res;
    }

    public BigInteger getKey() {
        return key;
    }
}
