package messager.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum SignUpResponse {
    OK, USER_ALREADY_EXISTS
}
