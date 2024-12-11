
import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AuctionSystemControllerTest {

    private AuctionSystemView mockView;
    private AuctionSystem mockSystem;
    private AuctionSystemController controller;

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
            // Initialize the AuctionSystemView
            mockView = new AuctionSystemView();
            mockSystem = new AuctionSystem();
            // Initialize the AuctionSystemController with the mockView
            controller = mockView.getController();

            // Normally you would need to start the JavaFX Application thread for this, but for now we'll assume this runs in a test environment that supports JavaFX
            latch.countDown();
        });
        latch.await(1, TimeUnit.SECONDS);
    }


    @Test
    void testAddCategory() throws InterruptedException  {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Prepare test data
            String newCategory = "Electronics";

            // Ensure the initial state is empty
            assertFalse(controller.getCategories().contains(newCategory));

            // Add category using the controller
            controller.addCategory(newCategory);

            // Verify that the category was added
            assertTrue(controller.getCategories().contains(newCategory));

        });
        latch.await(1, TimeUnit.SECONDS);
    }
    @Test
    void testSetPremium() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Set the premium value
            String premiumText = "10.5";
            controller.setPremium(premiumText);

            // Verify that the premium was set correctly
            assertEquals(10.5, mockSystem.getBuyersPremium());
        });
        latch.await(1, TimeUnit.SECONDS);
    }

    @Test
    void testSetCommission() throws InterruptedException{
        // Set the commission value
        String commissionText = "5.0";
        controller.setCommission(commissionText);

        // Verify that the commission was set correctly
        assertEquals(5.0, mockSystem.getSellerCommission());


    }

    @Test
    void testChangeTime() throws InterruptedException {
        // Prepare the test data
        LocalDate newDate = LocalDate.of(2024, 12, 10);
        String timeString = "10:00";
        LocalTime newTime = LocalTime.parse("10:00");

        // Change the time using the controller
        controller.changeTime(newDate, timeString, newTime);

        // Verify that the system time was updated correctly
        assertEquals(LocalDateTime.of(newDate, newTime), mockSystem.getClock().getTime());
    }

    @Test
    void testResumeTime() throws InterruptedException{
        // Prepare the system time
        LocalDateTime initialTime = mockSystem.getClock().getTime();

        // Pause and resume the time using the controller
        controller.pauseTime();
        controller.resumeTime();

        // Verify that the system time was resumed correctly
        assertNotEquals(initialTime, mockSystem.getClock().getTime());  // Check that time has resumed
    }

    @Test
    void testPauseTime() throws InterruptedException{
        // Pause time using the controller
        controller.pauseTime();

        // Verify that the system is paused
        assertTrue(mockSystem.getClock().isPaused());
    }

    @Test
    void testCreateAuction()throws InterruptedException {
        // Prepare test data
        String title = "Laptop";
        String weight = "2.5";
        String weightUnit = "kg";
        String description = "A high-performance laptop.";
        String category = "Electronics";
        String condition = "New";
        String tag1 = "Tech";
        String tag2 = "Computer";
        String tag3 = "Laptop";
        LocalDate endDate = LocalDate.of(2024, 12, 20);
        String endTime = "15:00";
        String buyItNowPriceText = "1500";
        String bidAmountText = "500";

        // Create the auction
        controller.createAuction(title, weight, weightUnit, description, category, condition, tag1, tag2, tag3, endDate, endTime, buyItNowPriceText, bidAmountText, new Label(), null);

        // Verify that the auction was created correctly
        assertEquals(1, mockSystem.getAuctions().size());
        Auction createdAuction = mockSystem.getAuctions().get(0);
        assertEquals(title, createdAuction.getItem().getTitle());
        assertEquals(description, createdAuction.getItem().getDescription());
        assertEquals(Double.parseDouble(bidAmountText), createdAuction.getCurrentBid());
    }

    @Test
    void testUpdateItemsDisplay()throws InterruptedException {
        // Prepare auctions
        Auction auction1 = new Auction(new Item("Item1", "1.0", "Description", "New"), "Electronics", "Tag1", "Tag2", "Tag3", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 100.0, 50.0, mockSystem.getClock());
        Auction auction2 = new Auction(new Item("Item2", "0.5", "Description", "Used"), "Electronics", "Tag1", "Tag2", "Tag3", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 200.0, 100.0, mockSystem.getClock());
        mockSystem.addAuction(auction1);
        mockSystem.addAuction(auction2);

        // Update the items display
        controller.updateItemsDisplay();

        // Verify that the items are sorted by the end date
        List<Auction> auctions = mockSystem.getAuctions();
        assertEquals(auction1, auctions.get(0)); // Auction1 should be before
    }
    }