package local.uniclog.ui.controlls;

import javafx.application.Platform;
import local.uniclog.model.actions.ActionsInterface;
import local.uniclog.services.DataConfigService;
import local.uniclog.ui.model.ControlPack;
import local.uniclog.ui.model.MacrosItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Boolean.FALSE;
import static java.lang.String.join;
import static java.util.Objects.requireNonNull;
import static local.uniclog.utils.ConfigConstants.EMPTY;

@Slf4j
@AllArgsConstructor
public abstract class ControlServiceAbstract {
    @Getter
    protected static ControlPack cp;
    protected static DataConfigService dataConfigService;

    public static void initializeControl(ControlPack controlPack) {
        cp = controlPack;
        cp.getTextAreaConsole().setOnKeyReleased(event -> macrosListRefresh());
        cp.getScriptNameTextField().setOnKeyReleased(event -> macrosListRefresh());
        cp.getTextAreaConsole().focusedProperty().addListener(ControlServiceAbstract::refreshIfUnfocused);
        cp.getScriptNameTextField().focusedProperty().addListener(ControlServiceAbstract::refreshIfUnfocused);
        // #PROJECT-12-T-7 начальная загрузка из конфигурации
        dataConfigService = new DataConfigService();
        dataConfigService.getItems().forEach(ControlServiceAbstract::macrosListLoadItem);
    }

    private static <T> void refreshIfUnfocused(T observable, Boolean oldValue, Boolean newValue) {
        if (FALSE.equals(newValue)) macrosListRefresh();
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
            dataConfigService.modifyItemByIndex(index, item);
        }
        cp.getMacrosList().refresh();
    }

    public static void macrosListAddItem(MacrosItem item) {
        Platform.runLater(() -> {
            cp.getMacrosList().getItems().add(item);
            cp.getMacrosList().getSelectionModel().selectLast();
            dataConfigService.addItem(item);
        });
    }

    public static void macrosListLoadItem(MacrosItem item) {
        Platform.runLater(() -> {
            cp.getMacrosList().getItems().add(item);
            cp.getMacrosList().getSelectionModel().selectFirst();
        });
    }

    public static void macrosListRemoveItem(Integer index) {
        var item = cp.getMacrosList().getItems().get(index);
        Platform.runLater(() -> {
            cp.getMacrosList().getItems().remove(item);
            dataConfigService.removeItem(item);
            cp.getMacrosList().refresh();
        });
    }

    public void addActionTextToConsoleArea(ActionsInterface action) {
        Platform.runLater(() -> {
            cp.getTextAreaConsole().setText(join("\n", cp.getTextAreaConsole().getText(), action.toString()));
            macrosListRefresh();
        });
    }

    public void addMacrosItemToList(MacrosItem item) {
        cp.getTextAreaConsole().clear();
        cp.getTextAreaConsole().setText(requireNonNull(item).getText());
        cp.getScriptNameTextField().setText(item.getName());
        if (cp.getMacrosList().getItems().isEmpty()) {
            macrosListAddItem(item);
            dataConfigService.addItem(item);
        } else {
            var index = cp.getMacrosList().getSelectionModel().getSelectedIndex();
            cp.getMacrosList().getItems().get(index)
                    .setName(item.getName());
            dataConfigService.modifyItemByIndex(index, item);
        }
        macrosListRefresh();
    }

}
