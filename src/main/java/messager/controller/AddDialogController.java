package messager.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.User;
import messager.requests.Request;
import messager.requests.TransferableObject;
import messager.response.UsersList;
import messager.server.Server;
import messager.view.UserListCellFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AddDialogController {

    private final Client client = new ClientXML();
    public Label corner;
    public HBox header;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<User> usersListView;
    private List<User> users;
    private Consumer<User> onUserSelected = user -> {
    };
    private double resizePressedX;
    private double resizePressedY;
    private double movePressedX;
    private double movePressedY;

    @FXML
    private void initialize() {
        usersListView.setCellFactory(new UserListCellFactory());

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<User> filteredUsers = users.stream()
                    .filter(user -> user.getName().toLowerCase().contains(newValue.trim().toLowerCase()))
                    .collect(Collectors.toList());
            usersListView.setItems(FXCollections.observableList(filteredUsers));
        });

        usersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedUser) -> {
            onUserSelected.accept(selectedUser);
        });

        corner.setOnMousePressed(event -> {
            resizePressedX = event.getX();
            resizePressedY = event.getY();
        });

        corner.setOnMouseDragged(event -> {
            getStage().setWidth(event.getScreenX() - getStage().getX() + corner.getWidth() - resizePressedX);
            getStage().setHeight(event.getScreenY() - getStage().getY() + corner.getHeight() - resizePressedY);
        });

        header.setOnMousePressed(event -> {
            movePressedX = event.getX();
            movePressedY = event.getY();
        });

        header.setOnMouseDragged(event -> {
            getStage().setX(event.getScreenX() - movePressedX);
            getStage().setY(event.getScreenY() - movePressedY);
        });
    }

    public void setOnUserSelected(Consumer<User> onUserSelected) {
        this.onUserSelected = onUserSelected;
    }

    @FXML
    private void onClose() {
        closeWindow();
    }

    public void closeWindow() {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) usersListView.getScene().getWindow();
    }

    public void setCurrentUser(User currentUser) {
        TransferableObject params = new TransferableObject();
        params.put("userId", currentUser.getId());
        if (!client.tryPost(new Request("usersList", params))) {
            return;
        }
        new Server().tryAccept(UsersList.class).ifPresent(usersList -> {
            usersListView.setItems(FXCollections.observableList(usersList.getUsers()));
        });
    }
}
