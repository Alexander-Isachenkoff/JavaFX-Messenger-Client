package messager.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.Dialog;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.requests.AddMessageRequest;
import messager.requests.MessagesRequest;
import messager.response.MessagesResponse;
import messager.view.MessageCellFactory;
import messager.view.NodeUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DialogController {

    private final Client client = new ClientXML();
    @FXML
    private Circle userImageCircle;
    @FXML
    private Label userNameLabel;
    @FXML
    private ListView<TextMessage> messagesList;
    @FXML
    private TextArea textArea;
    private Dialog dialog;
    private User currentUser;

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

        messagesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        messagesList.setCellFactory(new MessageCellFactory(() -> currentUser));
    }

    @FXML
    public void onMessagesRefresh() {
        if (dialog != null) {
            loadNewMessages(dialog, messages -> messagesList.getItems().addAll(messages));
        }
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
        AddMessageRequest request = new AddMessageRequest(dialog.getId(), currentUser.getId(), text, LocalDateTime.now().toString());
        client.post(request);
        textArea.clear();
        textArea.requestFocus();
        onMessagesRefresh();
    }

    private void loadNewMessages(Dialog dialog, Consumer<List<TextMessage>> onLoad) {
        loadAllMessages(dialog, messages -> {
            List<TextMessage> newMessages = messages.stream()
                    .filter(message -> messagesList.getItems().stream().noneMatch(message1 -> message1.getId() == message.getId()))
                    .collect(Collectors.toList());
            onLoad.accept(newMessages);
        });
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
        if (dialog != null) {
            loadAllMessages(dialog, messages -> {
                messagesList.setItems(FXCollections.observableArrayList(messages));
                Optional<User> optionalUser = dialog.getUsers().stream()
                        .filter(user -> user.getId() != currentUser.getId())
                        .findFirst();
                User userTo = optionalUser.get();
                userNameLabel.setText(userTo.getName());
                NodeUtils.setCircleStyle(userImageCircle, userTo);
            });
        } else {
            messagesList.getItems().clear();
        }
    }

    private void loadAllMessages(Dialog dialog, Consumer<List<TextMessage>> onLoad) {
        client.post(new MessagesRequest(dialog.getId()), MessagesResponse.class, response -> onLoad.accept(response.getMessages()));
    }

}
