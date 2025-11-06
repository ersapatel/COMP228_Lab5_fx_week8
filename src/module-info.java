module COMP228_Lab5_fx_week8 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens application to javafx.graphics, javafx.fxml;
    exports application;
}