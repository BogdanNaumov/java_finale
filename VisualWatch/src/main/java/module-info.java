module com.example.visualwatch {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.visualwatch to javafx.fxml;
    exports com.example.visualwatch;
}