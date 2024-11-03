package application;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainView {
    private TabPane tabPane;
    private ComboBox<Category> categoryComboBoxSystemAdmin;
    private ComboBox<Category> categoryComboBoxUserInterface;
    private TextField categoryInput;
    private Button addButton;
    private TextField premiumInput;
    private Button setPremiumButton;
    private TextField commissionInput;
    private Button setCommissionButton;
    private Label buyerPremiumLabel;
    private Label sellerCommissionLabel;
    private Button listItemButton;
    private VBox userInterfaceItemsBox;
    private VBox myProfileItemsBox;

    public MainView(ObservableList<Category> categories) {
        tabPane = new TabPane();

        // System Admin Tab
        categoryComboBoxSystemAdmin = new ComboBox<>(categories);
        categoryComboBoxSystemAdmin.setPromptText("Category");

        categoryInput = new TextField();
        categoryInput.setPromptText("Enter category name");
        categoryInput.setMaxWidth(2 * 400 / 3);

        addButton = new Button("Add Category");

        premiumInput = new TextField();
        premiumInput.setPromptText("Enter buyer's premium (%)");
        premiumInput.setMaxWidth(2 * 400 / 3);

        setPremiumButton = new Button("Set Premium");

        commissionInput = new TextField();
        commissionInput.setPromptText("Enter seller's commission (%)");
        commissionInput.setMaxWidth(2 * 400 / 3);

        setCommissionButton = new Button("Set Commission");

        VBox systemAdminContent = new VBox(10, categoryComboBoxSystemAdmin, new HBox(10, categoryInput, addButton), new HBox(10, premiumInput, setPremiumButton), new HBox(10, commissionInput, setCommissionButton));
        Tab systemAdminTab = new Tab("System Admin", systemAdminContent);
        systemAdminTab.setClosable(false);

        // User Interface Tab
        categoryComboBoxUserInterface = new ComboBox<>(categories);
        categoryComboBoxUserInterface.setPromptText("Category");

        buyerPremiumLabel = new Label("Buyer's Premium: Not set");
        sellerCommissionLabel = new Label("Seller's Commission: Not set");

        listItemButton = new Button("List Item for Sale");

        userInterfaceItemsBox = new VBox(10);
        ScrollPane userInterfaceScrollPane = new ScrollPane(userInterfaceItemsBox);        userInterfaceScrollPane.setFitToWidth(true);

        VBox userInterfaceContent = new VBox(10, categoryComboBoxUserInterface, buyerPremiumLabel, sellerCommissionLabel, listItemButton, userInterfaceScrollPane);
        Tab userInterfaceTab = new Tab("User Interface", userInterfaceContent);
        userInterfaceTab.setClosable(false);

        // My Profile Tab
        myProfileItemsBox = new VBox(10);
        ScrollPane myProfileScrollPane = new ScrollPane(myProfileItemsBox);
        myProfileScrollPane.setFitToWidth(true);

        VBox myProfileContent = new VBox(10, myProfileScrollPane);
        Tab myProfileTab = new Tab("My Profile", myProfileContent);
        myProfileTab.setClosable(false);

        tabPane.getTabs().addAll(systemAdminTab, userInterfaceTab, myProfileTab);
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

    public TextField getPremiumInput() {
        return premiumInput;
    }

    public Button getSetPremiumButton() {
        return setPremiumButton;
    }

    public TextField getCommissionInput() {
        return commissionInput;
    }

    public Button getSetCommissionButton() {
        return setCommissionButton;
    }

    public Label getBuyerPremiumLabel() {
        return buyerPremiumLabel;
    }

    public Label getSellerCommissionLabel() {
        return sellerCommissionLabel;
    }

    public Button getListItemButton() {
        return listItemButton;
    }

    public VBox getUserInterfaceItemsBox() {
        return userInterfaceItemsBox;
    }

    public VBox getMyProfileItemsBox() {
        return myProfileItemsBox;
    }
}
