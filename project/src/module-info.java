module project {
	requires javafx.controls;
	requires org.junit.jupiter.api;
	requires junit;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
}
