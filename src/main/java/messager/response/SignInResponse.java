package messager.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum SignInResponse {
    OK, WRONG_PASSWORD, USER_NOT_FOUND
}

