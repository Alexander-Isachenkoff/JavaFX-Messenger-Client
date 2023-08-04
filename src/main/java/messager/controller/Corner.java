package messager.controller;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

abstract class Corner extends Circle {

    private final int h;
    private final int v;
    private double localPressX;
    private double localPressY;

    public Corner(int h, int v, Cursor cursor) {
        this.h = h;
        this.v = v;
        setCursor(cursor);
        setRadius(8);
        setFill(Color.WHITE);
        setStroke(Color.DIMGRAY);

        setOnMousePressed(event -> {
            localPressX = event.getX();
            localPressY = event.getY();
        });

        setOnMouseDragged(this::onDragged);
    }

    abstract double getMinX();

    abstract double getMaxX();

    abstract double getMinY();

    abstract double getMaxY();

    private void onDragged(MouseEvent event) {
        double newX = event.getSceneX() - getParent().getLocalToSceneTransform().getTx() - localPressX;
        double newY = event.getSceneY() - getParent().getLocalToSceneTransform().getTy() - localPressY;
        newX = bound(newX, getMinX(), getMaxX());
        newY = bound(newY, getMinY(), getMaxY());
        double dx = newX - getLayoutX();
        double dy = newY - getLayoutY();
        double d = Math.min(h * dx, v * dy);

        setLayoutX(getLayoutX() + h * d);
        setLayoutY(getLayoutY() + v * d);
    }

    private double bound(double val, double min, double max) {
        return Math.min(Math.max(val, min), max);
    }

}
