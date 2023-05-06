module local.uniclog {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires lombok;
    requires org.slf4j;
    requires java.desktop;
    requires com.sun.jna.platform;
    requires com.sun.jna;
    requires com.google.gson;
    requires kotlin.stdlib;

    opens local.uniclog.services.support to com.google.gson;
    opens local.uniclog.services.entity to com.google.gson;
    opens local.uniclog.ui.model to com.google.gson, javafx.base;
    opens local.uniclog.ui to javafx.fxml;

    exports local.uniclog;
}