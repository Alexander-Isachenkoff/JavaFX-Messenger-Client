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
public class AddMessageRequest implements Request {
    @XmlElement
    private long dialogId;
    @XmlElement
    private long userId;
    @XmlElement
    private String text;
    @XmlElement
    private String dateTime;
}
