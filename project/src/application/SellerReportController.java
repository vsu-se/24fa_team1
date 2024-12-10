package application;

import javafx.collections.ObservableList;

public class SellerReportController {
    private SellerReportView view;
    private ObservableList<Item> items;

    public SellerReportController(SellerReportView view, ObservableList<Item> items) {
        this.view = view;
        this.items = items;
        generateReport();
    }

    private void generateReport() {
        view.getReportTable().setItems(items);
    }
}