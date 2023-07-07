package messager.requests;

import messager.entities.TextMessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class MessagesResponse {
    @XmlElementWrapper(name = "Messages")
    @XmlElement(name = "Message")
    List<TextMessage> messages;

    public MessagesResponse() {
    }

    public MessagesResponse(List<TextMessage> messages) {
        this.messages = messages;
    }

    public List<TextMessage> getMessages() {
        return messages;
    }
}
