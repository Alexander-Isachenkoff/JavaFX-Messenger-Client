package messager.controller;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import messager.entities.User;
import messager.view.NodeUtils;

public class UserCellController {

    public Label dialogTitle;
    public Circle imageCircle;

    public void setUser(User user) {
        dialogTitle.setText(user.getName());
        NodeUtils.setCircleStyle(imageCircle, user);
    }

}
