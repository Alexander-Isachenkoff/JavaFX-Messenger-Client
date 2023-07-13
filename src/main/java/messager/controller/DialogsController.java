package messager.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
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
import messager.entities.TextMessage;
import messager.entities.User;
import messager.requests.AddDialogRequest;
import messager.requests.DialogsListRequest;
import messager.requests.MessagesRequest;
import messager.response.AddDialogResponse;
import messager.response.DialogsListResponse;
import messager.response.MessagesResponse;
import messager.server.Server;
import messager.util.ImageUtils;
import messager.view.DialogListCellFactory;
import messager.view.MessageCellFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DialogsController {

    private final Client client = new ClientXML();
    @FXML
    private Circle userImageCircle;
    @FXML
    private Label userNameLabel;
    @FXML
    private ListView<TextMessage> messagesList;
    @FXML
    private ListView<Dialog> dialogsList;
    private User user;
    @FXML
    private TextArea textArea;

    @FXML
    private void initialize() {
        textArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.isShiftDown()) {
                    textArea.insertText(textArea.getCaretPosition(), "\n");
                } else {
                    sendMessage();
                    event.consume();
                    textArea.clear();
                }
            }
        });

        dialogsList.setCellFactory(new DialogListCellFactory(() -> user)); // TODO: 11.07.2023 Переделать доступ к текущему юзеру
        messagesList.setCellFactory(new MessageCellFactory(() -> user));

        dialogsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, dialog) -> {
            if (dialog != null) {
                List<TextMessage> messages = loadAllMessages(dialog);
                messagesList.setItems(FXCollections.observableArrayList(messages));
            } else {
                messagesList.getItems().clear();
            }
        });
    }

    private List<TextMessage> loadNewMessages(Dialog dialog) {
        List<TextMessage> messages = loadAllMessages(dialog);
        return messages.stream()
                .filter(message -> messagesList.getItems().stream().noneMatch(message1 -> message1.getId() == message.getId()))
                .collect(Collectors.toList());
    }

    private List<TextMessage> loadAllMessages(Dialog dialog) {
        client.post(new MessagesRequest(dialog));
        MessagesResponse messagesResponse = new Server().accept(MessagesResponse.class);
        return messagesResponse.getMessages();
    }

    void selectDialog(Dialog dialog) {
        dialogsList.getSelectionModel().select(dialog);
    }

    @FXML
    private void onRefresh() {
        loadDialogs();
    }

    public void loadDialogs() {
        client.post(new DialogsListRequest(user));
        DialogsListResponse response = new Server().accept(DialogsListResponse.class);
        dialogsList.setItems(FXCollections.observableArrayList(response.getDialogs()));
    }

    @FXML
    private void onSend() {
        sendMessage();
    }

    private void sendMessage() {
        String text = textArea.getText();
        if (text.trim().isEmpty()) {
            return;
        }
        Dialog dialog = dialogsList.getSelectionModel().getSelectedItem();
        TextMessage message = new TextMessage(user, text, LocalDateTime.now().toString(), dialog);
        client.post(message);
        textArea.clear();
        textArea.requestFocus();
        onMessagesRefresh();
    }

    public void postInit() {
        loadDialogs();
    }

    @FXML
    private void onMessagesRefresh() {
        Dialog dialog = dialogsList.getSelectionModel().getSelectedItem();
        if (dialog != null) {
            messagesList.getItems().addAll(loadNewMessages(dialog));
        }
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
                loadDialogs();
                selectDialog(addDialogResponse.getDialog());
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
        if (user.getEncodedImage() != null) {
            userImageCircle.setFill(new ImagePattern(ImageUtils.decodeImage(user.getEncodedImage())));
        }
    }
}
