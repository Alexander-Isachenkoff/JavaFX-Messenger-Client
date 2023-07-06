package messager.server;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 11112;
    private Unmarshaller unmarshaller;
    private ServerSocket serverSocket;

    public <T> T accept(Class<T> tClass) {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(tClass);
            unmarshaller = context.createUnmarshaller();
            serverSocket = new ServerSocket(PORT);
        } catch (JAXBException | IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            Object object;
            try {
                object = getObject();
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
                continue;
            }
            if (object.getClass() != tClass) {
                continue;
            }
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return (T) object;
        }
    }

    private Object getObject() throws IOException, JAXBException {
        Socket incoming = serverSocket.accept();

        Object object;
        try (ObjectInputStream ois = new ObjectInputStream(incoming.getInputStream())) {
            object = unmarshaller.unmarshal(ois);
        }

        return object;
    }

}