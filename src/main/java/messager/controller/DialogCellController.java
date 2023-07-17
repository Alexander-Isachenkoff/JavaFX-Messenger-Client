package messager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import messager.entities.Dialog;
import messager.entities.User;
import messager.view.NodeUtils;

import java.util.Optional;
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
        if (dialog.getName() != null) {
            dialogTitle.setText(dialog.getName());
        } else {
            dialogTitle.setText(dialog.getName());
            Optional<User> optionalUser = dialog.getUsers().stream()
                    .filter(user -> user.getId() != currentUser.getId())
                    .findFirst();
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                dialogTitle.setText(user.getName());
                NodeUtils.setCircleStyle(imageCircle, user);
            }
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
