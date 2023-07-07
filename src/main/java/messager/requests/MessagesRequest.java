package messager.requests;

import messager.entities.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessagesRequest {
    @XmlElement
    private User user;

    public MessagesRequest() {
    }

    public MessagesRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
