package local.uniclog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import local.uniclog.ui.controlls.actions.SceneControlService;

import java.io.IOException;

import static javafx.stage.StageStyle.UNDECORATED;

public class MainAppUi extends Application {


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        var loader = new FXMLLoader(getClass().getResource("view.fxml"));
        var root = (Parent) loader.load();
        var scene = new Scene(root);

        var controlService = new SceneControlService();
        root.setOnMouseMoved(mouseEvent -> controlService.setOnMouseMoved(mouseEvent, scene));
        root.setOnMousePressed(mouseEvent -> controlService.setOnMousePressed(mouseEvent, root, stage));
        root.setOnMouseDragged(mouseEvent -> controlService.setOnMouseDragged(mouseEvent, scene, stage));

        stage.setOpacity(0.90);
        stage.initStyle(UNDECORATED);
        stage.setTitle("Uniclog Software");
        // stage.setAlwaysOnTop(true)
        stage.toFront();

        stage.setScene(scene);
        stage.show();
    }


}