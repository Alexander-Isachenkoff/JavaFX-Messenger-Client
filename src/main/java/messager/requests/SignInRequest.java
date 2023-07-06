package messager.requests;

import messager.entities.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SignInRequest {
    @XmlElement
    private User user;

    public SignInRequest() {
    }

    public SignInRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
