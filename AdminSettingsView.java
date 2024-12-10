package application;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminSettingsView {
	private Tab adminTab;
	private AuctionSystemView parentView;
	
    private ComboBox<String> categoryComboBoxSystemAdmin;
    private ComboBox<String> categoryComboBoxConcludedAuctions;
    private TextArea displayTimeArea;
    private TextField categoryInput;
    private Button addButton;
    private TextField premiumInput;
    private Button setPremiumButton;
    private TextField commissionInput;
    private Button setCommissionButton;
    private DatePicker changeTimePicker;
    private TextField timeField;
    private Button changeTimeButton;
    private Button realTimeButton;
    private Button pauseTimeButton;
    private Button unpauseTimeButton;
    private VBox concludedAuctionsBox;

    // Error labels
    private Label categoryErrorLabel;
    private Label premiumErrorLabel;
    private Label commissionErrorLabel;
    
    private ObservableList<String> categories = null;
	
	public AdminSettingsView(AuctionSystemView parentView) {
		this.parentView = parentView;
		adminTab = generateAdminTab();
		AdminSettingsEventHandlers eventHandlers = new AdminSettingsEventHandlers(this);
	}
	
	private Tab generateAdminTab() {
		displayTimeArea = new TextArea();
        displayTimeArea.setMaxWidth(250);
        displayTimeArea.setMaxHeight(30);
        displayTimeArea.setEditable(false);
        
        categoryComboBoxSystemAdmin = new ComboBox<>(null);
        categoryComboBoxSystemAdmin.setPromptText("Category");

        categoryInput = new TextField();
        categoryInput.setPromptText("Enter category name");
        categoryInput.setMaxWidth(400);

        addButton = new Button("Add Category");

        premiumInput = new TextField();
        premiumInput.setPromptText("Enter buyer's premium (%)");
        premiumInput.setMaxWidth(400);

        setPremiumButton = new Button("Set Premium");

        commissionInput = new TextField();
        commissionInput.setPromptText("Enter seller's commission (%)");
        commissionInput.setMaxWidth(400);

        setCommissionButton = new Button("Set Commission");
        
        categoryComboBoxConcludedAuctions = new ComboBox<>(categories);
        categoryComboBoxConcludedAuctions.setPromptText("Select Category");
        
        changeTimePicker = new DatePicker();
        changeTimePicker.setPromptText("Select time for testing");
        
        timeField= new TextField();
        timeField.setPromptText("hh:mm:ss");
        
        changeTimeButton = new Button("Change Time");
        
        realTimeButton = new Button("Resume Real Time");
        
        pauseTimeButton = new Button("Pause Time");
        
        unpauseTimeButton= new Button("Unpause Time");
        
        concludedAuctionsBox = new VBox(10);
        Label concludedAuctionsLabel = new Label("Concluded Auctions:");

        // Initialize error labels
        categoryErrorLabel = new Label();
        categoryErrorLabel.setStyle("-fx-text-fill: red;");

        premiumErrorLabel = new Label();
        premiumErrorLabel.setStyle("-fx-text-fill: red;");

        commissionErrorLabel = new Label();
        commissionErrorLabel.setStyle("-fx-text-fill: red;");

        //VBox systemAdminContent = new VBox(10, categoryErrorLabel, displayTimeArea, categoryComboBoxSystemAdmin, new HBox(10, categoryInput, addButton), premiumErrorLabel, new HBox(10, premiumInput, setPremiumButton), commissionErrorLabel, new HBox(10, commissionInput, setCommissionButton), new HBox(10, changeTimePicker, timeField, changeTimeButton, realTimeButton, pauseTimeButton, unpauseTimeButton), concludedAuctionsLabel, categoryComboBoxConcludedAuctions, concludedAuctionsBox);
        VBox systemAdminContent = new VBox(10, categoryErrorLabel, displayTimeArea, categoryComboBoxSystemAdmin, new HBox(10, categoryInput, addButton), premiumErrorLabel, new HBox(10, premiumInput, setPremiumButton), commissionErrorLabel, new HBox(10, commissionInput, setCommissionButton), new HBox(10, changeTimePicker, timeField, changeTimeButton, realTimeButton, pauseTimeButton, unpauseTimeButton), concludedAuctionsLabel, categoryComboBoxConcludedAuctions, concludedAuctionsBox);
        Tab systemAdminTab = new Tab("System Admin", systemAdminContent);
        systemAdminTab.setClosable(false);
        
        return systemAdminTab;
	}
	
	public Tab getTab() {
		return adminTab;
	}

    public ComboBox<String> getCategoryComboBoxSystemAdmin() {
        return categoryComboBoxSystemAdmin;
    }

    public ComboBox<String> getCategoryComboBoxConcludedAuctions() {
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

    public VBox getConcludedAuctionsBox() {
        return concludedAuctionsBox;
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
	
	public AuctionSystemController getController() {
		return parentView.getController();
	}
	
}
