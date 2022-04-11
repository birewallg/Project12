package local.uniclog.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import local.uniclog.model.MouseButtonType;
import local.uniclog.model.actions.MouseClick;
import local.uniclog.model.actions.Sleep;
import local.uniclog.services.ActionProcessExecuteService;
import local.uniclog.services.FileServiceWrapper;
import local.uniclog.services.JnaKeyHookService;
import local.uniclog.services.MouseServiceWrapper;
import local.uniclog.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

import static local.uniclog.model.MouseButtonType.BUTTON_L;
import static local.uniclog.utils.ConfigConstants.DEFAULT_FILE_PATH;

@Slf4j
public class AppController {
    private static final String GUI_BUTTON_RED = "gui-button-red";
    private static final String GUI_BUTTON_GREEN = "gui-button-green";

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
    private boolean initializeRunExecute = false;

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
        setMouseActionChoiceBox.setValue(BUTTON_L);

        setActionChoiceBox.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    setMousePane.setVisible(false);
                    setSleepPane.setVisible(false);
                    switch (newValue) {
                        case MOUSE_CLICK -> setMousePane.setVisible(true);
                        case SLEEP -> setSleepPane.setVisible(true);
                        default -> log.debug("Action not choose");
                    }
                });
    }

    /**
     * Button: Load configuration
     */
    public void onLoad() {
        FileChooser fileChooser = new FileChooser();
        //Set to user directory or go to default if cannot access
        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File(DEFAULT_FILE_PATH);
        }
        fileChooser.setInitialDirectory(userDirectory);
        File file = fileChooser.showOpenDialog(exit.getScene().getWindow());
        if (file != null) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Load config from [" + file.getPath() + "]?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                textAreaConsole.clear();
                textAreaConsole.setText(FileServiceWrapper.read(file.getPath()));
            }
        }
    }

    /**
     * Button: Save configuration to file
     */
    public void onSave() {
        FileChooser fileChooser = new FileChooser();
        //Set to user directory or go to default if cannot access
        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File(DEFAULT_FILE_PATH);
        }
        fileChooser.setInitialDirectory(userDirectory);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(exit.getScene().getWindow());
        if (file != null) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Save config to [" + file.getPath() + "]?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                FileServiceWrapper.write(textAreaConsole.getText(), file.getPath());
            }
        }
    }

    /**
     * Button: Read Coordinates
     */
    public void setMouseActionReaderAction() {
        if (initializeHookListener) {
            setMouseActionReaderButton.setText("Stop Action Read");
            setMouseActionReaderButton.getStyleClass().removeAll();
            setMouseActionReaderButton.getStyleClass().add(GUI_BUTTON_RED);
        } else {
            setMouseActionReaderButton.setText("Start Action Read");
            setMouseActionReaderButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            setMouseActionReaderButton.getStyleClass().add(GUI_BUTTON_GREEN);
        }

        JnaKeyHookService jnaKeyHookService = new JnaKeyHookService();
        jnaKeyHookService.initialize(initializeHookListener, this::setMouseInfo, 162, false);
        initializeHookListener = !initializeHookListener;
    }

    /**
     * Button: Add mouse info to TextArea Console
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
        setTextToConsole(action);
    }

    /**
     * Button: Add sleep action info to TextArea Console
     */
    public void setSleepActionReaderAction() {
        setSleepInfo();
    }

    public void setSleepInfo() {
        Sleep action = Sleep.builder()
                .time(DataUtils.getLong(setSleepActionCountTextField.getText(), 0L))
                .build();
        setTextToConsole(action);
    }

    private void setTextToConsole(ActionsInterface action) {
        Platform.runLater(() -> textAreaConsole
                .setText(textAreaConsole.getText()
                        + "\n"
                        + action.toString())
        );
    }

    public void onRunAction() {
        onRunActionCompleteByUser(initializeRunExecute);
        // hook ctrl to stop
        JnaKeyHookService jnaKeyHookService = new JnaKeyHookService();
        jnaKeyHookService.initialize(initializeRunExecute, this::onRunActionCompleteByUser, 162, true);
        // start action execute
        ActionProcessExecuteService actionProcessExecuteService = new ActionProcessExecuteService();
        actionProcessExecuteService.initialize(initializeRunExecute, textAreaConsole.getText(), this::onRunActionCompleteCallback);

    }

    public void onRunActionCompleteByUser(Boolean complete) {
        if (complete.equals(false)) {
            Platform.runLater(() -> {
                onRunActionButton.setText("Stop");
                onRunActionButton.getStyleClass().add(GUI_BUTTON_RED);
            });
            initializeRunExecute = true;
        } else {
            ActionProcessExecuteService.stop();

            Platform.runLater(() -> {
                onRunActionButton.setText("Run");
                onRunActionButton.getStyleClass().removeAll(GUI_BUTTON_RED);
                onRunActionButton.getStyleClass().add(GUI_BUTTON_GREEN);
                onRunActionButton.setDisable(true);
            });
            initializeRunExecute = false;
        }
    }

    public void onRunActionCompleteCallback(Boolean ignore) {
        JnaKeyHookService.stop();

        Platform.runLater(() -> {
            onRunActionButton.setText("Run");
            onRunActionButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            onRunActionButton.getStyleClass().add(GUI_BUTTON_GREEN);
            onRunActionButton.setDisable(false);
        });
        initializeRunExecute = false;
    }
}