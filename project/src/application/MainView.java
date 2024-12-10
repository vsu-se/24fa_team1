package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainView {
    private TabPane tabPane;
    private ComboBox<Category> categoryComboBoxSystemAdmin;
    private ComboBox<Category> categoryComboBoxUserInterface;
    private ComboBox<Category> categoryComboBoxConcludedAuctions;
    private TextField categoryInput;
    private TextArea displayTimeArea;
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
    private VBox concludedAuctionsBox;
    private VBox sellerReportBox;
    private VBox buyerReportBox;
    private DatePicker changeTimePicker;
    private TextField timeField;
    private Button changeTimeButton;
    private Button realTimeButton;
    private Button pauseTimeButton;
    private Button unpauseTimeButton;
    private Button saveTextButton;



    private Label categoryErrorLabel;
    private Label premiumErrorLabel;
    private Label commissionErrorLabel;
    private Label listItemErrorLabel;
    private int numMyBids;

    public MainView(ObservableList<Category> categories) {
        tabPane = new TabPane();

        // System Admin Tab
        displayTimeArea = new TextArea();
        displayTimeArea.setMaxWidth(250);
        displayTimeArea.setMaxHeight(30);
        displayTimeArea.setEditable(false);

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

        categoryComboBoxConcludedAuctions = new ComboBox<>(categories);
        categoryComboBoxConcludedAuctions.setPromptText("Select Category");

        concludedAuctionsBox = new VBox(10);
        Label concludedAuctionsLabel = new Label("Concluded Auctions:");
        //clock
        changeTimePicker = new DatePicker();
        changeTimePicker.setPromptText("Select time for testing");

        timeField = new TextField();
        timeField.setPromptText("hh:mm:ss");

        changeTimeButton = new Button("Change Time");

        realTimeButton = new Button("Resume Real Time");

        pauseTimeButton = new Button("Pause Time");

        unpauseTimeButton = new Button("Unpause Time");


        // Initialize error labels
        categoryErrorLabel = new Label();
        categoryErrorLabel.setStyle("-fx-text-fill: red;");

        premiumErrorLabel = new Label();
        premiumErrorLabel.setStyle("-fx-text-fill: red;");

        commissionErrorLabel = new Label();
        commissionErrorLabel.setStyle("-fx-text-fill: red;");

        listItemErrorLabel = new Label();
        listItemErrorLabel.setStyle("-fx-text-fill: red;");

        VBox systemAdminContent = new VBox(10, categoryErrorLabel, categoryComboBoxSystemAdmin, new HBox(10, categoryInput, addButton), premiumErrorLabel, new HBox(10, premiumInput, setPremiumButton), commissionErrorLabel, new HBox(10, commissionInput, setCommissionButton), new HBox(10, changeTimePicker, timeField, changeTimeButton, realTimeButton, pauseTimeButton, unpauseTimeButton), concludedAuctionsLabel, categoryComboBoxConcludedAuctions, concludedAuctionsBox);
        Tab systemAdminTab = new Tab("System Admin", systemAdminContent);
        systemAdminTab.setClosable(false);


        // User Interface Tab
        categoryComboBoxUserInterface = new ComboBox<>(categories);
        categoryComboBoxUserInterface.setPromptText("Category");

        buyerPremiumLabel = new Label("Buyer's Premium: Not set");
        sellerCommissionLabel = new Label("Seller's Commission: Not set");

        listItemButton = new Button("List Item for Sale");

        userInterfaceItemsBox = new VBox(10);
        ScrollPane userInterfaceScrollPane = new ScrollPane(userInterfaceItemsBox);
        userInterfaceScrollPane.setFitToWidth(true);

        VBox userInterfaceContent = new VBox(10, listItemErrorLabel, categoryComboBoxUserInterface, buyerPremiumLabel, sellerCommissionLabel, listItemButton, userInterfaceScrollPane);
        Tab userInterfaceTab = new Tab("User Interface", userInterfaceContent);
        userInterfaceTab.setClosable(false);

        buyerReportBox = new VBox(10);
        ScrollPane buyerReportScrollPane = new ScrollPane(buyerReportBox);
        buyerReportScrollPane.setFitToWidth(true);



        userInterfaceContent = new VBox(10, listItemErrorLabel, categoryComboBoxUserInterface, buyerPremiumLabel, sellerCommissionLabel, listItemButton, userInterfaceScrollPane, new Label("Buyer Report:"), buyerReportScrollPane);
        userInterfaceTab = new Tab("User Interface", userInterfaceContent);
        userInterfaceTab.setClosable(false);



        // My Profile Tab
        myProfileItemsBox = new VBox(10);
        ScrollPane myProfileScrollPane = new ScrollPane(myProfileItemsBox);
        myProfileScrollPane.setFitToWidth(true);

        sellerReportBox = new VBox(10);
        ScrollPane sellerReportScrollPane = new ScrollPane(sellerReportBox);
        sellerReportScrollPane.setFitToWidth(true);

        VBox myProfileContent = new VBox(10, myProfileScrollPane, new Label("Seller Report:"), sellerReportScrollPane);
        Tab myProfileTab = new Tab("My Profile", myProfileContent);
        myProfileTab.setClosable(false);

        tabPane.getTabs().addAll(systemAdminTab, userInterfaceTab, myProfileTab);

        // Save Buttons
    }

void setupSaveOptionsTab(MainController controller) {
    Button saveTextButton = new Button("Save Categories");


    saveTextButton.setOnAction(event -> controller.saveCategoriesText("categories.txt"));

    VBox layout = new VBox(10, saveTextButton);
    Tab saveTab = new Tab("Save Options", layout);
    saveTab.setClosable(false);
    tabPane.getTabs().add(saveTab);

}
void setupLoadOptionsTab(MainController controller){
        Button loadTextButton = new Button("Load Categories");

        loadTextButton.setOnAction(event -> controller.loadCategoriesText("categories.txt"));
        VBox layout = new VBox(10, loadTextButton);
        Tab saveTab = new Tab("Save Options", layout);
        saveTab.setClosable(false);
        tabPane.getTabs().add(saveTab);

}



    public Button getSaveTextButton() {
        return saveTextButton;
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
    public ComboBox<Category> getCategoryComboBoxConcludedAuctions() {
        return categoryComboBoxConcludedAuctions;
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

    public VBox getConcludedAuctionsBox() {
        return concludedAuctionsBox;
    }

    public VBox getSellerReportBox() {
        return sellerReportBox;
    }
    public VBox getBuyerReportBox() {
        return buyerReportBox;
    }

    // Getters for error labels
    public Label getCategoryErrorLabel() {
        return categoryErrorLabel;
    }

    public Label getPremiumErrorLabel() {
        return premiumErrorLabel;
    }

    public Label getCommissionErrorLabel() {
        return commissionErrorLabel;
    }

    public Label getListItemErrorLabel() {
        return listItemErrorLabel;
    }

    public void setNumMyBids(int numMyBids) {
        this.numMyBids = numMyBids;
    }

    public int getNumMyBids() {
        return numMyBids;
    }
    public Button getChangeTimeButton() {
        return changeTimeButton;
    }

    public Button getResumeTimeButton() {
        return realTimeButton;
    }

    public TextField getTimeField() {
        return timeField;
    }

    public DatePicker getChangeTimePicker() {
        return changeTimePicker;
    }

    public Button getPauseTimeButton() {
        return pauseTimeButton;
    }

    public Button getUnpauseTimeButton() {
        return unpauseTimeButton;
    }

    public TextArea getDisplayTimeArea() {
        return displayTimeArea;
    }



}