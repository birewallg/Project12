package local.uniclog.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import local.uniclog.model.ActionType;
import local.uniclog.services.FileServiceWrapper;
import local.uniclog.services.JnaKeyHookService;
import local.uniclog.services.MouseServiceWrapper;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.function.Function;

@Slf4j
public class AppController {
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
    private TextArea textArea;
    @FXML
    private ToggleButton exit;
    @FXML
    private ChoiceBox<ActionType> setActionChoiceBox;

    private boolean initializeHookListener = true;

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

        setActionChoiceBox.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    setMousePane.setVisible(false);
                    if (newValue.equals(ActionType.MOUSE_CLICK))
                        setMousePane.setVisible(true);
                });
    }

    public void onLoad() {
        FileServiceWrapper file = new FileServiceWrapper();
        textArea.setText(file.read());
    }

    public void onSave() {
        FileServiceWrapper file = new FileServiceWrapper();
        file.write(textArea.getText());
    }

    public void setMouseActionReaderAction() {

        Function<Boolean, Point> getMouseInfoFunc = info -> getMouseInfo();

        if (initializeHookListener)
            setMouseActionReaderButton.setText("Stop Action Read");
        else setMouseActionReaderButton.setText("Start Action Read");
        JnaKeyHookService jnaKeyHookService = new JnaKeyHookService();
        jnaKeyHookService.initialize(initializeHookListener, getMouseInfoFunc);
        initializeHookListener = !initializeHookListener;
    }

    public Point getMouseInfo() {
        return MouseServiceWrapper.getMousePointer();
    }
}