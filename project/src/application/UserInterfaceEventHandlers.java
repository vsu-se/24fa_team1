package application;

import javafx.scene.control.Tab;

public class UserInterfaceEventHandlers {
	public UserInterfaceEventHandlers(UserInterfaceView view) {
		view.getListItemButton().setOnAction(event -> {
		  view.getController().openAddItemView();
		  });
	}
}