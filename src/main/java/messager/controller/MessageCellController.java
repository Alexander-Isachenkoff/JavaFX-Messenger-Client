package messager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.network.ClientServer;
import messager.requests.Request;
import messager.requests.TransferableObject;
import messager.response.XmlBoolean;
import messager.view.NodeUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class MessageCellController {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    @FXML
    private Polyline readCheck;
    @FXML
    private Polyline postedCheck;
    @FXML
    private Text messageText;
    @FXML
    private Circle imageCircle;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label timeLabel;

    @FXML
    private void initialize() {
        readCheck.setVisible(false);
    }

    public void setMessage(TextMessage message) {
        User userFrom = message.getUserFrom();
        userNameLabel.setText(userFrom.getName());
        messageText.setText(message.getText());
        LocalDateTime time = LocalDateTime.parse(message.getDateTime());
        String format = time.format(TIME_FORMATTER);
        timeLabel.setText(format);
        readCheck.visibleProperty().bind(message.getReadProperty());
        NodeUtils.setCircleStyle(imageCircle, userFrom);
        postIsMessageRead(message);
    }

    private void postIsMessageRead(TextMessage message) {
        Request request = new Request("isReadMessage", new TransferableObject().put("messageId", message.getId()));
        ClientServer.instance().postAndAcceptSilent(request, XmlBoolean.class, response -> {
            if (response.isValue()) {
                message.getReadProperty().set(true);
//                readCheck.setVisible(true);
            }
        });
    }
}
