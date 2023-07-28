package messager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import messager.Main;
import messager.entities.User;

import java.io.IOException;

public class MainController {

    private static MainController instance;

    @FXML
    private TabPane tabPane;

    public MainController() {
        instance = this;
    }

    public static MainController getInstance() {
        return instance;
    }

    private static Parent loadView(String s) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(s));
        Parent load;
        try {
            load = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return load;
    }

    @FXML
    private void initialize() {
        tabPane.getTabs().removeAll(tabPane.getTabs());
        Parent load = loadView("fxml/sign_in.fxml");
        newTab(load, "Вход");
    }

    @FXML
    private void onSignIn() {
        Parent load = loadView("fxml/sign_in.fxml");
        newTab(load, "Вход");
    }

    @FXML
    private void onSignUp() {
        Parent load = loadView("fxml/sign_up.fxml");
        newTab(load, "Регистрация");
    }

    @FXML
    private void onSettings() {
        Parent load = loadView("fxml/settings.fxml");
        newTab(load, "Параметры");
    }

    public void showDialogsView(User user) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/dialogs.fxml"));
        Parent load;
        try {
            load = fxmlLoader.load();
            DialogsController controller = fxmlLoader.getController();
            controller.setUser(user);
            controller.postInit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        showOnCurrentTab(user.getName(), load);
    }

    public void showSignInView() {
        Parent load = loadView("fxml/sign_in.fxml");
        showOnCurrentTab("Вход", load);
    }

    public void showSignUpView() {
        Parent load = loadView("fxml/sign_up.fxml");
        showOnCurrentTab("Регистрация", load);
    }

    private void newTab(Parent load, String title) {
        Tab signInTab = new Tab(title);
        signInTab.setContent(load);
        tabPane.getTabs().add(signInTab);
        tabPane.getSelectionModel().select(signInTab);
    }

    private void showOnCurrentTab(String user, Parent load) {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        tab.setText(user);
        tab.setContent(load);
    }

}
