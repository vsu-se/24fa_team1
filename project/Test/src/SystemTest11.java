import static org.junit.jupiter.api.Assertions.*;

import application.*;
import javafx.application.Platform;
import javafx.scene.control.Label;
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
import javafx.application.Application;


public class SystemTest11 extends Application {
    private static ObservableList<Item> items;    private static MainController mainController;
    private static MainView mainView;
    private static final CountDownLatch latch = new CountDownLatch(1);

    public void start(Stage stage) throws Exception {
        latch.countDown();
    }

    @BeforeAll
    public static void initToolkit() throws Exception {
        new Thread(() -> Application.launch(SystemTest11.class)).start();
        if (!latch.await(20, TimeUnit.SECONDS)) {
            throw new Exception("JavaFX initialization took too long");
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        CountDownLatch setupLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                SystemClock clock = new SystemClock();
                mainView = new MainView(FXCollections.observableArrayList());
                mainController = new MainController(mainView, clock);
                items = mainController.getItems();
            } finally {
                setupLatch.countDown();
            }
        });
        if (!setupLatch.await(5, TimeUnit.SECONDS)) {
            throw new Exception("JavaFX setup took too long");

        }
    }
    @Test
    public void testGenerateSellerReport() {
        // Setup categories
        Category electronics = new Category("Electronics");
        mainController.getCategories().add(electronics);

        // Setup items
        Item item1 = new Item("Laptop", "2kg", "High-end gaming laptop", electronics, "New", "Gaming", "Laptop", "Electronics", LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), 1200.0, 1000.0, mainController.getClock());
        Item item2 = new Item("Phone", "200g", "Latest smartphone", electronics, "New", "Smartphone", "Phone", "Electronics", LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2), 600.0, 500.0, mainController.getClock());
        mainController.addItem(item1);
        mainController.addItem(item2);

        // Conclude auctions
        mainController.checkAndUpdateItems();

        // Generate seller report
        mainController.generateSellerReport();

        // Assertions to verify the seller report details
        double expectedTotalWinningBids = 1500.0;
        double expectedTotalShippingCosts = item1.calculateShippingCost() + item2.calculateShippingCost();
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
    }



}