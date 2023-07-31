package messager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import messager.Main;
import messager.entities.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainController {

    private static final Map<String, String> nameFxmlMap;
    private static MainController instance;

    static {
        nameFxmlMap = new HashMap<>();
        nameFxmlMap.put("Вход", "fxml/sign_in.fxml");
        nameFxmlMap.put("Регистрация", "fxml/sign_up.fxml");
        nameFxmlMap.put("Параметры", "fxml/settings.fxml");
    }

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
        showNewOrSwitchTab("Вход");
    }

    @FXML
    private void onSignIn() {
        showNewOrSwitchTab("Вход");
    }

    @FXML
    private void onSignUp() {
        showNewOrSwitchTab("Регистрация");
    }

    @FXML
    private void onSettings() {
        showNewOrSwitchTab("Параметры");
    }

    private void showNewOrSwitchTab(String name) {
        Optional<Tab> optionalTab = tabPane.getTabs().stream()
                .filter(tab -> tab.getText().equals(name))
                .findFirst();
        if (optionalTab.isPresent()) {
            tabPane.getSelectionModel().select(optionalTab.get());
        } else {
            Parent load = loadView(nameFxmlMap.get(name));
            newTab(name, load);
        }
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

    private void newTab(String title, Parent load) {
        Tab signInTab = new Tab(title);
        signInTab.setContent(load);
        tabPane.getTabs().add(signInTab);
        tabPane.getSelectionModel().select(signInTab);
    }

    public void showSignInView() {
        Parent load = loadView(nameFxmlMap.get("Вход"));
        showOnCurrentTab("Вход", load);
    }

    public void showSignUpView() {
        Parent load = loadView(nameFxmlMap.get("Регистрация"));
        showOnCurrentTab("Регистрация", load);
    }

    private void showOnCurrentTab(String title, Parent load) {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        tab.setText(title);
        tab.setContent(load);
    }

}
