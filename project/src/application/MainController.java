package application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MainController {
    private ObservableList<Category> categories;
    private MainView view;
    private double buyerPremium;
    private double sellerCommission;

    public MainController(MainView view) {
        this.view = view;
        categories = FXCollections.observableArrayList();

        // Set up event handler for the add button
        view.getAddButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String categoryName = view.getCategoryInput().getText();
                if (!categoryName.isEmpty()) {
                    Category newCategory = new Category(categoryName);
                    categories.add(newCategory);
                    view.getCategoryInput().clear();
                }
            }
        });

        // Set up event handler for the set premium button
        view.getSetPremiumButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String premiumText = view.getPremiumInput().getText();
                try {
                    buyerPremium = Double.parseDouble(premiumText);
                    view.getPremiumInput().clear();
                    view.getBuyerPremiumLabel().setText("Buyer's Premium: " + buyerPremium + "%");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid premium value");
                }
            }
        });

        // Set up event handler for the set commission button
        view.getSetCommissionButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String commissionText = view.getCommissionInput().getText();
                try {
                    sellerCommission = Double.parseDouble(commissionText);
                    view.getCommissionInput().clear();
                    view.getSellerCommissionLabel().setText("Seller's Commission: " + sellerCommission + "%");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid commission value");
                }
            }
        });

        // Set up event handler for the list item button
        view.getListItemButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (categories.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Please add a category in the System Admin tab before listing an item.");
                    alert.showAndWait();
                    return;
                }

                Tab createItemTab = new Tab("Create Item");
                VBox createItemContent = new VBox(10);

                TextField titleInput = new TextField();
                titleInput.setPromptText("Enter item title");

                TextField weightInput = new TextField();
                weightInput.setPromptText("Enter item weight");

                TextArea descriptionInput = new TextArea();
                descriptionInput.setPromptText("Enter item description");

                ComboBox<Category> categoryComboBox = new ComboBox<>(categories);
                categoryComboBox.setPromptText("Select category");

                ComboBox<String> conditionComboBox = new ComboBox<>();
                conditionComboBox.getItems().addAll("New", "Used");
                conditionComboBox.setPromptText("Select condition");

                TextField tag1Input = new TextField();
                tag1Input.setPromptText("Enter tag 1 (optional)");
                tag1Input.setMaxWidth(2 * 400 / 3);

                TextField tag2Input = new TextField();
                tag2Input.setPromptText("Enter tag 2 (optional)");
                tag2Input.setMaxWidth(2 * 400 / 3);

                TextField tag3Input = new TextField();
                tag3Input.setPromptText("Enter tag 3 (optional)");
                tag3Input.setMaxWidth(2 * 400 / 3);

                Button createItemButton = new Button("Create Item");

                createItemButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String title = titleInput.getText();
                        String weight = weightInput.getText();
                        String description = descriptionInput.getText();
                        Category category = categoryComboBox.getValue();
                        String condition = conditionComboBox.getValue();
                        String tag1 = tag1Input.getText();
                        String tag2 = tag2Input.getText();
                        String tag3 = tag3Input.getText();

                        if (title.isEmpty() || weight.isEmpty() || description.isEmpty() || category == null || condition == null) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("Please fill in all required fields.");
                            alert.showAndWait();
                            return;
                        }

                        // Logic to create the item
                        // For now, we just print the item details to the console
                        System.out.println("Item created:");
                        System.out.println("Title: " + title);
                        System.out.println("Weight: " + weight);
                        System.out.println("Description: " + description);
                        System.out.println("Category: " + category.getName());
                        System.out.println("Condition: " + condition);
                        System.out.println("Tag 1: " + tag1);
                        System.out.println("Tag 2: " + tag2);
                        System.out.println("Tag 3: " + tag3);

                        // Close the "Create Item" tab
                        view.getTabPane().getTabs().remove(createItemTab);
                    }
                });

                createItemContent.getChildren().addAll(titleInput, weightInput, descriptionInput, categoryComboBox, conditionComboBox, tag1Input, tag2Input, tag3Input, createItemButton);
                createItemTab.setContent(createItemContent);
                view.getTabPane().getTabs().add(createItemTab);
                view.getTabPane().getSelectionModel().select(createItemTab);
            }
        });

        // Bind the categories list to the ComboBoxes
        view.getCategoryComboBoxSystemAdmin().setItems(categories);
        view.getCategoryComboBoxUserInterface().setItems(categories);
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    public double getBuyerPremium() {
        return buyerPremium;
    }

    public double getSellerCommission() {
        return sellerCommission;
    }
}