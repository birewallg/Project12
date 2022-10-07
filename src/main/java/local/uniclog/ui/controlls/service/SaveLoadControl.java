package local.uniclog.ui.controlls.service;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import local.uniclog.services.support.FileServiceWrapper;

import java.io.File;

import static javafx.scene.control.Alert.AlertType.NONE;
import static javafx.scene.control.ButtonType.NO;
import static javafx.scene.control.ButtonType.YES;
import static local.uniclog.utils.ConfigConstants.DEFAULT_FILE_PATH;
import static local.uniclog.utils.ConfigConstants.TEMPLATE_UTILITY_CLASS;

public class SaveLoadControl {
    private SaveLoadControl() {
        throw new IllegalStateException(TEMPLATE_UTILITY_CLASS);
    }

    /**
     * Save configuration to file
     *
     * @param textAreaConsole console
     */
    public static void onSave(TextArea textAreaConsole) {
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
    public static void onLoad(TextArea textAreaConsole) {
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
