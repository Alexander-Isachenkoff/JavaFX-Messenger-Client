package messager.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import messager.entities.Dialog;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class DialogsListResponse {
    @XmlElementWrapper
    @XmlElement
    public List<Dialog> dialogs;
}
