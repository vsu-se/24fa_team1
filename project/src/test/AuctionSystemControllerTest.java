package test;

import application.*;

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
    private AuctionSystemController controller;

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
            // Initialize the AuctionSystemView
            mockView = new AuctionSystemView();
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
            assertEquals(10.5, controller.getBuyerPremium());
        });
        latch.await(1, TimeUnit.SECONDS);
    }

    @Test
    void testSetCommission() throws InterruptedException{
        // Set the commission value
        String commissionText = "5.0";
        controller.setCommission(commissionText);

        // Verify that the commission was set correctly
        assertEquals(5.0, controller.getSellerCommission());


    }

    @Test
    void testChangeTime() throws InterruptedException {
        // Prepare the test data
        LocalDate newDate = LocalDate.of(2024, 12, 10);
        String timeString = "10:00";
        LocalTime newTime = LocalTime.parse("10:00");

        // Change the time using the controller
        controller.changeTime(newDate, newTime);

        // Verify that the system time was updated correctly
        assertEquals(LocalDateTime.of(newDate, newTime), controller.getClock().getTime());
    }

    @Test
    void testResumeTime() throws InterruptedException{
        // Prepare the system time
        LocalDateTime initialTime = controller.getClock().getTime();

        // Pause and resume the time using the controller
        controller.pauseTime();
        controller.resumeTime();

        // Verify that the system time was resumed correctly
        assertNotEquals(initialTime, controller.getClock().getTime());  // Check that time has resumed
    }

    @Test
    void testPauseTime() throws InterruptedException{
        // Pause time using the controller
        controller.pauseTime();

        // Verify that the system is paused
        assertTrue(controller.getClock().isPaused());
    }

    @Test
    void testCreateAuction()throws InterruptedException {
    	controller.getAuctions().clear();
    	LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Auction auction = new Auction(new Item("Laptop", "2.5 kg", "A powerful laptop", "New"), controller.getCategories().get(0), "tag1", "tag2", "tag3", LocalDateTime.now(), endDate, 1500.0, 500.0, controller.getClock());
        controller.addAuction(auction);
        
        Auction createdAuction = controller.getAuctions().get(0);
        
        assertEquals("Laptop", createdAuction.getItem().getTitle());
        assertEquals("2.5 kg", createdAuction.getItem().getWeight());
        assertEquals("A powerful laptop", createdAuction.getItem().getDescription());
        assertEquals(controller.getCategories().get(0), createdAuction.getCategory());
        assertEquals("New", createdAuction.getItem().getCondition());
        assertEquals("tag1", createdAuction.getTag1());
        assertEquals("tag2", createdAuction.getTag2());
        assertEquals("tag3", createdAuction.getTag3());
        assertEquals(endDate, createdAuction.getEndDate());
        assertEquals(1500.0, createdAuction.getBuyItNowPrice());
    }

    @Test
    void testUpdateItemsDisplay()throws InterruptedException {
        // Prepare auctions
        Auction auction1 = new Auction(new Item("Item1", "1.0", "Description", "New"), "Electronics", "Tag1", "Tag2", "Tag3", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 100.0, 50.0, controller.getClock());
        Auction auction2 = new Auction(new Item("Item2", "0.5", "Description", "Used"), "Electronics", "Tag1", "Tag2", "Tag3", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 200.0, 100.0, controller.getClock());
        controller.addAuction(auction1);
        controller.addAuction(auction2);

        // Update the items display
        controller.updateItemsDisplay();

        // Verify that the items are sorted by the end date
        List<Auction> auctions = controller.getAuctions();
        assertEquals(auction1, auctions.get(0)); // Auction1 should be before
    }
}