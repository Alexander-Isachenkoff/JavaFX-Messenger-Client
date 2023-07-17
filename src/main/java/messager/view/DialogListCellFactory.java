package messager.view;

import messager.controller.DialogCellController;
import messager.entities.Dialog;
import messager.entities.User;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DialogListCellFactory extends FxmlListCellFactory<Dialog, DialogCellController> {

    private final Supplier<User> userSupplier;
    private Consumer<Dialog> onDelete = dialog -> {
    };

    public DialogListCellFactory(Supplier<User> userSupplier) {
        super("fxml/dialog_cell.fxml");
        this.userSupplier = userSupplier;
    }

    @Override
    public void initController(Dialog dialog, DialogCellController controller) {
        controller.setDialog(dialog, userSupplier.get());
        controller.setOnDelete(dialog1 -> onDelete.accept(dialog1));
    }

    public void setOnDelete(Consumer<Dialog> onDelete) {
        this.onDelete = onDelete;
    }
}

