module project {

    requires org.junit.jupiter.api;
    requires javafx.graphics;
    requires javafx.controls;
	requires javafx.base;
    exports application;


    opens application to javafx.graphics, javafx.fxml;
}
