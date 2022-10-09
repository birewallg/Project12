package local.uniclog.model.actions.impl;

import local.uniclog.model.actions.ActionsInterface;
import local.uniclog.model.actions.types.ActionType;
import local.uniclog.model.actions.types.MouseButtonType;
import local.uniclog.utils.DataUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.awt.event.InputEvent.*;
import static java.lang.String.format;
import static local.uniclog.model.actions.types.MouseButtonType.BUTTON_L;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MouseClick implements ActionsInterface {
    @Builder.Default
    private Point point = null;
    @Builder.Default
    private MouseButtonType action = BUTTON_L;
    @Builder.Default
    private Integer count = 1;
    @Builder.Default
    private Long period = 0L;
    @Builder.Default
    private Long sleepAfter = 0L;

    @Override
    @SneakyThrows
    public void execute(String... args) throws InterruptedException {
        log.debug("{}", this);
        switch (action) {
            case BUTTON_L -> mouseAction(BUTTON1_DOWN_MASK);
            case BUTTON_M -> mouseAction(BUTTON2_DOWN_MASK);
            case BUTTON_R -> mouseAction(BUTTON3_DOWN_MASK);
            default -> log.debug("{}", this);
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
            case "x" -> setPoint(new Point(
                    DataUtils.getInteger(value, 0),
                    (Objects.nonNull(point)) ? (int) point.getY() : 0));
            case "y" -> setPoint(new Point(
                    (Objects.nonNull(point)) ? (int) point.getX() : 0,
                    DataUtils.getInteger(value, 0)));
            case "action" -> setAction(MouseButtonType.getType(value));
            case "count" -> setCount(DataUtils.getInteger(value, 0));
            case "wait" -> setPeriod(DataUtils.getLong(value, 0));
            case "sleepAfter" -> setSleepAfter(DataUtils.getLong(value, 0));
            default -> log.debug("Field: {}, not set: {}", key, value);
        }
    }

    private void mouseAction(int buttonCode) {
        try {
            Integer loopCount = 0;
            while (!loopCount.equals(count)) {
                var robot = getRobot();
                if (Objects.nonNull(point))
                    robot.mouseMove(point.x, point.y);
                robot.mousePress(buttonCode);
                robot.mouseRelease(buttonCode);
                if (period > 0)
                    TimeUnit.MILLISECONDS.sleep(period);
                loopCount++;
                if (loopCount.equals(count))
                    TimeUnit.MILLISECONDS.sleep(sleepAfter);
            }
        } catch (InterruptedException | AWTException e) {
            log.error("MouseClick Error: {}, object {}", e.getMessage(), this);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(format("%s [", getType().name()));
        sb.append(format("action=%s", action));
        if (Objects.nonNull(point)) sb.append(format(", x=%d, y=%d", point.x, point.y));
        if (count != 1) sb.append(format(", count=%d", count));
        if (period > 0) sb.append(format(", wait=%d", period));
        if (sleepAfter != 0) sb.append(format(", sleepAfter=%d", sleepAfter));
        sb.append("]");
        return sb.toString();
    }

    @Override
    public ActionType getType() {
        return ActionType.MOUSE_CLICK;
    }
}
