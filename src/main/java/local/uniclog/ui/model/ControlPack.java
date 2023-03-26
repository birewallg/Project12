package local.uniclog.ui.model;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import local.uniclog.model.actions.types.ActionType;
import local.uniclog.model.actions.types.EventStateType;
import local.uniclog.model.actions.types.MouseButtonType;
import local.uniclog.utils.ConfigConstants;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;

@Slf4j
@Getter
@Builder
public class ControlPack {
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
    private Label actionPaneLabel;
    private ChoiceBox<ActionType> actionChoiceBox;
    //region ListView
    private ListView<MacrosItem> macrosList;
    private TextField scriptNameTextField;
    private ObservableList<MacrosItem> macrosItemList;
    //endregion

    public ControlPack initialize() {
        topLogoLabel.setText(ConfigConstants.TOP_LOGO_TEXT);

        actionChoiceBox.getItems().setAll(ActionType.values());
        mouseActionChoiceBox.getItems().setAll(MouseButtonType.values());
        mouseActionChoiceBox.setValue(MouseButtonType.BUTTON_L);
        actionKeyPressStateChoiceBox.getItems().setAll(EventStateType.values());
        actionKeyPressStateChoiceBox.setValue(EventStateType.PRESS);

        actionChoiceBox.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    List.of(actionPaneLabel,
                                    mouseActionPane, sleepActionPane, whileActionPane,
                                    endActionPane, whileBreakPane, actionKeyPressPane)
                            .forEach(it -> it.setVisible(false));
                    switch (newValue) {
                        case MOUSE_CLICK -> mouseActionPane.setVisible(true);
                        case SLEEP -> sleepActionPane.setVisible(true);
                        case WHILE -> whileActionPane.setVisible(true);
                        case END -> endActionPane.setVisible(true);
                        case WHILE_BRAKE_BY_COLOR -> whileBreakPane.setVisible(true);
                        case KEY_PRESS -> actionKeyPressPane.setVisible(true);
                        default -> {
                            actionPaneLabel.setVisible(true);
                            log.debug("Action not choose");
                        }
                    }
                });

        macrosList.setItems(macrosItemList);
        MultipleSelectionModel<MacrosItem> items = macrosList.getSelectionModel();
        items.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (isNull(newValue)) return;
                    scriptNameTextField.setText(newValue.getName());
                    textAreaConsole.setText(newValue.getText());
                }
        );

        return this;
    }

}
