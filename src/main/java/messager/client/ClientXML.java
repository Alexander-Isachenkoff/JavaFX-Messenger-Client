package messager.client;

import messager.util.AppProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientXML extends Client {

    private static final boolean SHOW_XML = AppProperties.getShowXml();

    @Override
    public void post(Object object) {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(object.getClass());
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        try (ObjectOutputStream os = new ObjectOutputStream(getSocket().getOutputStream())) {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(object, os);
            if (SHOW_XML) {
                marshaller.marshal(object, System.out);
                System.out.println();
            }
        } catch (JAXBException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
