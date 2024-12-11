
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.*;

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

public class SystemTest5 {
    private AuctionSystemController AuctionSystemController;
    private ObservableList<Auction> items;
    private AuctionSystem auctionSystem;

    @BeforeEach
    public void setup() {
        auctionSystem = new AuctionSystem();
        items = FXCollections.observableArrayList();
        AuctionSystemController = new AuctionSystemController(new AuctionSystemView());

        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        try {
            latch.await();
        } catch (InterruptedException e) {
            fail("Failed to initialize JavaFX Platform");
        }
    }
    @BeforeAll
    static void initToolkit() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> latch.countDown());
        latch.await();
    }

        @Test
        public void testItemsSortedByEndDate() throws Exception {
            CountDownLatch testLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    AuctionSystemController.updateItemsDisplay();
                    assertEquals("Item 1", items.get(0).getEndDate());
                    assertEquals("Item 2", items.get(1).getEndDate());
                    assertEquals("Item 3", items.get(2).getEndDate());
                } finally {
                    testLatch.countDown();
                }
            });
            if (!testLatch.await(5, TimeUnit.SECONDS)) {
                throw new Exception("Test execution took too long");
            }
        }

        @Test
        public void testItemDetails() throws Exception {
            CountDownLatch testLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    Auction item = items.getItem();
                    assertEquals("Item 1", item.getTitle());
                    assertEquals("1 kg", item.getWeight());
                    assertEquals("Description 1", item.getDescription());
                    assertEquals("Category 1", item.getAuction().getName());
                    assertEquals("New", item.getCondition());
                    assertEquals(100.0, item.getBuyItNowPrice());
                    assertTrue(item.isActive());
                } finally {
                    testLatch.countDown();
                }
            });
            if (!testLatch.await(5, TimeUnit.SECONDS)) {
                throw new Exception("Test execution took too long");
            }
        }

        @Test
        public void testCurrentBid() throws Exception {
            CountDownLatch testLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    Auction item = items.get(0);
                    item.setCurrentBid(150.0);
                    assertEquals(150.0, item.getCurrentBid());
                } finally {
                    testLatch.countDown();
                }
            });
            if (!testLatch.await(5, TimeUnit.SECONDS)) {
                throw new Exception("Test execution took too long");
            }
        }

        @Test
        public void testEmptyItemList() throws Exception {
            CountDownLatch testLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    items.clear();
                    AuctionSystemController.updateItemsDisplay();
                    assertTrue(items.isEmpty());
                } finally {
                    testLatch.countDown();
                }
            });
            if (!testLatch.await(5, TimeUnit.SECONDS)) {
                throw new Exception("Test execution took too long");
            }
        }

        @Test
        public void testSingleItem() throws Exception {
            CountDownLatch testLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    items.clear();
                    items.add(new Item("Single Item", "1 kg", "Description", new String("Category"), "New", "Tag1", "Tag2", "Tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 100.0, 0.00, AuctionSystemController.getClock()));
                    AuctionSystemController.updateItemsDisplay();
                    assertEquals(1, items.size());
                    assertEquals("Single Item", items.get(0).getTitle());
                } finally {
                    testLatch.countDown();
                }
            });
            if (!testLatch.await(5, TimeUnit.SECONDS)) {
                throw new Exception("Test execution took too long");
            }
        }

        @Test
        public void testMultipleItemsSameEndDate() throws Exception {
            CountDownLatch testLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    items.clear();
                    LocalDateTime endDate = LocalDateTime.now().plusDays(1);
                    items.add(new Item("Item 1", "1 kg", "Description 1", new Category("Category 1"), "New", "Tag1", "Tag2", "Tag3", LocalDateTime.now(), endDate, 100.0, 0.00, AuctionSystemController.getClock()));
                    items.add(new Item("Item 2", "2 kg", "Description 2", new Category("Category 2"), "Used", "Tag1", "Tag2", "Tag3", LocalDateTime.now(), endDate, 200.0, 0.00, AuctionSystemController.getClock()));
                    AuctionSystemController.updateItemsDisplay();
                    assertEquals(2, items.size());
                    assertEquals("Item 1", items.get(0).getTitle());
                    assertEquals("Item 2", items.get(1).getTitle());
                } finally {
                    testLatch.countDown();
                }
            });
            if (!testLatch.await(5, TimeUnit.SECONDS)) {
                throw new Exception("Test execution took too long");
            }
        }

        @Test
        public void testPastEndDate() throws Exception {
            CountDownLatch testLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    items.clear();
                    items.add(new Item("Past Item", "1 kg", "Description", new Category("Category"), "New", "Tag1", "Tag2", "Tag3", LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), 100.0, 0.00, AuctionSystemController.getClock()));
                    AuctionSystemController.updateItemsDisplay();
                    assertFalse(items.get(0).isActive());
                } finally {
                    testLatch.countDown();
                }
            });
            if (!testLatch.await(5, TimeUnit.SECONDS)) {
                throw new Exception("Test execution took too long");
            }
        }

        @Test
        public void testFutureEndDate() throws Exception {
            CountDownLatch testLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    items.clear();
                    items.add(new Item("Future Item", "1 kg", "Description", new String("Category"), "New", "Tag1", "Tag2", "Tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 100.0, 0.00, AuctionSystemController.getClock()));
                    AuctionSystemController.updateItemsDisplay();
                    assertTrue(items.get(0).isActive());
                } finally {
                    testLatch.countDown();
                }
            });
            if (!testLatch.await(5, TimeUnit.SECONDS)) {
                throw new Exception("Test execution took too long");
            }
        }

        @Test
        public void testInvalidWeight() throws Exception {
            CountDownLatch testLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    Item invalidItem = new Item("Invalid Weight Item", "invalid weight", "Description", new Category("Category"), "New", "Tag1", "Tag2", "Tag3", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 100.0, 0.00, AuctionSystemController.getClock());
                    assertThrows(NumberFormatException.class, () -> {
                        Double.parseDouble(invalidItem.getWeight());
                    });
                } finally {
                    testLatch.countDown();
                }
            });
            if (!testLatch.await(5, TimeUnit.SECONDS)) {
                throw new Exception("Test execution took too long");
            }
        }
    }