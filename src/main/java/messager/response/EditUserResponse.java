package messager.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class EditUserResponse {
    @XmlElement
    private EditUserResponse.Status status;

    public enum Status {
        OK, ERROR
    }
}
