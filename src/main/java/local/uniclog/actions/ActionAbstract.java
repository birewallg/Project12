package local.uniclog.actions;

import java.awt.*;

abstract class ActionAbstract implements ActionsInterface {

    public Robot getRobot() throws AWTException {
        // todo may be JNI ??
        return new Robot();
    }
}
