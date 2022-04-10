package local.uniclog.model;

import java.util.Arrays;

public enum MouseButtonType {
    BUTTON1("BUTTON1"),
    BUTTON2("BUTTON2"),
    BUTTON3("BUTTON3");

    private final String value;

    MouseButtonType(String value) {
        this.value = value;
    }

    public static MouseButtonType getType(String msg) {
        return Arrays.stream(MouseButtonType.values())
                .filter(it -> it.value.equalsIgnoreCase(msg))
                .findFirst()
                .orElse(BUTTON1);
    }

    public String getStringValue() {
        return value;
    }
}
