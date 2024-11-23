package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

public class SellerReportView {
    private TableView<Item> reportTable;
    private VBox layout;

    public SellerReportView() {
        reportTable = new TableView<>();
        layout = new VBox(reportTable);

        initializeTableColumns();
    }

    private void initializeTableColumns() {
        TableColumn<Item, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Item, Category> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Item, String> conditionColumn = new TableColumn<>("Condition");
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));

        TableColumn<Item, LocalDateTime> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Item, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("buyItNowPrice"));

        reportTable.getColumns().addAll(titleColumn, categoryColumn, conditionColumn, endDateColumn, priceColumn);
    }

    public TableView<Item> getReportTable() {
        return reportTable;
    }

    public VBox getLayout() {
        return layout;
    }
}