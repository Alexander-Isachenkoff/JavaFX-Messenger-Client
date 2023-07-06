package messager.requests;

import messager.entities.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SignUpRequest {
    @XmlElement
    private User user;

    public SignUpRequest() {
    }

    public SignUpRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
