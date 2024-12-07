package application;

//Will need to be revamped once our classes have been reorganized

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
        view.setupSaveOptionsTab(controller);
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

        Scene scene = new Scene(view.getTabPane(), 800, 600);

        primaryStage.setTitle("Auction System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (controller != null) {
            // Save categories before shutting down
            controller.saveCategoriesText("categories.txt");

            controller.shutdownScheduler();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}