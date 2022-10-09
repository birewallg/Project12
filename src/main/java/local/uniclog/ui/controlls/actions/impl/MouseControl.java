package local.uniclog.ui.controlls.actions.impl;

import local.uniclog.model.actions.impl.MouseClick;
import local.uniclog.services.ThreadControlService;
import local.uniclog.services.support.MouseServiceWrapper;
import local.uniclog.ui.controlls.actions.ControlServiceAbstract;
import local.uniclog.utils.DataUtils;

import java.util.Objects;

import static java.util.Objects.isNull;
import static local.uniclog.utils.ConfigConstants.*;

/**
 * App Mouse controls
 *
 * @version 1.0
 */
public class MouseControl extends ControlServiceAbstract {

    public MouseControl() {
        if (isNull(cp)) throw new IllegalStateException(TEMPLATE_NOT_SET_CONTROLS);
    }

    /**
     * Button: Read Mouse Coordinates Add it to text console
     */
    public void setMouseActionReaderButton() {
        if (ThreadControlService.getHookListenerState()) {
            cp.getMouseActionReaderButton().setText("Scan Action (Ctrl)");
            cp.getMouseActionReaderButton().getStyleClass().removeAll(GUI_BUTTON_GREEN);
            cp.getMouseActionReaderButton().getStyleClass().add(GUI_BUTTON_RED);
            ThreadControlService.startHookListenerThread(this::setMouseInfo, 162, false);
        } else {
            cp.getMouseActionReaderButton().setText("Scan Action (Ctrl)");
            cp.getMouseActionReaderButton().getStyleClass().removeAll(GUI_BUTTON_RED);
            cp.getMouseActionReaderButton().getStyleClass().add(GUI_BUTTON_GREEN);
            ThreadControlService.stopHookListenerThread();
        }
    }

    /**
     * Button: Add Mouse no coords action
     */
    public void setMouseActionNoCoordsButton() {
        this.setMouseInfo(null);
    }

    /**
     * Callback: Add mouse info to TextArea Console
     *
     * @param code ignore
     */
    public void setMouseInfo(Integer code) {
        var action = MouseClick.builder()
                .action(cp.getMouseActionChoiceBox().getValue())
                .point(Objects.nonNull(code) ? MouseServiceWrapper.getMousePointer() : null)
                .count(DataUtils.getInteger(cp.getMouseActionCountTextField().getText(), 0))
                .period(DataUtils.getLong(cp.getMouseActionDelayTimeTextField().getText(), 0L))
                .sleepAfter(DataUtils.getLong(cp.getMouseActionSleepAfterTextField().getText(), 0L))
                .build();
        addActionTextToConsoleArea(action);
    }
}
