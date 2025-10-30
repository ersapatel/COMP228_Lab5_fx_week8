module COMP228_Lab5_fx_week8 {
    requires javafx.controls;
    requires javafx.fxml;

    opens application to javafx.graphics, javafx.fxml;
    exports application;
}