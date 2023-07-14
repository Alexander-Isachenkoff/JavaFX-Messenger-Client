package messager.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Getter
@XmlRootElement
public class Dialog {

    @XmlAttribute
    private Long id;

    @XmlAttribute
    private String name;

    @EqualsAndHashCode.Exclude
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    private List<User> users = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @XmlElementWrapper(name = "messages")
    @XmlElement(name = "message")
    private List<TextMessage> messages = new ArrayList<>();

    private Dialog() {
    }

}
