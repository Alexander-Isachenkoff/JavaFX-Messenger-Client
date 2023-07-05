package messager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.TextMessage;
import messager.entities.User;

public class MessagesController {

    private final Client client = new ClientXML();
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
