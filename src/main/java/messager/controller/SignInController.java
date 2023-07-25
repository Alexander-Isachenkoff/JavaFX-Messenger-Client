package messager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.User;
import messager.requests.Request;
import messager.requests.TransferableObject;
import messager.response.SignInResponse;
import messager.server.Server;
import messager.view.AlertUtil;

import java.net.SocketTimeoutException;

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
        responseLabel.setText("");
        TransferableObject params = new TransferableObject();
        params.put("userName", nameField.getText());
        params.put("password", passwordField.getText());
        try {
            client.post(new Request("signIn", params));
        } catch (SocketTimeoutException e) {
            responseLabel.setText("Превышено время ожидания подключения к серверу!");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert(e.getMessage());
            return;
        }

        new Server().tryAccept(SignInResponse.class).ifPresent(response -> {
            switch (response.getStatus()) {
                case OK:
                    signIn(response.getUser());
                    break;
                case WRONG_PASSWORD:
                    responseLabel.setText("Неверный пароль");
                    break;
                case USER_NOT_FOUND:
                    responseLabel.setText(String.format("Не зарегистрирован пользователь \"%s\"", nameField.getText()));
                    break;
            }
        });
    }

    private void signIn(User user) {
        MainController.getInstance().showDialogsView(user);
    }

    private void onEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            postLoginData();
        }
    }

    @FXML
    private void onSignUp() {
        MainController.getInstance().showSignUpView();
    }

}
