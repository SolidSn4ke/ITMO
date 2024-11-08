module com.example.dragonline {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.dragonline to javafx.fxml;
    exports com.example.dragonline;
}