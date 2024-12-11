package application;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

public class SellerReportView {
    private TableView<Auction> reportTable;
    private VBox layout;
    AuctionSystemView parentView;

    public SellerReportView(AuctionSystemView parentView) {
    	this.parentView = parentView;
        reportTable = new TableView<>();
        layout = new VBox(reportTable);

        initializeTableColumns();
    }

    private void initializeTableColumns() {
        TableColumn<Auction, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Auction, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Auction, String> conditionColumn = new TableColumn<>("Condition");
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));

        TableColumn<Auction, LocalDateTime> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Auction, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("buyItNowPrice"));

        reportTable.getColumns().addAll(titleColumn, categoryColumn, conditionColumn, endDateColumn, priceColumn);
    }

    public void setItems(ObservableList<Auction> auctions) {
        reportTable.setItems(auctions);
    }

    public TableView<Auction> getReportTable() {
        return reportTable;
    }

    public VBox getLayout() {
        return layout;
    }
}