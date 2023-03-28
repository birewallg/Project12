package local.uniclog.services;

import local.uniclog.ui.model.MacrosItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static local.uniclog.utils.ConfigConstants.TEMPLATE_CONFIG_PATH;
import static org.junit.jupiter.api.Assertions.*;

class DataConfigServiceTest {
    private final MacrosItem item = new MacrosItem("item", "content", TEMPLATE_CONFIG_PATH);
    private DataConfigService service;

    @BeforeEach
    void setUp() {
        service = new DataConfigService();
        service.clearItems();
    }

    @AfterEach
    void tearDown() {
        assertTrue(new File(TEMPLATE_CONFIG_PATH).delete());
    }

    @Test
    void initializeConfigTest() {
        assertAll(
                () -> assertNotNull(service),
                () -> assertNotNull(service.getItemsClone()),
                () -> assertTrue(service.getItemsClone().isEmpty())
        );
    }

    @Test
    void addNewItemTest() {
        service.addItem(item);
        service.forceSaveConfiguration();
        assertEquals(List.of(item), service.getItemsClone());
        assertEquals(List.of(item), new DataConfigService().getItemsClone());
    }

    @Test
    void addItemsTest() {
        var item2 = new MacrosItem("item_2", "content", TEMPLATE_CONFIG_PATH);
        service.addItem(item);
        service.addItem(item2);
        service.forceSaveConfiguration();
        assertEquals(List.of(item, item2), service.getItemsClone());
        assertEquals(List.of(item, item2), new DataConfigService().getItemsClone());
    }

    @Test
    void getItemsTest() {
        service.addItem(item);
        service.forceSaveConfiguration();
        assertEquals(List.of(item), service.getItemsClone());
    }

    @Test
    void getRemoveItemsTest() {
        service.addItem(item);
        service.forceSaveConfiguration();
        assertEquals(List.of(item), new DataConfigService().getItemsClone());
        service.removeItem(item);
        service.forceSaveConfiguration();
        assertTrue(new DataConfigService().getItemsClone().isEmpty());
    }

    @Test
    void modifyItemByIndexText() {
        service.addItem(item);
        service.forceSaveConfiguration();
        assertEquals(List.of(item), new DataConfigService().getItemsClone());
        item.setText("newText");
        service.modifyItemByIndex(0, item);
        var actual = service.getItemsClone().get(0).getText();
        assertEquals("newText", actual);
        assertEquals(1, service.getItemsClone().size());
    }
}