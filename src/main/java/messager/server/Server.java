package messager.server;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 11112;
    private ServerSocket serverSocket;

    public <T> T accept(Class<T> tClass) {
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

        try {
            return getObject(tClass);
        } catch (IOException | JAXBException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private <T> T getObject(Class<T> tClass) throws IOException, JAXBException {
        Socket incoming = serverSocket.accept();

        T object;
        try (ObjectInputStream ois = new ObjectInputStream(incoming.getInputStream())) {
            object = JAXB.unmarshal(ois, tClass);
        }

        return object;
    }

}