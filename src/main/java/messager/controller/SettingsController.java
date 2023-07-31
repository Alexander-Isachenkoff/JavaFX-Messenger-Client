package messager.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import messager.network.ClientServer;
import messager.network.ClientXML;
import messager.network.NetworkUtil;
import messager.requests.Request;
import messager.requests.TransferableObject;
import messager.response.CheckAccessResponse;
import messager.util.AppProperties;
import messager.view.AlertUtil;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;

public class SettingsController {

    @FXML
    private Button searchButton;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private VBox statusVbox;
    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private TextField connectionField;
    @FXML
    private TextField responseField;

    @FXML
    private void initialize() {
        progress.setVisible(false);
        ipField.setText(AppProperties.instance().getString("serverAddress"));
        portField.setText(String.valueOf(AppProperties.instance().getInt("serverPort")));
        connectionField.setText(AppProperties.instance().getString("connectionTimeOut"));
        responseField.setText(AppProperties.instance().getString("responseTimeOut"));
    }

    @FXML
    private void onCheck() {
        statusVbox.getChildren().clear();
        Request request = new Request("checkServerAccess", new TransferableObject());
        ClientServer.instance().postAndAccept(new ClientXML(ipField.getText()), request, CheckAccessResponse.class, response -> {
                    Platform.runLater(() -> {
                        Label label = new Label("Соединение с сервером установлено!");
                        label.setTextFill(Color.GREEN);
                        statusVbox.getChildren().add(label);
                    });
                },
                e -> {
                    if (e instanceof IOException) {
                        Platform.runLater(() -> {
                            Label label = new Label("Не удаётся установить соединение с сервером!");
                            label.setTextFill(Color.RED);
                            statusVbox.getChildren().add(label);
                            e.printStackTrace();
                        });
                    } else {
                        e.printStackTrace();
                        AlertUtil.showErrorAlert(e.getMessage());
                    }
                },
                e -> {
                    if (e instanceof SocketTimeoutException) {
                        Label label2 = new Label("Превышено время ожидания ответа от сервера!");
                        label2.setTextFill(Color.RED);
                        statusVbox.getChildren().add(label2);
                    } else {
                        e.printStackTrace();
                        AlertUtil.showErrorAlert(e.getMessage());
                    }
                });
    }

    @FXML
    private void onSave() {
        AppProperties.instance().setProperty("serverAddress", ipField.getText());
        AppProperties.instance().setProperty("serverPort", portField.getText());
        AppProperties.instance().setProperty("connectionTimeOut", connectionField.getText());
        AppProperties.instance().setProperty("responseTimeOut", responseField.getText());
        AppProperties.instance().save();
        statusVbox.getChildren().clear();
        statusVbox.getChildren().add(new Label("Сохранено!"));
    }

    @FXML
    private void onSearch() {
        progress.setVisible(true);
        searchButton.setDisable(true);
        new Thread(() -> {
            Optional<String> optional = NetworkUtil.findServerIp();
            Platform.runLater(() -> {
                progress.setVisible(false);
                searchButton.setDisable(false);
                if (optional.isPresent()) {
                    String ip = optional.get();
                    ipField.setText(ip);
                    AlertUtil.showInfoAlert("Сервер найден: " + ip);
                } else {
                    AlertUtil.showWarningAlert("Не найден сервер!");
                }
            });
        }).start();
    }
}
