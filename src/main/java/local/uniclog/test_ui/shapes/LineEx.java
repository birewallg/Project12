package local.uniclog.test_ui.shapes;

import javafx.scene.shape.Line;

public class LineEx extends Line {

    public LineEx(double startX,
                  double startY,
                  double endX,
                  double endY) {
        super(startX, startY, endX, endY);
        this.setStrokeWidth(5);
    }

}
