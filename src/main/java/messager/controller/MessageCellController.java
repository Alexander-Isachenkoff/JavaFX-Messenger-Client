package messager.controller;

import javafx.scene.control.Label;
import messager.entities.TextMessage;

public class MessageCellController {
    public Label userNameLabel;
    public Label messageTextLabel;
    public Label timeLabel;

    public void setTextMessage(TextMessage textMessage) {
        userNameLabel.setText(textMessage.getUserFrom().getName());
        messageTextLabel.setText(textMessage.getMessage());
    }
}
