package messager.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.Dialog;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.requests.MessagesRequest;
import messager.response.MessagesResponse;
import messager.server.Server;
import messager.view.MessageCellFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

        messagesList.setCellFactory(new MessageCellFactory(() -> currentUser));
    }

    @FXML
    public void onMessagesRefresh() {
        if (dialog != null) {
            messagesList.getItems().addAll(loadNewMessages(dialog));
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
        TextMessage message = new TextMessage(currentUser, text, LocalDateTime.now().toString(), dialog);
        client.post(message);
        textArea.clear();
        textArea.requestFocus();
        onMessagesRefresh();
    }

    private List<TextMessage> loadNewMessages(Dialog dialog) {
        List<TextMessage> messages = loadAllMessages(dialog);
        return messages.stream()
                .filter(message -> messagesList.getItems().stream().noneMatch(message1 -> message1.getId() == message.getId()))
                .collect(Collectors.toList());
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
        if (dialog != null) {
            List<TextMessage> messages = loadAllMessages(dialog);
            messagesList.setItems(FXCollections.observableArrayList(messages));
            Optional<User> optionalUser = dialog.getUsers().stream()
                    .filter(user -> user.getId() != currentUser.getId())
                    .findFirst();
            User userTo = optionalUser.get();
            userNameLabel.setText(userTo.getName());
            userImageCircle.setFill(new ImagePattern(userTo.getImageToView()));
        } else {
            messagesList.getItems().clear();
        }
    }

    private List<TextMessage> loadAllMessages(Dialog dialog) {
        client.post(new MessagesRequest(dialog.getId()));
        MessagesResponse messagesResponse = new Server().accept(MessagesResponse.class);
        return messagesResponse.getMessages();
    }

}
