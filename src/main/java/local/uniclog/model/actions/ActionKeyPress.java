package local.uniclog.model.actions;

import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import local.uniclog.utils.DataUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionKeyPress implements ActionsInterface {
    @Builder.Default
    private String keys = "";
    @Builder.Default
    private Integer keyCode = null;

    @Override
    public void execute(String... args) {
        actionPressText(keys);
        actionPressKey(keyCode);
    }

    private void actionPressText(String keys) {
        for (char c : keys.toCharArray()) {
            int code = KeyEvent.getExtendedKeyCodeForChar(c);
            if (!Objects.equals(KeyEvent.CHAR_UNDEFINED, code)) {
                actionPressKey(code);
            }
        }
    }

    @SneakyThrows
    private void actionPressKey(Integer code) {
        if (Objects.nonNull(code)) {
            Robot robot = getRobot();
            robot.keyPress(code);
            robot.delay(100);
            robot.keyRelease(code);
            robot.delay(100);
        }
    }

    @Override
    public ActionsInterface fieldInjection(Map<String, String> args) {
        if (Objects.nonNull(args))
            args.forEach(this::setFieldValue);
        return this;
    }

    private void setFieldValue(String key, String value) {
        switch (key) {
            case "keys" -> setKeys(value);
            case "keyCode" -> setKeyCode(DataUtils.getKeyCodeByString(value));

            default -> log.debug("Field: {}, not set: {}", key, value);
        }
    }

    @Override
    public String toString() {
        return format("%s [keys=%s, keyCode=%d]",
                getType().name(),
                keys, // todo convert to string value
                keyCode);
    }

    @Override
    public ActionType getType() {
        return ActionType.KEY_PRESS;
    }
}
