package messager.entities;

import javafx.beans.property.SimpleBooleanProperty;
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
    private String text;
    @XmlAttribute
    private String dateTime;

    public SimpleBooleanProperty readProperty = new SimpleBooleanProperty(false);

}