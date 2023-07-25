package messager.server;

import lombok.SneakyThrows;
import messager.util.AppProperties;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {

    public static final int PORT = 11112;
    public static final int TIMEOUT = AppProperties.instance().getInt("responseTimeOut");
    private ServerSocket serverSocket;

    @SneakyThrows
    public <T> T accept(Class<T> tClass) throws SocketTimeoutException {
        try (ServerSocket socket = new ServerSocket(PORT)) {
            serverSocket = socket;
            serverSocket.setSoTimeout(TIMEOUT);
            return getObject(tClass);
        }
    }

    private <T> T getObject(Class<T> tClass) throws IOException {
        Socket incoming = serverSocket.accept();

        try (ObjectInputStream ois = new ObjectInputStream(incoming.getInputStream())) {
            return JAXB.unmarshal(ois, tClass);
        }
    }

}