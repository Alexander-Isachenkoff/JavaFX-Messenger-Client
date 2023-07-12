package messager.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.User;
import messager.requests.UsersListRequest;
import messager.response.UsersListResponse;
import messager.server.Server;
import messager.view.UserListCellFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AddDialogController {

    private final Client client = new ClientXML();
    @FXML
    private TextField searchField;
    @FXML
    private ListView<User> usersListView;
    private List<User> users;
    private Consumer<User> onUserSelected = user -> {
    };

    @FXML
    private void initialize() {
        usersListView.setCellFactory(new UserListCellFactory());

        // TODO: 12.07.2023 Исправить некорректное отображение элементов
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<User> filteredUsers = users.stream()
                    .filter(user -> user.getName().toLowerCase().contains(newValue.trim().toLowerCase()))
                    .collect(Collectors.toList());
            usersListView.setItems(FXCollections.observableList(filteredUsers));
        });

        usersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedUser) -> {
            onUserSelected.accept(selectedUser);
        });

        client.post(new UsersListRequest());
        UsersListResponse response = new Server().accept(UsersListResponse.class);
        users = response.getUsers();
        usersListView.setItems(FXCollections.observableList(users));
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

}
