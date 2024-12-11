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
    private static ObservableList<Auction> auctions;
    private static AuctionSystemView view;
    private static AuctionSystemController controller;
    private static final CountDownLatch latch = new CountDownLatch(1);

    @BeforeAll
    public static void initToolkit() throws Exception {
    	AuctionStatePersistence.canWrite = false;
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
                view= new AuctionSystemView();
                controller = view.getController();
                auctions = controller.getAuctions();
            } finally {
                setupLatch.countDown();
            }
        });
        if (!setupLatch.await(5, TimeUnit.SECONDS)) {
            throw new Exception("JavaFX setup took too long");
        }

        // Add mock items
        auctions.add(new Auction(new Item("Item 1", "1 kg", "Description 1", "New"), "Category 1" , "Tag1", "Tag2", "Tag3", LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), 100.0, 0.00, controller.getClock()));
        auctions.add(new Auction(new Item("Item 2", "2 kg", "Description 2", "Used"), "Category 2", "Tag1", "Tag2", "Tag3", LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), 200.0, 0.00, controller.getClock()));
        auctions.add(new Auction(new Item("Item 3", "3 kg", "Description 3", "New"), "Category 3", "Tag1", "Tag2", "Tag3", LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(3), 300.0, 0.00, controller.getClock()));
        auctions.add(new Auction(new Item("Expired Item", "1 kg", "Expired Description", "Used"), "Category 4", "Tag1", "Tag2", "Tag3", LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(1), 50.0, 0.00, controller.getClock()));
    }

    @Test
    public void testShowActiveAuctions() throws Exception {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                ObservableList<Auction> activeAuctions = auctions.filtered(Auction::isActive).sorted(Comparator.comparing(Auction::getEndDate));
                assertEquals(3, activeAuctions.size());
                assertEquals("Item 1", activeAuctions.get(0).getItem().getTitle());
                assertEquals("Item 2", activeAuctions.get(1).getItem().getTitle());
                assertEquals("Item 3", activeAuctions.get(2).getItem().getTitle());
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
                ObservableList<Auction> activeAuctions = auctions.filtered(Auction::isActive).sorted(Comparator.comparing(Auction::getEndDate));
                assertEquals(3, activeAuctions.size());

                Auction auction1 = activeAuctions.get(0);
                assertEquals("Item 1", auction1.getItem().getTitle());
                assertEquals("1 kg", auction1.getItem().getWeight());
                assertEquals("Description 1", auction1.getItem().getDescription());
                assertEquals("Category 1", auction1.getCategory());
                assertEquals("New", auction1.getItem().getCondition());
                assertEquals(100.0, auction1.getBuyItNowPrice());
                assertTrue(auction1.isActive());

                Auction auction2 = activeAuctions.get(1);
                assertEquals("Item 2", auction2.getItem().getTitle());
                assertEquals("2 kg", auction2.getItem().getWeight());
                assertEquals("Description 2", auction2.getItem().getDescription());
                assertEquals("Category 2", auction2.getCategory());
                assertEquals("Used", auction2.getItem().getCondition());
                assertEquals(200.0, auction2.getBuyItNowPrice());
                assertTrue(auction2.isActive());

                Auction auction3 = activeAuctions.get(2);
                assertEquals("Item 3", auction3.getItem().getTitle());
                assertEquals("3 kg", auction3.getItem().getWeight());
                assertEquals("Description 3", auction3.getItem().getDescription());
                assertEquals("Category 3", auction3.getCategory());
                assertEquals("New", auction3.getItem().getCondition());
                assertEquals(300.0, auction3.getBuyItNowPrice());
                assertTrue(auction3.isActive());
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
                ObservableList<Auction> activeAuctions = auctions.filtered(Auction::isActive).sorted(Comparator.comparing(Auction::getEndDate));
                assertEquals(3, activeAuctions.size());

                // Check that the user can see what is available to bid on
                for (Auction auction : activeAuctions) {
                    assertTrue(auction.isActive());
                    assertNotNull(auction.getItem().getTitle());
                    assertNotNull(auction.getEndDate());
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