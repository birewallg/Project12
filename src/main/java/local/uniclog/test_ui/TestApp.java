package local.uniclog.test_ui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import local.uniclog.test_ui.shapes.LineEx;
import local.uniclog.test_ui.shapes.ShapeEx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestApp extends Application {


    List<Shape> shapes = new ArrayList<>();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        final LineEx path = new LineEx(0, 0, 0, 0);
        final ShapeEx point = new ShapeEx(path.getEndX(), path.getEndY());
        point.setOnMouseDragged(e -> {
            point.setX(e.getX());
            point.setY(e.getY());
            path.setEndX(point.getCenterX());
            path.setEndY(point.getCenterY());
        });


        final ShapeEx point1 = new ShapeEx(path.getEndX(), path.getEndY());
        point1.setOnMouseDragged(e -> {
            point1.setX(e.getX());
            point1.setY(e.getY());
            path.setStartX(point1.getCenterX());
            path.setStartY(point1.getCenterY());
        });

        final Group root = new Group(path, point, point1);
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    /*private Shape addShape() {

        final Circle point = new Circle(15);
        point.setCenterX(path.getEndX());
        point.setCenterY(path.getEndY());
        point.setFill(Paint.valueOf("red"));
        point.setOnMouseDragged(e -> {
            point.setCenterX(e.getX());
            point.setCenterY(e.getY());
            path.setEndX(point.getCenterX());
            path.setEndY(point.getCenterY());
        });

        shapes.add(point);

        return null;
    }

    final Line path = new Line(30, 30, 70, 75);
        path.setStrokeWidth(5);

        final Circle point = new Circle(15);
        point.setCenterX(path.getEndX());
        point.setCenterY(path.getEndY());
        point.setFill(Paint.valueOf("red"));

        point.setOnMouseDragged(e -> {

            point.setCenterX(e.getX());
            point.setCenterY(e.getY());

            path.setEndX(point.getCenterX());
            path.setEndY(point.getCenterY());

        });

        final Circle point1 = new Circle(15);
        point1.setCenterX(path.getEndX());
        point1.setCenterY(path.getEndY());
        point1.setFill(Paint.valueOf("red"));

        point1.setOnMouseDragged(e -> {

            point1.setCenterX(e.getX());
            point1.setCenterY(e.getY());

            path.setStartX(point1.getCenterX());
            path.setStartY(point1.getCenterY());

        });

        final Group root = new Group(path, point, point1);
        stage.setScene(new Scene(root));
        stage.show();
    */
}