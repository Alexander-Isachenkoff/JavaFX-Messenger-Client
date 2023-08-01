package messager.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import java.util.Optional;
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
                    postReadMessages(messages);
                }
            });
        }
    }

    private void postReadMessages(List<TextMessage> messages) {
        List<Long> readMessagesId = messages.stream().map(TextMessage::getId).collect(Collectors.toList());
        TransferableObject params = new TransferableObject();
        params.put("userId", currentUser.getId());
        params.put("messagesId", StringList.of(readMessagesId));
        ClientServer.instance().tryPost(new Request("readMessages", params), isAccess -> {
        });
    }

    public void updateMessagesState() {
        TransferableObject params = new TransferableObject()
                .put("dialogId", dialog.getId())
                .put("userId", currentUser.getId());
        Request request = new Request("getReadMessages", params);
        ClientServer.instance().postAndAcceptSilent(request, MessagesList.class, messagesList -> {
            List<TextMessage> updatedMessages = messagesList.getMessages();
            if (updatedMessages.isEmpty()) {
                return;
            }
            Platform.runLater(() -> replaceMessages(updatedMessages));

            List<Long> readMessagesId = updatedMessages.stream().map(TextMessage::getId).collect(Collectors.toList());
            TransferableObject backParams = new TransferableObject();
            backParams.put("userId", currentUser.getId());
            backParams.put("messagesId", StringList.of(readMessagesId));
            ClientServer.instance().tryPost(new Request("updateAccepted", backParams), isAccess -> {
            });
        });
    }

    private void replaceMessages(List<TextMessage> newMessages) {
        ObservableList<TextMessage> viewMessages = this.messagesList.getItems();
        for (int i = 0; i < viewMessages.size(); i++) {
            TextMessage textMessage = viewMessages.get(i);
            Optional<TextMessage> opt = newMessages.stream()
                    .filter(message -> message.getId() == textMessage.getId())
                    .findFirst();
            if (opt.isPresent()) {
                viewMessages.set(i, opt.get());
            }
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
            initDialogHeader(dialog);
            loadOldMessages(response -> {
                this.messagesList.getItems().setAll(response.getMessages());
            });
            loadNewMessages(response -> {
                this.messagesList.getItems().addAll(response.getMessages());
                postReadMessages(response.getMessages());
            });
        } else {
            messagesList.getItems().clear();
        }
    }

    private void initDialogHeader(Dialog dialog) {
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
    }

    private void loadNewMessages(Consumer<MessagesList> onLoad) {
        loadMessages(true, onLoad);
    }

    private void loadAllMessages(Consumer<MessagesList> onLoad) {
        loadMessages(false, onLoad);
    }

    private void loadOldMessages(Consumer<MessagesList> onLoad) {
        TransferableObject params = new TransferableObject();
        params.put("userId", currentUser.getId());
        params.put("dialogId", dialog.getId());
        ClientServer.instance().postAndAcceptSilent(new Request("getOldMessages", params), MessagesList.class, onLoad);
    }

    private void loadMessages(boolean unreadOnly, Consumer<MessagesList> onLoad) {
        TransferableObject params = new TransferableObject();
        params.put("userId", currentUser.getId());
        params.put("dialogId", dialog.getId());
        params.put("unreadOnly", unreadOnly);
        ClientServer.instance().postAndAcceptSilent(new Request("getMessages", params), MessagesList.class, onLoad);
    }

}
