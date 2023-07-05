package controller;

import client.Client;
import client.ClientXML;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sun.security.tools.keytool.Main;

import java.io.IOException;

public class StartController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;

    private final Client client = new ClientXML();

    @FXML
    private void onSignIn() {
        User user = new User(nameField.getText(), passwordField.getText());
        client.post(user);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("messages.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Messages");
        stage.setScene(scene);
        stage.show();
    }

}
