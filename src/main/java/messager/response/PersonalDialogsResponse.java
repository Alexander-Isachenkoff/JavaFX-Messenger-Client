package messager.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import messager.entities.PersonalDialog;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class PersonalDialogsResponse {
    @XmlElementWrapper(name = "PersonalDialogs")
    @XmlElement(name = "PersonalDialog")
    public List<PersonalDialog> dialogs;
}
