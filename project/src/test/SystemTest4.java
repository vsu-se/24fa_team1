package test;

import application.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class SystemTest4 {
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
        mainController = new MainController(new MainView(categories),  new SystemClock());

        controller = new ItemController(view, categories, tabPane, createItemTab, items, mainController,  new SystemClock());
    }
    
    @Test
    void testCreateItemWithInvalidData() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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
            }
        });
        latch.await();
    }
    
    @Test
    void testCreateMultipleItems() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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
                    Item createdItem = items.get(i - 1);
                    assertEquals("Item " + i, createdItem.getTitle());
                    assertEquals("2.5 kg", createdItem.getWeight());
                    assertEquals("Description for item " + i, createdItem.getDescription());
                    assertEquals(categories.get(0), createdItem.getCategory());
                    assertEquals("New", createdItem.getCondition());
                    assertEquals("Tag1", createdItem.getTag1());
                    assertEquals("Tag2", createdItem.getTag2());
                    assertEquals("Tag3", createdItem.getTag3());
                    assertEquals(LocalDate.now().plusDays(1).atTime(12, 0), createdItem.getEndDate());
                    assertEquals(1500.0, createdItem.getBuyItNowPrice());
                }
                latch.countDown();
            }
        });
        latch.await();
    }
    @Test
    void testCreateItem() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TextField titleInput = view.getTitleInput();
                titleInput.setText("Laptop");

                TextField weightInput = view.getWeightInput();
                weightInput.setText("2.5");

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

                // Verify that the tab is closed after item creation
                assertFalse(tabPane.getTabs().contains(createItemTab));

                // Verify that the item is added to the items list
                assertEquals(1, items.size());
                Item createdItem = items.get(0);
                assertEquals("Laptop", createdItem.getTitle());
                assertEquals("2.5 kg", createdItem.getWeight());
                assertEquals("A high-end gaming laptop.", createdItem.getDescription());
                assertEquals(categories.get(0), createdItem.getCategory());
                assertEquals("New", createdItem.getCondition());
                assertEquals("Gaming", createdItem.getTag1());
                assertEquals("Laptop", createdItem.getTag2());
                assertEquals("Electronics", createdItem.getTag3());
                assertEquals(LocalDate.now().plusDays(1).atTime(12, 0), createdItem.getEndDate());
                assertEquals(1500.0, createdItem.getBuyItNowPrice());
                latch.countDown();
            }
        });
        latch.await();
    }




}
