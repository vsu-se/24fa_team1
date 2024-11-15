package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public class Main extends Application {

    private MainController controller;

    @Override
    public void start(Stage primaryStage) {
        MainView view = new MainView(null);
        controller = new MainController(view);

        view.getListItemButton().setOnAction(event -> {
            if (controller.getCategories().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please add a category in the System Admin tab before listing an item.");
                alert.showAndWait();
                return;
            }

            ItemView itemView = new ItemView(controller.getCategories());
            Tab createItemTab = new Tab("Create Item", itemView.getLayout());
            createItemTab.setClosable(true);

            new ItemController(itemView, controller.getCategories(), view.getTabPane(), createItemTab, controller.getItems(), controller);

            view.getTabPane().getTabs().add(createItemTab);
            view.getTabPane().getSelectionModel().select(createItemTab);
        });

        Scene scene = new Scene(view.getTabPane(), 800, 600);

        primaryStage.setTitle("JavaFX TabPane Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (controller != null) {
            controller.shutdownScheduler();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}