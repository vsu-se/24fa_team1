package application;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.application.Platform;

import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainController {
    private ObservableList<Category> categories;
    private ObservableList<Item> items;
    private MainView view;
    private double buyerPremium;
    private double sellerCommission;
    private ScheduledExecutorService scheduler;

    public MainController(MainView view) {
        this.view = view;
        categories = FXCollections.observableArrayList();
        items = FXCollections.observableArrayList();

        // Initialize the scheduler
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::checkAndUpdateItems, 0, 1, TimeUnit.SECONDS);

        // Add listener to the items list
        items.addListener((ListChangeListener<Item>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    updateProfileItemsDisplay();
                }
            }
        });

        // Set up event handler for the add button
        view.getAddButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String categoryName = view.getCategoryInput().getText();
                if (!categoryName.isEmpty() && !isDuplicateCategory(categoryName)) {
                    Category newCategory = new Category(categoryName);
                    categories.add(newCategory);
                    view.getCategoryInput().clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Category already exists or name is empty.");
                    alert.showAndWait();
                }
            }
        });

        // Set up event handler for the set premium button
        view.getSetPremiumButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String premiumText = view.getPremiumInput().getText();
                try {
                    double premiumValue = Double.parseDouble(premiumText);
                    if (premiumValue < 0) {
                        throw new NumberFormatException("Negative value");
                    }
                    buyerPremium = premiumValue;
                    view.getPremiumInput().clear();
                    view.getBuyerPremiumLabel().setText("Buyer's Premium: " + buyerPremium + "%");
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid premium value. Please enter a non-negative number.");
                    alert.showAndWait();
                }
            }
        });

        // Set up event handler for the set commission button
        view.getSetCommissionButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String commissionText = view.getCommissionInput().getText();
                try {
                    double commissionValue = Double.parseDouble(commissionText);
                    if (commissionValue < 0) {
                        throw new NumberFormatException("Negative value");
                    }
                    sellerCommission = commissionValue;
                    view.getCommissionInput().clear();
                    view.getSellerCommissionLabel().setText("Seller's Commission: " + sellerCommission + "%");
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid commission value. Please enter a non-negative number.");
                    alert.showAndWait();
                }
            }
        });

        // Set up event handler for the list item button
        view.getListItemButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (categories.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Please add a category in the System Admin tab before listing an item.");
                    alert.showAndWait();
                    return;
                }
                ItemView itemView = new ItemView(categories);
                Tab createItemTab = new Tab("Create Item", itemView.getLayout());
                createItemTab.setClosable(true);
                new ItemController(itemView, categories, view.getTabPane(), createItemTab, items, MainController.this);
                view.getTabPane().getTabs().add(createItemTab);
                view.getTabPane().getSelectionModel().select(createItemTab);
            }
        });

        // Add listener to the category combo box in the user interface tab
        view.getCategoryComboBoxUserInterface().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateItemsDisplay();
        });

        // Bind the categories list to the ComboBoxes
        view.getCategoryComboBoxSystemAdmin().setItems(categories);
        view.getCategoryComboBoxUserInterface().setItems(categories);

        // Initial display update
        updateProfileItemsDisplay();
    }

    private void checkAndUpdateItems() {
        for (Item item : items) {
            item.checkAndSetInactive();
        }
        Platform.runLater(() -> {
            updateItemsDisplay();
            updateProfileItemsDisplay();
        });
    }

    private boolean isDuplicateCategory(String categoryName) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                return true;
            }
        }
        return false;
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    public double getBuyerPremium() {
        return buyerPremium;
    }

    public double getSellerCommission() {
        return sellerCommission;
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void updateItemsDisplay() {
        items.sort(Comparator.comparing(Item::getEndDate));
        view.getUserInterfaceItemsBox().getChildren().clear();
        Category selectedCategory = view.getCategoryComboBoxUserInterface().getValue();
        for (Item item : items) {
            if (selectedCategory == null || item.getCategory().equals(selectedCategory)) {
                if (item.isActive()) {
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
                    view.getUserInterfaceItemsBox().getChildren().add(itemBox);
                }
            }
        }
    }

    public void updateProfileItemsDisplay() {
        view.getMyProfileItemsBox().getChildren().clear();
        for (Item item : items) {
            HBox itemBoxProfile = new HBox(10);
            itemBoxProfile.getChildren().add(new Label("Title: " + item.getTitle()));
            if (item.getBuyItNowPrice() != null) {
                itemBoxProfile.getChildren().add(new Label("Buy It Now Price: $" + item.getBuyItNowPrice()));
            }
            itemBoxProfile.getChildren().addAll(
                new Label("Weight: " + item.getWeight()),
                new Label("Active: " + (item.isActive() ? "Yes" : "No")),
                new Label("Current Bid: $" + item.getCurrentBid())
            );
            view.getMyProfileItemsBox().getChildren().add(itemBoxProfile);
        }
    }

    public void shutdownScheduler() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}