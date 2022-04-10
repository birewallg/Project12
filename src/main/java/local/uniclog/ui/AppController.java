package local.uniclog.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import local.uniclog.model.ActionType;
import local.uniclog.model.MouseButtonType;
import local.uniclog.model.actions.MouseClick;
import local.uniclog.services.ActionProcessService;
import local.uniclog.services.FileServiceWrapper;
import local.uniclog.services.JnaKeyHookService;
import local.uniclog.services.MouseServiceWrapper;
import local.uniclog.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.function.Consumer;

import static local.uniclog.model.MouseButtonType.BUTTON1;

@Slf4j
public class AppController {
    @FXML
    private ChoiceBox<MouseButtonType> setMouseActionChoiceBox;
    @FXML
    private TextField setMouseActionCountTextField;
    @FXML
    private TextField setMouseActionPeriodTextField;
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

    private boolean initializeHookListener = true;
    private final ActionProcessService actionProcessService = new ActionProcessService();

    // Main Controls Block ============================================
    public void onExit() {
        System.exit(0);
    }

    public void onMin() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.setIconified(true);
    }
    // ================================================================

    public void initialize() {
        log.debug("App Controller init");

        setActionChoiceBox.getItems().setAll(ActionType.values());
        setMouseActionChoiceBox.getItems().setAll(MouseButtonType.values());
        setMouseActionChoiceBox.setValue(BUTTON1);

        setActionChoiceBox.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    setMousePane.setVisible(false);
                    if (newValue.equals(ActionType.MOUSE_CLICK))
                        setMousePane.setVisible(true);
                });
    }

    /**
     * Button: Load configuration
     */
    public void onLoad() {
        textAreaConsole.setText(FileServiceWrapper.read());
    }

    /**
     * Button: Save configuration to file
     */
    public void onSave() {
        FileServiceWrapper.write(textAreaConsole.getText());
    }

    /**
     * Button: Read Coordinates
     */
    public void setMouseActionReaderAction() {

        Consumer<Boolean> getMouseInfoFunc = this::setMouseInfo;

        if (initializeHookListener) {
            setMouseActionReaderButton.setText("Stop Action Read");
            setMouseActionReaderButton.setStyle("-fx-background-color: #5b0000");
        } else {
            setMouseActionReaderButton.setText("Start Action Read");
            setMouseActionReaderButton.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058)");
        }
        JnaKeyHookService jnaKeyHookService = new JnaKeyHookService();
        jnaKeyHookService.initialize(initializeHookListener, getMouseInfoFunc);
        initializeHookListener = !initializeHookListener;
    }

    /**
     * Add mouse info to TextArea Console
     *
     * @param ignore ignore
     */
    public void setMouseInfo(Boolean ignore) {
        MouseClick action = MouseClick.builder()
                .action(setMouseActionChoiceBox.getValue())
                .point(MouseServiceWrapper.getMousePointer())
                .count(DataUtils.getInteger(setMouseActionCountTextField.getText(), 0))
                .period(DataUtils.getLong(setMouseActionPeriodTextField.getText(), 0L))
                .sleepAfter(DataUtils.getLong(setMouseActionSleepAfterTextField.getText(), 0L))
                .build();

        textAreaConsole.setText(textAreaConsole.getText()
                + "\n"
                + action.toString());
    }

    public void onRunAction() {
        actionProcessService.getConfiguration(
                Arrays.stream(textAreaConsole.getText().trim().replaceAll("[ \\t\\x0B\\f\\r]", "")
                        .split("\n")).toList());
        actionProcessService.executeActionContainer();
    }
}