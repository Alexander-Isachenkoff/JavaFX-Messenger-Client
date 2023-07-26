package messager.view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertUtil {
    public static void showErrorAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
        alert.setHeaderText(text);
        Platform.runLater(alert::showAndWait);
    }

    public static void showWarningAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    public static void showInfoAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
        alert.setHeaderText(text);
        alert.showAndWait();
    }
}
