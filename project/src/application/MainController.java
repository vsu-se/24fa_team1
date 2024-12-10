package application;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.application.Platform;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.Timer;

public class MainController {
    private ObservableList<Category> categories;
    private ObservableList<Category> concludedCategories;
    private ObservableList<Item> items;
    private MainView view;
    private double buyerPremium;
    private double sellerCommission;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledFuture;
    private TextField currentBidInput;
    private SystemClock clock;
    private Category allAuctions = new Category("All Auctions");


    public MainController(MainView view, SystemClock clock) {
        this.view = view;
        this.clock = clock;
        buyerPremium = 0.00;
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
                    generateSellerReport();
                    generateBuyerReport();

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                view.getDisplayTimeArea().setText("Current Time: " + clock.getTime().format(formatter));
            }
        };
        timer.scheduleAtFixedRate(task,  0,  1000);













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
        concludedCategories.add(allAuctions);
        concludedCategories.addAll(categories);
        view.getCategoryComboBoxConcludedAuctions().setItems(concludedCategories);

        // Initial display update
        updateProfileItemsDisplay();
        generateSellerReport();
        generateBuyerReport();
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

    public void checkAndUpdateItems() {
        for (Item item : items) {
            item.checkAndSetInactive();
        }
        Platform.runLater(() -> {
            updateItemsDisplay();
            updateProfileItemsDisplay();
            updateConcludedAuctionsDisplay();
            generateSellerReport();
            generateBuyerReport();
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
                    Button bidHistoryButton = new Button("Show Bid History");
                    bidHistoryButton.setOnAction(event -> {

                                BidHistoryView bidHistoryView = new BidHistoryView(item);
                                Tab bidHistoryTab = new Tab("Bid History", bidHistoryView.getLayout());
                                bidHistoryTab.setClosable(true);

                                view.getTabPane().getTabs().add(bidHistoryTab);
                                view.getTabPane().getSelectionModel().select(bidHistoryTab);
                            });
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
            Button bidHistoryButton = new Button("Show Bid History");
            bidHistoryButton.setOnAction(event -> {

                BidHistoryView bidHistoryView = new BidHistoryView(item);
                Tab bidHistoryTab = new Tab("Bid History", bidHistoryView.getLayout());
                bidHistoryTab.setClosable(true);

                view.getTabPane().getTabs().add(bidHistoryTab);
                view.getTabPane().getSelectionModel().select(bidHistoryTab);
            });

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
                        new Label("My Bid: $" + item.getCurrentBid()), //currently equal, no profiles yet
                        bidHistoryButton
                );
                view.getMyProfileItemsBox().getChildren().add(itemBoxProfile);
            }
        }
        view.setNumMyBids(numMyBids);
    }

    private void updateConcludedAuctionsDisplay() {
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
               // totalBuyersPremiums += item.getBuyersPremium(buyersPremium);
                //totalPaidByBuyers += item.getBuyersPremium(buyersPremium) + item.getCurrentBid();
                totalWeight += Double.parseDouble(item.getWeight());
             //   totalSellersCommissions += item.getSellersCommission(sellerCommission);
                totalSellersProfit += item.getCurrentBid();

                HBox itemBox = new HBox(10);

                Button bidHistoryButton = new Button("Show Bid History");

                bidHistoryButton.setOnAction(event -> {

                    BidHistoryView bidHistoryView = new BidHistoryView(item);
                    Tab bidHistoryTab = new Tab("Bid History", bidHistoryView.getLayout());
                    bidHistoryTab.setClosable(true);

                    view.getTabPane().getTabs().add(bidHistoryTab);
                    view.getTabPane().getSelectionModel().select(bidHistoryTab);
                });
                itemBox.getChildren().add(new Label("Item Name: " + item.getTitle()));
                if (item.getBuyItNowPrice() != null) {
                    itemBox.getChildren().add(new Label("Buy It Now Price: $" + item.getBuyItNowPrice()));
                }
                itemBox.getChildren().addAll(
                        new Label("Winning Bid: $" + item.getCurrentBid()),
                        new Label("Weight: " + item.getWeight()),
                        new Label("End-date: " + item.getEndDate()),
                        bidHistoryButton
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

    public void generateSellerReport() {
        view.getSellerReportBox().getChildren().clear();
        Category selectedCategory = view.getCategoryComboBoxConcludedAuctions().getValue();

        double totalWinningBids = 0;
        double totalShippingCosts = 0;
        double totalSellerCommissions = 0;

        items.sort((item1, item2) -> item2.getEndDate().compareTo(item1.getEndDate()));

        for (Item item : items) {
            if (!item.isActive()) {
                double winningBid = item.getCurrentBid();
                double shippingCost = item.calculateShippingCost();
                double sellersCommission = (winningBid * sellerCommission) / 100;

                totalWinningBids += winningBid;
                totalShippingCosts += shippingCost;
                totalSellerCommissions += sellersCommission;

                HBox itemBox = new HBox(10);
                itemBox.getChildren().addAll(
                        new Label("Name: " + item.getTitle()),
                        new Label("Price: $" + winningBid),
                        new Label("Seller's Commission: $" + sellerCommission),
                        new Label("Shipping: $" + shippingCost)
                );
                view.getSellerReportBox().getChildren().add(itemBox);
            }
        }

        double totalProfits = totalWinningBids - totalSellerCommissions;

        view.getSellerReportBox().getChildren().addAll(
                new Label("Total Winning Bids: $" + totalWinningBids),
                new Label("Total Shipping Costs: $" + totalShippingCosts),
                new Label("Total Seller’s Commissions: $" + totalSellerCommissions),
                new Label("Total Profits: $" + totalProfits)
        );
    }
    public void generateBuyerReport() {
        view.getBuyerReportBox().getChildren().clear();

        double totalAmountBought = 0;
        double totalBuyerPremiumsPaid = 0;
        double totalShippingCostPaid = 0;

        items.sort((item1, item2) -> item2.getEndDate().compareTo(item1.getEndDate()));

        for (Item item : items) {
            if (!item.isActive()) {
                double price = item.getCurrentBid();
                double shippingCost = item.calculateShippingCost();
                double buyerPremiumAmount = (price * buyerPremium) / 100;

                totalAmountBought += price;
                totalBuyerPremiumsPaid += buyerPremiumAmount;
                totalShippingCostPaid += shippingCost;

                HBox itemBox = new HBox(10);
                itemBox.getChildren().addAll(
                        new Label("Name: " + item.getTitle()),
                        new Label("Price: $" + price),
                        new Label("Buyer's Premium: $" + buyerPremiumAmount),
                        new Label("Shipping: $" + shippingCost)
                );
                view.getBuyerReportBox().getChildren().add(itemBox);
            }
        }

        view.getBuyerReportBox().getChildren().addAll(
                new Label("Total Amount Bought: $" + totalAmountBought),
                new Label("Total Buyer’s Premiums Paid: $" + totalBuyerPremiumsPaid),
                new Label("Total Shipping Cost Paid: $" + totalShippingCostPaid)
        );
    }

    public void setBuyerPremiumForTest(double buyerPremium) {
        this.buyerPremium = buyerPremium;
    }

    public void setSellerCommissionForTest(double sellerCommission) {
        this.sellerCommission = sellerCommission;
    }



    // Method to save categories as text
    public void saveCategoriesText(String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            for (Category category : categories) {
                file.write(category + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCategoriesText(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!isDuplicateCategory(line)) {
                    categories.add(new Category(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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