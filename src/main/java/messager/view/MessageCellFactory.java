package messager.view;

import javafx.scene.Parent;
import messager.controller.MessageCellController;
import messager.entities.TextMessage;
import messager.entities.User;

import java.util.function.Supplier;

public class MessageCellFactory extends FxmlListCellFactory<TextMessage, MessageCellController> {

    private final Supplier<User> userSupplier;

    public MessageCellFactory(Supplier<User> userSupplier) {
        super("fxml/message.fxml");
        this.userSupplier = userSupplier;
    }

    @Override
    protected void initCell(TextMessage item, Parent cell) {
        String styleClass;
        if (item.getUserFrom().getName().equals(userSupplier.get().getName())) {
            styleClass = "my_message";
        } else {
            styleClass = "not_my_message";
        }
        cell.getStyleClass().add(styleClass);
    }

    @Override
    public void initController(TextMessage message, MessageCellController controller) {
        controller.setTextMessage(message);
    }
}
