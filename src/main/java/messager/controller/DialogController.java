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
import messager.entities.PersonalDialog;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.requests.Request;
import messager.requests.StringList;
import messager.requests.TransferableObject;
import messager.response.MessagesList;
import messager.view.MessageCellFactory;
import messager.view.NodeUtils;

import java.time.LocalDateTime;
import java.util.List;
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
            loadNewMessages(messages -> {
                if (!messages.isEmpty()) {
                    messagesList.getItems().addAll(messages);
                    List<Long> readMessagesId = messages.stream().map(TextMessage::getId).collect(Collectors.toList());
                    TransferableObject params = new TransferableObject();
                    params.put("userId", currentUser.getId());
                    params.put("messagesId", StringList.of(readMessagesId));
                    client.tryPost(new Request("readMessages", params));
                }
            });
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
        TransferableObject params = new TransferableObject();
        params.put("dialogId", dialog.getId());
        params.put("userId", currentUser.getId());
        params.put("text", text);
        params.put("dateTime", LocalDateTime.now().toString());
        Request request = new Request("addMessage", params);
        if (!client.tryPost(request)) {
            return;
        }
        textArea.clear();
        textArea.requestFocus();
        onMessagesRefresh();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
        if (dialog != null) {
            loadAllMessages(messages -> {
                messagesList.setItems(FXCollections.observableArrayList(messages));
                if (dialog instanceof PersonalDialog) {
                    PersonalDialog personalDialog = (PersonalDialog) dialog;
                    User userTo;
                    if (personalDialog.getUser1().getId() == currentUser.getId()) {
                        userTo = personalDialog.getUser2();
                    } else {
                        userTo = personalDialog.getUser1();
                    }
                    userNameLabel.setText(userTo.getName());
                    NodeUtils.setCircleStyle(userImageCircle, userTo);
                }
            });
        } else {
            messagesList.getItems().clear();
        }
    }

    private void loadNewMessages(Consumer<List<TextMessage>> onLoad) {
        TransferableObject params = new TransferableObject();
        params.put("userId", currentUser.getId());
        params.put("dialogId", dialog.getId());
        params.put("unreadOnly", true);
        client.post(new Request("getMessages", params), MessagesList.class, response -> onLoad.accept(response.getMessages()));
    }

    private void loadAllMessages(Consumer<List<TextMessage>> onLoad) {
        TransferableObject params = new TransferableObject();
        params.put("userId", currentUser.getId());
        params.put("dialogId", dialog.getId());
        params.put("unreadOnly", false);
        client.post(new Request("getMessages", params), MessagesList.class, response -> onLoad.accept(response.getMessages()));
    }

}
