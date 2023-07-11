package messager.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class User {

    @XmlAttribute
    private Long id;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String password;

}
