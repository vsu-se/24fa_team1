package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainView view = new MainView(null);
        MainController controller = new MainController(view);

        Scene scene = new Scene(view.getTabPane(), 400, 300);

        primaryStage.setTitle("JavaFX TabPane Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
