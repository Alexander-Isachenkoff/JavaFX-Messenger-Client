package messager.controller;

import javafx.scene.control.Label;
import messager.entities.Dialog;
import messager.entities.User;

import java.util.Optional;

public class DialogCellController {

    public Label dialogTitle;
    public Label messageTextLabel;
    public Label timeLabel;

    public void setDialog(Dialog dialog, User currentUser) {
        if (dialog.getName() != null) {
            dialogTitle.setText(dialog.getName());
        } else {
            dialogTitle.setText(dialog.getName());
            Optional<String> optionalName = dialog.getUsers().stream()
                    .filter(user -> user.getId() != currentUser.getId())
                    .map(User::getName)
                    .findFirst();
            optionalName.ifPresent(name -> dialogTitle.setText(name));
        }
    }

}
