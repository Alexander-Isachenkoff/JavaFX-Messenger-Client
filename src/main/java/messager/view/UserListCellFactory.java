package messager.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import messager.Main;
import messager.controller.UserCellController;
import messager.entities.User;

import java.io.IOException;

public class UserListCellFactory implements Callback<ListView<User>, ListCell<User>> {
    @Override
    public ListCell<User> call(ListView<User> param) {
        return new UserListCell();
    }
}

class UserListCell extends ListCell<User> {

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
    }

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (!empty) {

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/user_cell.fxml"));
            try {
                Parent cell = fxmlLoader.load();
                UserCellController controller = fxmlLoader.getController();
                controller.setUserName(user.getName());
                setGraphic(cell);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
