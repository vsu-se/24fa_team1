package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import application.*;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SystemTest8 {
    private MainController mainController;
    private MainView mainView;
    private ObservableList<Category> categories;
    private ObservableList<Item> items;

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
            mainController = new MainController(mainView);
            items = mainController.getItems();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testPlaceValidBid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	//add mock category
            Category category = new Category("Electronics");
            categories.add(category);
            //add mock items
            Item item1 = new Item("Laptop", "2 kg", "A powerful laptop", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0);
            Item item2 = new Item("PC", "2 kg", "A powerful PC", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 1000.0);
            Item item3 = new Item("Phone", "2 kg", "A powerful Phone", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 300.0);
            items.add(item1);
            items.add(item2);
            items.add(item3);
            
            //Place bid on item1
            boolean bidPlacedItem1 = item1.placeBid(600.0);
            assertTrue(bidPlacedItem1);
            assertEquals(600.0, item1.getCurrentBid());
            
            //repeat for other items
            boolean bidPlacedItem2 = item2.placeBid(10000.0);
            assertTrue(bidPlacedItem2);
            assertEquals(10000.0, item2.getCurrentBid());
            
            boolean bidPlacedItem3 = item3.placeBid(900.0);
            assertTrue(bidPlacedItem3);
            assertEquals(900.0, item3.getCurrentBid());
            
            //Would love to not do this manually, yk? maybe needs a fix
            item1.setHasBidder(true);
            item2.setHasBidder(true);
            item3.setHasBidder(true);
            
            mainController.updateProfileItemsDisplay();
            
            int numMyBids = mainView.getNumMyBids();
            assertEquals(3, numMyBids);
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
    
    
    
    

    @Test
    public void testNoActiveBids() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            
            int numMyBids = mainView.getNumMyBids();
            assertEquals(0, numMyBids);
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
    
    @Test
    public void testInactiveBid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	//add mock category
            Category category = new Category("Electronics");
            categories.add(category);
            //add mock items
            Item item1 = new Item("Laptop", "2 kg", "A powerful laptop", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0);
            items.add(item1);
            
            //Place bid on item1
            boolean bidPlacedItem1 = item1.placeBid(600.0);
            assertTrue(bidPlacedItem1);
            assertEquals(600.0, item1.getCurrentBid());
            
            //Would love to not do this manually, yk? maybe needs a fix, would love to just call the event handler for the button that's normally clicked
            item1.setHasBidder(true);
            
            mainController.updateProfileItemsDisplay();
            
            int numMyBids = mainView.getNumMyBids();
            assertEquals(1, numMyBids);
            
            //Skip time until after end-date
            item1.setEndDate(LocalDateTime.now().minusDays(1));
            mainController.updateItemsDisplay();
            mainController.updateProfileItemsDisplay();
            
            numMyBids = mainView.getNumMyBids();
            assertEquals(0, numMyBids);
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}