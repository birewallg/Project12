package local.uniclog.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import local.uniclog.utils.FileServiceWrapper;

public class UiController {
    // Main Controls ==================================================
    @FXML
    private ToggleButton exit;

    public void onExit() {
        System.exit(0);
    }

    public void onMin() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.setIconified(true);
    }

    // ================================================================
    @FXML
    private TextArea textArea;

    public void onLoad() {
        FileServiceWrapper file = new FileServiceWrapper();
        textArea.setText(file.read());
    }

    public void onSave() {
        FileServiceWrapper file = new FileServiceWrapper();
        file.write(textArea.getText());
    }
}