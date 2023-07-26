package messager.client;

import messager.util.AppProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientXML extends Client {

    private static final boolean SHOW_XML = AppProperties.instance().getShowXml();

    public ClientXML() {
        super();
    }

    public ClientXML(String address) {
        super(address);
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

}
