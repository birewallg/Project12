module local.uniclog {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires lombok;
    requires org.slf4j;
    requires java.desktop;
    requires com.google.gson;
    requires com.sun.jna.platform;
    requires com.sun.jna;

    opens local.uniclog.model.actions to com.google.gson;
    opens local.uniclog.model to com.google.gson;

    opens local.uniclog.ui to javafx.fxml;
    exports local.uniclog;
}