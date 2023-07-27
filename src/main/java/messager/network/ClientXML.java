package messager.network;

import messager.util.AppProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientXML extends Client {

    protected static final int PORT = AppProperties.instance().getServerPort();
    private static final boolean SHOW_XML = AppProperties.instance().getShowXml();
    private static final int TIMEOUT = AppProperties.instance().getInt("connectionTimeOut");
    protected final String address;
    private Socket socket;

    public ClientXML(String address) {
        this.address = address;
    }

    public ClientXML() {
        this.address = AppProperties.instance().getServerAddress();
    }

    @Override
    public void post(Object object) throws JAXBException, IOException {
        JAXBContext context;
        context = JAXBContext.newInstance(object.getClass());
        try (Socket socket = getSocket()) {
            try (ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream())) {
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                if (SHOW_XML) {
                    marshaller.marshal(object, System.out);
                    System.out.println();
                }
                marshaller.marshal(object, os);
            }
        }
    }

    protected Socket getSocket() throws IOException {
        if (socket == null || socket.isClosed()) {
            boolean socketCreated = false;
            do {
                try {
                    socket = new Socket(address, PORT);
                    socketCreated = true;
                } catch (BindException e) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } while (!socketCreated);
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

}
