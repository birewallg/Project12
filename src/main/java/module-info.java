module local.uniclog {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires lombok;
    requires org.slf4j;
    requires java.desktop;
    requires com.sun.jna.platform;
    requires com.sun.jna;
    requires com.google.gson;
    requires kotlin.stdlib;

    opens local.uniclog.services.support to com.google.gson;
    opens local.uniclog.services to com.google.gson;
    opens local.uniclog.ui.controlls.model to com.google.gson;
    opens local.uniclog.ui to javafx.fxml;
    exports local.uniclog;
}