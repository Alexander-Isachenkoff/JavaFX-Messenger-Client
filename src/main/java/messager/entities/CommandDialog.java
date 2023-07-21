package messager.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@XmlRootElement
public class CommandDialog extends Dialog {

    @EqualsAndHashCode.Exclude
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    private final List<User> users = new ArrayList<>();
    @XmlElement
    private String name;
    @XmlElement
    private String encodedImage;

    public boolean hasUser(long userId) {
        return getUsers().stream().map(User::getId).anyMatch(id -> id == userId);
    }

}
