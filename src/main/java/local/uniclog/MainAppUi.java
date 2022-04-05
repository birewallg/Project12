package local.uniclog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static javafx.stage.StageStyle.UNDECORATED;

public class MainAppUi extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        Parent root = loader.load();
        final Point delta = new Point(0, 0);
        root.setOnMousePressed(mouseEvent -> {
            root.requestFocus();
            delta.setLocation(stage.getX() - mouseEvent.getScreenX(), stage.getY() - mouseEvent.getScreenY());
        });
        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() + delta.getX());
            stage.setY(mouseEvent.getScreenY() + delta.getY());
        });
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/css.css")).toExternalForm());
        stage.setOpacity(0.95);
        stage.initStyle(UNDECORATED);
        stage.setTitle("Simple Clicker");
        // stage.setAlwaysOnTop(true)
        stage.toFront();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}