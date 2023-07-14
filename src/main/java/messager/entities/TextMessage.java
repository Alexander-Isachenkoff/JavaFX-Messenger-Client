package messager.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class TextMessage {

    @XmlAttribute
    private long id;
    @XmlElement
    private User userFrom;
    @XmlAttribute
    private String message;
    @XmlAttribute
    private String dateTime;
    @XmlElement
    private Dialog dialog;

    public TextMessage(User userFrom, String message, String dateTime, Dialog dialog) {
        this.userFrom = userFrom;
        this.message = message;
        this.dateTime = dateTime;
        this.dialog = dialog;
    }

}