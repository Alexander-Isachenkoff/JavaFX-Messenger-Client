package messager.client;

import messager.util.AppProperties;
import messager.view.AlertUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public abstract class Client {

    protected static final int PORT = AppProperties.instance().getServerPort();
    private static final int TIMEOUT = AppProperties.instance().getInt("connectionTimeOut");
    protected final String address;
    private Socket socket;

    public Client() {
        this(AppProperties.instance().getServerAddress());
    }

    public Client(String address) {
        this.address = address;
    }

    protected Socket getSocket() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket();
        }
        if (!socket.isConnected()) {
            try {
                socket.connect(new InetSocketAddress(address, PORT), TIMEOUT);
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

}
