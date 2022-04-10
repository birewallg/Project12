package local.uniclog.model;

import java.awt.*;
import java.util.Map;

import static local.uniclog.model.ActionType.DEFAULT;

public interface ActionsInterface {
    void execute(String... args);

    default Robot getRobot() throws AWTException {
        // todo may be JNI ??
        return new Robot();
    }

    ActionsInterface fieldInjection(Map<String, String> args);

    default ActionType getType() {
        return DEFAULT;
    }
}
