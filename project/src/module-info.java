module project {

    requires org.junit.jupiter.api;
    requires javafx.graphics;
    requires javafx.controls;
    exports application;


    opens application to javafx.graphics, javafx.fxml;
}
