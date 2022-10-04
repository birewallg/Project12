package local.uniclog.model.actions;

import local.uniclog.model.actions.types.ActionType;

import java.awt.*;
import java.util.Map;

import static local.uniclog.model.actions.types.ActionType.DEFAULT;

public interface ActionsInterface {

    /**
     * Action process
     *
     * @param args args
     * @throws InterruptedException exception
     */
    void execute(String... args) throws InterruptedException;

    default Robot getRobot() throws AWTException {
        return new Robot();
    }

    ActionsInterface fieldInjection(Map<String, String> args);

    default ActionType getType() {
        return DEFAULT;
    }
}
