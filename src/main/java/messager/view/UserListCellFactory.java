package messager.view;

import messager.controller.UserCellController;
import messager.entities.User;

public class UserListCellFactory extends FxmlListCellFactory<User, UserCellController> {

    public UserListCellFactory() {
        super("fxml/user_cell.fxml");
    }

    @Override
    public void initController(User user, UserCellController controller) {
        controller.setUserName(user.getName());
    }
}

