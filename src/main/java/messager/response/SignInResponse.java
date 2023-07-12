package messager.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import messager.entities.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class SignInResponse {

    @XmlElement
    private User user;
    @XmlElement
    private SignInStatus status;

    public enum SignInStatus {
        OK, WRONG_PASSWORD, USER_NOT_FOUND
    }
}

