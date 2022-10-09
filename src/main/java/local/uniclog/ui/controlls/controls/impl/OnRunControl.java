package local.uniclog.ui.controlls.controls.impl;

import javafx.application.Platform;
import local.uniclog.services.ThreadControlService;
import local.uniclog.ui.controlls.controls.ControlServiceAbstract;

import java.util.Objects;

import static java.util.Objects.isNull;
import static local.uniclog.utils.ConfigConstants.*;

/**
 * App Run controls
 *
 * @version 1.0
 */
public class OnRunControl extends ControlServiceAbstract {

    public OnRunControl() {
        if (isNull(cp)) throw new IllegalStateException(TEMPLATE_NOT_SET_CONTROLS);
    }

    /**
     * Button: Run Script
     */
    public void onRunAction() {
        onRunActionCompleteByUser(null);
    }

    public void onRunActionCompleteByUser(Integer complete) {
        if (Objects.isNull(complete) && ThreadControlService.getRunExecuteState()) {
            Platform.runLater(() -> {
                cp.getOnRunActionButton().setText("Stop");
                cp.getOnRunActionButton().getStyleClass().add(GUI_BUTTON_RED);
            });
            ThreadControlService.startRunExecuteThread(
                    cp.getTextAreaConsole().getText(),
                    this::onRunActionCompleteCallback,
                    this::onRunActionCompleteByUser
            );
        } else {
            Platform.runLater(() -> {
                cp.getOnRunActionButton().setText("Run");
                cp.getOnRunActionButton().getStyleClass().removeAll(GUI_BUTTON_RED);
                cp.getOnRunActionButton().getStyleClass().add(GUI_BUTTON_GREEN);
                cp.getOnRunActionButton().setDisable(true);
            });
            ThreadControlService.stopRunExecuteThread();
        }
    }

    public void onRunActionCompleteCallback(Boolean ignore) {
        Platform.runLater(() -> {
            cp.getOnRunActionButton().setText("Run");
            cp.getOnRunActionButton().getStyleClass().removeAll(GUI_BUTTON_RED);
            cp.getOnRunActionButton().getStyleClass().add(GUI_BUTTON_GREEN);
            cp.getOnRunActionButton().setDisable(false);
        });
        ThreadControlService.stopRunExecuteThread();
    }
}
