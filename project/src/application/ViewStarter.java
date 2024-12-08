
package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ViewStarter extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create sample categories
            ObservableList<Category> categories = FXCollections.observableArrayList(
                new Category("Electronics"),
                new Category("Books"),
                new Category("Furniture")
            );

            // Initialize MainView with valid categories
            MainView mainView = new MainView(categories);
            MainController controller = new MainController(mainView, null);

            // Set up the scene
            Scene scene = new Scene(mainView.getTabPane(), 600, 400);
            primaryStage.setTitle("Auction System");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
