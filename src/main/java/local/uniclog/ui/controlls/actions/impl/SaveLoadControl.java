package local.uniclog.ui.controlls.actions.impl;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import local.uniclog.services.support.FileServiceWrapper;
import local.uniclog.ui.controlls.actions.ControlServiceAbstract;
import local.uniclog.ui.controlls.model.MacrosItem;

import java.io.File;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
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
                var item = FileServiceWrapper.write(textAreaConsole.getText(), file.getPath());
                addToMacrosList(item);
            }
        }
    }

    /**
     * Load configuration from file
     */
    public void onLoad() {
        var fileChooser = new FileChooser();
        //Set to user directory or go to default if it cannot access
        var userDirectoryString = System.getProperty("user.home");
        var userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File(DEFAULT_FILE_PATH);
        }
        fileChooser.setInitialDirectory(userDirectory);
        var file = fileChooser.showOpenDialog(cp.getTextAreaConsole().getScene().getWindow());
        if (file != null) {
            var alert = new Alert(NONE, "Load config from [" + file.getPath() + "]?", YES, NO);
            alert.showAndWait();
            if (alert.getResult() == YES) {
                var item = FileServiceWrapper.read(file.getPath());
                addToMacrosList(item);
            }
        }
    }

    private void addToMacrosList(MacrosItem item) {
        cp.getTextAreaConsole().clear();
        cp.getTextAreaConsole().setText(requireNonNull(item).getText());
        cp.getScriptNameTextField().setText(item.getName());
        if (cp.getMacrosList().getItems().isEmpty()) {
            cp.macrosListAddItem(item);
        } else {
            cp.getMacrosList().getItems()
                    .get(cp.getMacrosList().getSelectionModel().getSelectedIndex())
                    .setName(item.getName());
        }
        cp.getMacrosList().refresh();
    }
}
