package application;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MainView {
    private TabPane tabPane;
    private ComboBox<Category> categoryComboBoxSystemAdmin;
    private ComboBox<Category> categoryComboBoxUserInterface;
    private TextField categoryInput;
    private Button addButton;

    public MainView(ObservableList<Category> categories) {
        tabPane = new TabPane();

        // System Admin Tab
        categoryComboBoxSystemAdmin = new ComboBox<>(categories);
        categoryComboBoxSystemAdmin.setPromptText("Category");

        categoryInput = new TextField();
        categoryInput.setPromptText("Enter category name");

        addButton = new Button("Add Category");

        VBox systemAdminContent = new VBox(10, categoryComboBoxSystemAdmin, categoryInput, addButton);
        Tab systemAdminTab = new Tab("System Admin", systemAdminContent);
        systemAdminTab.setClosable(false);

        // User Interface Tab
        categoryComboBoxUserInterface = new ComboBox<>(categories);
        categoryComboBoxUserInterface.setPromptText("Category");

        VBox userInterfaceContent = new VBox(10, categoryComboBoxUserInterface);
        Tab userInterfaceTab = new Tab("User Interface", userInterfaceContent);
        userInterfaceTab.setClosable(false);

        // Other Tab
        Tab tab3 = new Tab("Tab 3", new Label("Content for Tab 3"));
        tab3.setClosable(false);

        tabPane.getTabs().addAll(systemAdminTab, userInterfaceTab, tab3);
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public ComboBox<Category> getCategoryComboBoxSystemAdmin() {
        return categoryComboBoxSystemAdmin;
    }

    public ComboBox<Category> getCategoryComboBoxUserInterface() {
        return categoryComboBoxUserInterface;
    }

    public TextField getCategoryInput() {
        return categoryInput;
    }

    public Button getAddButton() {
        return addButton;
    }
}