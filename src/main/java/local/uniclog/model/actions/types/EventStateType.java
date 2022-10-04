package local.uniclog.model.actions.types;

import java.util.Arrays;

public enum EventStateType {
    PRESS("PRESS"),
    UP("UP"),
    DOWN("DOWN");

    private final String value;

    EventStateType(String value) {
        this.value = value;
    }

    public static EventStateType getType(String msg) {
        return Arrays.stream(EventStateType.values())
                .filter(it -> it.value.equalsIgnoreCase(msg))
                .findFirst()
                .orElse(PRESS);
    }

    public String getStringValue() {
        return value;
    }
}
