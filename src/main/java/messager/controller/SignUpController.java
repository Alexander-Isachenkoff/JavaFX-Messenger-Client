package messager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import messager.Main;

import java.io.IOException;

public class SignUpController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label responseLabel;

    @FXML
    private void initialize() {
        responseLabel.setText("");
    }

    @FXML
    private void onSignUp() {

    }

    @FXML
    private void onSignIn() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/sign_in.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        NodeUtils.setScene(getStage(), newScene);
    }

    private Stage getStage() {
        return (Stage) nameField.getScene().getWindow();
    }

}
