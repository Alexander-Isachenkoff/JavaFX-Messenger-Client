package messager.controller;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class NodeUtils {

    public static Tab getParentTab(Node node) {
        Node parent = node;
        do {
            parent = parent.getParent();
            if (parent == null) {
                return null;
            }
        } while (!(parent instanceof TabPane));
        return ((TabPane) parent).getSelectionModel().getSelectedItem();
    }

}
