package application;

import application.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SystemTest6 {

    private ItemView view;
    private ItemController controller;
    private ObservableList<Category> categories;
    private ObservableList<Item> items;
    private TabPane tabPane;
    private Tab createItemTab;
    private MainController mainController;

    @BeforeAll
    static void initToolkit() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            latch.countDown();
        });
        latch.await();
    }

    @BeforeEach
    void setUp() throws Exception {
        categories = FXCollections.observableArrayList(new Category("Electronics"));
        items = FXCollections.observableArrayList();
        view = new ItemView(categories);
        tabPane = new TabPane();
        createItemTab = new Tab();
        mainController = new MainController(new MainView(categories));

        controller = new ItemController(view, categories, tabPane, createItemTab, items, mainController);
    }

    @Test
    @DisplayName("User Story 6: Verify Active Auctions Display Correctly with All Details")
    void testShowActiveAuctions() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Use AtomicReference to allow mutable access to variables inside lambda
        AtomicReference<Item> activeItem1 = new AtomicReference<>();
        AtomicReference<Item> activeItem2 = new AtomicReference<>();
        AtomicReference<Item> expiredItem = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                // Arrange
                activeItem1.set(new Item("Laptop", "1.5", "High-end gaming laptop", categories.get(0), "New", "Tech", "Portable", "Computer",
                        LocalDate.now().minusDays(1).atStartOfDay(), LocalDate.now().plusDays(2).atTime(12, 0), 1200.0, 100.0));
                activeItem2.set(new Item("Phone", "0.3", "Smartphone", categories.get(0), "New", "Tech", "Mobile", "Gadget",
                        LocalDate.now().minusDays(3).atStartOfDay(), LocalDate.now().plusDays(1).atTime(15, 0), 700.0, 150.0));
                expiredItem.set(new Item("Tablet", "0.8kg", "Old model tablet", categories.get(0), "Used", "Tech", "Portable", "Tablet",
                        LocalDate.now().minusDays(5).atStartOfDay(), LocalDate.now().minusDays(1).atTime(12, 0), 300.0, 50.0));

                // Use mainController's addItem to ensure items are added to the correct list
                mainController.addItem(activeItem1.get());
                mainController.addItem(activeItem2.get());
                mainController.addItem(expiredItem.get());

                // Act
                mainController.updateItemsDisplay();

                // Wait to ensure JavaFX updates have taken place
                latch.countDown();

            } finally {
                latch.countDown();
            }
        });
        latch.await();

        CountDownLatch assertionLatch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Get the active items after updating the display
                List<Item> activeItems = new ArrayList<>(mainController.getItems().filtered(Item::isActive));
                activeItems.sort((i1, i2) -> i1.getEndDate().compareTo(i2.getEndDate()));

                // Debugging: Print out the active items
                System.out.println("Active items after update: " + activeItems.size());
                for (Item item : activeItems) {
                    System.out.println("Active Item: " + item.getTitle() + ", isActive: " + item.isActive() + ", End Date: " + item.getEndDate());
                }

                // Assert: Verify the number of active items in the list
                assertEquals(2, activeItems.size(), "Expected the active item list to contain 2 items.");

                // Assert: Verify the sorting order by end date
                assertTrue(activeItems.get(0).getEndDate().isBefore(activeItems.get(1).getEndDate()), "Active items should be sorted based on end date.");

                // Assert: Verify that all items are active
                for (Item item : activeItems) {
                    assertTrue(item.isActive(), "Each item in the active items list should be active.");
                }

                // Assert: Verify that the expired item is not present in the active items list
                assertFalse(activeItems.contains(expiredItem.get()), "Expired item should not be present in the active items list.");

                // Assert: Verify the item details for active items
                assertEquals("Phone", activeItems.get(0).getTitle(), "First active item in list should be 'Phone'.");
                assertEquals(150.0, activeItems.get(0).getCurrentBid(), "First active item should have a current bid of 150.0.");
                assertEquals(700.0, activeItems.get(0).getBuyItNowPrice(), "First active item should have a buy-it-now price of 700.0.");

                assertEquals("Laptop", activeItems.get(1).getTitle(), "Second active item in list should be 'Laptop'.");
                assertEquals(100.0, activeItems.get(1).getCurrentBid(), "Second active item should have a current bid of 100.0.");
                assertEquals(1200.0, activeItems.get(1).getBuyItNowPrice(), "Second active item should have a buy-it-now price of 1200.0.");

            } finally {
                assertionLatch.countDown();
            }
        });

        assertionLatch.await();
    }
}
