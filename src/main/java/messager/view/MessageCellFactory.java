package messager.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import messager.Main;
import messager.controller.MessageCellController;
import messager.entities.TextMessage;

import java.io.IOException;

public class MessageCellFactory implements Callback<ListView<TextMessage>, ListCell<TextMessage>> {
    @Override
    public ListCell<TextMessage> call(ListView<TextMessage> param) {
        return new MessageCell();
    }
}

class MessageCell extends ListCell<TextMessage> {

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
    }

    @Override
    protected void updateItem(TextMessage message, boolean empty) {
        super.updateItem(message, empty);
        if (!empty) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/message.fxml"));
            try {
                Parent cell = fxmlLoader.load();
                MessageCellController controller = fxmlLoader.getController();
                controller.setTextMessage(message);
                setGraphic(cell);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
