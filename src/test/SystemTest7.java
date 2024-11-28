package test;



//FIX? These tests don't actually follow the process of typing in a bid amount and pressing the button,
// they really just test the Item.placeBid() method.
// I would like if they could actually access the text fields and press the buttons for the bids, but I can't seem to find a way to access them.
// Also, they are in the mainController? The GUI elemetns should definitely be in the mainView.
// Don't have time rn, but plan to fix once everything is revamped/remodeled with Roosevelts proposed format okok ? ty





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

public class SystemTest7 {
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
            items = FXCollections.observableArrayList();
            mainView = new MainView(categories);
            mainController = new MainController(mainView);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testPlaceValidBid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Category category = new Category("Electronics");
            categories.add(category);
            //add mock item
            Item item = new Item("Laptop", "2 kg", "A powerful laptop", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 100.0);
            items.add(item);

            boolean bidPlaced = item.placeBid(150.0);

            assertTrue(bidPlaced);
            assertEquals(150.0, item.getCurrentBid());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testPlaceBidLowerThanCurrentBid() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Category category = new Category("Electronics");
            categories.add(category);
            //add mock item
            Item item = new Item("Laptop", "2 kg", "A powerful laptop", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 100.0);
            items.add(item);

            boolean bidPlaced = item.placeBid(50.0);

            assertFalse(bidPlaced);
            assertEquals(100.0, item.getCurrentBid());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testPlaceBidWithInvalidAmount() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Category category = new Category("Electronics");
            categories.add(category);
            Item item = new Item("Laptop", "2 kg", "A powerful laptop", category, "New", "tag1", "tag2", "tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 100.0);
            items.add(item);
            
            //assertEquals(mainView.getListItemErrorLabel().getText(), "Please enter a valid bid amount.");
            assertEquals(100.0, item.getCurrentBid());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}