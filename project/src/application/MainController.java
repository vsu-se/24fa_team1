package application;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.application.Platform;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainController {
    private ObservableList<Category> categories;
    private ObservableList<Item> items;
    private MainView view;
    private double buyerPremium;
    private double sellerCommission;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledFuture;
    private TextField currentBidInput;
    private SystemClock clock;

    public MainController(MainView view, SystemClock clock) {
        this.view = view;
        this.clock = clock;
        categories = FXCollections.observableArrayList();
        items = FXCollections.observableArrayList();

        // Initialize the scheduler
        scheduler = Executors.newScheduledThreadPool(1);

        // Add listener to the items list
        items.addListener((ListChangeListener<Item>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    updateProfileItemsDisplay();
                }
            }
        });

        // Set up event handler for the add button
        view.getAddButton().setOnAction(event -> {
            clearErrorMessages();
            String categoryName = view.getCategoryInput().getText();
            if (!categoryName.isEmpty() && !isDuplicateCategory(categoryName)) {
                Category newCategory = new Category(categoryName);
                categories.add(newCategory);
                view.getCategoryInput().clear();
                view.getCategoryErrorLabel().setText(""); // Clear error message
            } else {
                view.getCategoryErrorLabel().setText("Category already exists or name is empty.");
            }
        });

        // Set up event handler for the set premium button
        view.getSetPremiumButton().setOnAction(event -> {
            clearErrorMessages();
            String premiumText = view.getPremiumInput().getText();
            try {
                double premiumValue = Double.parseDouble(premiumText);
                if (premiumValue < 0) {
                    throw new NumberFormatException("Negative value");
                }
                buyerPremium = premiumValue;
                view.getPremiumInput().clear();
                view.getBuyerPremiumLabel().setText("Buyer's Premium: " + buyerPremium + "%");
                view.getPremiumErrorLabel().setText(""); // Clear error message
            } catch (NumberFormatException e) {
                view.getPremiumErrorLabel().setText("Invalid premium value. Please enter a non-negative number.");
            }
        });

        // Set up event handler for the set commission button
        view.getSetCommissionButton().setOnAction(event -> {
            clearErrorMessages();
            String commissionText = view.getCommissionInput().getText();
            try {
                double commissionValue = Double.parseDouble(commissionText);
                if (commissionValue < 0) {
                    throw new NumberFormatException("Negative value");
                }
                sellerCommission = commissionValue;
                view.getCommissionInput().clear();
                view.getSellerCommissionLabel().setText("Seller's Commission: " + sellerCommission + "%");
                view.getCommissionErrorLabel().setText(""); // Clear error message
            } catch (NumberFormatException e) {
                view.getCommissionErrorLabel().setText("Invalid commission value. Please enter a non-negative number.");
            }
        });

        // Set up event handler for the list item button
        view.getListItemButton().setOnAction(event -> {
            clearErrorMessages();
            if (categories.isEmpty()) {
                view.getListItemErrorLabel().setText("Please add a category in the System Admin tab before listing an item.");
            } else {
                view.getListItemErrorLabel().setText(""); // Clear error message
                ItemView itemView = new ItemView(categories);
                Tab createItemTab = new Tab("Create Item", itemView.getLayout());
                createItemTab.setClosable(true);
                new ItemController(itemView, categories, view.getTabPane(), createItemTab, items, MainController.this, clock);
                view.getTabPane().getTabs().add(createItemTab);
                view.getTabPane().getSelectionModel().select(createItemTab);
            }
        });

        // Add listener to the category combo box in the user interface tab
        view.getCategoryComboBoxUserInterface().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateItemsDisplay();
        });

        // Add listener to clear error message when switching tabs
        view.getTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            clearErrorMessages();
        });

        // Bind the categories list to the ComboBoxes
        view.getCategoryComboBoxSystemAdmin().setItems(categories);
        view.getCategoryComboBoxUserInterface().setItems(categories);

        // Initial display update
        updateProfileItemsDisplay();
    }

    public void scheduleNextUpdate() {
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            scheduledFuture.cancel(false);
        }

        Item nextExpiringItem = getNextExpiringItem();
        if (nextExpiringItem != null) {
            long delay = nextExpiringItem.getEndDate().toEpochSecond(ZoneOffset.UTC) - clock.getTime().toEpochSecond(ZoneOffset.UTC);
            if (delay > 0) {
                scheduledFuture = scheduler.schedule(this::checkAndUpdateItems, delay, TimeUnit.SECONDS);
            } else {
                checkAndUpdateItems();
            }
        }
    }

    private Item getNextExpiringItem() {
        return items.stream()
                .filter(Item::isActive)
                .min(Comparator.comparing(Item::getEndDate))
                .orElse(null);
    }

    private void checkAndUpdateItems() {
        for (Item item : items) {
            item.checkAndSetInactive();
        }
        Platform.runLater(() -> {
            updateItemsDisplay();
            updateProfileItemsDisplay();
            updateConcludedAuctionsDisplay();
        });
        scheduleNextUpdate();
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
        scheduleNextUpdate(); // Reschedule when a new item is added
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

                    // Add bid input and button
                    TextField bidAmountInput = new TextField();
                    bidAmountInput.setPromptText("Enter bid amount");
                    Button placeBidButton = new Button("Place Bid");
                    placeBidButton.setOnAction(event -> {
                        try {
                            double bidAmount = Double.parseDouble(bidAmountInput.getText());
                            if (item.placeBid(bidAmount)) {
                            	item.setHasBidder(true);
                                updateItemsDisplay();
                                updateProfileItemsDisplay();
                                
                                view.getListItemErrorLabel().setText("Bid placed successfully!");
                            } else {
                                view.getListItemErrorLabel().setText("Bid amount must be higher than the current bid.");
                            }
                        } catch (NumberFormatException e) {
                            view.getListItemErrorLabel().setText("Please enter a valid bid amount.");
                        }
                    });

                    itemBox.getChildren().addAll(bidAmountInput, placeBidButton);
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
        
        view.getMyProfileItemsBox().getChildren().add(new Label("Current Bids: "));
        
        int numMyBids = 0;
        for (Item item : items) {
        	
        	if(item.hasBidder() && item.isActive()) { //Do not show inactive auctions with a bid.
        		numMyBids++;
        		HBox itemBoxProfile = new HBox(10);
                itemBoxProfile.getChildren().add(new Label("Title: " + item.getTitle()));
                if (item.getBuyItNowPrice() != null) {
                    itemBoxProfile.getChildren().add(new Label("Buy It Now Price: $" + item.getBuyItNowPrice()));
                }
                itemBoxProfile.getChildren().addAll(
                    new Label("Weight: " + item.getWeight()),
                    new Label("Active: " + (item.isActive() ? "Yes" : "No")),
                    new Label("Current Bid: $" + item.getCurrentBid()),
                    new Label("My Bid: $" + item.getCurrentBid()) //currently equal, no profiles yet
                );
                view.getMyProfileItemsBox().getChildren().add(itemBoxProfile);	
        	}
        }
        view.setNumMyBids(numMyBids);
    }

    private void updateConcludedAuctionsDisplay() {
        view.getConcludedAuctionsBox().getChildren().clear();
        for (Item item : items) {
            if (!item.isActive()) {
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
                view.getConcludedAuctionsBox().getChildren().add(itemBox);
            }
        }
    }

    private void clearErrorMessages() {
        view.getCategoryErrorLabel().setText("");
        view.getPremiumErrorLabel().setText("");
        view.getCommissionErrorLabel().setText("");
        view.getListItemErrorLabel().setText("");
    }

    public void shutdownScheduler() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
    
    public void setTime(LocalDateTime time) {
    	this.clock.setTime(time);
    }
    
    public LocalDateTime getTime() {
    	return clock.getTime();
    }

	public SystemClock getClock() {
		return clock;
	}
}