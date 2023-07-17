package messager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import messager.Main;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.User;
import messager.requests.SignInRequest;
import messager.response.SignInResponse;
import messager.server.Server;

import java.io.IOException;

public class SignInController {

    private final Client client = new ClientXML();
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label responseLabel;

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
        String userName = nameField.getText();
        String password = passwordField.getText();
        client.post(new SignInRequest(userName, password));

        Server server = new Server();
        SignInResponse response = server.accept(SignInResponse.class);

        if (response.getStatus() != SignInResponse.SignInStatus.OK) {
            responseLabel.setTextFill(Color.RED);
        }
        switch (response.getStatus()) {
            case OK:
                signIn(response.getUser());
                break;
            case WRONG_PASSWORD:
                responseLabel.setText("Неверный пароль");
                break;
            case USER_NOT_FOUND:
                responseLabel.setText(String.format("Не зарегистрирован пользователь \"%s\"", userName));
                break;
        }
    }

    private void signIn(User user) {
        Main.getMainController().showDialogsView(user);
    }

    private void onEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            postLoginData();
        }
    }

    @FXML
    private void onSignUp() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/sign_up.fxml"));
        Parent load;
        try {
            load = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Tab currentTab = NodeUtils.getParentTab(nameField);
        currentTab.setText("Регистрация");
        currentTab.setContent(load);
    }

}
