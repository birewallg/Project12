package local.uniclog.ui.controlls.service.impl;

import javafx.application.Platform;
import local.uniclog.model.actions.impl.ActionKeyPress;
import local.uniclog.services.ThreadControlService;
import local.uniclog.ui.controlls.service.ControlServiceAbstract;
import local.uniclog.utils.DataUtils;

import static java.util.Objects.isNull;
import static local.uniclog.utils.ConfigConstants.*;

/**
 * App KeyPress controls
 *
 * @version 1.0
 */
public class KeyPressControl extends ControlServiceAbstract {

    public KeyPressControl() {
        if (isNull(cp)) throw new IllegalStateException(TEMPLATE_NOT_SET_CONTROLS);
    }

    /**
     * Button: Add Key Press info to TextArea Console
     */
    public void setActionKeyPressReaderAction() {
        if (ThreadControlService.getHookListenerState()) {
            cp.getActionKeyPressReaderButton().setText("Stop");
            cp.getActionKeyPressReaderButton().getStyleClass().removeAll(GUI_BUTTON_GREEN);
            cp.getActionKeyPressReaderButton().getStyleClass().add(GUI_BUTTON_RED);
            ThreadControlService.startHookListenerThread(this::setKeyPressInfo, -1, true);
        } else {
            cp.getActionKeyPressReaderButton().setText("Listen Key Code");
            cp.getActionKeyPressReaderButton().getStyleClass().removeAll(GUI_BUTTON_RED);
            cp.getActionKeyPressReaderButton().getStyleClass().add(GUI_BUTTON_GREEN);
            ThreadControlService.stopHookListenerThread();
        }
    }

    /**
     * Button: Add Key Press info to TextArea Console
     */
    public void setActionKeyPressReaderActionSingle() {
        var action = ActionKeyPress.builder()
                .text(cp.getActionKeyPressTextField().getText())
                .sleepAfter(DataUtils.getLong(cp.getActionKeyPressSleepAfterTextField().getText(), 0))
                .build();
        addActionTextToConsoleArea(action);
    }

    /**
     * Callback: Add Key Press info to TextArea Console
     *
     * @param keyCode key codes by action press
     */
    public void setKeyPressInfo(Integer keyCode) {
        var action = ActionKeyPress.builder()
                .keyCode(keyCode)
                .text(cp.getActionKeyPressTextField().getText())
                .sleepAfter(DataUtils.getLong(cp.getActionKeyPressSleepAfterTextField().getText(), 0))
                .eventStateType(cp.getActionKeyPressStateChoiceBox().getValue())
                .build();
        addActionTextToConsoleArea(action);

        Platform.runLater(() -> {
            cp.getActionKeyPressReaderButton().setText("Listen Key Code");
            cp.getActionKeyPressReaderButton().getStyleClass().removeAll(GUI_BUTTON_RED);
            cp.getActionKeyPressReaderButton().getStyleClass().add(GUI_BUTTON_GREEN);
        });
        ThreadControlService.stopHookListenerThread();
    }
}
