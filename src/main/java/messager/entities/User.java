package messager.entities;

import javafx.scene.image.Image;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import messager.util.ImageUtils;
import messager.util.Resources;

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
    @XmlElement
    private String encodedImage;

    public Image getImageToView() {
        Image image;
        if (getEncodedImage() != null) {
            image = ImageUtils.decodeImage(getEncodedImage());
        } else {
            image = Resources.getDefaultUserImage();
        }
        return image;
    }

}
