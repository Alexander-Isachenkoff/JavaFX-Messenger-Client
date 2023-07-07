package messager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import messager.entities.TextMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class MessageCellController {
    @FXML
    private Label userNameLabel;
    @FXML
    private Label messageTextLabel;
    @FXML
    private Label timeLabel;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    public void setTextMessage(TextMessage textMessage) {
        userNameLabel.setText(textMessage.getUserFrom().getName());
        messageTextLabel.setText(textMessage.getMessage());
        LocalDateTime time = LocalDateTime.parse(textMessage.getDateTime());
        String format = time.format(TIME_FORMATTER);
        timeLabel.setText(format);
    }
}
