package local.uniclog.ui.controlls.service;

import javafx.application.Platform;
import local.uniclog.model.actions.ActionsInterface;
import local.uniclog.ui.controlls.model.ControlPack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public abstract class ControlServiceAbstract {
    @Getter
    @Setter
    protected static ControlPack cp;

    /**
     * Add action info to text-area console
     *
     * @param action action
     */
    public void addActionTextToConsoleArea(ActionsInterface action) {
        Platform.runLater(() -> cp.getTextAreaConsole()
                .setText(String.join("\n", cp.getTextAreaConsole().getText(), action.toString()))
        );
    }
}
