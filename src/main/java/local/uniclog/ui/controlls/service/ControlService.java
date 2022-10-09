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
public abstract class ControlService {
    @Getter
    @Setter
    protected static ControlPack cp;

    public void addActionTextToConsoleArea(ActionsInterface action) {
        Platform.runLater(() -> cp.getTextAreaConsole()
                .setText(String.join("\n", cp.getTextAreaConsole().getText(), action.toString()))
        );
    }
}
