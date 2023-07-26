package messager.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Pair;
import messager.Main;
import messager.client.ClientServer;
import messager.client.ClientXML;
import messager.entities.Dialog;
import messager.entities.PersonalDialog;
import messager.entities.User;
import messager.requests.Request;
import messager.requests.TransferableObject;
import messager.response.PersonalDialogsResponse;
import messager.view.DialogListCellFactory;
import messager.view.NodeUtils;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DialogsController {

    private final ClientXML client = new ClientXML();
    private final ClientServer clientServer = new ClientServer();
    private final Map<Dialog, Pair<Node, DialogController>> dialogNodeMap = new HashMap<>();

    @FXML
    private VBox dialogWrapper;
    @FXML
    private Circle userImageCircle;
    @FXML
    private Label userNameLabel;
    @FXML
    private ListView<Dialog> dialogsList;
    private User user;

    @FXML
    private void initialize() {
        DialogListCellFactory cellFactory = new DialogListCellFactory(() -> user); // TODO: 11.07.2023 Переделать доступ к текущему юзеру
        cellFactory.setOnDelete(dialog -> {
            String text = String.format("Вы уверены, что хотите удалить диалог \"%s\"?", "dialogTitle.getText()");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, text, ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                TransferableObject params = new TransferableObject();
                params.put("dialogId", dialog.getId());
                if (client.tryPost(new Request("deleteDialog", params))) {
                    loadDialogs();
                }
            }
        });
        dialogsList.setCellFactory(cellFactory);

        dialogsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, dialog) -> {
            showDialog(dialog);
        });

        Timer timer = new Timer(5000, actionEvent -> Platform.runLater(() -> {
            Dialog dialog = dialogsList.getSelectionModel().getSelectedItem();
            if (dialog != null) {
                DialogController dialogController = dialogNodeMap.get(dialog).getValue();
                dialogController.onMessagesRefresh();
            }
        }));
        timer.setRepeats(true);
        timer.start();
    }

    private void showDialog(Dialog dialog) {
        if (dialogNodeMap.containsKey(dialog)) {
            dialogWrapper.getChildren().setAll(dialogNodeMap.get(dialog).getKey());
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/dialog.fxml"));
            Parent node;
            DialogController controller;
            try {
                node = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            controller = fxmlLoader.getController();
            controller.setCurrentUser(user);
            controller.setDialog(dialog);
            dialogNodeMap.put(dialog, new Pair<>(node, controller));
            dialogWrapper.getChildren().setAll(node);
            VBox.setVgrow(node, Priority.ALWAYS);
        }
    }

    void selectDialog(Dialog dialog) {
        dialogsList.getSelectionModel().select(dialog);
    }

    @FXML
    private void onRefresh() {
        loadDialogs();
    }

    public void loadDialogs() {
        TransferableObject params = new TransferableObject().put("userId", user.getId());
        Request request = new Request("getPersonalDialogs", params);
        clientServer.tryPostAndAccept(request, PersonalDialogsResponse.class).ifPresent(response -> {
            dialogsList.setItems(FXCollections.observableArrayList(response.getDialogs()));
        });
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
            controller.setCurrentUser(user);
            controller.setOnUserSelected(selectedUser -> {
                TransferableObject params = new TransferableObject()
                        .put("userFromId", user.getId())
                        .put("userToId", selectedUser.getId());
                Request request = new Request("addDialog", params);
                clientServer.tryPostAndAccept(request, PersonalDialog.class).ifPresent(newDialog -> {
                    dialogsList.getItems().add(newDialog);
                    selectDialog(newDialog);
                    controller.closeWindow();
                });
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

    public void setUser(User user) {
        this.user = user;
        userNameLabel.setText(user.getName());
        NodeUtils.setCircleStyle(userImageCircle, user);
    }
}
