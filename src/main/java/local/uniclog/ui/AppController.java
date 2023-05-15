package local.uniclog.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import local.uniclog.model.actions.impl.ActionEnd;
import local.uniclog.model.actions.impl.Sleep;
import local.uniclog.model.actions.types.ActionType;
import local.uniclog.model.actions.types.EventStateType;
import local.uniclog.model.actions.types.MouseButtonType;
import local.uniclog.ui.controlls.ControlServiceAbstract;
import local.uniclog.ui.controlls.SceneControlService;
import local.uniclog.ui.controlls.impl.*;
import local.uniclog.ui.model.ControlPack;
import local.uniclog.ui.model.MacrosItem;
import local.uniclog.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * App main controller
 */
@Slf4j
public class AppController {

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
    private Label actionPaneLabel;
    @FXML
    private ChoiceBox<ActionType> actionChoiceBox;
    @FXML
    private TextField scriptNameTextField;
    @FXML
    private TableView<MacrosItem> macrosTable;
    @FXML
    private TableColumn<MacrosItem, String> tableColumnName;
    @FXML
    private TableColumn<MacrosItem, Integer> tableColumnActivation;

    private ControlPack buildControlsPack() {
        return ControlPack.builder()
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
                .actionPaneLabel(actionPaneLabel)
                .scriptNameTextField(scriptNameTextField)
                .macrosItemList(FXCollections.observableArrayList())
                .macrosList(macrosTable)
                .tableColumnName(tableColumnName)
                .tableColumnActivation(tableColumnActivation)
                .build();
    }
    //endregion

    /**
     * Controller post construct
     */
    public void initialize() {
        ControlServiceAbstract.initializeControl(buildControlsPack().initialize());
    }

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
        SceneControlService.onMin((Stage) exit.getScene().getWindow());
    }
    //endregion

    //region : Actions: save load context

    /**
     * Button: Load configuration
     */
    public void onLoad() {
        new SaveLoadControl().onLoad();
    }

    /**
     * Button: Save configuration to file
     */
    public void onSave() {
        new SaveLoadControl().onSave();
    }
    //endregion

    //region : Actions: ui buttons

    /**
     * Button: Read Mouse Coordinates Add it to text console
     */
    public void setMouseActionReaderButton() {
        new MouseControl().setMouseActionReaderButton();
    }

    /**
     * Button: Add Mouse no coords action
     */
    public void setMouseActionNoCoordsButton() {
        new MouseControl().setMouseActionNoCoordsButton();
    }

    /**
     * Button: Add sleep action info to TextArea Console
     */
    public void setSleepActionReaderAction() {
        var action = Sleep.builder()
                .time(DataUtils.getLong(ControlServiceAbstract.getCp().getSleepActionCountTextField().getText(), 0L))
                .build();
        new DefaultControl().addActionTextToConsoleArea(action);
    }

    /**
     * Button: Add While Action info to TextArea Console
     */
    public void setWhileActionReaderAction() {
        new WhileActionControl().setWhileActionReaderAction();
    }

    /**
     * Button: Add While Break Action info to TextArea Console
     */
    public void setWhileBreakActionReaderAction() {
        new WhileActionControl().setWhileBreakActionReaderAction();
    }

    /**
     * Button: Add Key Press info to TextArea Console
     */
    public void setActionKeyPressReaderActionSingle() {
        new KeyPressControl().setActionKeyPressReaderActionSingle();
    }

    /**
     * Button: Add Key Press info to TextArea Console
     */
    public void setActionKeyPressReaderAction() {
        new KeyPressControl().setActionKeyPressReaderAction();
    }

    /**
     * Button: Add While End Action info to TextArea Console
     */
    public void setEndActionReaderAction() {
        var action = new ActionEnd();
        new DefaultControl().addActionTextToConsoleArea(action);
    }

    /**
     * Button: Run Script
     */
    public void onRunAction() {
        new OnRunControl().onRunAction();
    }

    /**
     * Button: New Macros Script
     */
    public void newMacrosButtonAction() {
        new MacrosListControls().newMacrosButtonAction();
    }

    /**
     * Button: Remove Macros Script
     */
    public void delMacrosButtonAction() {
        new MacrosListControls().delMacrosButtonAction();
    }

    // endregion
}