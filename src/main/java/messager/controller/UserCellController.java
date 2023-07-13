package messager.controller;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import messager.entities.User;
import messager.util.ImageUtils;

public class UserCellController {

    public Label dialogTitle;
    public Circle imageCircle;

    public void setUser(User user) {
        dialogTitle.setText(user.getName());
        String encodedImage = user.getEncodedImage();
        if (encodedImage != null) {
            Image image = ImageUtils.decodeImage(encodedImage);
            imageCircle.setFill(new ImagePattern(image));
        }
    }

}
