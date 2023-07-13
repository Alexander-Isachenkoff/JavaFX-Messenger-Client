package messager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.util.ImageUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class MessageCellController {
    @FXML
    private Circle imageCircle;
    @FXML
    private Label textLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Label timeLabel;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    @FXML
    private void initialize() {
        textLabel.textProperty().bind(messageTextArea.textProperty());
    }

    public void setTextMessage(TextMessage textMessage) {
        User userFrom = textMessage.getUserFrom();
        userNameLabel.setText(userFrom.getName());
        messageTextArea.setText(textMessage.getMessage());
        LocalDateTime time = LocalDateTime.parse(textMessage.getDateTime());
        String format = time.format(TIME_FORMATTER);
        timeLabel.setText(format);

        if (userFrom.getEncodedImage() != null) {
            imageCircle.setFill(new ImagePattern(ImageUtils.decodeImage(userFrom.getEncodedImage())));
        }
    }
}
