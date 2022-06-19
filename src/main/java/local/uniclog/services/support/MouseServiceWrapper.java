package local.uniclog.services.support;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

import static local.uniclog.utils.ConfigConstants.TEMPLATE_UTILITY_CLASS;

@Slf4j
public class MouseServiceWrapper {

    private MouseServiceWrapper() {
        throw new IllegalStateException(TEMPLATE_UTILITY_CLASS);
    }

    public static Point getMousePointer() {
        var point = MouseInfo.getPointerInfo().getLocation();
        log.debug("{}", point);
        return point;
    }

    public static Color getPixelColor() {
        var point = getMousePointer();
        return getPixelColor(point.x, point.y);
    }

    @SneakyThrows
    public static Color getPixelColor(int x, int y) {
        var color = new Robot().getPixelColor(x, y);
        log.debug("{}", color);
        return color;
    }
}
