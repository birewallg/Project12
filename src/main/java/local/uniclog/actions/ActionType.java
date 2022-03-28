package local.uniclog.actions;

/**
 * click_l - (x, y, up/down/click, count, period, sleepAfter)
 * click_r - (x, y, up/down/click, count, period, sleepAfter)
 * sleep - (time)
 * log - (x, y, color, alert)
 *
 * key - buttons ?
 *
 * default - console log
 */
public enum ActionType {
    MOUSE_CLICK,
    SLEEP,
    LOG,

    DEFAULT
}
