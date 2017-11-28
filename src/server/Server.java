package server;

import service.RSA;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
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
    private Socket client;

    /** RSA encryption. */
    private RSA encryption;

    /** Input Stream. */
    private DataInputStream in;

    /** Output Stream. */
    private DataOutputStream out;

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
            client = server.accept();

            if (client != null) {
                in = new DataInputStream(client.getInputStream());
                out = new DataOutputStream(client.getOutputStream());

                out.writeUTF(e.toString());
                out.flush();
                out.writeUTF(d.toString());
                out.flush();
                out.writeUTF(n.toString());
                out.flush();

                return client;
            } else {
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void sendMessage() {
        try {
            while (true) {
                String message = in.readUTF();

                if (message != null) {
                    System.out.println("fuck " + message);
                    out.writeUTF(message);
                    out.flush();
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }
}
