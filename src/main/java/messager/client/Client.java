package messager.client;

import messager.util.AppProperties;

import java.io.IOException;
import java.net.Socket;

public abstract class Client implements Poster {

    public static final String ADDRESS = AppProperties.getServerAddress();
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
