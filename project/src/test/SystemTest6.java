package test;
import application.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.Comparator;

public class SystemTest6 extends Application {
    private static ObservableList<Item> items;
    private static MainController mainController;
    private static MainView mainView;
    private static final CountDownLatch latch = new CountDownLatch(1);

    @BeforeAll
    public static void initToolkit() throws Exception {
        new Thread(() -> Application.launch(SystemTest6.class)).start();
        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new Exception("JavaFX initialization took too long");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        latch.countDown();
    }

    @BeforeEach
    public void setUp() throws Exception {
        CountDownLatch setupLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                mainView = new MainView(FXCollections.observableArrayList());
                mainController = new MainController(mainView, new SystemClock());
                items = mainController.getItems();
            } finally {
                setupLatch.countDown();
            }
        });
        if (!setupLatch.await(5, TimeUnit.SECONDS)) {
            throw new Exception("JavaFX setup took too long");
        }

        // Add mock items
        items.add(new Item("Item 1", "1 kg", "Description 1", new Category("Category 1"), "New", "Tag1", "Tag2", "Tag3", LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), 100.0, 0.00, mainController.getClock()));
        items.add(new Item("Item 2", "2 kg", "Description 2", new Category("Category 2"), "Used", "Tag1", "Tag2", "Tag3", LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), 200.0, 0.00, mainController.getClock()));
        items.add(new Item("Item 3", "3 kg", "Description 3", new Category("Category 3"), "New", "Tag1", "Tag2", "Tag3", LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(3), 300.0, 0.00, mainController.getClock()));
        items.add(new Item("Expired Item", "1 kg", "Expired Description", new Category("Category 4"), "Used", "Tag1", "Tag2", "Tag3", LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(1), 50.0, 0.00, mainController.getClock()));
    }

    @Test
    public void testShowActiveAuctions() throws Exception {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                ObservableList<Item> activeAuctions = items.filtered(Item::isActive).sorted(Comparator.comparing(Item::getEndDate));
                assertEquals(3, activeAuctions.size());
                assertEquals("Item 1", activeAuctions.get(0).getTitle());
                assertEquals("Item 2", activeAuctions.get(1).getTitle());
                assertEquals("Item 3", activeAuctions.get(2).getTitle());
            } finally {
                testLatch.countDown();
            }
        });
        if (!testLatch.await(5, TimeUnit.SECONDS)) {
            throw new Exception("Test execution took too long");
        }
    }

    @Test
    public void testActiveAuctionDetails() throws Exception {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                ObservableList<Item> activeAuctions = items.filtered(Item::isActive).sorted(Comparator.comparing(Item::getEndDate));
                assertEquals(3, activeAuctions.size());

                Item item1 = activeAuctions.get(0);
                assertEquals("Item 1", item1.getTitle());
                assertEquals("1 kg", item1.getWeight());
                assertEquals("Description 1", item1.getDescription());
                assertEquals("Category 1", item1.getCategory().getName());
                assertEquals("New", item1.getCondition());
                assertEquals(100.0, item1.getBuyItNowPrice());
                assertTrue(item1.isActive());

                Item item2 = activeAuctions.get(1);
                assertEquals("Item 2", item2.getTitle());
                assertEquals("2 kg", item2.getWeight());
                assertEquals("Description 2", item2.getDescription());
                assertEquals("Category 2", item2.getCategory().getName());
                assertEquals("Used", item2.getCondition());
                assertEquals(200.0, item2.getBuyItNowPrice());
                assertTrue(item2.isActive());

                Item item3 = activeAuctions.get(2);
                assertEquals("Item 3", item3.getTitle());
                assertEquals("3 kg", item3.getWeight());
                assertEquals("Description 3", item3.getDescription());
                assertEquals("Category 3", item3.getCategory().getName());
                assertEquals("New", item3.getCondition());
                assertEquals(300.0, item3.getBuyItNowPrice());
                assertTrue(item3.isActive());
            } finally {
                testLatch.countDown();
            }
        });
        if (!testLatch.await(5, TimeUnit.SECONDS)) {
            throw new Exception("Test execution took too long");
        }
    }

    @Test
    public void testActiveAuctionsAvailability() throws Exception {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                ObservableList<Item> activeAuctions = items.filtered(Item::isActive).sorted(Comparator.comparing(Item::getEndDate));
                assertEquals(3, activeAuctions.size());

                // Check that the user can see what is available to bid on
                for (Item item : activeAuctions) {
                    assertTrue(item.isActive());
                    assertNotNull(item.getTitle());
                    assertNotNull(item.getEndDate());
                }
            } finally {
                testLatch.countDown();
            }
        });
        if (!testLatch.await(5, TimeUnit.SECONDS)) {
            throw new Exception("Test execution took too long");
        }
    }
}