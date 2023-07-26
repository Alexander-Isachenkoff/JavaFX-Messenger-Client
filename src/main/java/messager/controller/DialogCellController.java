package messager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import messager.client.ClientServer;
import messager.entities.*;
import messager.requests.Request;
import messager.requests.TransferableObject;
import messager.view.NodeUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.function.Consumer;

public class DialogCellController {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    @FXML
    private Label dialogTitle;
    @FXML
    private Label messageTextLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Circle imageCircle;

    private Dialog dialog;

    private Consumer<Dialog> onDelete = dialog -> {
    };

    public void setDialog(Dialog dialog, User currentUser) {
        this.dialog = dialog;
        if (dialog instanceof CommandDialog) {
            dialogTitle.setText(((CommandDialog) dialog).getName());
        } else {
            PersonalDialog personalDialog = (PersonalDialog) dialog;
            User user;
            if (personalDialog.getUser1().getId() == currentUser.getId()) {
                user = personalDialog.getUser2();
            } else {
                user = personalDialog.getUser1();
            }
            dialogTitle.setText(user.getName());
            NodeUtils.setCircleStyle(imageCircle, user);
        }
        TransferableObject params = new TransferableObject().put("dialogId", dialog.getId());
        Request request = new Request("getLastMessage", params);
        ClientServer.instance().tryPostAndAccept(request, TextMessage.class).ifPresent(message -> {
            String messageText;
            String timeText;
            if (message.getId() == 0) {
                messageText = "Нет сообщений";
                timeText = "";
            } else {
                messageText = message.getText();
                messageTextLabel.setText(message.getText());
                LocalDateTime time = LocalDateTime.parse(message.getDateTime());
                timeText = time.format(TIME_FORMATTER);
            }
            messageTextLabel.setText(messageText);
            timeLabel.setText(timeText);
        });
    }

    @FXML
    private void onDelete() {
        onDelete.accept(dialog);
    }

    public void setOnDelete(Consumer<Dialog> onDelete) {
        this.onDelete = onDelete;
    }
}
