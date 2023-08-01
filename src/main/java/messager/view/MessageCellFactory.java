package messager.view;

import javafx.geometry.NodeOrientation;
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
        NodeOrientation orientation;
        if (item.getUserFrom().getName().equals(userSupplier.get().getName())) {
            styleClass = "my_message";
            orientation = NodeOrientation.RIGHT_TO_LEFT;
        } else {
            styleClass = "not_my_message";
            orientation = NodeOrientation.LEFT_TO_RIGHT;
        }
        Parent centeredMessageWrapper = (Parent) cell.getChildrenUnmodifiable().get(0);
        centeredMessageWrapper.getStyleClass().add(styleClass);
        centeredMessageWrapper.setNodeOrientation(orientation);
    }

    @Override
    public void initController(TextMessage message, MessageCellController controller) {
        controller.setMessage(message);
    }
}
