package local.uniclog.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void getBoolean() {
        assertTrue(DataUtils.getBoolean("true", false));
        assertFalse(DataUtils.getBoolean("false", false));
        assertFalse(DataUtils.getBoolean("text", false));
    }
}