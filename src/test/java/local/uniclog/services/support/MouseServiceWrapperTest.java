package local.uniclog.services.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MouseServiceWrapperTest {

    @Test
    void getMousePointerTest() {
        assertNotNull(MouseServiceWrapper.getMousePointer());
    }

    @Test
    void getPixelColorTest() {
        assertNotNull(MouseServiceWrapper.getPixelColor());
    }
}