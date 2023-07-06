package messager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import messager.Main;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.User;
import messager.requests.SignInRequest;
import messager.requests.SignInResponse;
import messager.server.Server;

import java.io.IOException;

public class SignInController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label responseLabel;

    private final Client client = new ClientXML();

    @FXML
    private void initialize() {
        responseLabel.setText("");
        nameField.setOnKeyPressed(this::onEnterPressed);
        passwordField.setOnKeyPressed(this::onEnterPressed);
    }

    @FXML
    private void onSignIn() {
        postLoginData();
    }

    private void postLoginData() {
        User user = new User(nameField.getText(), passwordField.getText());
        client.post(new SignInRequest(user));

        Server server = new Server();
        SignInResponse response = server.accept(SignInResponse.class);

        if (response != SignInResponse.OK) {
            responseLabel.setTextFill(Color.RED);
        }
        switch (response) {
            case OK:
                signIn(user);
                break;
            case WRONG_PASSWORD:
                responseLabel.setText("Неверный пароль");
                break;
            case USER_NOT_FOUND:
                responseLabel.setText(String.format("Не зарегистрирован пользователь \"%s\"", user.getName()));
                break;
        }
    }

    private void signIn(User user) {
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

    private void onEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            postLoginData();
        }
    }
}
