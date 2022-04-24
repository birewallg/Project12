package local.uniclog.model.actions;

import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import local.uniclog.utils.DataUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionWhileBrakeByColor implements ActionsInterface {

    @Builder.Default
    private Color color = new Color(0);
    @Builder.Default
    private Point point = new Point(0, 0);
    @Builder.Default
    private Boolean equal = false;

    @Override
    public void execute(String... args) throws InterruptedException {
        log.debug("{}", this);
    }

    /**
     * equal = true, color = true -> false
     * equal = true, color = false -> true
     * equal = false, color = true -> true
     * equal = false, color = false -> false
     *
     * @return color changes
     */
    @SneakyThrows
    public boolean checkColorChange() {
        return equal != getRobot().getPixelColor(point.x, point.y).equals(color);
    }

    @Override
    public ActionsInterface fieldInjection(Map<String, String> args) {
        if (Objects.nonNull(args))
            args.forEach(this::setFieldValue);
        return this;
    }

    private void setFieldValue(String key, String value) {
        switch (key) {
            case "red" -> setColor(new Color(DataUtils.getInteger(value, 0), color.getGreen(), color.getBlue()));
            case "green" -> setColor(new Color(color.getRed(), DataUtils.getInteger(value, 0), color.getBlue()));
            case "blue" -> setColor(new Color(color.getRed(), color.getGreen(), DataUtils.getInteger(value, 0)));
            case "x" -> setPoint(new Point(DataUtils.getInteger(value, 0), (int) point.getY()));
            case "y" -> setPoint(new Point((int) point.getX(), DataUtils.getInteger(value, 0)));
            case "equal" -> setEqual(Boolean.parseBoolean(value));
            default -> log.debug("Field: {}, not set: {}", key, value);
        }
    }

    @Override
    public String toString() {
        return format("%s [red=%d, green=%d, blue=%d, x=%d, y=%d, equal=%b]",
                getType().name(),
                color.getRed(), color.getGreen(), color.getBlue(),
                point.x, point.y,
                equal);
    }

    @Override
    public ActionType getType() {
        return ActionType.WHILE_BRAKE_BY_COLOR;
    }
}
