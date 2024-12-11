package test;
import application.*;

import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class SystemTest11{
    private ObservableList<Auction> auctions;
    private AuctionSystemController controller;
    private AuctionSystemView view;
    private ObservableList<String> categories;

    @BeforeAll
    public static void initToolkit() throws Exception {
    	AuctionStatePersistence.canWrite = false;
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await(5, TimeUnit.SECONDS);
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            categories = FXCollections.observableArrayList();
            view = new AuctionSystemView();
            controller = view.getController();
            auctions = controller.getAuctions();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testItemWithZeroInitialBid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Creating an item with an initial bid of zero
            String category = "Books";
            LocalDateTime startDate = controller.getClock().getTime();
            LocalDateTime endDate = controller.getClock().getTime().plusDays(7);

            Auction auction = new Auction(new Item("Book", "0.5kg", "A rare book", "New"), category, "books", "rare", "collectible", startDate, endDate, 100.0,  0.0, controller.getClock());

            auctions.clear();
            auctions.add(auction);

            //Simulate a user placing a bid
            auction.placeBid(101.00);

            //Skip time until item has closed
            controller.getClock().setTime(LocalDateTime.now().plusDays(8));
            controller.getScheduler().scheduleNextUpdate();

            //Generate Report
            controller.generateSellerReport();

            assertEquals(5, view.getSellerReportBox().getChildren().size());
            //must be 5, since there are always at least 4 labels (which display totals). Adding an item should add 1 label (4 + 1 = 5)
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testItemWithoutBuyItNowPrice() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Creating an item with no "Buy It Now" price
            String category =  "Home";
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.now().plusDays(7);

            Auction auction = new Auction(new Item("Sofa", "30kg", "A comfy sofa", "Used"), category, "furniture", "couch", "livingroom", startDate, endDate, null, 1000.0, controller.getClock());

            auctions.clear();
            auctions.add(auction);

            //Simulate a user placing a bid
            auction.placeBid(1001.00);

            //Skip time until after item has closed
            controller.getClock().setTime(LocalDateTime.now().plusDays(8));
            controller.getScheduler().scheduleNextUpdate();

            controller.generateSellerReport(); // Generate the report

            // Verify if the item is included in the report despite missing the Buy It Now price
            assertEquals(5, view.getSellerReportBox().getChildren().size());
            //must be 5, since there are always at least 4 labels (which display totals). Adding an item should add 1 label (4 + 1 = 5)
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testItemWithTags() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Creating an item with tags
            String category = "Sports";
            SystemClock clock = new SystemClock();
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.now().plusDays(7);

            Auction auction = new Auction(new Item("Football", "1kg", "A professional football", "New"), category, "sports", "football", "game", startDate, endDate, 50.0, 40.0, controller.getClock());

            auctions.clear();
            auctions.add(auction);

            //Simulate a user placing a bid
            auction.placeBid(60.00);

            //Skip time until after item has closed
            controller.getClock().setTime(LocalDateTime.now().plusDays(8));
            controller.getScheduler().scheduleNextUpdate();

            controller.generateSellerReport(); // Generate the report

            // Check if the item is included in the report
            assertEquals(5, view.getSellerReportBox().getChildren().size());
            //must be 5, since there are always at least 4 labels (which display totals). Adding an item should add 1 label (4 + 1 = 5)
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testItemWithEmptyBid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Creating an item with an empty bid (initial bid set to zero)
            String category = "Electronics";
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.now().plusDays(7);

            Auction auction = new Auction(new Item("Old TV", "10kg", "A used TV in decent condition", "Used"), category, "electronics", "tv", "used", startDate, endDate, 0.0, 0.0, controller.getClock());

            auctions.clear();
            auctions.add(auction);

            //Skip time until after item has closed without placing a bid
            controller.getClock().setTime(LocalDateTime.now().plusDays(8));
            controller.getScheduler().scheduleNextUpdate();

            controller.generateSellerReport(); // Generate the report

            // Ensure that the report does not include an item with an empty or zero bid
            assertEquals(4, view.getSellerReportBox().getChildren().size());


            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }


    @Test
    public void testItemWithCommission() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            String category = "Art";
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.now().plusDays(7);

            Auction auction= new Auction(new Item("Painting", "5kg", "A rare painting", "New"), category, "art", "painting", "fineart", startDate, endDate, 200.0,200.00, controller.getClock());

            auctions.clear();
            auctions.add(auction);

            //Simulate an admin changing the seller's commission rate to 10%
            controller.setCommission("10");

            //Simulate a user placing a bid
            auction.placeBid(4000.00);

            double expectedCommission = 4000.0 * 0.1; // 10% of the winning bid

            //Skip time until after item has closed
            controller.getClock().setTime(LocalDateTime.now().plusDays(8));
            controller.getScheduler().scheduleNextUpdate();

            controller.generateSellerReport(); // Generate the report

            // Check if the report includes the item and calculates the commission correctly
            // Assuming that the item has a commission of 10% of the winning bid
            assertEquals(expectedCommission, view.getSellerReportBox().getChildren().stream()
                    .filter(node -> node instanceof Label && ((Label) node).getText().startsWith("Total Seller’s Commissions:"))
                    .mapToDouble(node -> Double.parseDouble(((Label) node).getText().split("\\$")[1]))
                    .sum());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }





    @Test
    public void testGenerateSellerReport() throws Exception {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                // Setup categories
                String electronics = "Electronics";
                controller.getCategories().add(electronics);
                // Setup items
                Auction auction1 = new Auction(new Item("Laptop", "2kg", "High-end gaming laptop", "New"), electronics, "Gaming", "Laptop", "Electronics", LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), 1200.0, 0.0, controller.getClock());
                Auction auction2 = new Auction(new Item("Phone", "200g", "Latest smartphone", "New"), electronics, "Smartphone", "Phone", "Electronics", LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2), 600.0, 0.0, controller.getClock());
                controller.addAuction(auction1);
                controller.addAuction(auction2);

                auction1.placeBid(1200);
                auction2.placeBid(300);


                //Skip time until after item has closed
                controller.getClock().setTime(LocalDateTime.now().plusDays(8));
                controller.getScheduler().scheduleNextUpdate();

                controller.generateSellerReport(); // Generate the report

                // Assertions to verify the seller report details
                double expectedTotalWinningBids = 1500.0;
                double expectedTotalShippingCosts = auction1.getShippingCost() + auction2.getShippingCost();
                double expectedTotalSellerCommissions = (auction1.getCurrentBid() * controller.getSellerCommission() / 100) +
                        (auction2.getCurrentBid() * controller.getSellerCommission() / 100);
                double expectedTotalProfits = expectedTotalWinningBids - expectedTotalSellerCommissions;

                // Verify the total winning bids
                assertEquals(expectedTotalWinningBids, view.getSellerReportBox().getChildren().stream()
                        .filter(node -> node instanceof Label && ((Label) node).getText().startsWith("Total Winning Bids:"))
                        .mapToDouble(node -> Double.parseDouble(((Label) node).getText().split("\\$")[1]))
                        .sum());

                // Verify the total shipping costs
                assertEquals(expectedTotalShippingCosts, view.getSellerReportBox().getChildren().stream()
                        .filter(node -> node instanceof Label && ((Label) node).getText().startsWith("Total Shipping Costs:"))
                        .mapToDouble(node -> Double.parseDouble(((Label) node).getText().split("\\$")[1]))
                        .sum());

                // Verify the total seller commissions
                assertEquals(expectedTotalSellerCommissions, view.getSellerReportBox().getChildren().stream()
                        .filter(node -> node instanceof Label && ((Label) node).getText().startsWith("Total Seller’s Commissions:"))
                        .mapToDouble(node -> Double.parseDouble(((Label) node).getText().split("\\$")[1]))
                        .sum());

                // Verify the total profits
                assertEquals(expectedTotalProfits, view.getSellerReportBox().getChildren().stream()
                        .filter(node -> node instanceof Label && ((Label) node).getText().startsWith("Total Profits:"))
                        .mapToDouble(node -> Double.parseDouble(((Label) node).getText().split("\\$")[1]))
                        .sum());
            } finally {
                testLatch.countDown();
            }
        });
        if (!testLatch.await(5, TimeUnit.SECONDS)) {
            throw new Exception("Test execution took too long");
        }
    }
}