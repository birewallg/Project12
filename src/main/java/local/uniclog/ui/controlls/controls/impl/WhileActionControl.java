package local.uniclog.ui.controlls.controls.impl;

import javafx.application.Platform;
import local.uniclog.model.actions.impl.ActionWhile;
import local.uniclog.model.actions.impl.ActionWhileBrakeByColor;
import local.uniclog.services.ThreadControlService;
import local.uniclog.services.support.MouseServiceWrapper;
import local.uniclog.ui.controlls.controls.ControlServiceAbstract;
import local.uniclog.utils.DataUtils;

import static java.util.Objects.isNull;
import static local.uniclog.utils.ConfigConstants.*;

/**
 * App While controls
 *
 * @version 1.0
 */
public class WhileActionControl extends ControlServiceAbstract {

    public WhileActionControl() {
        if (isNull(cp)) throw new IllegalStateException(TEMPLATE_NOT_SET_CONTROLS);
    }

    /**
     * Button: Add While Break Action info to TextArea Console
     */
    public void setWhileBreakActionReaderAction() {
        if (ThreadControlService.getHookListenerState()) {
            cp.getMouseBrakeActionReaderButton().setText("Stop");
            cp.getMouseBrakeActionReaderButton().getStyleClass().removeAll(GUI_BUTTON_GREEN);
            cp.getMouseBrakeActionReaderButton().getStyleClass().add(GUI_BUTTON_RED);
            ThreadControlService.startHookListenerThread(this::setMouseColorInfo, 162, true);
        } else {
            cp.getMouseBrakeActionReaderButton().setText("Get Color");
            cp.getMouseBrakeActionReaderButton().getStyleClass().removeAll(GUI_BUTTON_RED);
            cp.getMouseBrakeActionReaderButton().getStyleClass().add(GUI_BUTTON_GREEN);
            ThreadControlService.stopHookListenerThread();
        }
    }

    /**
     * Button: Add While Action info to TextArea Console
     */
    public void setWhileActionReaderAction() {
        var action = ActionWhile.builder()
                .count(DataUtils.getInteger(cp.getWhileActionCountTextField().getText(), 0))
                .eternity(cp.getWhileActionCountCheckBox().isSelected())
                .build();
        addActionTextToConsoleArea(action);
    }

    /**
     * Callback: Add mouse color info to TextArea Console
     *
     * @param ignore ignore
     */
    public void setMouseColorInfo(Integer ignore) {
        var action = ActionWhileBrakeByColor.builder()
                .point(MouseServiceWrapper.getMousePointer())
                .color(MouseServiceWrapper.getPixelColor())
                .build();
        addActionTextToConsoleArea(action);

        Platform.runLater(() -> {
            cp.getMouseBrakeActionReaderButton().setText("Get Color");
            cp.getMouseBrakeActionReaderButton().getStyleClass().removeAll(GUI_BUTTON_RED);
            cp.getMouseBrakeActionReaderButton().getStyleClass().add(GUI_BUTTON_GREEN);
        });
        ThreadControlService.stopHookListenerThread();
    }
}
