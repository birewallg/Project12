package local.uniclog.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataUtilsTest {

    @Test
    void getInteger() {
        assertEquals(5, DataUtils.getInteger("5", 0));
        assertEquals(0, DataUtils.getInteger("five", 0));
    }

    @Test
    void getLong() {
        assertEquals(5L, DataUtils.getLong("5", 0L));
        assertEquals(0L, DataUtils.getLong("five", 0L));
    }
}