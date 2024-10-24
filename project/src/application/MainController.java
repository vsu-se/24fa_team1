package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MainController {
    private ObservableList<Category> categories;
    private MainView view;

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

        // Bind the categories list to the ComboBoxes
        view.getCategoryComboBoxSystemAdmin().setItems(categories);
        view.getCategoryComboBoxUserInterface().setItems(categories);
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }
}
