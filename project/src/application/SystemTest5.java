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
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class SystemTest5 {

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
    @DisplayName("User Story 5: Verify My Auctions Display Correctly with All Details")
    void testShowMyAuctions() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                // Arrange
                Item item1 = new Item("Laptop", "1.5", "Used laptop in good condition", categories.get(0), "Used", "Tech", "Portable", "Computer",
                        LocalDate.now().minusDays(1).atStartOfDay(), LocalDate.now().plusDays(1).atTime(12, 0), 500.0, 100.0);
                Item item2 = new Item("Phone", "0.3", "Latest model smartphone", categories.get(0), "New", "Tech", "Gadget", "Mobile",
                        LocalDate.now().minusDays(3).atStartOfDay(), LocalDate.now().plusDays(2).atTime(15, 0), 700.0, 150.0);

                // Use mainController's addItem to ensure items are added to the correct list
                mainController.addItem(item1);
                mainController.addItem(item2);

                // Debugging: Check the size of the items list after addition
                System.out.println("Items added to the list: " + mainController.getItems().size());

                // Act
                mainController.updateProfileItemsDisplay();

                // Wait to ensure JavaFX updates have taken place
                latch.countDown();

            } finally {
                latch.countDown();
            }
        });
        latch.await();

        // Get the items after updating the profile display
        ObservableList<Item> profileItems = mainController.getItems().filtered(item -> item.getCategory().getName().equals("Electronics"));

        // Debugging: Check the size of the profile items list
        System.out.println("Profile items list after update: " + profileItems.size());

        // Assert: Verify the number of items in the profile list
        assertEquals(2, profileItems.size(), "Expected the profile item list to contain 2 items.");

        // Assert: Verify the sorting order by end date
        assertTrue(profileItems.get(0).getEndDate().isBefore(profileItems.get(1).getEndDate()), "Profile items should be sorted based on end date.");

        // Assert: Verify the item details
        assertEquals("Laptop", profileItems.get(0).getTitle(), "First item in profile should be 'Laptop'.");
        assertEquals(100.0, profileItems.get(0).getCurrentBid(), "First item in profile should have a current bid of 100.0.");
        assertEquals(500.0, profileItems.get(0).getBuyItNowPrice(), "First item in profile should have a buy-it-now price of 500.0.");
        assertTrue(profileItems.get(0).isActive(), "First item should be active.");

        assertEquals("Phone", profileItems.get(1).getTitle(), "Second item in profile should be 'Phone'.");
        assertEquals(150.0, profileItems.get(1).getCurrentBid(), "Second item in profile should have a current bid of 150.0.");
        assertEquals(700.0, profileItems.get(1).getBuyItNowPrice(), "Second item in profile should have a buy-it-now price of 700.0.");
        assertTrue(profileItems.get(1).isActive(), "Second item should be active.");
    }


    @Test
    @DisplayName("User Story 5: Verify Adding Item")
    void testAddItem() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                // Arrange
                Item newItem = new Item("Tablet", "0.8kg", "A brand new tablet", categories.get(0), "New", "Tech", "Portable", "Tablet",
                        LocalDate.now().minusDays(1).atStartOfDay(), LocalDate.now().plusDays(1).atTime(12, 0), 300.0, 50.0);

                // Act
                mainController.addItem(newItem);

                // Assert
                assertTrue(mainController.getItems().contains(newItem), "Expected the item list to contain the newly added 'Tablet'.");
            } finally {
                latch.countDown();
            }
        });
        latch.await();
    }
}
