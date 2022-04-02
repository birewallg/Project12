package local.uniclog.model;

import java.awt.*;

import static local.uniclog.model.ActionType.*;

public interface ActionsInterface {
    void execute(String... args);

    default Robot getRobot() throws AWTException {
        // todo may be JNI ??
        return new Robot();
    }
    default ActionType getType() {
        return DEFAULT;
    }
}
