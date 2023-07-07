package messager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import messager.Main;

import java.io.IOException;

public class MainController {
    @FXML
    private TabPane tabPane;

    @FXML
    private void initialize() {
        addSignInTab();
    }

    @FXML
    private void onAdd() {
        addSignInTab();
    }

    private void addSignInTab() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/sign_in.fxml"));
        Parent load;
        try {
            load = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Tab signInTab = new Tab("Вход");
        signInTab.setContent(load);
        tabPane.getTabs().add(signInTab);
        tabPane.getSelectionModel().select(signInTab);
    }
}
