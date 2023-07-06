package messager.controller;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class NodeUtils {
    public static void setScene(Stage stage, Scene newScene) {
        Scene oldScene = stage.getScene();
        double width = oldScene.getWidth();
        double height = oldScene.getHeight();
        ((Region) newScene.getRoot()).setPrefSize(width, height);
        stage.setScene(newScene);
    }
}
