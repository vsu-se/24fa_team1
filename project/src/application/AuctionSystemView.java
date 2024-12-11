//Previously MainView, just the main auction view
package application;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuctionSystemView extends Application{
	
	private AuctionSystemController controller;
	private AdminSettingsView adminView;
	private UserInterfaceView userInterfaceView;
	private MyProfileView myProfileView;
	private TabPane tabPane;
	private int numMyBids = 0;
  
  
	@Override
	public void start(Stage primaryStage) {
		adminView = new AdminSettingsView(this);
		userInterfaceView = new UserInterfaceView(this);
		myProfileView = new MyProfileView(this);
	    controller = new AuctionSystemController(this);
        //SystemClock clock = new SystemClock();

		tabPane = generateTabPane(adminView, userInterfaceView, myProfileView);
		Scene scene = new Scene(tabPane, 1000, 800);

	    primaryStage.setTitle("Auction System");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

	private TabPane generateTabPane(AdminSettingsView adminView, UserInterfaceView userInterfaceView, MyProfileView myProfileView) {
		TabPane tabPane = new TabPane();
		
		Tab systemAdminTab = adminView.getTab();
		Tab userInterfaceTab = userInterfaceView.getTab();
		Tab myProfileTab = myProfileView.getTab();
		
		tabPane.getTabs().addAll(systemAdminTab, userInterfaceTab, myProfileTab);
		  
		return tabPane;
	}

	@Override
	public void stop() {
	//      if (controller != null) {
	//          controller.shutdownScheduler();
	//      }
	}
	
	public static void main(String[] args) {
	      launch(args);
	}
	
	public TabPane getTabPane() {
        return tabPane;
    }

    public ComboBox<String> getCategoryComboBoxSystemAdmin() {
        return adminView.getCategoryComboBoxSystemAdmin();
    }

    public ComboBox<String> getCategoryComboBoxUserInterface() {
        return userInterfaceView.getCategoryComboBoxUserInterface();
    }
    
    public ComboBox<String> getCategoryComboBoxConcludedAuctions() {
        return adminView.getCategoryComboBoxConcludedAuctions();
    }

    public TextField getCategoryInput() {
        return adminView.getCategoryInput();
    }

    public Button getAddButton() {
        return adminView.getAddButton();
    }

    public TextField getPremiumInput() {
        return adminView.getPremiumInput();
    }

    public Button getSetPremiumButton() {
        return adminView.getSetPremiumButton();
    }

    public TextField getCommissionInput() {
        return adminView.getCommissionInput();
    }

    public Button getSetCommissionButton() {
        return adminView.getSetCommissionButton();
    }

    public Label getBuyerPremiumLabel() {
        return userInterfaceView.getBuyerPremiumLabel();
    }

    public Label getSellerCommissionLabel() {
        return userInterfaceView.getSellerCommissionLabel();
    }

    public Button getListItemButton() {
        return userInterfaceView.getListItemButton();
    }

    public VBox getUserInterfaceItemsBox() {
        return userInterfaceView.getUserInterfaceItemsBox();
    }

    public VBox getMyProfileItemsBox() {
        return myProfileView.getMyProfileItemsBox();
    }

    public VBox getConcludedAuctionsBox() {
        return adminView.getConcludedAuctionsBox();
    }

    // Getters for error labels
    public Label getCategoryErrorLabel() {
        return adminView.getCategoryErrorLabel();
    }

    public Label getPremiumErrorLabel() {
        return adminView.getPremiumErrorLabel();
    }

    public Label getCommissionErrorLabel() {
        return adminView.getCommissionErrorLabel();
    }

    public Label getListItemErrorLabel() {
        return userInterfaceView.getListItemErrorLabel();
    }

	public void setNumMyBids(int numMyBids) {
		this.numMyBids  = numMyBids;
	}
	
	public int getNumMyBids() {
		return numMyBids;
	}

	public Button getChangeTimeButton() {
		return adminView.getChangeTimeButton();
	}
	
	public Button getResumeTimeButton() {
		return adminView.getResumeTimeButton();
	}

	public TextField getTimeField() {
		return adminView.getTimeField();
	}

	public DatePicker getChangeTimePicker() {
		return adminView.getChangeTimePicker();
	}

	public Button getPauseTimeButton() {
		return adminView.getPauseTimeButton();
	}
	
	public Button getUnpauseTimeButton() {
		return adminView.getUnpauseTimeButton();
	}
	
	public TextArea getDisplayTimeArea() {
		return adminView.getDisplayTimeArea();
	}

	public void createAddItemTab() {
		AddItemView addItemView = new AddItemView(this);

        tabPane.getTabs().add(addItemView.getTab());
        tabPane.getSelectionModel().select(addItemView.getTab());
	}
	
	public AuctionSystemController getController(){
		return controller;
	}
}