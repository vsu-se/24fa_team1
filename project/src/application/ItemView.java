package application;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ItemView {
    private VBox layout;
    private TextField titleInput;
    private TextField weightInput;
    private ComboBox<String> weightUnitComboBox;
    private TextArea descriptionInput;
    private ComboBox<Category> categoryComboBox;
    private ComboBox<String> conditionComboBox;
    private TextField tag1Input;
    private TextField tag2Input;
    private TextField tag3Input;
    private DatePicker endDatePicker;
    private TextField endTimeInput;
    private TextField buyItNowPriceInput;
    private Label startDateLabel;
    private Button createItemButton;

    public ItemView(ObservableList<Category> categories) {
        layout = new VBox(10);

        titleInput = new TextField();
        titleInput.setPromptText("Enter item title");

        weightInput = new TextField();
        weightInput.setPromptText("Enter item weight");

        weightUnitComboBox = new ComboBox<>();
        weightUnitComboBox.getItems().addAll("oz", "g", "lbs", "kg");
        weightUnitComboBox.setPromptText("Select unit");

        descriptionInput = new TextArea();
        descriptionInput.setPromptText("Enter item description");

        categoryComboBox = new ComboBox<>(categories);
        categoryComboBox.setPromptText("Select category");

        conditionComboBox = new ComboBox<>();
        conditionComboBox.getItems().addAll("New", "Used");
        conditionComboBox.setPromptText("Select condition");

        tag1Input = new TextField();
        tag1Input.setPromptText("Enter tag 1 (optional)");
        tag1Input.setMaxWidth(2 * 400 / 3);

        tag2Input = new TextField();
        tag2Input.setPromptText("Enter tag 2 (optional)");
        tag2Input.setMaxWidth(2 * 400 / 3);

        tag3Input = new TextField();
        tag3Input.setPromptText("Enter tag 3 (optional)");
        tag3Input.setMaxWidth(2 * 400 / 3);

        startDateLabel = new Label("Start Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Select end date");

        endTimeInput = new TextField();
        endTimeInput.setPromptText("Enter end time (HH:mm)");

        buyItNowPriceInput = new TextField();
        buyItNowPriceInput.setPromptText("Enter Buy it now price (USD)");

        createItemButton = new Button("Create Item");

        layout.getChildren().addAll(titleInput, new HBox(10, weightInput, weightUnitComboBox), descriptionInput, categoryComboBox, conditionComboBox, tag1Input, tag2Input, tag3Input, startDateLabel, new HBox(10, endDatePicker, endTimeInput), buyItNowPriceInput, createItemButton);
    }

    public VBox getLayout() {
        return layout;
    }

    public TextField getTitleInput() {
        return titleInput;
    }

    public TextField getWeightInput() {
        return weightInput;
    }

    public ComboBox<String> getWeightUnitComboBox() {
        return weightUnitComboBox;
    }

    public TextArea getDescriptionInput() {
        return descriptionInput;
    }

    public ComboBox<Category> getCategoryComboBox() {
        return categoryComboBox;
    }

    public ComboBox<String> getConditionComboBox() {
        return conditionComboBox;
    }

    public TextField getTag1Input() {
        return tag1Input;
    }

    public TextField getTag2Input() {
        return tag2Input;
    }

    public TextField getTag3Input() {
        return tag3Input;
    }

    public DatePicker getEndDatePicker() {
        return endDatePicker;
    }

    public TextField getEndTimeInput() {
        return endTimeInput;
    }

    public TextField getBuyItNowPriceInput() {
        return buyItNowPriceInput;
    }

    public Label getStartDateLabel() {
        return startDateLabel;
    }

    public Button getCreateItemButton() {
        return createItemButton;
    }
}
