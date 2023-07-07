package messager.entities;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class TextMessage implements Serializable {

    @XmlElement
    private User userFrom;
    @XmlElement
    private User userTo;
    @XmlAttribute
    private String message;

    public TextMessage() {
    }

    public TextMessage(User userFrom, User userTo, String message) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.message = message;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", getUserFrom().getName(), message);
    }

}