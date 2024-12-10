//Previously MainController, should just be the main controller for the system, I will figure out as I go ig
package application;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;

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

public class AuctionSystemController {
	private AuctionSystemView view;
	private AuctionSystem system;
	private UpdateScheduler scheduler;
	
	public AuctionSystemController(AuctionSystemView view) {
		this.view = view;
		system = new AuctionSystem();
		scheduler = new UpdateScheduler(this);
		
		// Add listener to the items list
        system.getAuctions().addListener((ListChangeListener<Auction>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    updateProfileItemsDisplay();
                }
            }
        });
        
        // Add listener to the category combo box in the user interface tab
        view.getCategoryComboBoxUserInterface().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateItemsDisplay();
        });

        view.getCategoryComboBoxConcludedAuctions().valueProperty().addListener((observable, oldValue, newValue) -> {
            updateConcludedAuctionsDisplay();
        });
        
        // Bind the categories list to the ComboBoxes, THIS PART IS MESSY I KNOW IM SORRY
        view.getCategoryComboBoxSystemAdmin().setItems(system.getCategories());
        view.getCategoryComboBoxUserInterface().setItems(system.getCategories());
        system.getConcludedCategories().add("All Auctions");
        system.getConcludedCategories().addAll(system.getCategories());
        view.getCategoryComboBoxConcludedAuctions().setItems(system.getConcludedCategories());
        
        setTimer();
        
        // Initial display update
        updateProfileItemsDisplay();
	}
	
	public void setTimer() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
			@Override
			public void run() {
				view.getDisplayTimeArea().setText("Current Time: " + system.getClock().getTime().format(formatter));
			}
		};
		timer.scheduleAtFixedRate(task,  0,  1000);
	}

	public void checkAndUpdateAuctions() {
        for (Auction auction : system.getAuctions()) {
            auction.checkAndSetInactive();
        }
        Platform.runLater(() -> {
            updateItemsDisplay();
            updateProfileItemsDisplay();
            updateConcludedAuctionsDisplay();
        });
        scheduler.scheduleNextUpdate();
    }

	public Auction getNextExpiringAuction() {
        return system.getAuctions().stream()
                .filter(Auction::isActive)
                .min(Comparator.comparing(Auction::getEndDate))
                .orElse(null);
    }

	private void updateConcludedAuctionsDisplay() { //working?? W
		view.getConcludedAuctionsBox().getChildren().clear();
        String selectedCategory = view.getCategoryComboBoxConcludedAuctions().getValue();
        
        double totalOfWinningBids = 0.00;
        double totalShippingCosts = 0.00;
        double totalBuyersPremiums = 0.00;
        double totalPaidByBuyers = 0.00;
        double totalWeight = 0.00;
        double totalSellersCommissions = 0.00;
        double totalSellersProfit = 0.00;
         
        for (Auction auction : system.getAuctions()) {
            if (!auction.isActive() && selectedCategory != null && (auction.getCategory().equals(selectedCategory) || selectedCategory.equals("All Auctions"))) {
            	totalOfWinningBids += auction.getCurrentBid();
            	totalShippingCosts += auction.getShippingCost();
            	totalBuyersPremiums += auction.getBuyersPremium(system.getBuyersPremium());
            	totalPaidByBuyers += auction.getBuyersPremium(system.getBuyersPremium()) + auction.getCurrentBid();
            	totalWeight += Double.parseDouble(auction.getItem().getWeight());
            	totalSellersCommissions += auction.getSellersCommission(system.getSellerCommission());
            	totalSellersProfit += auction.getCurrentBid();
            	
                HBox itemBox = new HBox(10);
                itemBox.getChildren().add(new Label("Item Name: " + auction.getItem().getTitle()));
                if (auction.getBuyItNowPrice() != null) {
                    itemBox.getChildren().add(new Label("Buy It Now Price: $" + auction.getBuyItNowPrice()));
                }
                itemBox.getChildren().addAll(
                	new Label("Winning Bid: $" + auction.getCurrentBid()),
                    new Label("Weight: " + auction.getItem().getWeight()),
                    new Label("End-date: " + auction.getEndDate())
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

	public void addCategory(String categoryName) {
		clearErrorMessages();
		if (!categoryName.isEmpty() && !system.getCategories().contains(categoryName)) {
            system.getCategories().add(categoryName);
            //system.getConcludedCategories.add(categoryName);
            view.getCategoryInput().clear();
            view.getCategoryErrorLabel().setText(""); // Clear error message
            updateItemsDisplay();
        } else {
            view.getCategoryErrorLabel().setText("Category already exists or name is empty.");
        }
	}
	
	public void setPremium(String premiumText) {
		clearErrorMessages();
		try {
            double premiumValue = Double.parseDouble(premiumText);
            if (premiumValue < 0) {
                throw new NumberFormatException("Negative value");
            }
            system.setBuyersPremium(premiumValue);
            view.getPremiumInput().clear();
            view.getBuyerPremiumLabel().setText(String.format("Buyer's Premium: %.2f", premiumValue) + "%");
            view.getPremiumErrorLabel().setText(""); // Clear error message
        } catch (NumberFormatException e) {
            view.getPremiumErrorLabel().setText("Invalid premium value. Please enter a non-negative number.");
        }
	}
	
	public void setCommission(String commissionText) {
		clearErrorMessages();
		try {
            double commissionValue = Double.parseDouble(commissionText);
            if (commissionValue < 0) {
                throw new NumberFormatException("Negative value");
            }
            system.setSellerCommission(commissionValue);
            view.getCommissionInput().clear();
            view.getSellerCommissionLabel().setText(String.format("Buyer's Premium: %.2f", commissionValue) + "%");
            view.getCommissionErrorLabel().setText(""); // Clear error message
        } catch (NumberFormatException e) {
            view.getCommissionErrorLabel().setText("Invalid commission value. Please enter a non-negative number.");
        }
	}
	
	public void changeTime(LocalDate date, String timeString, LocalTime time) {
		clearErrorMessages();
		try {
            system.getClock().setTime(LocalDateTime.of(date, time));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            view.getCategoryErrorLabel().setText("Time changed.");
            
            scheduler.scheduleNextUpdate();
        } catch (Exception e) {
            changeTimeError();
            return;
        }
	}
	
	public void changeTimeError() {
		view.getCategoryErrorLabel().setText("Please enter a valid time in the format yyyy-MM-dd HH:mm:ss.");
	}
	
	public void resumeTime() {
		clearErrorMessages();
		system.getClock().setTime(LocalDateTime.now());
        scheduler.scheduleNextUpdate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        view.getCategoryErrorLabel().setText("Time resumed. It is now " + system.getClock().getTime().format(formatter));
        system.getClock().setIsPaused(false);
	}
	
	public void pauseTime() {
		clearErrorMessages();
        system.getClock().setIsPaused(true);
        view.getCategoryErrorLabel().setText("Time has been paused.");
        updateItemsDisplay();
	}
	
	public void unpauseTime() {
		clearErrorMessages();
        system.getClock().setIsPaused(false);
        view.getCategoryErrorLabel().setText("Time has been unpaused.");
        updateItemsDisplay();
	}

	private void clearErrorMessages() {
		view.getCategoryErrorLabel().setText("");
        view.getPremiumErrorLabel().setText("");
        view.getCommissionErrorLabel().setText("");
        view.getListItemErrorLabel().setText("");
	}
	
	private void updateItemsDisplay() {
		system.getAuctions().sort(Comparator.comparing(Auction::getEndDate));
        view.getUserInterfaceItemsBox().getChildren().clear();
        String selectedCategory = view.getCategoryComboBoxUserInterface().getValue();
        for (Auction auction : system.getAuctions()) {
            if (selectedCategory == null || auction.getCategory().equals(selectedCategory)) {
                if (auction.isActive()) {
                    HBox itemBox = new HBox(10);
                    itemBox.getChildren().add(new Label("Title: " + auction.getItem().getTitle()));
                    if (auction.getBuyItNowPrice() != null) {
                        itemBox.getChildren().add(new Label("Buy It Now Price: $" + auction.getBuyItNowPrice()));
                    }
                    itemBox.getChildren().addAll(
                        new Label("Weight: " + auction.getItem().getWeight()),
                        new Label("Active: " + (auction.isActive() ? "Yes" : "No")),
                        new Label("Current Bid: $" + auction.getCurrentBid())
                    );

                    // Add bid input and button
                    TextField bidAmountInput = new TextField();
                    bidAmountInput.setPromptText("Enter bid amount");
                    Button placeBidButton = new Button("Place Bid");
                    placeBidButton.setOnAction(event -> {
                        try {
                            double bidAmount = Double.parseDouble(bidAmountInput.getText());
                            if (auction.placeBid(bidAmount)) {
                            	auction.setHasBidder(true);
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

	private void updateProfileItemsDisplay() {
		view.getMyProfileItemsBox().getChildren().clear();
        for (Auction auction : system.getAuctions()) {
            HBox itemBoxProfile = new HBox(10);
            itemBoxProfile.getChildren().add(new Label("Title: " + auction.getItem().getTitle()));
            if (auction.getBuyItNowPrice() != null) {
                itemBoxProfile.getChildren().add(new Label("Buy It Now Price: $" + auction.getBuyItNowPrice()));
            }
            itemBoxProfile.getChildren().addAll(
                new Label("Weight: " + auction.getItem().getWeight()),
                new Label("Active: " + (auction.isActive() ? "Yes" : "No")),
                new Label("Current Bid: $" + auction.getCurrentBid())
            );
            
            view.getMyProfileItemsBox().getChildren().add(itemBoxProfile);
        }
        
        view.getMyProfileItemsBox().getChildren().add(new Label("Current Bids: "));
        
        int numMyBids = 0;
        for (Auction auction : system.getAuctions()) {
        	
        	if(auction.hasBidder() && auction.isActive()) { //Do not show inactive auctions with a bid.
        		numMyBids++;
        		HBox itemBoxProfile = new HBox(10);
                itemBoxProfile.getChildren().add(new Label("Title: " + auction.getItem().getTitle()));
                if (auction.getBuyItNowPrice() != null) {
                    itemBoxProfile.getChildren().add(new Label("Buy It Now Price: $" + auction.getBuyItNowPrice()));
                }
                itemBoxProfile.getChildren().addAll(
                    new Label("Weight: " + auction.getItem().getWeight()),
                    new Label("Active: " + (auction.isActive() ? "Yes" : "No")),
                    new Label("Current Bid: $" + auction.getCurrentBid()),
                    new Label("My Bid: $" + auction.getCurrentBid()) //currently equal, no profiles yet
                );
                view.getMyProfileItemsBox().getChildren().add(itemBoxProfile);	
        	}
        }
        view.setNumMyBids(numMyBids);
	}


	public void openAddItemView() {
		clearErrorMessages();
		if(system.getCategories().isEmpty()) { //Get state from model
			//Update View
			view.getListItemErrorLabel().setText("Please add a category in the System Admin tab before listing an item.");
			return;
		}
		//Update View
		view.createAddItemTab();
	}
	
	public void createAuction(String title, String weight, String weightUnit, String description, String category, String condition, String tag1, String tag2, String tag3, LocalDate endDate, String endTime, String buyItNowPriceText, String bidAmountText, Label errorLabel, Tab tab) {
		if (title.isEmpty() || weight.isEmpty() || weightUnit == null || description.isEmpty() || category == null || condition == null || endDate == null || endTime.isEmpty() || bidAmountText.isEmpty()) {
	        errorLabel.setText("Please fill in all required fields.");
	          
	        return;
	    }
	
	    try {
	        Double.parseDouble(weight);
	    } catch (NumberFormatException e) {
	    	errorLabel.setText("Please enter a valid number for the weight.");
	    	return;
	    }
	
	    LocalDateTime startDate = system.getTime();
	    LocalDateTime endDateTime;
	    try {
	        endDateTime = LocalDateTime.of(endDate, LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm")));
	    } catch (Exception e) {
	    	errorLabel.setText("Please enter a valid end time in the format HH:mm.");
	    	return;
	    }
	
	    if (endDateTime.isBefore(startDate)) {
	    	errorLabel.setText("The end date and time cannot be before the current date and time.");
	    	return;
	    }
	
	    Double buyItNowPrice = null;
	    if (!buyItNowPriceText.isEmpty()) {
	        try {
	            buyItNowPrice = Double.parseDouble(buyItNowPriceText);
	        } catch (NumberFormatException e) {
	        	errorLabel.setText("Please enter a valid number for the Buy it now price.");
	        	return;
	        }
	    }
	      
	   double bidAmount;
	   
	    try {
	        bidAmount = Double.parseDouble(bidAmountText);
	        if (bidAmount >= 0.00) {
	          	
	        } else {
	        	errorLabel.setText("Initial bid amount cannot be negative.");
	      		return;
	        }
	    } catch (NumberFormatException e) {
	    	errorLabel.setText("Please enter a valid bid amount.");
	    	return;
	    }
	    
	    Item newItem = new Item(title, weight, description, condition);
	    Auction newAuction = new Auction(newItem, category, tag1, tag2, tag3, startDate, endDateTime, buyItNowPrice, bidAmount, system.getClock());
	    
	    system.addAuction(newAuction);
	    
	    // Clear error message
	    errorLabel.setText("");
	    
	    // Close the "Create Item" tab
	    view.getTabPane().getTabs().remove(tab);
	    
	    // Update the items display in the MainController
	    updateItemsDisplay(); 
	    scheduler.scheduleNextUpdate(); 
	}


	public ObservableList<String> getCategories() {
		return system.getCategories();
	}
    
	public SystemClock getClock() { //messy maybe fix ok ty
		return system.getClock();
	}
	
	private boolean isDuplicateCategory(String categoryName) {
        for (String category : system.getCategories()) {
            if (category.equalsIgnoreCase(categoryName)) {
                return true;
            }
        }
        return false;
    }
	
	public void addAuction(Auction auction) {
		system.getAuctions().add(auction);
		scheduler.scheduleNextUpdate();
	}
	
	
}