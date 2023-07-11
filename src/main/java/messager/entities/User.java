package messager.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@NoArgsConstructor
@XmlRootElement
public class User {

    @XmlAttribute
    private Long id;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
