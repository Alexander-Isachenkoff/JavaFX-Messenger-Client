package messager.client;

import javafx.application.Platform;
import messager.server.Server;
import messager.util.AppProperties;
import messager.view.AlertUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class Client {

    protected static final String ADDRESS = AppProperties.instance().getServerAddress();
    protected static final int PORT = AppProperties.instance().getServerPort();
    private static final int TIMEOUT = AppProperties.instance().getInt("connectionTimeOut");
    private Socket socket;

    protected Socket getSocket() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket();
        }
        if (!socket.isConnected()) {
            try {
                socket.connect(new InetSocketAddress(ADDRESS, PORT), TIMEOUT);
            } catch (IOException e) {
                socket.close();
                throw e;
            }
        }
        return socket;
    }

    public boolean tryPost(Object object) {
        String errorText;
        try {
            post(object);
            return true;
        } catch (SocketTimeoutException e) {
            errorText = "Превышено время ожидания подключения к серверу!";
        } catch (Exception e) {
            e.printStackTrace();
            errorText = e.getMessage();
        }
        AlertUtil.showErrorAlert(errorText);
        return false;
    }

    public abstract void post(Object object) throws Exception;

    public <T, R> void post(T request, Class<R> responseClass, Consumer<Optional<R>> onResponse) {
        new Thread(() -> {
            if (!tryPost(request)) {
                return;
            }
            Optional<R> optionalResponse = new Server().tryAccept(responseClass);
            Platform.runLater(() -> onResponse.accept(optionalResponse));
        }).start();
    }

}
