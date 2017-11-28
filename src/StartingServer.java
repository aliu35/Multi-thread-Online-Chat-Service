import server.Server;
import server.ServerThread;
import service.RSA;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class StartingServer {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        Server server = new Server();
        RSA encryption = new RSA();
        System.out.println(InetAddress.getLocalHost().toString());

        while (true) {
            Socket client = server.clientSocket(encryption.getE(), encryption.getD(), encryption.getN());

            if (client != null) {
                System.out.println("Connected to a client.");
                new Thread().sleep(4000);
                ServerThread thread =  new ServerThread(server);
                thread.start();
            }
        }
    }
}
