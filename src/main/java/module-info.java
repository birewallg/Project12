module local.uniclog {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires lombok;
    requires org.slf4j;
    requires java.desktop;
    requires com.sun.jna.platform;
    requires com.sun.jna;

    opens local.uniclog.ui to javafx.fxml;
    exports local.uniclog;
}