import service.graphics.ChatBox;

/**
 * Starting the client side of the chat application.
 */
public class StartingClient {
    public static void main(String[] args) {
        ChatBox chat = new ChatBox();
        chat.chatInit();
    }
}
