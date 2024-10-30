package application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MainController {
    private ObservableList<Category> categories;
    private MainView view;
    private double buyerPremium;
    private double sellerCommission;

    public MainController(MainView view) {
        this.view = view;
        categories = FXCollections.observableArrayList();

        // Set up event handler for the add button
        view.getAddButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String categoryName = view.getCategoryInput().getText();
                if (!categoryName.isEmpty()) {
                    Category newCategory = new Category(categoryName);
                    categories.add(newCategory);
                    view.getCategoryInput().clear();
                }
            }
        });

        // Set up event handler for the set premium button
        view.getSetPremiumButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String premiumText = view.getPremiumInput().getText();
                try {
                    buyerPremium = Double.parseDouble(premiumText);
                    view.getPremiumInput().clear();
                    view.getBuyerPremiumLabel().setText("Buyer's Premium: " + buyerPremium + "%");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid premium value");
                }
            }
        });

        // Set up event handler for the set commission button
        view.getSetCommissionButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String commissionText = view.getCommissionInput().getText();
                try {
                    sellerCommission = Double.parseDouble(commissionText);
                    view.getCommissionInput().clear();
                    view.getSellerCommissionLabel().setText("Seller's Commission: " + sellerCommission + "%");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid commission value");
                }
            }
        });

        // Bind the categories list to the ComboBoxes
        view.getCategoryComboBoxSystemAdmin().setItems(categories);
        view.getCategoryComboBoxUserInterface().setItems(categories);
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    public double getBuyerPremium() {
        return buyerPremium;
    }

    public double getSellerCommission() {
        return sellerCommission;
    }
}
