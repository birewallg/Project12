package local.uniclog.model;

import java.util.Arrays;

/**
 * click_l - (x, y, up/down/click, count, period, sleepAfter)
 * click_r - (x, y, up/down/click, count, period, sleepAfter)
 * sleep - (time)
 * log - (x, y, color, alert)
 * <p>
 * key - buttons ?
 * <p>
 * default - console log
 */
public enum ActionType {
    MOUSE_CLICK("MOUSE_CLICK"),
    SLEEP("SLEEP"),
    LOG("LOG"),

    DEFAULT("DEFAULT");

    private final String value;

    ActionType(String value) {
        this.value = value;
    }

    public static ActionType getType(String msg) {
        return Arrays.stream(ActionType.values())
                .filter(it -> it.value.equalsIgnoreCase(msg))
                .findFirst()
                .orElse(DEFAULT);
    }

    public String getStringValue() {
        return value;
    }
}
