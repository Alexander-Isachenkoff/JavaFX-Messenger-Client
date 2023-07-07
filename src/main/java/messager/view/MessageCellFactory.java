package messager.view;

import messager.controller.MessageCellController;
import messager.entities.TextMessage;

public class MessageCellFactory extends FxmlListCellFactory<TextMessage, MessageCellController> {

    public MessageCellFactory() {
        super("fxml/message.fxml");
    }

    @Override
    public void initController(TextMessage message, MessageCellController controller) {
        controller.setTextMessage(message);
    }

}
