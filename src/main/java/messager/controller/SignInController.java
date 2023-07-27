package messager.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import messager.entities.User;
import messager.network.ClientServer;
import messager.requests.Request;
import messager.requests.TransferableObject;
import messager.response.SignInResponse;
import messager.view.AlertUtil;

import java.net.SocketTimeoutException;

public class SignInController {

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

        Request request = new Request("signIn", params);
        ClientServer.instance().postAndAccept(request, SignInResponse.class,
                response -> Platform.runLater(() -> processSignInResponse(response)),
                e -> {
                    if (e instanceof SocketTimeoutException) {
                        responseLabel.setText("Превышено время ожидания подключения к серверу!");
                    } else {
                        e.printStackTrace();
                        AlertUtil.showErrorAlert(e.getMessage());
                    }
                },
                e -> {
                    if (e instanceof SocketTimeoutException) {
                        responseLabel.setText("Превышено время ожидания ответа от сервера!");
                    } else {
                        e.printStackTrace();
                        AlertUtil.showErrorAlert(e.getMessage());
                    }
                });
    }

    private void processSignInResponse(SignInResponse response) {
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
