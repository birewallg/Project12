package local.uniclog.actions;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;

@Slf4j
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class MouseClick extends ActionAbstract {
    private Point point = new Point(0, 0);
    private Integer action = 0;
    private Integer count = 0;
    private Integer period = 0;
    private Integer sleepAfter = 0;

    @Override
    public void execute(String... args) {
        try {
            switch (action) {
                case 1:
                    Robot robot = getRobot();
                    robot.mouseMove(point.x, point.y);
                    robot.mousePress(BUTTON1_DOWN_MASK);
                    robot.mouseRelease(BUTTON1_DOWN_MASK);
                    break;

                case 2:
                    log.info("MouseClick: action not realise");
                    break;

                default:
                    log.info("MouseClick: {}", getType());
                    break;
            }
            log.info("MouseClick: {}", this);
        } catch (AWTException e) {
            log.info("MouseClick Error: {}, object {}", e.getMessage(), this);
        }
    }

    @Override
    public ActionType getType() {
        return ActionType.MOUSE_CLICK;
    }
}
