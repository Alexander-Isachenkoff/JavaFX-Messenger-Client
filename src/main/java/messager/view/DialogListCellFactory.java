package messager.view;

import messager.controller.DialogCellController;
import messager.entities.Dialog;
import messager.entities.User;

import java.util.function.Supplier;

public class DialogListCellFactory extends FxmlListCellFactory<Dialog, DialogCellController> {

    private final Supplier<User> userSupplier;

    public DialogListCellFactory(Supplier<User> userSupplier) {
        super("fxml/user_cell.fxml");
        this.userSupplier = userSupplier;
    }

    @Override
    public void initController(Dialog dialog, DialogCellController controller) {
        controller.setDialog(dialog, userSupplier.get());
    }
}

