package messager.requests;

import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@XmlRootElement
public class ObjectList {

    @XmlElement(name = "el")
    private final List<TransferableObject> list = new ArrayList<>();

    public void put(TransferableObject transferableObject) {
        list.add(transferableObject);
    }

    public TransferableObject get(int i) {
        return list.get(i);
    }

}
