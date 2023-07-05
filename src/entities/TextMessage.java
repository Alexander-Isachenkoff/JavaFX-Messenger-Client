package entities;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class TextMessage implements Serializable {

    @XmlElement
    private User user;
    @XmlAttribute
    private String message;

    public TextMessage() {
    }

    public TextMessage(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
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
                "}", user, message);
    }

}