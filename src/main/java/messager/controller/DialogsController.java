package messager.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.Dialog;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.requests.DialogsListRequest;
import messager.requests.MessagesRequest;
import messager.response.DialogsListResponse;
import messager.response.MessagesResponse;
import messager.server.Server;
import messager.view.DialogListCellFactory;
import messager.view.MessageCellFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DialogsController {

    private final Client client = new ClientXML();
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

        dialogsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            loadMessages(newValue);
        });
    }

    private void loadMessages(Dialog dialog) {
        client.post(new MessagesRequest(dialog));
        MessagesResponse messagesResponse = new Server().accept(MessagesResponse.class);
        List<TextMessage> messages = messagesResponse.getMessages();
        List<TextMessage> newMessages = messages.stream()
                .filter(message -> messagesList.getItems().stream().noneMatch(message1 -> message1.getId() == message.getId()))
                .collect(Collectors.toList());
        messagesList.getItems().addAll(newMessages);
    }

    @FXML
    private void onRefresh() {
        loadDialogs();
    }

    private void loadDialogs() {
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

    public void setUser(User user) {
        this.user = user;
        userNameLabel.setText(user.getName());
    }

    public void postInit() {
        loadDialogs();
    }

    @FXML
    private void onMessagesRefresh() {
        Dialog dialog = dialogsList.getSelectionModel().getSelectedItem();
        if (dialog != null) {
            loadMessages(dialog);
        }
    }
}
