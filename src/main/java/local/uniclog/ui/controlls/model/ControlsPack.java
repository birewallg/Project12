package local.uniclog.ui.controlls.model;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import local.uniclog.model.actions.types.ActionType;
import local.uniclog.model.actions.types.EventStateType;
import local.uniclog.model.actions.types.MouseButtonType;
import local.uniclog.utils.ConfigConstants;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Builder
public class ControlsPack {
    private TextField actionKeyPressSleepAfterTextField;
    private Button actionKeyPressReaderButton;
    private TextField actionKeyPressTextField;
    private Pane actionKeyPressPane;
    private ChoiceBox<EventStateType> actionKeyPressStateChoiceBox;
    private Button mouseBrakeActionReaderButton;
    private Pane whileBreakPane;
    private Pane endActionPane;
    private TextField whileActionCountTextField;
    private CheckBox whileActionCountCheckBox;
    private Pane whileActionPane;
    private TextField sleepActionCountTextField;
    private Pane sleepActionPane;
    private Button onRunActionButton;
    private ChoiceBox<MouseButtonType> mouseActionChoiceBox;
    private TextField mouseActionCountTextField;
    private TextField mouseActionDelayTimeTextField;
    private TextField mouseActionSleepAfterTextField;
    private Button mouseActionReaderButton;
    private Pane mouseActionPane;
    private TextArea textAreaConsole;
    private ToggleButton exit;
    private Label topLogoLabel;
    private ChoiceBox<ActionType> actionChoiceBox;

    public void init() {
        topLogoLabel.setText(ConfigConstants.TOP_LOGO_TEXT);

        actionChoiceBox.getItems().setAll(ActionType.values());
        mouseActionChoiceBox.getItems().setAll(MouseButtonType.values());
        mouseActionChoiceBox.setValue(MouseButtonType.BUTTON_L);
        actionKeyPressStateChoiceBox.getItems().setAll(EventStateType.values());
        actionKeyPressStateChoiceBox.setValue(EventStateType.PRESS);

        actionChoiceBox.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    mouseActionPane.setVisible(false);
                    sleepActionPane.setVisible(false);
                    whileActionPane.setVisible(false);
                    endActionPane.setVisible(false);
                    whileBreakPane.setVisible(false);
                    actionKeyPressPane.setVisible(false);
                    switch (newValue) {
                        case MOUSE_CLICK -> mouseActionPane.setVisible(true);
                        case SLEEP -> sleepActionPane.setVisible(true);
                        case WHILE -> whileActionPane.setVisible(true);
                        case END -> endActionPane.setVisible(true);
                        case WHILE_BRAKE_BY_COLOR -> whileBreakPane.setVisible(true);
                        case KEY_PRESS -> actionKeyPressPane.setVisible(true);
                        default -> log.debug("Action not choose");
                    }
                });
    }
}
