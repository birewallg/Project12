package local.uniclog.test_ui;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class Scene3D extends Application {

    public static void main(String[] args) {
        launch();
    }

    private double startX = 0;
    private double baseRotate = 0;

    @Override
    public void start(Stage stage) throws Exception {
        DoubleProperty rotate = new SimpleDoubleProperty(45);
        Sphere sphere = new Sphere(100);
        sphere.setTranslateX(400);
        sphere.setTranslateY(250);
        Box box = new Box(150, 150, 150);
        box.setTranslateX(150);
        box.setTranslateY(250);
        box.setRotationAxis(new Point3D(1, 1, 1));
        box.rotateProperty().bind(rotate);
        Cylinder cylinder = new Cylinder(100, 150);
        cylinder.setTranslateX(650);
        cylinder.setTranslateY(250);
        cylinder.setRotationAxis(new Point3D(1, 1, 1));
        cylinder.rotateProperty().bind(rotate);
        Group root = new Group(sphere, box, cylinder);
        root.setOnMousePressed(event -> {
            startX = event.getX();
            baseRotate = rotate.get();
        });
        root.setOnMouseDragged(event -> {
            rotate.set(baseRotate - (event.getX() - startX) / 2);
        });
        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.show();
    }
}
