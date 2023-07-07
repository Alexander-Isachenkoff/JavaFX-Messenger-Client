package messager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import messager.Main;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.User;
import messager.requests.SignUpRequest;
import messager.response.SignUpResponse;
import messager.server.Server;

import java.io.IOException;

public class SignUpController {

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
    }

    @FXML
    private void onSignUp() {
        postSignUpData();
    }

    private void postSignUpData() {
        User user = new User(nameField.getText(), passwordField.getText());
        client.post(new SignUpRequest(user));

        Server server = new Server();
        SignUpResponse response = server.accept(SignUpResponse.class);

        if (response == SignUpResponse.OK) {
            responseLabel.setTextFill(Color.GREEN);
        } else {
            responseLabel.setTextFill(Color.RED);
        }
        switch (response) {
            case OK:
                responseLabel.setText(String.format("Пользователь \"%s\" успешно зарегистрирован", user.getName()));
                nameField.clear();
                passwordField.clear();
                break;
            case USER_ALREADY_EXISTS:
                responseLabel.setText(String.format("Пользователь с именем \"%s\" уже зарегистрирован", user.getName()));
                break;
        }
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

}
