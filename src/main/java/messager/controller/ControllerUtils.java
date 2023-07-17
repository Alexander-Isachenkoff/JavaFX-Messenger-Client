package messager.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import messager.Main;
import messager.entities.User;

import java.io.IOException;

public class ControllerUtils {

    public static void loadDialogsView(User user, Tab tab) {
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

        tab.setText(user.getName());
        tab.setContent(load);
    }

}
