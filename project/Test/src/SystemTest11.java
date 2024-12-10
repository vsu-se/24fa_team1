
import static org.junit.jupiter.api.Assertions.*;

import application.*;
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
    private ObservableList<Item> items;
    private MainController mainController;
    private MainView mainView;
    private ObservableList<Category> categories;

    @BeforeAll
    public static void initToolkit() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await(5, TimeUnit.SECONDS);
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            categories = FXCollections.observableArrayList();
            mainView = new MainView(categories);
            mainController = new MainController(mainView, new SystemClock());
            items = mainController.getItems();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testItemWithZeroInitialBid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Creating an item with an initial bid of zero
            Category category = new Category("Books");
            LocalDateTime startDate = mainController.getTime();
            LocalDateTime endDate = mainController.getTime().plusDays(7);

            Item item = new Item("Book", "0.5kg", "A rare book", category, "New", "books", "rare", "collectible", startDate, endDate, 100.0,  0.0, mainController.getClock());

            items.clear();
            items.add(item);

            //Simulate a user placing a bid
            item.placeBid(101.00);

            //Skip time until item has closed
            mainController.setTime(LocalDateTime.now().plusDays(8));
            mainController.scheduleNextUpdate();

            //Generate Report
            mainController.generateSellerReport();

            assertEquals(5, mainView.getSellerReportBox().getChildren().size());
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
            Category category = new Category("Home");
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.now().plusDays(7);

            Item item = new Item("Sofa", "30kg", "A comfy sofa", category, "Used", "furniture", "couch", "livingroom", startDate, endDate, null, 1000.0, mainController.getClock());

            items.clear();
            items.add(item);

            //Simulate a user placing a bid
            item.placeBid(1001.00);

            //Skip time until after item has closed
            mainController.setTime(LocalDateTime.now().plusDays(8));
            mainController.scheduleNextUpdate();

            mainController.generateSellerReport(); // Generate the report

            // Verify if the item is included in the report despite missing the Buy It Now price
            assertEquals(5, mainView.getSellerReportBox().getChildren().size());
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
            Category category = new Category("Sports");
            SystemClock clock = new SystemClock();
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.now().plusDays(7);

            Item item = new Item("Football", "1kg", "A professional football", category, "New", "sports", "football", "game", startDate, endDate, 50.0, 40.0, mainController.getClock());

            items.clear();
            items.add(item);

            //Simulate a user placing a bid
            item.placeBid(60.00);

            //Skip time until after item has closed
            mainController.setTime(LocalDateTime.now().plusDays(8));
            mainController.scheduleNextUpdate();

            mainController.generateSellerReport(); // Generate the report

            // Check if the item is included in the report
            assertEquals(5, mainView.getSellerReportBox().getChildren().size());
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
            Category category = new Category("Electronics");
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.now().plusDays(7);

            Item item = new Item("Old TV", "10kg", "A used TV in decent condition", category, "Used", "electronics", "tv", "used", startDate, endDate, 0.0, 0.0, mainController.getClock());

            items.clear();
            items.add(item);

            //Skip time until after item has closed without placing a bid
            mainController.setTime(LocalDateTime.now().plusDays(8));
            mainController.scheduleNextUpdate();

            mainController.generateSellerReport(); // Generate the report

            // Ensure that the report does not include an item with an empty or zero bid
            assertEquals(4, mainView.getSellerReportBox().getChildren().size());


            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }


    @Test
    public void testItemWithCommission() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Category category = new Category("Art");
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.now().plusDays(7);

            double specialCommissionRate = 10.0;

            Item item = new Item("Painting", "5kg", "A rare painting", category, "New", "art", "painting", "fineart", startDate, endDate, 200.0,200.00, mainController.getClock());

            items.clear();
            items.add(item);

            //Simulate an admin changing the seller's commission rate to 10%
            mainController.setSellerCommissionForTest(10);

            //Simulate a user placing a bid
            item.placeBid(4000.00);

            double expectedCommission = 4000.0 * 0.1; // 10% of the winning bid

            //Skip time until after item has closed
            mainController.setTime(LocalDateTime.now().plusDays(8));
            mainController.scheduleNextUpdate();

            mainController.generateSellerReport(); // Generate the report

            // Check if the report includes the item and calculates the commission correctly
            // Assuming that the item has a commission of 10% of the winning bid
            assertEquals(expectedCommission, mainView.getSellerReportBox().getChildren().stream()
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
                Category electronics = new Category("Electronics");
                mainController.getCategories().add(electronics);
                // Setup items
                Item item1 = new Item("Laptop", "2kg", "High-end gaming laptop", electronics, "New", "Gaming", "Laptop", "Electronics", LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), 1200.0, 0.0, mainController.getClock());
                Item item2 = new Item("Phone", "200g", "Latest smartphone", electronics, "New", "Smartphone", "Phone", "Electronics", LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2), 600.0, 0.0, mainController.getClock());
                mainController.addItem(item1);
                mainController.addItem(item2);

                item1.placeBid(1200);
                item2.placeBid(300);


                //Skip time until after item has closed
                mainController.setTime(LocalDateTime.now().plusDays(8));
                mainController.scheduleNextUpdate();

                mainController.generateSellerReport(); // Generate the report

                // Assertions to verify the seller report details
                double expectedTotalWinningBids = 1500.0;
                double expectedTotalShippingCosts = item1.getShippingCost() + item2.getShippingCost();
                double expectedTotalSellerCommissions = (item1.getCurrentBid() * mainController.getSellerCommission() / 100) +
                        (item2.getCurrentBid() * mainController.getSellerCommission() / 100);
                double expectedTotalProfits = expectedTotalWinningBids - expectedTotalSellerCommissions;

                // Verify the total winning bids
                assertEquals(expectedTotalWinningBids, mainView.getSellerReportBox().getChildren().stream()
                        .filter(node -> node instanceof Label && ((Label) node).getText().startsWith("Total Winning Bids:"))
                        .mapToDouble(node -> Double.parseDouble(((Label) node).getText().split("\\$")[1]))
                        .sum());

                // Verify the total shipping costs
                assertEquals(expectedTotalShippingCosts, mainView.getSellerReportBox().getChildren().stream()
                        .filter(node -> node instanceof Label && ((Label) node).getText().startsWith("Total Shipping Costs:"))
                        .mapToDouble(node -> Double.parseDouble(((Label) node).getText().split("\\$")[1]))
                        .sum());

                // Verify the total seller commissions
                assertEquals(expectedTotalSellerCommissions, mainView.getSellerReportBox().getChildren().stream()
                        .filter(node -> node instanceof Label && ((Label) node).getText().startsWith("Total Seller’s Commissions:"))
                        .mapToDouble(node -> Double.parseDouble(((Label) node).getText().split("\\$")[1]))
                        .sum());

                // Verify the total profits
                assertEquals(expectedTotalProfits, mainView.getSellerReportBox().getChildren().stream()
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