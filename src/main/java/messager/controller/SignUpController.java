package messager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import messager.Main;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.User;
import messager.requests.SignUpRequest;
import messager.response.SignUpResponse;
import messager.server.Server;
import messager.util.FileUtils;
import messager.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static messager.response.SignUpResponse.SignUpStatus.OK;

public class SignUpController {

    private final Client client = new ClientXML();
    public Button signUpButton;
    public Button finishButton;
    @FXML
    private GridPane inputPane;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private PasswordField passwordApprovalField;
    @FXML
    private Label passwordCheckLabel;
    @FXML
    private Label responseLabel;

    private File imageFile;
    private SignUpResponse signUpResponse;

    @FXML
    private void initialize() {
        responseLabel.setText("");
        passwordCheckLabel.setText("");
        signUpButton.setVisible(true);
        finishButton.setVisible(false);
    }

    @FXML
    private void onSignUp() {
        responseLabel.setText("");
        if (!checkPassword()) {
            return;
        }
        this.signUpResponse = postSignUpData();
        showResponse(signUpResponse.getStatus());
    }

    private boolean checkPassword() {
        if (!passwordField.getText().equals(passwordApprovalField.getText())) {
            passwordCheckLabel.setText("Пароли не совпадают");
            return false;
        } else {
            passwordCheckLabel.setText("");
            return true;
        }
    }

    private SignUpResponse postSignUpData() {
        SignUpRequest request = createSignUpRequest();
        client.post(request);
        Server server = new Server();
        return server.accept(SignUpResponse.class);
    }

    private SignUpRequest createSignUpRequest() {
        String encodedImage = null;
        String imageFormat = null;
        if (imageFile != null) {
            try {
                BufferedImage image = ImageIO.read(imageFile);
                imageFormat = FileUtils.getExtension(imageFile);
                encodedImage = ImageUtils.encodeImage(image, imageFormat);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new SignUpRequest(nameField.getText(), passwordField.getText(), encodedImage, imageFormat);
    }

    private void showResponse(SignUpResponse.SignUpStatus status) {
        if (status == OK) {
            responseLabel.setTextFill(Color.GREEN);
        } else {
            responseLabel.setTextFill(Color.RED);
        }
        switch (status) {
            case OK:
                responseLabel.setText(String.format("Пользователь \"%s\" успешно зарегистрирован", nameField.getText()));
                inputPane.setDisable(true);
                signUpButton.setVisible(false);
                finishButton.setVisible(true);
                break;
            case USER_ALREADY_EXISTS:
                responseLabel.setText(String.format("Пользователь с именем \"%s\" уже зарегистрирован", nameField.getText()));
                break;
        }
    }

    @FXML
    private void onSignIn() {
        Main.getMainController().showSignInView();
    }

    @FXML
    private void onAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы изображений", "*.jpg", "*.jpeg", "*.png", ".bmp"));
        File file = fileChooser.showOpenDialog(getStage());
        if (file == null) {
            return;
        }
        this.imageFile = file;
        Image image;
        try {
            image = new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        imageView.setImage(image);
    }

    private Stage getStage() {
        return (Stage) nameField.getScene().getWindow();
    }

    @FXML
    private void onFinish() {
        User user = this.signUpResponse.getUser();
        Main.getMainController().showDialogsView(user);
    }

}
