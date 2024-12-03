// ItemController.java
package application;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class ItemController {
    private ItemView view;
    private ObservableList<Category> categories;
    private TabPane tabPane;
    private Tab createItemTab;
    private ObservableList<Item> items;
    private MainController mainController;

    public ItemController(ItemView view, ObservableList<Category> categories, TabPane tabPane, Tab createItemTab, ObservableList<Item> items, MainController mainController, SystemClock clock) {
        this.view = view;
        this.categories = categories;
        this.tabPane = tabPane;
        this.createItemTab = createItemTab;
        this.items = items;
        this.mainController = mainController;

        view.getCreateItemButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String title = view.getTitleInput().getText();
                String weight = view.getWeightInput().getText();
                String weightUnit = view.getWeightUnitComboBox().getValue();
                String description = view.getDescriptionInput().getText();
                Category category = view.getCategoryComboBox().getValue();
                String condition = view.getConditionComboBox().getValue();
                String tag1 = view.getTag1Input().getText();
                String tag2 = view.getTag2Input().getText();
                String tag3 = view.getTag3Input().getText();
                LocalDate endDate = view.getEndDatePicker().getValue();
                String endTime = view.getEndTimeInput().getText();
                String buyItNowPriceText = view.getBuyItNowPriceInput().getText();
                String bidAmountText = view.getBidAmountInput().getText();

                if (title.isEmpty() || weight.isEmpty() || weightUnit == null || description.isEmpty() || category == null || condition == null || endDate == null || endTime.isEmpty() || bidAmountText.isEmpty()) {
                    view.getCreateItemErrorLabel().setText("Please fill in all required fields.");
                    return;
                }

                try {
                    Double.parseDouble(weight);
                } catch (NumberFormatException e) {
                    view.getCreateItemErrorLabel().setText("Please enter a valid number for the weight.");
                    return;
                }

                LocalDateTime startDate = LocalDateTime.now();
                LocalDateTime endDateTime;
                try {
                    endDateTime = LocalDateTime.of(endDate, LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm")));
                } catch (Exception e) {
                    view.getCreateItemErrorLabel().setText("Please enter a valid end time in the format HH:mm.");
                    return;
                }

                if (endDateTime.isBefore(startDate)) {
                    view.getCreateItemErrorLabel().setText("The end date and time cannot be before the current date and time.");
                    return;
                }

                Double buyItNowPrice = null;
                if (!buyItNowPriceText.isEmpty()) {
                    try {
                        buyItNowPrice = Double.parseDouble(buyItNowPriceText);
                    } catch (NumberFormatException e) {
                        view.getCreateItemErrorLabel().setText("Please enter a valid number for the Buy it now price.");
                        return;
                    }
                }

                double bidAmount;
                try {
                    bidAmount = Double.parseDouble(bidAmountText);
                    if (bidAmount < 0) {
                        view.getCreateItemErrorLabel().setText("Initial bid amount cannot be negative.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    view.getCreateItemErrorLabel().setText("Please enter a valid bid amount.");
                    return;
                }

                Item newItem = new Item(title, weight, description, category, condition, tag1, tag2, tag3, startDate, endDateTime, buyItNowPrice, bidAmount, mainController.getClock());
                items.add(newItem);

                // Clear error message
                view.getCreateItemErrorLabel().setText("");

                // Close the "Create Item" tab
                tabPane.getTabs().remove(createItemTab);

                // Update the items display in the MainController
                mainController.updateItemsDisplay();
                mainController.scheduleNextUpdate();
            }
        });
    }
}