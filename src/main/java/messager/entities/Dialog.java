package messager.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@XmlRootElement
public class Dialog {

    @XmlAttribute
    private long id;

}
