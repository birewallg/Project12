package local.uniclog.model;

import java.util.Arrays;

public enum MouseButtonType {
    BUTTON_L("BUTTON_L"),
    BUTTON_R("BUTTON_R"),
    BUTTON_M("BUTTON_M");

    private final String value;

    MouseButtonType(String value) {
        this.value = value;
    }

    public static MouseButtonType getType(String msg) {
        return Arrays.stream(MouseButtonType.values())
                .filter(it -> it.value.equalsIgnoreCase(msg))
                .findFirst()
                .orElse(BUTTON_L);
    }

    public String getStringValue() {
        return value;
    }
}
