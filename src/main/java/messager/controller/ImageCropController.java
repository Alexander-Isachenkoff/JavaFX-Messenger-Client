package messager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.SneakyThrows;
import messager.Main;

import java.util.Arrays;
import java.util.function.Consumer;

public class ImageCropController {
    @FXML
    private Pane wrapper;
    @FXML
    private Circle circle;
    @FXML
    private ImageView imageView;
    @FXML
    private Rectangle rect;

    private final Circle brCorner = new Corner(1, 1, Cursor.SE_RESIZE) {
        @Override
        double getMinX() {
            return blCorner.getLayoutX();
        }

        @Override
        double getMaxX() {
            return getParent().getLayoutBounds().getWidth();
        }

        @Override
        double getMinY() {
            return trCorner.getLayoutY();
        }

        @Override
        double getMaxY() {
            return getParent().getLayoutBounds().getHeight();
        }
    };
    private final Circle blCorner = new Corner(-1, 1, Cursor.SW_RESIZE) {
        @Override
        double getMinX() {
            return 0;
        }

        @Override
        double getMaxX() {
            return brCorner.getLayoutX();
        }

        @Override
        double getMinY() {
            return tlCorner.getLayoutY();
        }

        @Override
        double getMaxY() {
            return getParent().getLayoutBounds().getHeight();
        }
    };
    private final Circle trCorner = new Corner(1, -1, Cursor.NE_RESIZE) {
        @Override
        double getMinX() {
            return tlCorner.getLayoutX();
        }

        @Override
        double getMaxX() {
            return getParent().getLayoutBounds().getWidth();
        }

        @Override
        double getMinY() {
            return 0;
        }

        @Override
        double getMaxY() {
            return brCorner.getLayoutY();
        }
    };
    private final Circle tlCorner = new Corner(-1, -1, Cursor.NW_RESIZE) {
        @Override
        double getMinX() {
            return 0;
        }

        @Override
        double getMaxX() {
            return trCorner.getLayoutX();
        }

        @Override
        double getMinY() {
            return 0;
        }

        @Override
        double getMaxY() {
            return blCorner.getLayoutY();
        }
    };

    private double movePressedX;
    private double movePressedY;

    private Consumer<com.sun.javafx.geom.Rectangle> onAccept = r -> {};

    @SneakyThrows
    public static void show(Image image, Window owner, Consumer<com.sun.javafx.geom.Rectangle> onAccept) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/image_crop.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);

        ImageCropController controller = fxmlLoader.getController();
        controller.setImage(image);
        controller.setOnAccept(onAccept);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void initialize() {
        wrapper.getChildren().addAll(brCorner, blCorner, trCorner, tlCorner);

        rect.setOnMousePressed(event -> {
            movePressedX = event.getX();
            movePressedY = event.getY();
        });

        rect.setOnMouseDragged(event -> {
            double width = getWidth();
            tlCorner.setLayoutX(Math.max(0, Math.min(tlCorner.getLayoutX() + event.getX() - movePressedX, imageView.getBoundsInLocal().getWidth() - width)));
            brCorner.setLayoutX(tlCorner.getLayoutX() + width);

            double height = getHeight();
            tlCorner.setLayoutY(Math.max(0, Math.min(tlCorner.getLayoutY() + event.getY() - movePressedY, imageView.getBoundsInLocal().getHeight() - height)));
            brCorner.setLayoutY(tlCorner.getLayoutY() + height);
        });

        for (Circle circle : Arrays.asList(brCorner, blCorner, tlCorner, trCorner)) {
            circle.layoutXProperty().addListener((observable, oldValue, newValue) -> drawRect());
            circle.layoutYProperty().addListener((observable, oldValue, newValue) -> drawRect());
        }

        tlCorner.layoutXProperty().bindBidirectional(blCorner.layoutXProperty());
        brCorner.layoutYProperty().bindBidirectional(blCorner.layoutYProperty());
        brCorner.layoutXProperty().bindBidirectional(trCorner.layoutXProperty());
        tlCorner.layoutYProperty().bindBidirectional(trCorner.layoutYProperty());
    }

    private void drawCircleImage() {
        double x = (circle.getLayoutX() - circle.getRadius()) / imageView.getBoundsInLocal().getWidth() * imageView.getImage().getWidth();
        double y = (circle.getLayoutY() - circle.getRadius()) / imageView.getBoundsInLocal().getHeight() * imageView.getImage().getHeight();
        double width = circle.getRadius() * 2 / imageView.getBoundsInLocal().getWidth() * imageView.getImage().getWidth();
        double height = circle.getRadius() * 2 / imageView.getBoundsInLocal().getHeight() * imageView.getImage().getHeight();
        if (x < 0 || y < 0 || width <= 0 || height <= 0) {
            return;
        }
        Image fxImage = new WritableImage(imageView.getImage().getPixelReader(),
                (int) Math.round(x),
                (int) Math.round(y),
                (int) Math.round(width),
                (int) Math.round(height)
        );
        circle.setFill(new ImagePattern(fxImage));
    }

    private double getHeight() {
        return brCorner.getLayoutY() - trCorner.getLayoutY();
    }

    private double getWidth() {
        return trCorner.getLayoutX() - tlCorner.getLayoutX();
    }

    private void drawRect() {
        rect.setLayoutX(tlCorner.getLayoutX());
        rect.setLayoutY(tlCorner.getLayoutY());
        rect.setWidth(brCorner.getLayoutX() - blCorner.getLayoutX());
        rect.setHeight(brCorner.getLayoutY() - trCorner.getLayoutY());

        double radius = (Math.min(getHeight(), getWidth())) / 2;
        circle.setRadius(radius);
        circle.setLayoutX(tlCorner.getLayoutX() + radius);
        circle.setLayoutY(tlCorner.getLayoutY() + radius);

        drawCircleImage();
    }

    private void setImage(Image image) {
        imageView.setImage(image);
        wrapper.setMaxWidth(imageView.getBoundsInLocal().getWidth());
        wrapper.setMaxHeight(imageView.getBoundsInLocal().getHeight());

        double size = Math.min(imageView.getBoundsInLocal().getWidth(), imageView.getBoundsInLocal().getHeight());
        tlCorner.setLayoutX((imageView.getBoundsInLocal().getWidth() - size) / 2);
        tlCorner.setLayoutY((imageView.getBoundsInLocal().getHeight() - size) / 2);
        brCorner.setLayoutX((imageView.getBoundsInLocal().getWidth() + size) / 2);
        brCorner.setLayoutY((imageView.getBoundsInLocal().getHeight() + size) / 2);
    }

    @FXML
    private void onAccept() {
        double x = rect.getLayoutX() / imageView.getBoundsInLocal().getWidth() * imageView.getImage().getWidth();
        double y = rect.getLayoutY() / imageView.getBoundsInLocal().getHeight() * imageView.getImage().getHeight();
        double width = rect.getWidth() / imageView.getBoundsInLocal().getWidth() * imageView.getImage().getWidth();
        double height = rect.getHeight() / imageView.getBoundsInLocal().getHeight() * imageView.getImage().getHeight();
        onAccept.accept(new com.sun.javafx.geom.Rectangle((int) x, (int) y, (int) width, (int) height));
        wrapper.getScene().getWindow().hide();
    }

    public void setOnAccept(Consumer<com.sun.javafx.geom.Rectangle> onAccept) {
        this.onAccept = onAccept;
    }

    @FXML
    private void onCancel() {
        wrapper.getScene().getWindow().hide();
    }

}

