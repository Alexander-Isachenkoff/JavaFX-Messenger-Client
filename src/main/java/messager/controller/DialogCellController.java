package messager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import messager.entities.Dialog;
import messager.entities.User;

import java.util.Optional;

public class DialogCellController {

    @FXML
    private Label dialogTitle;
    @FXML
    private Label messageTextLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Circle imageCircle;

    public void setDialog(Dialog dialog, User currentUser) {
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
                imageCircle.setFill(new ImagePattern(user.getImageToView()));
            }
        }
    }

}
