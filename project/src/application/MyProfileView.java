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

	public MyProfileView(AuctionSystemView parentView) {
		myProfileTab = generateTab();
		this.parentView = parentView;
	}
	
	private Tab generateTab() {
		myProfileItemsBox = new VBox(10);
        ScrollPane myProfileScrollPane = new ScrollPane(myProfileItemsBox);
        myProfileScrollPane.setFitToWidth(true);

        VBox myProfileContent = new VBox(10, myProfileScrollPane);
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
}
