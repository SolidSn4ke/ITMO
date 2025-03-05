module com.example.compmath2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;

    opens com.example.compmath2 to javafx.fxml;
    exports com.example.compmath2;
}