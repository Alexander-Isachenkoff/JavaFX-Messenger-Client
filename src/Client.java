import java.io.IOException;
import java.net.Socket;

public abstract class Client implements Post {

    public static final String ADDRESS = "127.0.0.1";
    public static final int PORT = 11111;
    private Socket socket;

    protected Socket getSocket() {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket(ADDRESS, PORT);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return socket;
    }

}
