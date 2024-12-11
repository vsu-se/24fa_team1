import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
public class SystemTest4 {
    private AddItemView view;
    private ObservableList<String> categories;
    private ObservableList<Auction> items;
    private Tab createItemTab;
    private AuctionSystemController mainController;

    @BeforeAll
    static void initToolkit() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> latch.countDown());
        latch.await();
    }

    @BeforeEach
    void setUp() throws Exception {
        categories = FXCollections.observableArrayList("Electronics");
        items = FXCollections.observableArrayList();
        AuctionSystemView auctionSystemView = new AuctionSystemView();
        view = new AddItemView(auctionSystemView);
        VBox layout = new VBox();
        createItemTab = new Tab();
        mainController = new AuctionSystemController(auctionSystemView);
    }

    @Test
    void testCreateItemWithInvalidData() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            TextField titleInput = view.getTitleInput();
            titleInput.setText("Laptop");

            TextField weightInput = view.getWeightInput();
            weightInput.setText("invalid_weight"); // Invalid weight

            ComboBox<String> weightUnitComboBox = view.getWeightUnitComboBox();
            weightUnitComboBox.getItems().addAll("kg");
            weightUnitComboBox.setValue("kg");

            view.getDescriptionInput().setText("A high-end gaming laptop.");
            view.getCategoryComboBox().setValue(categories.get(0));
            view.getConditionComboBox().getItems().addAll("New", "Used");
            view.getConditionComboBox().setValue("New");

            view.getTag1Input().setText("Gaming");
            view.getTag2Input().setText("Laptop");
            view.getTag3Input().setText("Electronics");

            view.getEndDatePicker().setValue(LocalDate.now().plusDays(1));
            view.getEndTimeInput().setText("12:00");
            view.getBuyItNowPriceInput().setText("1500");
            view.getBidAmountInput().setText("0.00");

            Button createItemButton = view.getCreateItemButton();
            createItemButton.fire();

            // Verify that the item is not added to the items list
            assertEquals(0, items.size());
            latch.countDown();
        });
        latch.await();
    }

    @Test
    void testCreateMultipleItems() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            for (int i = 1; i <= 3; i++) {
                TextField titleInput = view.getTitleInput();
                titleInput.setText("Item " + i);

                TextField weightInput = view.getWeightInput();
                weightInput.setText("2.5");

                ComboBox<String> weightUnitComboBox = view.getWeightUnitComboBox();
                weightUnitComboBox.getItems().addAll("kg");
                weightUnitComboBox.setValue("kg");

                view.getDescriptionInput().setText("Description for item " + i);
                view.getCategoryComboBox().setValue(categories.get(0));
                view.getConditionComboBox().getItems().addAll("New", "Used");
                view.getConditionComboBox().setValue("New");

                view.getTag1Input().setText("Tag1");
                view.getTag2Input().setText("Tag2");
                view.getTag3Input().setText("Tag3");

                view.getEndDatePicker().setValue(LocalDate.now().plusDays(1));
                view.getEndTimeInput().setText("12:00");
                view.getBuyItNowPriceInput().setText("1500");
                view.getBidAmountInput().setText("0.00");

                Button createItemButton = view.getCreateItemButton();
                createItemButton.fire();
            }

            // Verify that all items are added to the items list
            assertEquals(3, items.size());
            for (int i = 1; i <= 3; i++) {
                Auction createdItem = items.get(i - 1);
                assertEquals("Item " + i, createdItem.getItem().getTitle());
                assertEquals("2.5", createdItem.getItem().getWeight());
                assertEquals("Description for item " + i, createdItem.getItem().getDescription());
                assertEquals("Electronics", createdItem.getCategory());
                assertEquals("New", createdItem.getItem().getCondition());
                assertEquals("Tag1", createdItem.getTag1());
                assertEquals("Tag2", createdItem.getTag2());
                assertEquals("Tag3", createdItem.getTag3());
                assertEquals(LocalDate.now().plusDays(1).atTime(12, 0), createdItem.getEndDate());
                assertEquals(1500.0, createdItem.getBuyItNowPrice());
            }
            latch.countDown();
        });
        latch.await();
    }


    @Test
    void testCreateItem() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            TextField titleInput = view.getTitleInput();
            titleInput.setText("Laptop");

            TextField weightInput = view.getWeightInput();
            weightInput.setText("2.5");

            ComboBox<String> weightUnitComboBox = view.getWeightUnitComboBox();
            weightUnitComboBox.setValue("kg");

            view.getDescriptionInput().setText("A high-end gaming laptop.");
            view.getCategoryComboBox().setValue(categories.get(0));
            view.getConditionComboBox().setValue("New");

            view.getTag1Input().setText("Gaming");
            view.getTag2Input().setText("Laptop");
            view.getTag3Input().setText("Electronics");

            view.getEndDatePicker().setValue(LocalDate.now().plusDays(1));
            view.getEndTimeInput().setText("12:00");
            view.getBuyItNowPriceInput().setText("1500");
            view.getBidAmountInput().setText("0.00");

            Button createItemButton = view.getCreateItemButton();
            createItemButton.fire();

            // Verify that the tab is closed after item creation
            assertFalse(createItemTab.getTabPane().getTabs().contains(createItemTab));

            // Verify that the item is added to the items list
            assertEquals(1, items.size());
            Auction createdItem = items.get(0);
            assertEquals("Laptop", createdItem.getItem().getTitle());
            assertEquals("2.5", createdItem.getItem().getWeight());
            assertEquals("A high-end gaming laptop.", createdItem.getItem().getDescription());
            assertEquals(categories.get(0), createdItem.getCategory());
            assertEquals("New", createdItem.getItem().getCondition());
            assertEquals("Gaming", createdItem.getTag1());
            assertEquals("Laptop", createdItem.getTag2());
            assertEquals("Electronics", createdItem.getTag3());
            assertEquals(LocalDate.now().plusDays(1).atTime(12, 0), createdItem.getEndDate());
            assertEquals(1500.0, createdItem.getBuyItNowPrice());
            latch.countDown();
        });
        latch.await();
    }





}
