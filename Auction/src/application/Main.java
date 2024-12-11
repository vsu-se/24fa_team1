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
        SystemClock clock = new SystemClock();
        controller = new MainController(view, clock);
        
        //Load the saved state on startup
        controller.loadState();

        view.getListItemButton().setOnAction(event -> {
            if (controller.getCategories().isEmpty()) {
                view.getListItemErrorLabel().setText("Please add a category in the System Admin tab before listing an item.");
                return;
            }
            
            ItemView itemView = new ItemView(controller.getCategories());
            Tab createItemTab = new Tab("Create Item", itemView.getLayout());
            createItemTab.setClosable(true);

            new ItemController(itemView, controller.getCategories(), view.getTabPane(), createItemTab, controller.getItems(), controller, clock);

            view.getTabPane().getTabs().add(createItemTab);
            view.getTabPane().getSelectionModel().select(createItemTab);
        });

        Scene scene = new Scene(view.getTabPane(), 1000, 800);

        primaryStage.setTitle("Auction System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @Override
    public void stop() {
        if (controller != null) {
        	controller.saveState();
            controller.shutdownScheduler();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}