package messager.view;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import messager.entities.User;
import messager.util.ImageUtils;
import messager.util.Resources;

public class NodeUtils {

    public static void setCircleStyle(Circle circle, User user) {
        Image image;
        if (user.getEncodedImage() != null) {
            image = ImageUtils.decodeImage(user.getEncodedImage());
        } else {
            image = Resources.getDefaultUserImage();
            circle.setStroke(Color.LIGHTGRAY);
            circle.setStrokeWidth(1);
        }
        circle.setFill(new ImagePattern(image));
    }

}
