package uge4;

import uge1.Modulo;
import uge2.Signature;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Based on code from dDist.
 * Start the server first, then connect by running the client with the IP of the server to connect to.
 *
 * @author Christoffer
 * @since 2017-05-06
 *
 */
public class Server extends Base {
    public static void main(String[] args) {
        System.out.println("Server starting");

        Server server = new Server();
        server.printLocalHostAddress();
        server.registerOnPort();
        try {
            server.run(); // Loops until something goes wrong
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        server.deregisterOnPort();

    }

    private int portNumber = 4321;
    private ServerSocket serverSocket;
    private BigInteger b;

    private void run() throws IOException, ClassNotFoundException {
        generatePublicKey();

        while (true) {
            Socket socket = waitForConnectionFromClient();

            if (socket != null) {

                System.out.println("Connection from " + socket);
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                // Receive g^a mod p + public key from client
                BigInteger fromClient = (BigInteger) objectInputStream.readObject();
                PublicKey clientPublicKey = (PublicKey) objectInputStream.readObject();

                // Choose random number and send g^a mod p + public key
                sendInitialMessage(objectOutputStream);

                // Compute common key
                key = fromClient.modPow(b, KeyExchangeCommons.P);

                // Receive clients signature of messages
                BigInteger clientSignature = (BigInteger) objectInputStream.readObject();

                // Check signature
                BigInteger messagesSoFar = fromClient.add(clientPublicKey.n.add(clientPublicKey.e)).add(toSend).add(publicKey.n.add(publicKey.e));
                if (!verifyClientSignature(messagesSoFar, clientSignature, clientPublicKey)) {
                    throw new RuntimeException("Client signature could not be verified! Aborting");
                }

                // Sign all messages seen so far, and send signature to Client
                BigInteger signature = Signature.createSignature(messagesSoFar.add(clientSignature), keyPair);
                objectOutputStream.writeObject(signature);

                // Ready to receive stuff from the Client
                System.out.println("Everything went well, common key is " + key);

            } else {
                break;
            }
        }
    }

    private boolean verifyClientSignature(BigInteger messagesSoFar, BigInteger clientSignature, PublicKey clientPublicKey) {
        Modulo serverKey = new Modulo();
        serverKey.n = clientPublicKey.n;
        return Signature.verifySignature(messagesSoFar, clientSignature, serverKey);
    }

    private void sendInitialMessage(ObjectOutputStream objectOutputStream) throws IOException {
        b = KeyExchangeCommons.randomNumber();
        toSend = KeyExchangeCommons.G.modPow(b, KeyExchangeCommons.P);
        objectOutputStream.writeObject(toSend);
        objectOutputStream.writeObject(publicKey);
    }

    /**
     * Will print out the IP address of the local host and the port on which this
     * server is accepting connections.
     */
    private void printLocalHostAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String localhostAddress = localhost.getHostAddress();
            System.out.println("Contact this server on the IP address " + localhostAddress);
            System.out.println("Port number: " + portNumber);
        } catch (UnknownHostException e) {
            System.err.println("Cannot resolve the Internet address of the local host.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Will register this server on the port number portNumber. Will not start waiting
     * for connections. For this you should call waitForConnectionFromClient().
     */
    private void registerOnPort() {
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            serverSocket = null;
            System.err.println("Cannot open server socket on port number" + portNumber);
            System.err.println(e);
            System.exit(-1);
        }
    }

    public void deregisterOnPort() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /**
     * Waits for the next client to connect on port number portNumber or takes the
     * next one in line in case a client is already trying to connect. Returns the
     * socket of the connection, null if there were any failures.
     */
    protected Socket waitForConnectionFromClient() {
        Socket res = null;
        try {
            res = serverSocket.accept();
        } catch (IOException e) {
            // We return null on IOExceptions
        }
        return res;
    }

}
