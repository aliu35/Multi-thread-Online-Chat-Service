package client;

import service.RSA;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

/**
 * Class for client and related methods.
 */
public class Client {

    /** Socket for Client. */
    private Socket client;

    /** Input Stream. */
    private DataInputStream in;

    /** Output Stream. */
    private DataOutputStream out;

    /**
     * Constructor for client.
     *
     * @param address   address of the host
     */
    public Client(String address) {
        try {
            client = new Socket(address, 8081);
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send message to server.
     *
     * @param message   message
     */
    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receive initial server message and create an RSA encryption/decryption object.
     *
     * @return  RSA
     */
    public RSA getRSA() {
        String e, d, N;

        try {
            e = in.readUTF();
            d = in.readUTF();
            N = in.readUTF();

            return new RSA(new BigInteger(e), new BigInteger(d), new BigInteger(N));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Receive message from server.
     *
     * @return  message
     */
    public String receive() {
        try {
            String message = in.readUTF();
            if (message != null) {
                return message;
            } else {
                return null;
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
