package application;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BidHistoryTab {
	private VBox layout;
	private Item item;
	private ArrayList<Bid> bidHistory;
	public BidHistoryTab(Auction auction) {
		this.item = auction.getItem();
		this.bidHistory = auction.getBidHistory();
		layout = new VBox(10);
		layout.getChildren().add(new Label("Item Name: " + item.getTitle()));
		layout.getChildren().add(new Label("BID HISTORY: "));
		layout.getChildren().add(new Label("___________________________________________________________________________________________"));
		layout.getChildren().add(formatHistory());
		
	}
	
	private VBox formatHistory() {
		VBox historyBox = new VBox(10);
		
		for(int i = bidHistory.size() - 1; i >= 0; --i) {
			HBox bidBox = new HBox(10);
			Bid bid = bidHistory.get(i);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			Label bidOrBIN = new Label(bid.getIsBIN() ? "BIN: " : "Bid: ");
			Label amount = new Label(String.format("$%.2f, ", bid.getAmount()));
			Label time = new Label("Time: " + bid.getTime().format(formatter) + ", ");
			Label user = new Label("User: Unemplemented");
			bidBox.getChildren().addAll(bidOrBIN, amount, time, user);
			historyBox.getChildren().add(bidBox);
			
		}
		
		
		return historyBox;
	}
	
	
	
	
	
	
	
	public VBox getLayout() {
        return layout;
    }
}
