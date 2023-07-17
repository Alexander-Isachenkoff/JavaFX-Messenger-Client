package messager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import messager.controller.MainController;

public class Main extends Application {

    private static MainController mainController;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        mainController = fxmlLoader.getController();
        stage.setTitle("Client");
        stage.setScene(scene);
        stage.show();
    }

    public static MainController getMainController() {
        return mainController;
    }

}