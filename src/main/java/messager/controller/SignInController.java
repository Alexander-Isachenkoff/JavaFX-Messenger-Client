package messager.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import messager.entities.User;
import messager.network.ClientServer;
import messager.network.ClientXML;
import messager.network.Server;
import messager.requests.Request;
import messager.requests.TransferableObject;
import messager.response.SignInResponse;
import messager.view.AlertUtil;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class SignInController {

    public ProgressIndicator progress;
    public Button signInButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label responseLabel;

    @FXML
    private void initialize() {
        setLoadingState(false);
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
        params.put("login", nameField.getText());
        params.put("password", passwordField.getText());

        setLoadingState(true);

        Request request = new Request("signIn", params);
        ClientServer.runLater(() -> {
            try {
                new ClientXML().post(request);
            } catch (SocketTimeoutException | ConnectException e) {
                Platform.runLater(() -> {
                    responseLabel.setText("Превышено время ожидания подключения к серверу!");
                });
                return;
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
                AlertUtil.showErrorAlert(e.getMessage());
            } finally {
                setLoadingState(false);
            }
            try {
                SignInResponse response = new Server().accept(SignInResponse.class);
                Platform.runLater(() -> {
                    processSignInResponse(response);
                });
            } catch (SocketTimeoutException e) {
                Platform.runLater(() -> {
                    responseLabel.setText("Превышено время ожидания ответа от сервера!");
                });
            } catch (IOException e) {
                e.printStackTrace();
                AlertUtil.showErrorAlert(e.getMessage());
            } finally {
                setLoadingState(false);
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

    private void setLoadingState(boolean isLoading) {
        progress.setVisible(isLoading);
        progress.setManaged(isLoading);
        responseLabel.setVisible(!isLoading);
        responseLabel.setManaged(!isLoading);
        signInButton.setDisable(isLoading);
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
