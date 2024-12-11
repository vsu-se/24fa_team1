package test;

import application.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

public class SystemTest1 {
    private static boolean isJavaFXInitialized = false;
    private AuctionSystemView mockView;
    private AuctionSystemController controller;
    private AuctionSystem mockSystem;

    private ObservableList<String> categories;

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

            // Initialize the controller with the mockView
            controller = mockView.getController();

            // Normally you would need to start the JavaFX Application thread for this, but for now we'll assume this runs in a test environment that supports JavaFX
            latch.countDown();
        });
        latch.await(1, TimeUnit.SECONDS);
    }


    @Test
    public void testAddCategory() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            String categoryName = "Electronics";
            mockView.getCategoryInput().setText(categoryName);
            mockView.getAddButton().fire();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS); // Wait for JavaFX thread to complete

        assertNotNull(controller); // Ensure controller is not null
        assertEquals(1, controller.getCategories().size());
        assertEquals("Electronics", controller.getCategories().get(0));
    }

    @Test
    public void testAddCategoryDuplicateName() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            String categoryName = "Electronics";
            controller.getCategories().add(categoryName);
            mockView.getCategoryInput().setText(categoryName);
            mockView.getAddButton().fire();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS); // Wait for JavaFX thread to complete

        assertNotNull(controller); // Ensure controller is not null
        assertEquals(1, controller.getCategories().size());
    }

    @Test
    public void testAddCategoryEmptyName() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            mockView.getCategoryInput().setText("");
            mockView.getAddButton().fire();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS); // Wait for JavaFX thread to complete

        assertNotNull(controller); // Ensure controller is not null
        assertEquals(0, controller.getCategories().size());
    }
}