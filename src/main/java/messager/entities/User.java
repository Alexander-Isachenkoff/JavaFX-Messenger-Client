package messager.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@XmlRootElement
public class User {

    @XmlAttribute
    private long id;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String password;
    @XmlElement
    private String encodedImage;

    public User(String name, String password, String encodedImage) {
        this.name = name;
        this.password = password;
        this.encodedImage = encodedImage;
    }

}
