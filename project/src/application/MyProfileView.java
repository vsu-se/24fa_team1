package application;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MyProfileView {
	Tab myProfileTab;
    private VBox myProfileItemsBox;
    private AuctionSystemView parentView;
    private VBox sellerReportBox;
    private VBox buyerReportBox;

	public MyProfileView(AuctionSystemView parentView) {
		myProfileTab = generateTab();
		this.parentView = parentView;
	}
	
	private Tab generateTab() {
		
		buyerReportBox = new VBox(10);
        ScrollPane buyerReportScrollPane = new ScrollPane(buyerReportBox);
        buyerReportScrollPane.setFitToWidth(true);
        
		myProfileItemsBox = new VBox(10);
        ScrollPane myProfileScrollPane = new ScrollPane(myProfileItemsBox);
        myProfileScrollPane.setFitToWidth(true);

        sellerReportBox = new VBox(10);
        ScrollPane sellerReportScrollPane = new ScrollPane(sellerReportBox);
        sellerReportScrollPane.setFitToWidth(true);

        VBox myProfileContent = new VBox(10, myProfileScrollPane, new Label("Seller Report:"), sellerReportScrollPane, new Label("Buyer Report:"), buyerReportBox);
        Tab myProfileTab = new Tab("My Profile", myProfileContent);
        myProfileTab.setClosable(false);
        
        return myProfileTab;
	}
	
	public Tab getTab(){
		return myProfileTab;
	}

    public VBox getMyProfileItemsBox() {
        return myProfileItemsBox;
    }
    
    public AuctionSystemController getController() {
    	return parentView.getController();
    }
    
    public VBox getSellerReportBox() {
        return sellerReportBox;
    }
    public VBox getBuyerReportBox() {
        return buyerReportBox;
    }
}