package messager.requests;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class MessagesReadRequest {
    @XmlElement
    private long userId;

    @XmlElementWrapper(name = "Messages")
    @XmlElement(name = "MessageId")
    private List<Long> readMessagesId;
}
