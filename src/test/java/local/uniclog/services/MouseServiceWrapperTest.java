package local.uniclog.services;

import local.uniclog.services.support.MouseServiceWrapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MouseServiceWrapperTest {

    @Test
    void getMousePointer() {
        assertNotNull(MouseServiceWrapper.getMousePointer());
    }

    @Test
    void getPixelColor() {
        assertNotNull(MouseServiceWrapper.getPixelColor());
    }
}