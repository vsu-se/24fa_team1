module project {
	requires javafx.controls;
	requires javafx.graphics;
	requires org.junit.jupiter.api;
	requires junit;
	opens application to javafx.graphics, javafx.fxml;
}
