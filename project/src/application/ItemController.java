package application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ItemController {
    private ItemView view;
    private ObservableList<Category> categories;
    private TabPane tabPane;
    private Tab createItemTab;
    public ItemController(ItemView view, ObservableList<Category> categories, TabPane tabPane, Tab createItemTab) {
        this.view = view;
        this.categories = categories;
        this.tabPane = tabPane;
        this.createItemTab = createItemTab;
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


                    if (title.isEmpty() || weight.isEmpty() || weightUnit == null || description.isEmpty() || category == null || condition == null || endDate == null || endTime.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill in all required fields.");
                        alert.showAndWait();
                        return;
                    }


                    try {
                        Double.parseDouble(weight);
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter a valid number for the weight.");
                        alert.showAndWait();
                        return;
                    }

                    LocalDateTime startDate = LocalDateTime.now();
                    LocalDateTime endDateTime;
                    try {
                        endDateTime = LocalDateTime.of(endDate, LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm")));
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter a valid end time in the format HH:mm.");
                        alert.showAndWait();
                        return;
                    }

                    if (endDateTime.isBefore(startDate)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("The end date and time cannot be before the current date and time.");
                        alert.showAndWait();
                        return;
                    }

                    Double buyItNowPrice = null;
                    if (!buyItNowPriceText.isEmpty()) {
                        try {
                            buyItNowPrice = Double.parseDouble(buyItNowPriceText);
                        } catch (NumberFormatException e) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("Please enter a valid number for the Buy it now price.");
                            alert.showAndWait();
                            return;
                        }
                    }

                    Item newItem = new Item(title, weight + " " + weightUnit, description, category, condition, tag1, tag2, tag3, startDate, endDateTime, buyItNowPrice);
                    // Logic to handle the created item (e.g., add to a list, database, etc.)

                    // Close the "Create Item" tab
                    tabPane.getTabs().remove(createItemTab);
                }
            });
        }
    }
