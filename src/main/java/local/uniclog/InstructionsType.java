package local.uniclog;

/**
 * click_l - (x, y, up/down/click, count, period)
 * click_r - (x, y, up/down/click, count, period)
 * sleep - (time)
 * log - (x, y, color, alert)
 *
 * key - buttons ?
 *
 * default - console log
 */
public enum InstructionsType {
    CLICK_ML,
    CLICK_MR,
    SLEEP,
    LOG,

    DEFAULT
}
