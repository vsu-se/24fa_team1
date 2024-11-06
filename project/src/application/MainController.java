package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

import java.util.Comparator;

public class MainController {
    private ObservableList<Category> categories;
    private ObservableList<Item> items;
    private MainView view;
    private double buyerPremium;
    private double sellerCommission;

    public MainController(MainView view) {
        this.view = view;
        categories = FXCollections.observableArrayList();
        items = FXCollections.observableArrayList();

        // Set up event handler for the add button
        view.getAddButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String categoryName = view.getCategoryInput().getText();
                if (!categoryName.isEmpty()) {
                    Category newCategory = new Category(categoryName);
                    categories.add(newCategory);
                    view.getCategoryInput().clear();
                }
            }
        });

        // Set up event handler for the set premium button
        view.getSetPremiumButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String premiumText = view.getPremiumInput().getText();
                try {
                    buyerPremium = Double.parseDouble(premiumText);
                    view.getPremiumInput().clear();
                    view.getBuyerPremiumLabel().setText("Buyer's Premium: " + buyerPremium + "%");
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid premium value");
                    alert.showAndWait();
                }
            }
        });

        // Set up event handler for the set commission button
        view.getSetCommissionButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String commissionText = view.getCommissionInput().getText();
//                try {
//                    sellerCommission = Double.parseDouble(commissionText);
//                    view.getCommissionInput().clear();
//                    view.getSellerCommissionLabel().setText("Seller's Commission: " + sellerCommission + "%");
//                } catch (NumberFormatException e) {
//                    Alert alert = new Alert(Alert.AlertType.WARNING);
//                    alert.setTitle("Warning");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Invalid commission value");
//                    alert.showAndWait();
//                }
                try {
                    double commission = Double.parseDouble(commissionText);
                    if (commission < 0) {
                        throw new IllegalArgumentException("Commission cannot be negative");
                    }
                    view.getSellerCommissionLabel().setText("Seller's Commission: " + commission + "%");
                    view.getCommissionInput().clear();
                } catch (IllegalArgumentException e) {
                	Alert alert = new Alert(Alert.AlertType.WARNING);
                	alert.setContentText("Invalid Commission Value Please enter a valid non-negative number for the commission.");
                    // Do not clear the input in case of invalid input
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
        updateItemsDisplay();
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
        updateItemsDisplay();
    }

    public void updateItemsDisplay() {
        items.sort(Comparator.comparing(Item::getEndDate));

        view.getUserInterfaceItemsBox().getChildren().clear();
        view.getMyProfileItemsBox().getChildren().clear();

        Category selectedCategory = view.getCategoryComboBoxUserInterface().getValue();

        for (Item item : items) {
            if (selectedCategory == null || item.getCategory().equals(selectedCategory)) {
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

            // Always add items to the My Profile tab
            HBox itemBoxProfile = new HBox(10);
            itemBoxProfile.getChildren().add(new Label("Title: " + item.getTitle()));

            if (item.getBuyItNowPrice() != null) {
                itemBoxProfile.getChildren().add(new Label("Buy It Now Price: $" + item.getBuyItNowPrice()));
            }

            itemBoxProfile.getChildren().addAll(
                new Label("Weight: " + item.getWeight()), // Assuming weight is used as shipping cost
                new Label("Active: " + (item.isActive() ? "Yes" : "No")),
                new Label("Current Bid: $" + item.getCurrentBid())
            );

            view.getMyProfileItemsBox().getChildren().add(itemBoxProfile);
        }
    }
}