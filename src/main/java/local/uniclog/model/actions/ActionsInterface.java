package local.uniclog.model.actions;

import com.sun.jna.Library;
import com.sun.jna.Native;
import local.uniclog.model.actions.types.ActionType;

import java.awt.*;
import java.util.Map;

import static local.uniclog.model.actions.types.ActionType.DEFAULT;

public interface ActionsInterface {

    interface User32 extends Library {
        User32 INSTANCE = Native.load("user32.dll", User32.class);

        void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);
    }

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
