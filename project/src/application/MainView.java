
package application;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MainView {
    private TabPane tabPane;
    private Label currentTimeLabel;
    private TextField customTimeField;
    private Button setTimeButton;
    private Button realTimeButton;

    public MainView(ObservableList<Category> categories) {
        tabPane = new TabPane();

        // Time Management UI
        VBox timeManagement = new VBox();
        currentTimeLabel = new Label("Current Time: ");
        timeManagement.getChildren().add(currentTimeLabel);

        customTimeField = new TextField();
        customTimeField.setPromptText("Enter custom time (yyyy-MM-dd HH:mm)");
        timeManagement.getChildren().add(customTimeField);

        setTimeButton = new Button("Set Custom Time");
        timeManagement.getChildren().add(setTimeButton);

        realTimeButton = new Button("Resume Real Time");
        timeManagement.getChildren().add(realTimeButton);

        Tab timeTab = new Tab("Time Management");
        timeTab.setContent(timeManagement);
        tabPane.getTabs().add(timeTab);
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public Label getCurrentTimeLabel() {
        return currentTimeLabel;
    }

    public TextField getCustomTimeField() {
        return customTimeField;
    }

    public Button getSetTimeButton() {
        return setTimeButton;
    }

    public Button getRealTimeButton() {
        return realTimeButton;
    }
}
