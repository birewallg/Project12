package local.uniclog.services.support;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceWrapperTest {
    private static final String PATH_OF_CONFIG = "test.json";

    @Test
    void checkNotFoundConfigFile() {
        assertNull(FileServiceWrapper.loadObjectFromJson(PATH_OF_CONFIG, TestData.class));
    }

    @Test
    void saveLoadJsonTest() {
        var config = new TestData();
        var savedConfig = FileServiceWrapper.saveObjectAsJson(PATH_OF_CONFIG, config);
        var loadConfig = FileServiceWrapper.loadObjectFromJson(PATH_OF_CONFIG, TestData.class);
        assertAll(
                () -> assertEquals(loadConfig, savedConfig),
                () -> assertEquals(savedConfig, config),
                () -> assertEquals(loadConfig, config));
        assertTrue(new File(PATH_OF_CONFIG).delete());
    }

    @Test
    void getFileNameTest() {
        var path = "C:\\Users\\admin\\Desktop\\app\\app\\scripts\\filename.txt";
        assertEquals("filename", FileServiceWrapper.getFileName(path));
        path = "C:\\Users\\admin\\Desktop\\app\\app\\scripts\\filename";
        assertEquals("filename", FileServiceWrapper.getFileName(path));
    }

    static class TestData {
        final int num = 1;
        final String text = "text";
        final Set<String> setOfStrings = Set.of("str1", "str2", "str3", "str4");

        @Override
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof final TestData other)) return false;
            return Objects.equals(this.setOfStrings.toString(), other.setOfStrings.toString());
        }
    }
}