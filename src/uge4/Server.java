package uge4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class Server {
    public static void main(String[] args) {
        System.out.println("Server starting");

        Server server = new Server();
        server.printLocalHostAddress();
        server.registerOnPort();
        server.run(); // Loops until something goes wrong
        server.deregisterOnPort();

    }

    private int portNumber = 4321;
    private ServerSocket serverSocket;

    private void run() {
        while (true) {
            Socket socket = waitForConnectionFromClient();

            if (socket != null) {

                System.out.println("Connection from " + socket);
                try {
                    BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String s;
                    // Read and print what the client is sending
                    while ((s = fromClient.readLine()) != null) { // Ctrl-D terminates the connection
                        System.out.println("From the client: " + s);
                    }
                    socket.close();
                } catch (IOException e) {
                    // We report but otherwise ignore IOExceptions
                    e.printStackTrace();
                }
                System.out.println("Connection closed by client.");
            } else {
                break;
            }
        }
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
