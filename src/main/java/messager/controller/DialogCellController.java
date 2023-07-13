package messager.controller;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import messager.entities.Dialog;
import messager.entities.User;
import messager.util.ImageUtils;

import java.util.Optional;

public class DialogCellController {

    public Label dialogTitle;
    public Label messageTextLabel;
    public Label timeLabel;
    public ImageView imageView;

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
                String encodedImage = user.getEncodedImage();
                if (encodedImage != null) {
                    imageView.setImage(ImageUtils.decodeImage(encodedImage));
                }
            }
        }
    }

}
