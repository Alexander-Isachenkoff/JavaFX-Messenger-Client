package messager.controller;

import javafx.scene.control.Label;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import messager.entities.User;

public class UserCellController {

    public Label dialogTitle;
    public Circle imageCircle;

    public void setUser(User user) {
        dialogTitle.setText(user.getName());
        imageCircle.setFill(new ImagePattern(user.getImageToView()));
    }

}
