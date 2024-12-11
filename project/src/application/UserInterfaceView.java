//This the view with the create item and bid button yk?

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class UserInterfaceView {
	private AuctionSystemView parentView;
	private ObservableList<String> categories = null;
	Tab userInterfaceTab;
    private ComboBox<String> categoryComboBoxUserInterface;
    private Label buyerPremiumLabel;
    private Label sellerCommissionLabel;
    private Button listItemButton;
    private VBox userInterfaceItemsBox;

    private Label listItemErrorLabel;
	
	public UserInterfaceView(AuctionSystemView parentView) {
		userInterfaceTab = generateUserInterfaceTab();
		this.parentView = parentView;
		UserInterfaceEventHandlers eventHandler = new UserInterfaceEventHandlers(this);
	}
    
	
	public Tab generateUserInterfaceTab() {		
		categoryComboBoxUserInterface = new ComboBox<>(categories);
        categoryComboBoxUserInterface.setPromptText("Category");

        buyerPremiumLabel = new Label("Buyer's Premium: 0.00%");
        sellerCommissionLabel = new Label("Seller's Commission: 0.00%");

        listItemButton = new Button("List Item for Sale");

        userInterfaceItemsBox = new VBox(10);
        ScrollPane userInterfaceScrollPane = new ScrollPane(userInterfaceItemsBox);
        userInterfaceScrollPane.setFitToWidth(true);
        
        listItemErrorLabel = new Label();
        listItemErrorLabel.setStyle("-fx-text-fill: red;");

        VBox userInterfaceContent = new VBox(10, listItemErrorLabel, categoryComboBoxUserInterface, buyerPremiumLabel, sellerCommissionLabel, listItemButton, userInterfaceScrollPane);
        Tab userInterfaceTab = new Tab("User Interface", userInterfaceContent);
        
        userInterfaceTab.setClosable(false);
		
		return userInterfaceTab;
	}
	
	public Tab getTab() {
		return userInterfaceTab;
	}

    public ComboBox<String> getCategoryComboBoxUserInterface() {
        return categoryComboBoxUserInterface;
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

    public Label getListItemErrorLabel() {
        return listItemErrorLabel;
    }


	public AuctionSystemController getController() {
		return parentView.getController();
	}
}
