package local.uniclog.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

public class UiController {
    @FXML
    private ToggleButton exit;

    public void onExit() {
        System.exit(0);
    }
    public void onMin() {
        Stage stage = (Stage)exit.getScene().getWindow();
        stage.setIconified(true);
    }
}