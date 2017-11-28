package server;

/**
 * Class for the thread for each client.
 */
public class ServerThread extends Thread {

    /** Socket for Client. */
    private Server server;

    /**
     * Constructor for server thread.
     *
     * @param socket    server
     */
    public ServerThread(Server socket) {
        server = socket;
    }

    @Override
    public void run() {
        server.sendMessage();
    }
}
