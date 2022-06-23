package local.uniclog.ui.controlls;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.awt.*;

import static javafx.scene.Cursor.*;

@NoArgsConstructor
@AllArgsConstructor
public class SceneControlService {

    private final Point deltaMove = new Point(0, 0);
    private final Point deltaResize = new Point(0, 0);
    private int border = 4;

    public void setOnMousePressed(MouseEvent mouseEvent, Parent root, Stage stage) {
        root.requestFocus();
        deltaMove.setLocation(stage.getX() - mouseEvent.getScreenX(), stage.getY() - mouseEvent.getScreenY());
        deltaResize.setLocation(stage.getWidth() - mouseEvent.getSceneX(), stage.getHeight() - mouseEvent.getSceneY());
    }

    public void setOnMouseMoved(MouseEvent mouseEvent, Scene scene) {
        var mouseEventX = mouseEvent.getSceneX();
        var mouseEventY = mouseEvent.getSceneY();
        var sceneWidth = scene.getWidth();
        var sceneHeight = scene.getHeight();
        var cursorEvent = DEFAULT;
        if (mouseEventX < border && mouseEventY < border) {
            cursorEvent = NW_RESIZE;
        } else if (mouseEventX < border && mouseEventY > sceneHeight - border) {
            cursorEvent = SW_RESIZE;
        } else if (mouseEventX > sceneWidth - border && mouseEventY < border) {
            cursorEvent = NE_RESIZE;
        } else if (mouseEventX > sceneWidth - border && mouseEventY > sceneHeight - border) {
            cursorEvent = SE_RESIZE;
        } else if (mouseEventX < border) {
            cursorEvent = W_RESIZE;
        } else if (mouseEventX > sceneWidth - border) {
            cursorEvent = E_RESIZE;
        } else if (mouseEventY < border) {
            cursorEvent = N_RESIZE;
        } else if (mouseEventY > sceneHeight - border) {
            cursorEvent = S_RESIZE;
        }
        scene.setCursor(cursorEvent);
    }

    public void setOnMouseDragged(MouseEvent mouseEvent, Scene scene, Stage stage) {
        if (scene.getCursor().equals(DEFAULT)) {
            stage.setX(mouseEvent.getScreenX() + deltaMove.getX());
            stage.setY(mouseEvent.getScreenY() + deltaMove.getY());
            return;
        }
        if (!scene.getCursor().equals(W_RESIZE) && !scene.getCursor().equals(E_RESIZE)) {
            horizontalResize(mouseEvent, scene, stage);
        }
        if (!scene.getCursor().equals(N_RESIZE) && !scene.getCursor().equals(S_RESIZE)) {
            verticalResize(mouseEvent, scene, stage);
        }
    }

    private void horizontalResize(MouseEvent mouseEvent, Scene scene, Stage stage) {
        var minHeight = stage.getMinHeight() > (border * 2) ? stage.getMinHeight() : (border * 2);
        if (scene.getCursor().equals(NW_RESIZE) || scene.getCursor().equals(N_RESIZE) || scene.getCursor().equals(NE_RESIZE)) {
            if (stage.getHeight() > minHeight || deltaResize.getY() < 0) {
                stage.setHeight(stage.getY() - mouseEvent.getScreenY() + stage.getHeight());
                stage.setY(mouseEvent.getScreenY());
            }
        } else {
            if (stage.getHeight() > minHeight || mouseEvent.getSceneY() + deltaResize.getY() - stage.getHeight() > 0) {
                stage.setHeight(mouseEvent.getSceneY() + deltaResize.getY());
            }
        }
    }

    private void verticalResize(MouseEvent mouseEvent, Scene scene, Stage stage) {
        var minWidth = stage.getMinWidth() > (border * 2) ? stage.getMinWidth() : (border * 2);
        if (scene.getCursor().equals(NW_RESIZE) || scene.getCursor().equals(W_RESIZE) || scene.getCursor().equals(SW_RESIZE)) {
            if (stage.getWidth() > minWidth || deltaResize.getX() < 0) {
                stage.setWidth(stage.getX() - mouseEvent.getScreenX() + stage.getWidth());
                stage.setX(mouseEvent.getScreenX());
            }
        } else {
            if (stage.getWidth() > minWidth || mouseEvent.getSceneX() + deltaResize.getX() - stage.getWidth() > 0) {
                stage.setWidth(mouseEvent.getSceneX() + deltaResize.getX());
            }
        }
    }
}
