package messager.controller;

import javafx.scene.control.Label;

public class UserCellController {

    public Label userNameLabel;
    public Label messageTextLabel;
    public Label timeLabel;

    public void setUserName(String name) {
        userNameLabel.setText(name);
    }

}
