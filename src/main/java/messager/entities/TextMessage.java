package messager.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class TextMessage implements Serializable {

    @XmlElement
    private User userFrom;
    @XmlElement
    private User userTo;
    @XmlAttribute
    private String message;
    @XmlAttribute
    private String dateTime;

}