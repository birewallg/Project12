package local.uniclog.model.actions;

import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import local.uniclog.model.MouseButtonType;
import local.uniclog.utils.DataUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.awt.event.InputEvent.*;
import static java.lang.String.format;
import static local.uniclog.model.MouseButtonType.BUTTON1;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MouseClick implements ActionsInterface {
    @Builder.Default
    private Point point = new Point(0, 0);
    @Builder.Default
    private MouseButtonType action = BUTTON1;
    @Builder.Default
    private Integer count = 0;
    @Builder.Default
    private Long period = 50L;
    @Builder.Default
    private Long sleepAfter = 0L;

    @Override
    @SneakyThrows
    public void execute(String... args) {
        switch (action) {
            case BUTTON1 -> mouseAction(BUTTON1_DOWN_MASK);
            case BUTTON2 -> mouseAction(BUTTON2_DOWN_MASK);
            case BUTTON3 -> mouseAction(BUTTON3_DOWN_MASK);
            default -> log.debug("MouseClick: {}", getType());
        }
        log.debug("MouseClick: {}", this);
    }

    @Override
    public ActionsInterface fieldInjection(Map<String, String> args) {
        args.forEach(this::setFieldValue);
        return this;
    }

    private void setFieldValue(String key, String value) {
        switch (key) {
            case "x" -> setPoint(new Point(DataUtils.getInteger(value, 0), (int) point.getY()));
            case "y" -> setPoint(new Point((int) point.getX(), DataUtils.getInteger(value, 0)));
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
                Robot robot = getRobot();
                robot.mouseMove(point.x, point.y);
                robot.mousePress(buttonCode);
                robot.mouseRelease(buttonCode);
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
        return format(
                "%s [action=%s, x=%d, y=%d, count=%d, wait=%d, sleepAfter=%d]",
                getType().name(),
                action,
                point.x, point.y,
                count,
                period,
                sleepAfter
        );
    }

    @Override
    public ActionType getType() {
        return ActionType.MOUSE_CLICK;
    }
}
