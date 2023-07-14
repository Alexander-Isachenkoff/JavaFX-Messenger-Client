package messager.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import messager.Main;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.Dialog;
import messager.entities.User;
import messager.requests.AddDialogRequest;
import messager.requests.DialogsListRequest;
import messager.response.AddDialogResponse;
import messager.response.DialogsListResponse;
import messager.server.Server;
import messager.view.DialogListCellFactory;

import javax.swing.*;
import java.io.IOException;

public class DialogsController {

    private final Client client = new ClientXML();
    @FXML
    private Circle userImageCircle;
    @FXML
    private Label userNameLabel;
    @FXML
    private ListView<Dialog> dialogsList;
    @FXML
    private DialogController dialogController;

    private User user;

    @FXML
    private void initialize() {
        dialogsList.setCellFactory(new DialogListCellFactory(() -> user)); // TODO: 11.07.2023 Переделать доступ к текущему юзеру

        dialogsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, dialog) -> {
            dialogController.setDialog(dialog);
        });

        Timer timer = new Timer(1000, actionEvent -> Platform.runLater(() -> dialogController.onMessagesRefresh()));
        timer.setRepeats(true);
        timer.start();
    }

    void selectDialog(Dialog dialog) {
        dialogsList.getSelectionModel().select(dialog);
    }

    @FXML
    private void onRefresh() {
        loadDialogs();
    }

    public void loadDialogs() {
        client.post(new DialogsListRequest(user.getId()));
        DialogsListResponse response = new Server().accept(DialogsListResponse.class);
        dialogsList.setItems(FXCollections.observableArrayList(response.getDialogs()));
    }

    public void postInit() {
        loadDialogs();
    }

    @FXML
    private void onAddDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/add_dialog.fxml"));
        Parent load;
        try {
            load = fxmlLoader.load();
            AddDialogController controller = fxmlLoader.getController();
            controller.setOnUserSelected(selectedUser -> {
                client.post(new AddDialogRequest(user, selectedUser));
                AddDialogResponse addDialogResponse = new Server().accept(AddDialogResponse.class);
                Dialog newDialog = addDialogResponse.getDialog();
                dialogsList.getItems().add(newDialog);
                selectDialog(newDialog);
                controller.closeWindow();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        Scene scene = new Scene(load);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        Window owner = dialogsList.getScene().getWindow();
        stage.initOwner(owner);
        stage.show();
        stage.setX(owner.getX() + owner.getWidth() / 2 - stage.getWidth() / 2);
        stage.setY(owner.getY() + owner.getHeight() / 2 - stage.getHeight() / 2);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        userNameLabel.setText(user.getName());
        userImageCircle.setFill(new ImagePattern(user.getImageToView()));
        dialogController.setCurrentUser(user);
    }
}
