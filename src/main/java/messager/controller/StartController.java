package messager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import messager.Main;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.User;

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

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/messages.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(fxmlLoader.load());
            MessagesController controller = fxmlLoader.getController();
            controller.setUser(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) nameField.getScene().getWindow();
        NodeUtils.setScene(stage, newScene);
    }

}
