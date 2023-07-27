package messager.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import messager.entities.Dialog;
import messager.entities.PersonalDialog;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.network.ClientServer;
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
            loadNewMessages(response -> {
                List<TextMessage> messages = response.getMessages();
                if (messages == null) {
                    return; // TODO: 26.07.2023 fix NullPointerException
                }
                if (!messages.isEmpty()) {
                    Platform.runLater(() -> messagesList.getItems().addAll(messages));
                    List<Long> readMessagesId = messages.stream().map(TextMessage::getId).collect(Collectors.toList());
                    TransferableObject params = new TransferableObject();
                    params.put("userId", currentUser.getId());
                    params.put("messagesId", StringList.of(readMessagesId));
                    ClientServer.instance().tryPost(new Request("readMessages", params), isAccess -> {
                    });
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
        ClientServer.instance().tryPost(request, isAccess -> {
            if (isAccess) {
                Platform.runLater(() -> {
                    textArea.clear();
                    textArea.requestFocus();
                });
                onMessagesRefresh();
            }
        });
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
        if (dialog != null) {
            loadAllMessages(response -> {
                this.messagesList.setItems(FXCollections.observableArrayList(response.getMessages()));
                if (dialog instanceof PersonalDialog) {
                    PersonalDialog personalDialog = (PersonalDialog) dialog;
                    User userTo;
                    if (personalDialog.getUser1().getId() == currentUser.getId()) {
                        userTo = personalDialog.getUser2();
                    } else {
                        userTo = personalDialog.getUser1();
                    }
                    Platform.runLater(() -> {
                        userNameLabel.setText(userTo.getName());
                        NodeUtils.setCircleStyle(userImageCircle, userTo);
                    });
                }
            });
        } else {
            messagesList.getItems().clear();
        }
    }

    private void loadNewMessages(Consumer<MessagesList> onLoad) {
        loadMessages(true, onLoad);
    }

    private void loadAllMessages(Consumer<MessagesList> onLoad) {
        loadMessages(false, onLoad);
    }

    private void loadMessages(boolean unreadOnly, Consumer<MessagesList> onLoad) {
        TransferableObject params = new TransferableObject();
        params.put("userId", currentUser.getId());
        params.put("dialogId", dialog.getId());
        params.put("unreadOnly", unreadOnly);
        ClientServer.instance().postAndAcceptSilent(new Request("getMessages", params), MessagesList.class, onLoad);
    }

}
