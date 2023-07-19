package messager.client;

import javafx.application.Platform;
import messager.server.Server;
import messager.util.AppProperties;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class Client {

    public static final String ADDRESS = AppProperties.getServerAddress();
    public static final int PORT = 11111;
    private Socket socket;

    protected Socket getSocket() {
        if (socket == null || socket.isClosed()) {
            boolean socketCreated = false;
            do {
                try {
                    socket = new Socket(ADDRESS, PORT);
                    socketCreated = true;
                } catch (IOException e) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } while (!socketCreated);
        }

        return socket;
    }

    public abstract void post(Object object);

    public <T, R> void post(T request, Class<R> responseClass, Consumer<R> onResponse) {
        new Thread(() -> {
            post(request);
            R response = new Server().accept(responseClass);
            Platform.runLater(() -> onResponse.accept(response));
        }).start();
    }

}
