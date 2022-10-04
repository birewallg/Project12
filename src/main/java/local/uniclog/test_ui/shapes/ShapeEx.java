package local.uniclog.test_ui.shapes;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ShapeEx extends Rectangle {
    public ShapeEx(double dX, double dY) {
        super(75, 50);
        this.setX(dX);
        this.setY(dY);
        this.setFill(Paint.valueOf("red"));
    }

    public double getCenterX() {
        return this.getX() + (this.getWidth() / 2);
    }

    public double getCenterY() {
        return this.getY() + (this.getHeight() / 2);
    }
}
