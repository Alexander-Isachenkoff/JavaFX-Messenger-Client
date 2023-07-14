package messager.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class SignUpRequest {
    @XmlElement
    private String userName;
    @XmlElement
    private String password;
    @XmlElement
    private String encodedImage;
}
