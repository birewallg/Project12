package local.uniclog.ui.controlls.actions;

import javafx.application.Platform;
import local.uniclog.model.actions.ActionsInterface;
import local.uniclog.services.DataConfigService;
import local.uniclog.ui.controlls.model.ControlPack;
import local.uniclog.ui.controlls.model.MacrosItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.requireNonNull;
import static local.uniclog.utils.ConfigConstants.EMPTY;

@Slf4j
@AllArgsConstructor
public abstract class ControlServiceAbstract {
    @Getter
    protected static ControlPack cp;
    @Setter
    protected static DataConfigService dataConfigService;

    public static void setControlPack(ControlPack controlPack) {
        cp = controlPack;
        cp.getTextAreaConsole().setOnKeyReleased(event -> macrosListRefresh());
        cp.getScriptNameTextField().setOnKeyReleased(event -> macrosListRefresh());
    }

    private static void macrosListRefresh() {
        var index = cp.getMacrosList().getSelectionModel().getSelectedIndex();
        MacrosItem item;
        if (index == -1) {
            item = new MacrosItem(cp.getScriptNameTextField().getText(), cp.getTextAreaConsole().getText(), EMPTY);
            macrosListAddItem(item);
        } else {
            item = cp.getMacrosList().getItems().get(index);
            item.setName(cp.getScriptNameTextField().getText());
            item.setText(cp.getTextAreaConsole().getText());
        }
        cp.getMacrosList().refresh();
    }

    private static void macrosListAddItem(MacrosItem item) {
        cp.getMacrosList().getItems().add(item);
        cp.getMacrosList().getSelectionModel().selectLast();
    }

    public void addActionTextToConsoleArea(ActionsInterface action) {
        Platform.runLater(() -> {
                    cp.getTextAreaConsole()
                            .setText(String.join("\n", cp.getTextAreaConsole().getText(), action.toString()));
                    macrosListRefresh();
                }
        );
    }

    public void addMacrosItemToList(MacrosItem item) {
        cp.getTextAreaConsole().clear();
        cp.getTextAreaConsole().setText(requireNonNull(item).getText());
        cp.getScriptNameTextField().setText(item.getName());
        if (cp.getMacrosList().getItems().isEmpty()) {
            macrosListAddItem(item);
        } else {
            cp.getMacrosList().getItems()
                    .get(cp.getMacrosList().getSelectionModel().getSelectedIndex())
                    .setName(item.getName());
        }
        macrosListRefresh();
    }

}
