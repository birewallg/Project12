package local.uniclog.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import local.uniclog.model.actions.ActionsInterface;
import local.uniclog.model.actions.impl.*;
import local.uniclog.model.actions.types.ActionType;
import local.uniclog.model.actions.types.EventStateType;
import local.uniclog.model.actions.types.MouseButtonType;
import local.uniclog.services.ThreadControlService;
import local.uniclog.services.support.MouseServiceWrapper;
import local.uniclog.ui.controlls.model.ControlsPack;
import local.uniclog.ui.controlls.service.SaveLoadControl;
import local.uniclog.ui.controlls.service.SceneControlService;
import local.uniclog.utils.DataUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static local.uniclog.utils.ConfigConstants.GUI_BUTTON_GREEN;
import static local.uniclog.utils.ConfigConstants.GUI_BUTTON_RED;

@Slf4j
public class AppController {
    @Getter
    private ControlsPack cp;

    //region : Controls Pack
    @FXML
    private TextField actionKeyPressSleepAfterTextField;
    @FXML
    private Button actionKeyPressReaderButton;
    @FXML
    private TextField actionKeyPressTextField;
    @FXML
    private Pane actionKeyPressPane;
    @FXML
    private ChoiceBox<EventStateType> actionKeyPressStateChoiceBox;
    @FXML
    private Button mouseBrakeActionReaderButton;
    @FXML
    private Pane whileBreakPane;
    @FXML
    private Pane endActionPane;
    @FXML
    private TextField whileActionCountTextField;
    @FXML
    private CheckBox whileActionCountCheckBox;
    @FXML
    private Pane whileActionPane;
    @FXML
    private TextField sleepActionCountTextField;
    @FXML
    private Pane sleepActionPane;
    @FXML
    private Button onRunActionButton;
    @FXML
    private ChoiceBox<MouseButtonType> mouseActionChoiceBox;
    @FXML
    private TextField mouseActionCountTextField;
    @FXML
    private TextField mouseActionDelayTimeTextField;
    @FXML
    private TextField mouseActionSleepAfterTextField;
    @FXML
    private Button mouseActionReaderButton;
    @FXML
    private Pane mouseActionPane;
    @FXML
    private TextArea textAreaConsole;
    @FXML
    private ToggleButton exit;
    @FXML
    private Label topLogoLabel;
    @FXML
    private ChoiceBox<ActionType> actionChoiceBox;

    private ControlsPack createControlsPack() {
        return ControlsPack.builder()
                .actionKeyPressSleepAfterTextField(actionKeyPressSleepAfterTextField)
                .actionKeyPressReaderButton(actionKeyPressReaderButton)
                .actionKeyPressTextField(actionKeyPressTextField)
                .actionKeyPressPane(actionKeyPressPane)
                .actionKeyPressStateChoiceBox(actionKeyPressStateChoiceBox)
                .mouseBrakeActionReaderButton(mouseBrakeActionReaderButton)
                .whileBreakPane(whileBreakPane)
                .endActionPane(endActionPane)
                .whileActionCountTextField(whileActionCountTextField)
                .whileActionCountCheckBox(whileActionCountCheckBox)
                .whileActionPane(whileActionPane)
                .sleepActionCountTextField(sleepActionCountTextField)
                .sleepActionPane(sleepActionPane)
                .onRunActionButton(onRunActionButton)
                .mouseActionChoiceBox(mouseActionChoiceBox)
                .mouseActionCountTextField(mouseActionCountTextField)
                .mouseActionDelayTimeTextField(mouseActionDelayTimeTextField)
                .mouseActionSleepAfterTextField(mouseActionSleepAfterTextField)
                .mouseActionReaderButton(mouseActionReaderButton)
                .mouseActionPane(mouseActionPane)
                .textAreaConsole(textAreaConsole)
                .exit(exit)
                .topLogoLabel(topLogoLabel)
                .actionChoiceBox(actionChoiceBox)
                .build();
    }
    //endregion

    //region : Controls Pack Main Controls Block

    /**
     * Button: Exit
     */
    public void onExit() {
        SceneControlService.onExit();
    }

    /**
     * Button: Minimize window
     */
    public void onMin() {
        SceneControlService.onMin((Stage) cp.getExit().getScene().getWindow());
    }
    //endregion

    public void initialize() {
        cp = createControlsPack();
        cp.init();

        log.debug("App Controller init");
    }

    //region : Action: save load context

    /**
     * Button: Load configuration
     */
    public void onLoad() {
        SaveLoadControl.onLoad(textAreaConsole);
    }

    /**
     * Button: Save configuration to file
     */
    public void onSave() {
        SaveLoadControl.onSave(textAreaConsole);
    }
    //endregion

    /**
     * Button: Read Mouse Coordinates Add it to text console
     */
    public void setMouseActionReaderButton() {
        if (ThreadControlService.getHookListenerState()) {
            mouseActionReaderButton.setText("Scan Action (Ctrl)");
            mouseActionReaderButton.getStyleClass().removeAll(GUI_BUTTON_GREEN);
            mouseActionReaderButton.getStyleClass().add(GUI_BUTTON_RED);
            ThreadControlService.startHookListenerThread(this::setMouseInfo, 162, false);
        } else {
            mouseActionReaderButton.setText("Scan Action (Ctrl)");
            mouseActionReaderButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            mouseActionReaderButton.getStyleClass().add(GUI_BUTTON_GREEN);
            ThreadControlService.stopHookListenerThread();
        }
    }

    /**
     * Button: Add Mouse no coords action
     */
    public void setMouseActionNoCoordsButton() {
        this.setMouseInfo(null);
    }

    /**
     * Callback: Add mouse info to TextArea Console
     *
     * @param code ignore
     */
    public void setMouseInfo(Integer code) {
        var action = MouseClick.builder()
                .action(mouseActionChoiceBox.getValue())
                .point(Objects.nonNull(code) ? MouseServiceWrapper.getMousePointer() : null)
                .count(DataUtils.getInteger(mouseActionCountTextField.getText(), 0))
                .period(DataUtils.getLong(mouseActionDelayTimeTextField.getText(), 0L))
                .sleepAfter(DataUtils.getLong(mouseActionSleepAfterTextField.getText(), 0L))
                .build();
        addActionTextToConsoleArea(action);
    }

    /**
     * Button: Add sleep action info to TextArea Console
     */
    public void setSleepActionReaderAction() {
        var action = Sleep.builder()
                .time(DataUtils.getLong(sleepActionCountTextField.getText(), 0L))
                .build();
        addActionTextToConsoleArea(action);
    }

    /**
     * Button: Add While Action info to TextArea Console
     */
    public void setWhileActionReaderAction() {
        var action = ActionWhile.builder()
                .count(DataUtils.getInteger(whileActionCountTextField.getText(), 0))
                .eternity(whileActionCountCheckBox.isSelected())
                .build();
        addActionTextToConsoleArea(action);
    }

    /**
     * Button: Add While Break Action info to TextArea Console
     */
    public void setWhileBreakActionReaderAction() {
        if (ThreadControlService.getHookListenerState()) {
            mouseBrakeActionReaderButton.setText("Stop");
            mouseBrakeActionReaderButton.getStyleClass().removeAll(GUI_BUTTON_GREEN);
            mouseBrakeActionReaderButton.getStyleClass().add(GUI_BUTTON_RED);
            ThreadControlService.startHookListenerThread(this::setMouseColorInfo, 162, true);
        } else {
            mouseBrakeActionReaderButton.setText("Get Color");
            mouseBrakeActionReaderButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            mouseBrakeActionReaderButton.getStyleClass().add(GUI_BUTTON_GREEN);
            ThreadControlService.stopHookListenerThread();
        }
    }

    /**
     * Callback: Add mouse color info to TextArea Console
     *
     * @param ignore ignore
     */
    public void setMouseColorInfo(Integer ignore) {
        var action = ActionWhileBrakeByColor.builder()
                .point(MouseServiceWrapper.getMousePointer())
                .color(MouseServiceWrapper.getPixelColor())
                .build();
        addActionTextToConsoleArea(action);

        Platform.runLater(() -> {
            mouseBrakeActionReaderButton.setText("Get Color");
            mouseBrakeActionReaderButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            mouseBrakeActionReaderButton.getStyleClass().add(GUI_BUTTON_GREEN);
        });
        ThreadControlService.stopHookListenerThread();
    }


    public void setActionKeyPressReaderActionSingle() {
        var action = ActionKeyPress.builder()
                .text(actionKeyPressTextField.getText())
                .sleepAfter(DataUtils.getLong(actionKeyPressSleepAfterTextField.getText(), 0))
                .build();
        addActionTextToConsoleArea(action);
    }

    /**
     * Button: Add Key Press info to TextArea Console
     */
    public void setActionKeyPressReaderAction() {
        if (ThreadControlService.getHookListenerState()) {
            actionKeyPressReaderButton.setText("Stop");
            actionKeyPressReaderButton.getStyleClass().removeAll(GUI_BUTTON_GREEN);
            actionKeyPressReaderButton.getStyleClass().add(GUI_BUTTON_RED);
            ThreadControlService.startHookListenerThread(this::setKeyPressInfo, -1, true);
        } else {
            actionKeyPressReaderButton.setText("Listen Key Code");
            actionKeyPressReaderButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            actionKeyPressReaderButton.getStyleClass().add(GUI_BUTTON_GREEN);
            ThreadControlService.stopHookListenerThread();
        }
    }

    /**
     * Callback: Add Key Press info to TextArea Console
     *
     * @param keyCode key codes by action press
     */
    public void setKeyPressInfo(Integer keyCode) {
        var action = ActionKeyPress.builder()
                .keyCode(keyCode)
                .text(actionKeyPressTextField.getText())
                .sleepAfter(DataUtils.getLong(actionKeyPressSleepAfterTextField.getText(), 0))
                .eventStateType(actionKeyPressStateChoiceBox.getValue())
                .build();
        addActionTextToConsoleArea(action);

        Platform.runLater(() -> {
            actionKeyPressReaderButton.setText("Listen Key Code");
            actionKeyPressReaderButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            actionKeyPressReaderButton.getStyleClass().add(GUI_BUTTON_GREEN);
        });
        ThreadControlService.stopHookListenerThread();
    }

    /**
     * Button: Add While End Action info to TextArea Console
     */
    public void setEndActionReaderAction() {
        var action = new ActionEnd();
        addActionTextToConsoleArea(action);
    }

    private void addActionTextToConsoleArea(ActionsInterface action) {
        Platform.runLater(() -> textAreaConsole
                .setText(String.join("\n", textAreaConsole.getText(), action.toString()))
        );
    }

    /**
     * Button: Run Script
     */
    public void onRunAction() {
        onRunActionCompleteByUser(null);
    }

    public void onRunActionCompleteByUser(Integer complete) {
        if (Objects.isNull(complete) && ThreadControlService.getRunExecuteState()) {
            Platform.runLater(() -> {
                onRunActionButton.setText("Stop");
                onRunActionButton.getStyleClass().add(GUI_BUTTON_RED);
            });
            ThreadControlService.startRunExecuteThread(textAreaConsole.getText(), this::onRunActionCompleteCallback, this::onRunActionCompleteByUser);
        } else {
            Platform.runLater(() -> {
                onRunActionButton.setText("Run");
                onRunActionButton.getStyleClass().removeAll(GUI_BUTTON_RED);
                onRunActionButton.getStyleClass().add(GUI_BUTTON_GREEN);
                onRunActionButton.setDisable(true);
            });
            ThreadControlService.stopRunExecuteThread();
        }
    }

    public void onRunActionCompleteCallback(Boolean ignore) {
        Platform.runLater(() -> {
            onRunActionButton.setText("Run");
            onRunActionButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            onRunActionButton.getStyleClass().add(GUI_BUTTON_GREEN);
            onRunActionButton.setDisable(false);
        });
        ThreadControlService.stopRunExecuteThread();
    }

    /**
     * Button: New Macros Script
     */
    public void newMacrosButtonAction() {

    }
}