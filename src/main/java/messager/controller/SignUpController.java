package messager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import messager.Main;
import messager.client.Client;
import messager.client.ClientXML;
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
    @FXML
    private ImageView imageView;
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label responseLabel;

    private File imageFile;

    @FXML
    private void initialize() {
        responseLabel.setText("");
    }

    @FXML
    private void onSignUp() {
        postSignUpData();
    }

    private void postSignUpData() {
        String encodedImage = getEncodedImage();

        client.post(new SignUpRequest(nameField.getText(), passwordField.getText(), encodedImage));

        Server server = new Server();
        SignUpResponse response = server.accept(SignUpResponse.class);

        if (response.getStatus() == OK) {
            responseLabel.setTextFill(Color.GREEN);
        } else {
            responseLabel.setTextFill(Color.RED);
        }
        switch (response.getStatus()) {
            case OK:
                responseLabel.setText(String.format("Пользователь \"%s\" успешно зарегистрирован", response.getUser().getName()));
                nameField.clear();
                passwordField.clear();
                break;
            case USER_ALREADY_EXISTS:
                responseLabel.setText(String.format("Пользователь с именем \"%s\" уже зарегистрирован", nameField.getText()));
                break;
        }
    }

    private String getEncodedImage() {
        String encodedImage;
        if (imageFile != null) {
            try {
                BufferedImage image = ImageIO.read(imageFile);
                encodedImage = ImageUtils.encodeImage(image, FileUtils.getExtension(imageFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            encodedImage = null;
        }
        return encodedImage;
    }

    @FXML
    private void onSignIn() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/sign_in.fxml"));
        Parent load;
        try {
            load = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Tab currentTab = NodeUtils.getParentTab(nameField);
        currentTab.setText("Вход");
        currentTab.setContent(load);
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

}
