package application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

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
                String description = view.getDescriptionInput().getText();
                Category category = view.getCategoryComboBox().getValue();
                String condition = view.getConditionComboBox().getValue();
                String tag1 = view.getTag1Input().getText();
                String tag2 = view.getTag2Input().getText();
                String tag3 = view.getTag3Input().getText();

                if (title.isEmpty() || weight.isEmpty() || description.isEmpty() || category == null || condition == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill in all required fields.");
                    alert.showAndWait();
                    return;
                }

                Item newItem = new Item(title, weight, description, category, condition, tag1, tag2, tag3);
                // Logic to handle the created item (e.g., add to a list, database, etc.)

                // Close the "Create Item" tab
                tabPane.getTabs().remove(createItemTab);
            }
        });
    }
}
