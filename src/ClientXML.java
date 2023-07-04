import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientXML extends Client {

    @Override
    public <T> void post(T object) {
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
        } catch (JAXBException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
