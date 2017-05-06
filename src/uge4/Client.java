package uge4;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Christoffer on 2017-05-06.
 * uge4
 */
public class Client {
    public static void main(String[] args) {
        String servername = args[0];
        int portNumber = Integer.parseInt(args[1]);

        System.out.println("Client starting, connecting to " + servername + ":" + portNumber);
        Client client = new Client();
        Socket socket = client.connectToServer(servername, portNumber);

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Choose random number and send g^a mod p + public key
            // Receive servers g^b mod p + public key
            // Compute common key
            // Sign all messages seen so far, and send signature to Server
            // Receive signature from server
            // Check signature
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connects to the server on IP address serverName and port number portNumber.
     */
    protected Socket connectToServer(String serverName, int portNumber) {
        Socket res = null;
        try {
            res = new Socket(serverName, portNumber);
        } catch (IOException e) {
            // We return null on IOExceptions
        }
        return res;
    }
}
