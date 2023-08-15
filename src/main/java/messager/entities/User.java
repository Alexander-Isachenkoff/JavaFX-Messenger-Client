package messager.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@XmlRootElement
public class User {

    @XmlElement
    private long id;
    @XmlElement
    private String login;
    @XmlElement
    private String name;
    @XmlElement
    private String encodedImage;

}
