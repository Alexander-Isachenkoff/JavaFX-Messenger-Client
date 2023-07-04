import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class TextMessage implements Serializable {

    @XmlElement
    private String name;
    @XmlElement
    private String message;

    public TextMessage() {
    }

    public TextMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("TextMessage\n" +
                "{\n" +
                "    name = '%s',\n" +
                "    message = '%s'\n" +
                "}", name, message);
    }

}