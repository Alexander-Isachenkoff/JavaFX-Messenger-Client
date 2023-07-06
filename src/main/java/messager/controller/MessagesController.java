package messager.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.requests.UsersListRequest;
import messager.requests.UsersListResponse;
import messager.server.Server;

public class MessagesController {

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
        TextMessage message = new TextMessage(user, text);
        client.post(message);
        textArea.clear();
        textArea.requestFocus();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
