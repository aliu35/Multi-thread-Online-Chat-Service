package server;

import service.RSA;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class for server.
 */
public class Server {

    /** Socket for Server. */
    private ServerSocket server;

    /** Socket for Client. */
    private Socket[] clients = new Socket[20];

    /** RSA encryption. */
    private RSA encryption;

    /** Input Stream. */
    private DataInputStream[] in = new DataInputStream[20];

    /** Output Stream. */
    private DataOutputStream[] out = new DataOutputStream[20];

    private int numClients = 0;

    /**
     * Constructor for a server.
     */
    public Server() {
        try {
            System.out.println("Creating Server...");
            server = new ServerSocket(8081);
            encryption = new RSA();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connect to a client.
     *
     * @return  client socket
     */
    public Socket clientSocket(BigInteger e, BigInteger d, BigInteger n) {
        try {
            System.out.println("Connecting to client...");
            clients[numClients] = server.accept();

            if (clients[numClients] != null) {
                in[numClients] = new DataInputStream(clients[numClients].getInputStream());
                out[numClients] = new DataOutputStream(clients[numClients].getOutputStream());

                out[numClients].writeUTF(e.toString());
                out[numClients].flush();
                out[numClients].writeUTF(d.toString());
                out[numClients].flush();
                out[numClients].writeUTF(n.toString());
                out[numClients].flush();

                numClients++;

                return clients[numClients-1];
            } else {
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Sending the message received from one client to all clients.
     */
    public void sendMessage() {
        try {
            while (true) {
                byte[] message = new byte[256];

                for (int i = 0; i < numClients; ++i) {
                    if (in[i].read(message) != -1) {
                        for (int j = 0; j < numClients; ++j) {
                            out[j].write(message, 0, 256);
                            out[j].flush();
                        }
                    }
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
