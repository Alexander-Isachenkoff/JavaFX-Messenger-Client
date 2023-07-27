package messager.network;

import messager.util.AppProperties;
import messager.view.AlertUtil;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Optional;

public class Server {

    public static final int PORT = 11112;
    public static final int TIMEOUT = AppProperties.instance().getInt("responseTimeOut");
    private ServerSocket serverSocket;

    public <T> T accept(Class<T> tClass) throws IOException {
        boolean socketCreated = false;
        do {
            try {
                serverSocket = new ServerSocket(PORT);
                socketCreated = true;
            } catch (BindException e) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (!socketCreated);
        try (ServerSocket socket = serverSocket) {
            socket.setSoTimeout(TIMEOUT);
            return getObject(tClass);
        }
    }

    <T> Optional<T> acceptSilent(Class<T> tClass) {
        try {
            return Optional.of(accept(tClass));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    <T> Optional<T> tryAccept(Class<T> tClass) {
        try {
            return Optional.of(accept(tClass));
        } catch (SocketTimeoutException e) {
            AlertUtil.showErrorAlert("Превышено время ожидания ответа от сервера!");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert(e.getMessage());
        }
        return Optional.empty();
    }

    private <T> T getObject(Class<T> tClass) throws IOException {
        Socket incoming = serverSocket.accept();

        try (ObjectInputStream ois = new ObjectInputStream(incoming.getInputStream())) {
            return JAXB.unmarshal(ois, tClass);
        }
    }

}