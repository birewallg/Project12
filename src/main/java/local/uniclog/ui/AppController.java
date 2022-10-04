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
import local.uniclog.ui.controlls.SaveLoadControl;
import local.uniclog.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static local.uniclog.utils.ConfigConstants.GUI_BUTTON_GREEN;
import static local.uniclog.utils.ConfigConstants.GUI_BUTTON_RED;

@Slf4j
public class AppController {
    @FXML
    private TextField setActionKeyPressSleepAfterTextField;
    @FXML
    private Button setActionKeyPressReaderButton;
    @FXML
    private TextField setActionKeyPressTextField;
    @FXML
    private Pane setActionKeyPressPane;
    @FXML
    private ChoiceBox<EventStateType> setActionKeyPressStateChoiceBox;
    @FXML
    private Button setMouseBrakeActionReaderButton;
    //@FXML
    //private TextField setWhileBreakActionColorTextField
    @FXML
    private Pane setWhileBreakPane;
    @FXML
    private Pane setEndPane;
    @FXML
    private TextField setWhileActionCountTextField;
    @FXML
    private Pane setWhilePane;
    @FXML
    private TextField setSleepActionCountTextField;
    @FXML
    private Pane setSleepPane;
    @FXML
    private Button onRunActionButton;
    @FXML
    private ChoiceBox<MouseButtonType> setMouseActionChoiceBox;
    @FXML
    private TextField setMouseActionCountTextField;
    @FXML
    private TextField setMouseActionDelayTimeTextField;
    @FXML
    private TextField setMouseActionSleepAfterTextField;
    @FXML
    private Button setMouseActionReaderButton;
    @FXML
    private Pane setMousePane;
    @FXML
    private TextArea textAreaConsole;
    @FXML
    private ToggleButton exit;
    @FXML
    private ChoiceBox<ActionType> setActionChoiceBox;

    // Main Controls Block ============================================

    /**
     * Button: Exit
     */
    public void onExit() {
        ThreadControlService.stopRunExecuteThread();
        System.exit(0);
    }

    /**
     * Button: Minimize window
     */
    public void onMin() {
        var stage = (Stage) exit.getScene().getWindow();
        stage.setIconified(true);
    }
    // ================================================================

    public void initialize() {
        log.debug("App Controller init");

        setActionChoiceBox.getItems().setAll(ActionType.values());
        setMouseActionChoiceBox.getItems().setAll(MouseButtonType.values());
        setMouseActionChoiceBox.setValue(MouseButtonType.BUTTON_L);
        setActionKeyPressStateChoiceBox.getItems().setAll(EventStateType.values());
        setActionKeyPressStateChoiceBox.setValue(EventStateType.PRESS);

        setActionChoiceBox.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    setMousePane.setVisible(false);
                    setSleepPane.setVisible(false);
                    setWhilePane.setVisible(false);
                    setEndPane.setVisible(false);
                    setWhileBreakPane.setVisible(false);
                    setActionKeyPressPane.setVisible(false);
                    switch (newValue) {
                        case MOUSE_CLICK -> setMousePane.setVisible(true);
                        case SLEEP -> setSleepPane.setVisible(true);
                        case WHILE -> setWhilePane.setVisible(true);
                        case END -> setEndPane.setVisible(true);
                        case WHILE_BRAKE_BY_COLOR -> setWhileBreakPane.setVisible(true);
                        case KEY_PRESS -> setActionKeyPressPane.setVisible(true);
                        default -> log.debug("Action not choose");
                    }
                });
    }

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

    /**
     * Button: Read Mouse Coordinates Add it to text console
     */
    public void setMouseActionReaderButton() {
        if (ThreadControlService.getHookListenerState()) {
            setMouseActionReaderButton.setText("Scan Action (Ctrl)");
            setMouseActionReaderButton.getStyleClass().removeAll(GUI_BUTTON_GREEN);
            setMouseActionReaderButton.getStyleClass().add(GUI_BUTTON_RED);
            ThreadControlService.startHookListenerThread(this::setMouseInfo, 162, false);
        } else {
            setMouseActionReaderButton.setText("Scan Action (Ctrl)");
            setMouseActionReaderButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            setMouseActionReaderButton.getStyleClass().add(GUI_BUTTON_GREEN);
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
                .action(setMouseActionChoiceBox.getValue())
                .point(Objects.nonNull(code) ? MouseServiceWrapper.getMousePointer() : null)
                .count(DataUtils.getInteger(setMouseActionCountTextField.getText(), 0))
                .period(DataUtils.getLong(setMouseActionDelayTimeTextField.getText(), 0L))
                .sleepAfter(DataUtils.getLong(setMouseActionSleepAfterTextField.getText(), 0L))
                .build();
        addActionTextToConsoleArea(action);
    }

    /**
     * Button: Add sleep action info to TextArea Console
     */
    public void setSleepActionReaderAction() {
        var action = Sleep.builder()
                .time(DataUtils.getLong(setSleepActionCountTextField.getText(), 0L))
                .build();
        addActionTextToConsoleArea(action);
    }

    /**
     * Button: Add While Action info to TextArea Console
     */
    public void setWhileActionReaderAction() {
        var action = ActionWhile.builder()
                .count(DataUtils.getInteger(setWhileActionCountTextField.getText(), 0))
                .build();
        addActionTextToConsoleArea(action);
    }

    /**
     * Button: Add While Break Action info to TextArea Console
     */
    public void setWhileBreakActionReaderAction() {
        if (ThreadControlService.getHookListenerState()) {
            setMouseBrakeActionReaderButton.setText("Stop");
            setMouseBrakeActionReaderButton.getStyleClass().removeAll(GUI_BUTTON_GREEN);
            setMouseBrakeActionReaderButton.getStyleClass().add(GUI_BUTTON_RED);
            ThreadControlService.startHookListenerThread(this::setMouseColorInfo, 162, true);
        } else {
            setMouseBrakeActionReaderButton.setText("Get Color");
            setMouseBrakeActionReaderButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            setMouseBrakeActionReaderButton.getStyleClass().add(GUI_BUTTON_GREEN);
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
            setMouseBrakeActionReaderButton.setText("Get Color");
            setMouseBrakeActionReaderButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            setMouseBrakeActionReaderButton.getStyleClass().add(GUI_BUTTON_GREEN);
        });
        ThreadControlService.stopHookListenerThread();
    }


    public void setActionKeyPressReaderActionSingle() {
        var action = ActionKeyPress.builder()
                .text(setActionKeyPressTextField.getText())
                .sleepAfter(DataUtils.getLong(setActionKeyPressSleepAfterTextField.getText(), 0))
                .build();
        addActionTextToConsoleArea(action);
    }

    /**
     * Button: Add Key Press info to TextArea Console
     */
    public void setActionKeyPressReaderAction() {
        if (ThreadControlService.getHookListenerState()) {
            setActionKeyPressReaderButton.setText("Stop");
            setActionKeyPressReaderButton.getStyleClass().removeAll(GUI_BUTTON_GREEN);
            setActionKeyPressReaderButton.getStyleClass().add(GUI_BUTTON_RED);
            ThreadControlService.startHookListenerThread(this::setKeyPressInfo, -1, true);
        } else {
            setActionKeyPressReaderButton.setText("Listen Key Code");
            setActionKeyPressReaderButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            setActionKeyPressReaderButton.getStyleClass().add(GUI_BUTTON_GREEN);
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
                .text(setActionKeyPressTextField.getText())
                .sleepAfter(DataUtils.getLong(setActionKeyPressSleepAfterTextField.getText(), 0))
                .eventStateType(setActionKeyPressStateChoiceBox.getValue())
                .build();
        addActionTextToConsoleArea(action);

        Platform.runLater(() -> {
            setActionKeyPressReaderButton.setText("Listen Key Code");
            setActionKeyPressReaderButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            setActionKeyPressReaderButton.getStyleClass().add(GUI_BUTTON_GREEN);
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