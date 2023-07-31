package messager.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import messager.entities.User;
import messager.network.ClientServer;
import messager.network.ClientXML;
import messager.network.Server;
import messager.requests.Request;
import messager.requests.TransferableObject;
import messager.response.SignUpResponse;
import messager.util.FileUtils;
import messager.util.ImageUtils;
import messager.view.AlertUtil;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.function.Consumer;

import static messager.response.SignUpResponse.SignUpStatus.OK;

public class SignUpController {

    @FXML
    private Button signUpButton;
    @FXML
    private Button finishButton;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private GridPane inputPane;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private PasswordField passwordApprovalField;
    @FXML
    private Label checkLabel;
    @FXML
    private Label responseLabel;

    private File imageFile;
    private SignUpResponse signUpResponse;

    @FXML
    private void initialize() {
        responseLabel.setText("");
        checkLabel.setText("");
        signUpButton.setVisible(true);
        signUpButton.setManaged(true);
        finishButton.setVisible(false);
        finishButton.setManaged(false);
        setLoadingState(false);
    }

    @FXML
    private void onSignUp() {
        responseLabel.setText("");
        if (!checkLogin()) {
            return;
        }
        if (!checkPassword()) {
            return;
        }
        postSignUpData(response -> {
            this.signUpResponse = response;
            Platform.runLater(() -> showStatus(response.getStatus()));
        });
    }

    private boolean checkLogin() {
        if (nameField.getText().trim().isEmpty()) {
            checkLabel.setText("Имя пользователя не должно быть пустым");
            return false;
        } else {
            checkLabel.setText("");
            return true;
        }
    }

    private boolean checkPassword() {
        if (passwordField.getText().trim().isEmpty()) {
            checkLabel.setText("Пароль не должен быть пустым");
            return false;
        }
        if (!passwordField.getText().equals(passwordApprovalField.getText())) {
            checkLabel.setText("Пароли не совпадают");
            return false;
        } else {
            checkLabel.setText("");
            return true;
        }
    }

    private void postSignUpData(Consumer<SignUpResponse> onResponse) {
        Request request = createSignUpRequest();
        setLoadingState(true);
        ClientServer.runLater(() -> {
            try {
                new ClientXML().post(request);
            } catch (SocketTimeoutException | ConnectException e) {
                Platform.runLater(() -> {
                    responseLabel.setTextFill(Color.RED);
                    responseLabel.setText("Превышено время ожидания подключения к серверу!");
                });
                return;
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
                AlertUtil.showErrorAlert(e.getMessage());
                return;
            } finally {
                setLoadingState(false);
            }
            try {
                SignUpResponse response = new Server().accept(SignUpResponse.class);
                onResponse.accept(response);
            } catch (SocketTimeoutException e) {
                Platform.runLater(() -> {
                    responseLabel.setTextFill(Color.RED);
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

    private void setLoadingState(boolean loading) {
        responseLabel.setVisible(!loading);
        responseLabel.setManaged(!loading);
        progress.setVisible(loading);
        progress.setManaged(loading);
        signUpButton.setDisable(loading);
    }

    private Request createSignUpRequest() {
        String encodedImage = null;
        String imageFormat = null;
        if (imageFile != null) {
            try {
                BufferedImage image = ImageIO.read(imageFile);
                imageFormat = FileUtils.getExtension(imageFile);
                encodedImage = ImageUtils.encodeImage(image, imageFormat);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        TransferableObject params = new TransferableObject();
        params.put("userName", nameField.getText());
        params.put("password", passwordField.getText());
        params.put("encodedImage", encodedImage);
        params.put("imageFormat", imageFormat);
        return new Request("signUp", params);
    }

    private void showStatus(SignUpResponse.SignUpStatus status) {
        if (status == OK) {
            responseLabel.setTextFill(Color.GREEN);
        } else {
            responseLabel.setTextFill(Color.RED);
        }
        switch (status) {
            case OK:
                responseLabel.setText(String.format("Пользователь \"%s\" успешно зарегистрирован", nameField.getText()));
                inputPane.setDisable(true);
                signUpButton.setVisible(false);
                signUpButton.setManaged(false);
                finishButton.setVisible(true);
                finishButton.setManaged(true);
                break;
            case USER_ALREADY_EXISTS:
                responseLabel.setText(String.format("Пользователь с именем \"%s\" уже зарегистрирован", nameField.getText()));
                break;
        }
    }

    @FXML
    private void onSignIn() {
        MainController.getInstance().showSignInView();
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

    @FXML
    private void onFinish() {
        User user = this.signUpResponse.getUser();
        MainController.getInstance().showDialogsView(user);
    }

}
