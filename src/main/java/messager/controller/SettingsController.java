package messager.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import messager.client.ClientXML;
import messager.requests.Request;
import messager.requests.TransferableObject;
import messager.response.CheckAccessResponse;
import messager.server.Server;
import messager.util.AppProperties;
import messager.util.NetworkUtil;
import messager.view.AlertUtil;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;

public class SettingsController {

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
        ipField.setText(AppProperties.instance().getServerAddress());
        portField.setText(String.valueOf(AppProperties.instance().getServerPort()));
        connectionField.setText(AppProperties.instance().getString("connectionTimeOut"));
        responseField.setText(AppProperties.instance().getString("responseTimeOut"));
    }

    @FXML
    private void onCheck() {
        statusVbox.getChildren().clear();
        try {
            new ClientXML(ipField.getText()).post(new Request("checkServerAccess", new TransferableObject()));
        } catch (IOException e) {
            Label label = new Label("Не удаётся установить соединение с сервером!");
            label.setTextFill(Color.RED);
            statusVbox.getChildren().add(label);
            e.printStackTrace();
            return;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(() -> {
            Label label = new Label("Соединение с сервером установлено!");
            label.setTextFill(Color.GREEN);
            statusVbox.getChildren().add(label);
        });
        try {
            new Server().accept(CheckAccessResponse.class);
        } catch (SocketTimeoutException ex) {
            Label label2 = new Label("Превышено время ожидания ответа от сервера!");
            label2.setTextFill(Color.RED);
            statusVbox.getChildren().add(label2);
        } catch (IOException e) {
            AlertUtil.showErrorAlert(e.getMessage());
        }
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
        Optional<String> optional = NetworkUtil.findServerIp();
        if (optional.isPresent()) {
            String ip = optional.get();
            ipField.setText(ip);
            AlertUtil.showInfoAlert("Сервер найден: " + ip);
        } else {
            AlertUtil.showWarningAlert("Не найден сервер!");
        }
    }
}
