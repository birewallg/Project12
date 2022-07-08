package local.uniclog.services.support;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileServiceWrapperTest {

    private static final String PATH_OF_CONFIG = "test.json";

    @Test
    void saveLoadJsonTest() {
        var config = new TestData();
        FileServiceWrapper.saveJson(PATH_OF_CONFIG, config, TestData.class);
        var assertConfig = FileServiceWrapper.loadJson(PATH_OF_CONFIG, TestData.class);

        assertEquals(assertConfig, config);
    }

    static class TestData {
        int num = 1;
        String text = "text";
        Set<String> setOfStrings = Set.of("str1", "str2", "str3", "str4");

        @Override
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof final TestData other)) return false;
            if (this.num != other.num) return false;
            if (!Objects.equals(this.text, other.text)) return false;
            return Objects.equals(this.setOfStrings.toString(), other.setOfStrings.toString());
        }
    }
}