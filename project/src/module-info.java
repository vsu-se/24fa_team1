module project {
	requires javafx.controls;
	requires org.junit.jupiter.api;
	requires junit;
	requires javafx.graphics;
	requires javafx.swing;
  
  //exports application;
	
	//exports test to javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
}