package messager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import messager.entities.CommandDialog;
import messager.entities.Dialog;
import messager.entities.PersonalDialog;
import messager.entities.User;
import messager.view.NodeUtils;

import java.util.function.Consumer;

public class DialogCellController {

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
    }

    @FXML
    private void onDelete() {
        onDelete.accept(dialog);
    }

    public void setOnDelete(Consumer<Dialog> onDelete) {
        this.onDelete = onDelete;
    }
}
