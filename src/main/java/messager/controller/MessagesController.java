package messager.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.requests.MessagesRequest;
import messager.requests.UsersListRequest;
import messager.response.MessagesResponse;
import messager.response.UsersListResponse;
import messager.server.Server;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class MessagesController {

    @FXML
    private ListView<TextMessage> messagesList;
    @FXML
    private ListView<User> usersList;

    private User user;
    private final Client client = new ClientXML();

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

        usersList.setCellFactory(new UserListCellFactory());

        loadUsers();

        Timer timer = new Timer(500, actionEvent -> {
            client.post(new MessagesRequest(user));
            MessagesResponse messagesResponse = new Server().accept(MessagesResponse.class);
            List<TextMessage> messages = messagesResponse.getMessages();
            User userFrom = usersList.getSelectionModel().getSelectedItem();
            if (userFrom != null) {
                List<TextMessage> messagesFromSelectedUser = messages.stream()
                        .filter(message -> message.getUserFrom().getName().equals(userFrom.getName()))
                        .collect(Collectors.toList());
                Platform.runLater(() -> messagesList.setItems(FXCollections.observableArrayList(messagesFromSelectedUser)));
            }
        });
        timer.setInitialDelay(0);
        timer.setRepeats(true);
        timer.start();
    }

    private void loadUsers() {
        client.post(new UsersListRequest());
        UsersListResponse response = new Server().accept(UsersListResponse.class);
        usersList.setItems(FXCollections.observableArrayList(response.getUsers()));
    }

    public void onSend() {
        sendMessage();
    }

    private void sendMessage() {
        String text = textArea.getText();
        if (text.trim().isEmpty()) {
            return;
        }
        User userTo = usersList.getSelectionModel().getSelectedItem();
        TextMessage message = new TextMessage(user, userTo, text);
        client.post(message);
        textArea.clear();
        textArea.requestFocus();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
