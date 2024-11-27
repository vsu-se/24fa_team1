package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    private MainController controller;
    private EndedAuctionsController endedAuctionsController;

    @Override
    public void start(Stage primaryStage) {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        MainView view = new MainView(categories);
        controller = new MainController(view);
        endedAuctionsController = new EndedAuctionsController(controller.getItems());

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

        view.getShowReportButton().setOnAction(event -> {
            SellerReportView reportView = new SellerReportView();
            new SellerReportController(reportView, controller.getItems());

            Tab reportTab = new Tab("Seller Report", reportView.getLayout());
            reportTab.setClosable(true);

            view.getTabPane().getTabs().add(reportTab);
            view.getTabPane().getSelectionModel().select(reportTab);
        });

        // Display ended auctions
        ObservableList<Item> endedAuctions = endedAuctionsController.getEndedAuctions();
        for (Item item : endedAuctions) {
            HBox itemBox = new HBox(10);
            itemBox.getChildren().add(new Label("Title: " + item.getTitle()));
            if (item.getBuyItNowPrice() != null) {
                itemBox.getChildren().add(new Label("Buy It Now Price: $" + item.getBuyItNowPrice()));
            }
            itemBox.getChildren().addAll(
                    new Label("Weight: " + item.getWeight()),
                    new Label("Active: " + (item.isActive() ? "Yes" : "No")),
                    new Label("Current Bid: $" + item.getCurrentBid())
            );
            view.getEndedAuctionsBox().getChildren().add(itemBox);
        }

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