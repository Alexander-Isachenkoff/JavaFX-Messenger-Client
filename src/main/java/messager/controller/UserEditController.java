package messager.controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import messager.entities.User;
import messager.network.ClientServer;
import messager.requests.Request;
import messager.requests.TransferableObject;
import messager.response.EditUserResponse;
import messager.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UserEditController {

    @FXML
    private Button finishButton;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private GridPane inputPane;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField loginField;
    @FXML
    private TextField nameField;
    @FXML
    private Label responseLabel;
    private User user;

    @FXML
    private void initialize() {
        responseLabel.setText("");
        setLoadingState(false);
    }

    private void setLoadingState(boolean loading) {
        responseLabel.setVisible(!loading);
        responseLabel.setManaged(!loading);
        progress.setVisible(loading);
        progress.setManaged(loading);
        inputPane.setDisable(loading);
        finishButton.setDisable(loading);
    }

    @FXML
    private void onAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы изображений", "*.jpg", "*.jpeg", "*.png", ".bmp"));
        File file = fileChooser.showOpenDialog(getStage());
        if (file == null) {
            return;
        }
        Image image;
        try {
            image = new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ImageCropController.show(image, getStage(), rect -> {
            WritableImage crop = new WritableImage(image.getPixelReader(), rect.x, rect.y, rect.width, rect.height);
            imageView.setImage(crop);
        });
    }

    private Stage getStage() {
        return (Stage) nameField.getScene().getWindow();
    }

    @FXML
    private void onFinish() {
        Request request = createEditUserRequest();
        setLoadingState(true);
        ClientServer.instance().tryPostAndAccept(request, EditUserResponse.class, response -> {
            Platform.runLater(() -> {
                if (response.getStatus() == EditUserResponse.Status.OK) {
                    responseLabel.setTextFill(Color.GREEN);
                    responseLabel.setText("Успешно сохранено");
                } else {
                    responseLabel.setTextFill(Color.RED);
                    responseLabel.setText("Не удалось сохранить");
                }
                setLoadingState(false);
            });
        });
    }

    private Request createEditUserRequest() {
        String encodedImage = null;
        String imageFormat = null;
        if (imageView.getImage() != null) {
            try {
                BufferedImage image = SwingFXUtils.fromFXImage(imageView.getImage(), null);
                imageFormat = "png";
                encodedImage = ImageUtils.encodeImage(image, imageFormat);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        TransferableObject params = new TransferableObject();
        params.put("userId", user.getId());
        params.put("userName", nameField.getText());
        params.put("encodedImage", encodedImage);
        params.put("imageFormat", imageFormat);
        return new Request("editUser", params);
    }

    public void setUser(User user) {
        this.user = user;
        loginField.setText(user.getLogin());
        nameField.setText(user.getName());
        if (user.getEncodedImage() != null) {
            imageView.setImage(ImageUtils.decodeImage(user.getEncodedImage()));
        }
    }

}
