package local.uniclog.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataUtilsTest {

    @Test
    void getIntegerTest() {
        assertEquals(5, DataUtils.getInteger("5", 0));
        assertEquals(0, DataUtils.getInteger("five", 0));
    }

    @Test
    void getLongTest() {
        assertEquals(5L, DataUtils.getLong("5", 0L));
        assertEquals(0L, DataUtils.getLong("five", 0L));
    }

    @Test
    void getBooleanTest() {
        assertTrue(DataUtils.getBoolean("true", false));
        assertFalse(DataUtils.getBoolean("false", false));
        assertFalse(DataUtils.getBoolean("text", false));
    }

    @Test
    void convertCodeToASCIITest() {
        assertEquals(DataUtils.convertCodeToASCII(65), "A");
        assertEquals(DataUtils.convertCodeToASCII(48), "0");
        assertEquals(DataUtils.convertCodeToASCII(13), "ENTER");
        assertEquals(DataUtils.convertCodeToASCII(123), "F12");
        assertEquals(DataUtils.convertCodeToASCII(1), "{1}");
        assertEquals(DataUtils.convertCodeToASCII(192), "~");
    }

    @Test
    void convertASCIIToCodeTest() {
        assertEquals(DataUtils.convertASCIIToCode("A"), 65);
        assertEquals(DataUtils.convertASCIIToCode("0"), 48);
        assertEquals(DataUtils.convertASCIIToCode("ENTER"), 13);
        assertEquals(DataUtils.convertASCIIToCode("F12"), 123);
        assertEquals(DataUtils.convertASCIIToCode("{1}"), 1);
        assertEquals(DataUtils.convertASCIIToCode("{200}"), 200);
        assertEquals(DataUtils.convertASCIIToCode("~"), 192);
    }
}