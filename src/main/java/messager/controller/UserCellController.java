package messager.controller;

import javafx.scene.control.Label;
import messager.entities.User;

public class UserCellController {

    public Label dialogTitle;

    public void setUser(User user) {
        dialogTitle.setText(user.getName());
    }

}
