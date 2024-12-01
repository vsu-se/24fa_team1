package test;

import application.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

class SystemTest10 {

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
            mainController = new MainController(mainView, new SystemClock());
            items = mainController.getItems();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
    
    @Test
    public void testEndOneAuctionWithOneAuctionActive() throws InterruptedException{
    	CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	//add mock category
            Category category = new Category("Electronics");
            categories.add(category);
            //add mock item
            Item item1 = new Item("Laptop", "2 kg", "A powerful laptop", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0, mainController.getClock());
            items.add(item1);
            
            //Make sure item is active and that there are no concluded auctions
            assertEquals(items.size(), 1);
            assertTrue(item1.isActive());
            mainController.updateConcludedAuctionsDisplay();
            assertEquals(mainView.getConcludedAuctionsBox().getChildren().size(), 0);
            
            //Skip time until after end-date
            mainController.setTime(LocalDateTime.now().plusDays(2));
            mainController.scheduleNextUpdate();
            
            //Make sure item is inactive
            assertEquals(items.size(), 1);
            assertFalse(item1.isActive());
            
            //Verify that the item is displayed under concluded auctions
            mainController.updateConcludedAuctionsDisplay();
            assertEquals(mainView.getConcludedAuctionsBox().getChildren().size(), 1);
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
    
    @Test
    public void testEndOneAuctionWithMultipleAuctionsActive() throws InterruptedException{
    	CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	//add mock category
            Category category = new Category("Electronics");
            categories.add(category);
            //add mock items
            Item item1 = new Item("Laptop", "2 kg", "A powerful laptop", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0, mainController.getClock());
            Item item2 = new Item("PC", "2 kg", "A powerful PC", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, 1000.0, mainController.getClock());
            Item item3 = new Item("Phone", "2 kg", "A powerful Phone", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, 300.0, mainController.getClock());
            items.add(item1);
            items.add(item2);
            items.add(item3);
            
            //Make sure item is active and that there are no concluded auctions
            assertEquals(items.size(), 3);
            assertTrue(item1.isActive());
            mainController.updateConcludedAuctionsDisplay();
            assertEquals(mainView.getConcludedAuctionsBox().getChildren().size(), 0);
            
            //Skip time until after end-date
            mainController.setTime(LocalDateTime.now().plusDays(2));
            mainController.scheduleNextUpdate();
            
            //Make sure item is inactive
            assertEquals(items.size(), 3);
            assertFalse(item1.isActive());
            
            //Verify that the item is displayed under concluded auctions
            mainController.updateConcludedAuctionsDisplay();
            assertEquals(mainView.getConcludedAuctionsBox().getChildren().size(), 1);
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
    
    @Test
    public void testEndTwoAuctionsWithMultipleAuctionsActive() throws InterruptedException{
    	CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	//add mock category
            Category category = new Category("Electronics");
            categories.add(category);
            //add mock items
            Item item1 = new Item("Laptop", "2 kg", "A powerful laptop", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0, mainController.getClock());
            Item item2 = new Item("PC", "2 kg", "A powerful PC", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null, 1000.0, mainController.getClock());
            Item item3 = new Item("Phone", "2 kg", "A powerful Phone", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(5), null, 300.0, mainController.getClock());
            items.add(item1);
            items.add(item2);
            items.add(item3);
            
            //Make sure item is active and that there are no concluded auctions
            assertEquals(items.size(), 3);
            assertTrue(item1.isActive());
            mainController.updateConcludedAuctionsDisplay();
            assertEquals(mainView.getConcludedAuctionsBox().getChildren().size(), 0);
            
            //Skip time until after end-date of first item
            mainController.setTime(LocalDateTime.now().plusDays(2));
            mainController.scheduleNextUpdate();
            
            //Make sure first item is inactive
            assertEquals(items.size(), 3);
            assertFalse(item1.isActive());
            
            //Verify that the first item is displayed under concluded auctions
            mainController.updateConcludedAuctionsDisplay();
            assertEquals(mainView.getConcludedAuctionsBox().getChildren().size(), 1);
            
            //Make sure the rest of the items are still active
            assertTrue(item2.isActive() && item3.isActive());
            
            //Skip time until after end-date of second item
            mainController.setTime(LocalDateTime.now().plusDays(4));
            mainController.scheduleNextUpdate();
            
            //Make sure second and first items are inactive
            assertEquals(items.size(), 3);
            assertFalse(item1.isActive() && item2.isActive());
            
            //Verify that the first and second item are displayed under concluded auctions
            mainController.updateConcludedAuctionsDisplay();
            assertEquals(mainView.getConcludedAuctionsBox().getChildren().size(), 2);
            
            //Make sure third item is still active
            assertTrue(item3.isActive());
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
    
    @Test
    public void testEndTwoAuctionsSimultaneously() throws InterruptedException{
    	CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	//add mock category
            Category category = new Category("Electronics");
            categories.add(category);
            //add mock items
            Item item1 = new Item("Laptop", "2 kg", "A powerful laptop", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 500.0, mainController.getClock());
            Item item2 = new Item("PC", "2 kg", "A powerful PC", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 1000.0, mainController.getClock());
            items.add(item1);
            items.add(item2);
            
            //Make sure both items are active and that there are no concluded auctions
            assertEquals(items.size(), 2);
            assertTrue(item1.isActive() && item2.isActive());
            mainController.updateConcludedAuctionsDisplay();
            assertEquals(mainView.getConcludedAuctionsBox().getChildren().size(), 0);
            
            //Skip time until after end-date
            mainController.setTime(LocalDateTime.now().plusDays(2));
            mainController.scheduleNextUpdate();
            
            //Make sure both items are inactive
            assertEquals(items.size(), 2);
            assertFalse(item1.isActive() && item2.isActive());
            
            //Verify that both of the items are displayed under "Concluded Auctions"
            mainController.updateConcludedAuctionsDisplay();
            assertEquals(mainView.getConcludedAuctionsBox().getChildren().size(), 2);
            
            //Switch category under concluded auctions
            //NOT IMPLEMENTED
            //Then, Update concluded auctions display
            mainController.updateConcludedAuctionsDisplay();
            //Finally, ensure only one item Active
            //NOT IMPLEMENTED
            
            
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}
