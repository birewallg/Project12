package local.uniclog.utils;

import java.util.HashMap;
import java.util.Map;

public class DataUtils {
    private DataUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get Integer value from String
     *
     * @param value        string value
     * @param defaultValue default
     * @return Integer
     */
    public static Integer getInteger(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    /**
     * Get Long value from String
     *
     * @param value        string value
     * @param defaultValue default
     * @return Long
     */
    public static Long getLong(String value, long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    /**
     * Get Key Code value from String
     *
     * @param value string value
     * @return (Integer) Key Code
     */
    public static Integer getKeyCodeByString(String value) {
        return getKeyMap().getOrDefault(value, 0);
    }

    private static Map<String, Integer> getKeyMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("f1", 112);
        map.put("f2", 113);
        map.put("f3", 114);
        map.put("f4", 115);
        map.put("f5", 116);
        map.put("f6", 117);
        map.put("f7", 118);
        map.put("f8", 119);
        map.put("f9", 120);
        map.put("f10", 121);
        map.put("f11", 122);
        map.put("f12", 123);
        return map;
    }
}
