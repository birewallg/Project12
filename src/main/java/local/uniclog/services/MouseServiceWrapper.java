package local.uniclog.services;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

@Slf4j
public class MouseServiceWrapper {

    private MouseServiceWrapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Point getMousePointer() {
        Point point = MouseInfo.getPointerInfo().getLocation();
        log.debug("{}", point);
        return point;
    }

    @SneakyThrows
    public static Color getPixelColor() {
        Point point = getMousePointer();
        Color color = new Robot().getPixelColor(point.x, point.y);
        log.debug("{}", color);
        return color;
    }
}
