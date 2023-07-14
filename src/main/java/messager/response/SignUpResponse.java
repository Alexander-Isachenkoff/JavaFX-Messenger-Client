package messager.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import messager.entities.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@Getter
@XmlRootElement
public class SignUpResponse {

    @XmlElement
    private User user;
    @XmlElement
    private SignUpStatus status;

    public enum SignUpStatus {
        OK, USER_ALREADY_EXISTS
    }
}
