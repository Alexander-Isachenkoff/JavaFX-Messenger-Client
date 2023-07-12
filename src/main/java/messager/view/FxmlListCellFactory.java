package messager.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import messager.Main;

import java.io.IOException;

public abstract class FxmlListCellFactory<T, C> implements Callback<ListView<T>, ListCell<T>> {

    private final String fxml;

    public FxmlListCellFactory(String fxml) {
        this.fxml = fxml;
    }

    @Override
    public ListCell<T> call(ListView<T> param) {
        return new FxmlCell();
    }

    public abstract void initController(T item, C controller);

    protected void initCell(T item, Parent cell) {
    }

    private class FxmlCell extends ListCell<T> {

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
                try {
                    Parent cell = fxmlLoader.load();
                    initCell(item, cell);
                    C controller = fxmlLoader.getController();
                    initController(item, controller);
                    setGraphic(cell);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                setGraphic(null);
            }
        }
    }

}


