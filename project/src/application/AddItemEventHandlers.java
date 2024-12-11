package application;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class AddItemEventHandlers {
	private AddItemView view;
	public AddItemEventHandlers(AddItemView view) {
		this.view = view;

        view.getCreateItemButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	String title = view.getTitleInput().getText();
                String weight = view.getWeightInput().getText();
                String weightUnit = view.getWeightUnitComboBox().getValue();
                String description = view.getDescriptionInput().getText();
                String category = view.getCategoryComboBox().getValue();
                String condition = view.getConditionComboBox().getValue();
                String tag1 = view.getTag1Input().getText();
                String tag2 = view.getTag2Input().getText();
                String tag3 = view.getTag3Input().getText();
                LocalDate endDate = view.getEndDatePicker().getValue();
                String endTime = view.getEndTimeInput().getText();
                String buyItNowPriceText = view.getBuyItNowPriceInput().getText();
                String bidAmountText = view.getBidAmountInput().getText();
                view.getController().createAuction(title, weight, weightUnit, description, category, condition, tag1, tag2, tag3, endDate, endTime, buyItNowPriceText, bidAmountText, view.getCreateItemErrorLabel(), view.getTab());
            }
        });
	}
}