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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainController {
	private ObservableList<Category> categories;
	private ObservableList<Category> concludedCategories;
    private ObservableList<Item> items;
    private MainView view;
    private double buyersPremium;
    private double sellerCommission;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledFuture;
    private TextField currentBidInput;
    private SystemClock clock;
    private Category allAuctions = new Category("All Auctions");

    public MainController(MainView view, SystemClock clock) {
        this.view = view;
        this.clock = clock;
        buyersPremium = 0.00;
        sellerCommission = 0.00;
        categories = FXCollections.observableArrayList();
        concludedCategories = FXCollections.observableArrayList();
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
                concludedCategories.add(newCategory);
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
                buyersPremium = premiumValue;
                view.getPremiumInput().clear();
                view.getBuyerPremiumLabel().setText("Buyer's Premium: " + buyersPremium + "%");
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
        
        //Set up event handler for change time button
        view.getChangeTimeButton().setOnAction(event -> {
            clearErrorMessages();
            try {
            	LocalDate date = view.getChangeTimePicker().getValue();
            	
            	String timeString = view.getTimeField().getText();
            	LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
            	
                clock.setTime(LocalDateTime.of(date, time));
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                view.getCategoryErrorLabel().setText("Time changed.");
                
                scheduleNextUpdate();
            } catch (Exception e) {
                view.getCategoryErrorLabel().setText("Please enter a valid time in the format yyyy-MM-dd HH:mm:ss.");
                return;
            }
        });
        
        //Set up event handler for resume real time button
        view.getResumeTimeButton().setOnAction(event -> {
            clearErrorMessages();
            clock.setTime(LocalDateTime.now());
            scheduleNextUpdate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            view.getCategoryErrorLabel().setText("Time resumed. It is now " + clock.getTime().format(formatter));
            clock.setIsPaused(false);
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
        
        // Set up event handler for the pause button
        view.getPauseTimeButton().setOnAction(event -> {
            clearErrorMessages();
            clock.setIsPaused(true);
            view.getCategoryErrorLabel().setText("Time has been paused.");
            updateItemsDisplay();
        });
        
        // Set up event handler for the unpause button
        view.getUnpauseTimeButton().setOnAction(event -> {
            clearErrorMessages();
            clock.setIsPaused(false);
            view.getCategoryErrorLabel().setText("Time has been unpaused.");
            updateItemsDisplay();
        });

        // Add listener to the category combo box in the user interface tab
        view.getCategoryComboBoxUserInterface().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateItemsDisplay();
        });
        
        
        // Add listener to clear error message when switching tabs
        view.getTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            clearErrorMessages();
        });
        
        view.getCategoryComboBoxConcludedAuctions().valueProperty().addListener((observable, oldValue, newValue) -> {
            updateConcludedAuctionsDisplay();
        });

        // Bind the categories list to the ComboBoxes
        view.getCategoryComboBoxSystemAdmin().setItems(categories);
        view.getCategoryComboBoxUserInterface().setItems(categories);
        concludedCategories.add(allAuctions);
        concludedCategories.addAll(categories);
        view.getCategoryComboBoxConcludedAuctions().setItems(concludedCategories);
        
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
			@Override
			public void run() {
				view.getDisplayTimeArea().setText("Current Time: " + clock.getTime().format(formatter));
			}
		};
		timer.scheduleAtFixedRate(task,  0,  1000);

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
        return buyersPremium;
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

    public void updateConcludedAuctionsDisplay() {
        view.getConcludedAuctionsBox().getChildren().clear();
        Category selectedCategory = view.getCategoryComboBoxConcludedAuctions().getValue();
        
        double totalOfWinningBids = 0.00;
        double totalShippingCosts = 0.00;
        double totalBuyersPremiums = 0.00;
        double totalPaidByBuyers = 0.00;
        double totalWeight = 0.00;
        double totalSellersCommissions = 0.00;
        double totalSellersProfit = 0.00;
         
        for (Item item : items) {
            if (!item.isActive() && selectedCategory != null && (item.getCategory().equals(selectedCategory) || selectedCategory.equals(allAuctions))) {
            	totalOfWinningBids += item.getCurrentBid();
            	totalShippingCosts += item.getShippingCost();
            	totalBuyersPremiums += item.getBuyersPremium(buyersPremium);
            	totalPaidByBuyers += item.getBuyersPremium(buyersPremium) + item.getCurrentBid();
            	totalWeight += Double.parseDouble(item.getWeight());
            	totalSellersCommissions += item.getSellersCommission(sellerCommission);
            	totalSellersProfit += item.getCurrentBid();
            	
                HBox itemBox = new HBox(10);
                itemBox.getChildren().add(new Label("Item Name: " + item.getTitle()));
                if (item.getBuyItNowPrice() != null) {
                    itemBox.getChildren().add(new Label("Buy It Now Price: $" + item.getBuyItNowPrice()));
                }
                itemBox.getChildren().addAll(
                	new Label("Winning Bid: $" + item.getCurrentBid()),
                    new Label("Weight: " + item.getWeight()),
                    new Label("End-date: " + item.getEndDate())
                );
                view.getConcludedAuctionsBox().getChildren().add(itemBox);
                itemBox.toBack();
            }
        }
        
        HBox itemBox = new HBox(10);
        itemBox.getChildren().addAll(
        		new Label("TOTAL:"), 
                new Label("Winning Bid/BIN: $" + totalOfWinningBids),
                new Label("Shipping Costs: $" + totalShippingCosts),
                new Label("Buyers Premiums: $" + totalBuyersPremiums),
                new Label("Paid by Buyers: $" + totalPaidByBuyers),
                new Label("Seller's Commissions: $" + totalSellersCommissions),
                new Label("Seller's Profit: $" + totalSellersProfit),
                new Label("Weight: " + totalWeight)
            );
        view.getConcludedAuctionsBox().getChildren().add(itemBox);
        itemBox.toBack();
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