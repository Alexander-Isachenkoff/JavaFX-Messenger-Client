package messager.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import messager.client.Client;
import messager.client.ClientXML;
import messager.entities.User;
import messager.requests.AddDialogRequest;
import messager.requests.UsersListRequest;
import messager.response.AddDialogResponse;
import messager.response.UsersListResponse;
import messager.server.Server;

public class AddDialogController {

    @FXML
    private ListView<User> usersListView;

    private final Client client = new ClientXML();
    private DialogsController controller;

    @FXML
    private void initialize() {
        client.post(new UsersListRequest());
        UsersListResponse response = new Server().accept(UsersListResponse.class);
        usersListView.setItems(FXCollections.observableList(response.getUsers()));

        usersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, userTo) -> {
            client.post(new AddDialogRequest(controller.getUser(), userTo));
            AddDialogResponse addDialogResponse = new Server().accept(AddDialogResponse.class);
            controller.loadDialogs();
            controller.selectDialog(addDialogResponse.getDialog());
            onClose();
        });
    }

    @FXML
    private void onClose() {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) usersListView.getScene().getWindow();
    }

    public void setDialogsController(DialogsController controller) {
        this.controller = controller;
    }

}
