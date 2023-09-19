package local.uniclog.utils

object DataUtils {

    /**
     * Get Integer value from String
     *
     * @param value        string value
     * @param defaultValue default
     * @return Integer
     */
    @JvmStatic
    fun getInteger(value: String, defaultValue: Int): Int =
        value.toIntOrNull() ?: defaultValue

    /**
     * Get Long value from String
     * @param value        string value
     * @param defaultValue default
     * @return Long
     */
    @JvmStatic
    fun getLong(value: String, defaultValue: Long): Long =
        value.toLongOrNull() ?: defaultValue

    /**
     * Get Boolean value from String
     *
     * @param value        string value
     * @param defaultValue default
     * @return Long
     */
    @JvmStatic
    fun getBoolean(value: String, defaultValue: Boolean): Boolean =
        value.toBooleanStrictOrNull() ?: defaultValue

    /**
     * Convert Int code to ASCII code
     *
     * @param code Int symbol
     * @return Name by ascii code
     */
    @JvmStatic
    fun convertCodeToASCII(code: Int): String? {
        return when (code) {
            in 48..57, in 65..90 -> Char(code).toString()
            in codeNameMap -> codeNameMap[code]
            else -> "{$code}"
        }
    }

    /**
     * Convert ASCII code to Int code
     *
     * @param codeName String name ascii symbol
     * @return ascii code
     */
    @JvmStatic
    fun convertASCIIToCode(codeName: String): Int? {
        return when {
            codeNameMap.containsValue(codeName) -> codeNameMap.entries.firstOrNull { it.value == codeName }?.key
            codeName.length == 1 -> codeName[0].code
            else -> """(?<=\{).*(?=})""".toRegex().find(codeName)?.groupValues?.get(0)?.toIntOrNull()
        }
    }

    private val codeNameMap = mapOf(
        9 to "TUB",
        20 to "CAPS_LOCK",
        13 to "ENTER",
        27 to "ESC",
        32 to "SPACE",
        160 to "R_SHIFT", 161 to "L_SHIFT",
        162 to "R_CTRL", 163 to "L_CTRL",
        164 to "L_ALT", 165 to "R_ALT",
        192 to "~",
        112 to "F1", 113 to "F2", 114 to "F3", 115 to "F4",
        116 to "F5", 117 to "F6", 118 to "F7", 119 to "F8",
        120 to "F9", 121 to "F10", 122 to "F11", 123 to "F12"
    )
}