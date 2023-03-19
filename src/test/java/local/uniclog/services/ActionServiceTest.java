package local.uniclog.services;

import local.uniclog.model.actions.ActionsInterface;
import org.junit.jupiter.api.Test;

import java.util.List;

import static local.uniclog.model.actions.types.ActionType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionServiceTest {
    private ActionService service;

    @Test
    void setConfigurationTest() {
        var actionList = List.of(
                "MOUSE_CLICK [action=BUTTON_L, x=1113, y=888]",
                "KEY_PRESS [keyCode=52, state=PRESS]",
                "WHILE_BRAKE_BY_COLOR [code=52]",
                "WHILE [count=1]"
        );
        var expectedActionList = List.of(MOUSE_CLICK, KEY_PRESS, WHILE_BRAKE_BY_COLOR, WHILE, END);

        service = new ActionService(actionList);
        var actions = service.getContainer().getData().stream()
                .map(ActionsInterface::getType).toList();
        assertEquals(expectedActionList, actions);
    }

    @Test
    void setConfigurationWithNoParamsTest() {
        var actionList = List.of("MOUSE_CLICK");
        var expectedActionList = List.of(MOUSE_CLICK, END);

        service = new ActionService(actionList);
        var actions = service.getContainer().getData().stream()
                .map(ActionsInterface::getType).toList();
        assertEquals(expectedActionList, actions);
    }

    @Test
    void setConfigurationDefaultTest() {
        var actionList = List.of("DEFAULT");
        var expectedActionList = List.of(END);

        service = new ActionService(actionList);
        var actions = service.getContainer().getData().stream()
                .map(ActionsInterface::getType).toList();
        assertEquals(expectedActionList, actions);
    }

    @Test
    void setConfigurationUnCorrectDataTest() {
        var actionList = List.of("UN_CORRECT");
        var expectedActionList = List.of(END);

        service = new ActionService(actionList);
        var actions = service.getContainer().getData().stream()
                .map(ActionsInterface::getType).toList();
        assertEquals(expectedActionList, actions);
    }

    @Test
    void setConfigurationNoCorrectDataTest() {
        var actionList = List.of("SLEEP [count]");
        var expectedActionList = List.of(SLEEP, END);

        service = new ActionService(actionList);
        var actions = service.getContainer().getData().stream()
                .map(ActionsInterface::getType).toList();
        assertEquals(expectedActionList, actions);
    }
}