package application;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

public class EndedAuctionsView {
    private TableView<Item> endedAuctionsTable;
    private VBox layout;
    private EndedAuctionsController controller;

    public EndedAuctionsView(EndedAuctionsController controller) {
        this.controller = controller;
        endedAuctionsTable = new TableView<>();
        layout = new VBox(endedAuctionsTable);

        initializeTableColumns();
        loadEndedAuctions();
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

        endedAuctionsTable.getColumns().addAll(titleColumn, categoryColumn, conditionColumn, endDateColumn, priceColumn);
    }

    private void loadEndedAuctions() {
        ObservableList<Item> endedAuctions = controller.getEndedAuctions();
        endedAuctionsTable.setItems(endedAuctions);
    }

    public TableView<Item> getEndedAuctionsTable() {
        return endedAuctionsTable;
    }

    public VBox getLayout() {
        return layout;
    }
}