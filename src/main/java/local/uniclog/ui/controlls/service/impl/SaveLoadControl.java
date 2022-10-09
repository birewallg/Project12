package local.uniclog.ui.controlls.service.impl;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import local.uniclog.services.support.FileServiceWrapper;
import local.uniclog.ui.controlls.service.ControlServiceAbstract;

import java.io.File;

import static java.util.Objects.isNull;
import static javafx.scene.control.Alert.AlertType.NONE;
import static javafx.scene.control.ButtonType.NO;
import static javafx.scene.control.ButtonType.YES;
import static local.uniclog.utils.ConfigConstants.DEFAULT_FILE_PATH;
import static local.uniclog.utils.ConfigConstants.TEMPLATE_NOT_SET_CONTROLS;

/**
 * App SaveLoad controls
 *
 * @version 1.0
 */
public class SaveLoadControl extends ControlServiceAbstract {

    public SaveLoadControl() {
        if (isNull(cp)) throw new IllegalStateException(TEMPLATE_NOT_SET_CONTROLS);
    }

    /**
     * Button: Load configuration
     */
    public void onLoad() {
        this.onLoad(cp.getTextAreaConsole());
    }

    /**
     * Button: Save configuration to file
     */
    public void onSave() {
        this.onSave(cp.getTextAreaConsole());
    }

    /**
     * Save configuration to file
     *
     * @param textAreaConsole console
     */
    private void onSave(TextArea textAreaConsole) {
        var fileChooser = new FileChooser();
        //Set to user directory or go to default if it cannot access
        var userDirectoryString = System.getProperty("user.home");
        var userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File(DEFAULT_FILE_PATH);
        }
        fileChooser.setInitialDirectory(userDirectory);
        var extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        var file = fileChooser.showSaveDialog(textAreaConsole.getScene().getWindow());
        if (file != null) {
            var alert = new Alert(NONE, "Save config to [" + file.getPath() + "]?", YES, NO);
            alert.showAndWait();
            if (alert.getResult() == YES) {
                FileServiceWrapper.write(textAreaConsole.getText(), file.getPath());
            }
        }
    }

    /**
     * Load configuration from file
     *
     * @param textAreaConsole console
     */
    private void onLoad(TextArea textAreaConsole) {
        var fileChooser = new FileChooser();
        //Set to user directory or go to default if it cannot access
        var userDirectoryString = System.getProperty("user.home");
        var userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File(DEFAULT_FILE_PATH);
        }
        fileChooser.setInitialDirectory(userDirectory);
        var file = fileChooser.showOpenDialog(textAreaConsole.getScene().getWindow());
        if (file != null) {
            var alert = new Alert(NONE, "Load config from [" + file.getPath() + "]?", YES, NO);
            alert.showAndWait();
            if (alert.getResult() == YES) {
                textAreaConsole.clear();
                textAreaConsole.setText(FileServiceWrapper.read(file.getPath()));
            }
        }
    }
}
