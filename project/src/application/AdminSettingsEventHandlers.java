import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AdminSettingsEventHandlers {
	public AdminSettingsEventHandlers(AdminSettingsView view) {
        view.getAddButton().setOnAction(event -> {
        	String categoryName = view.getCategoryInput().getText();
            view.getController().addCategory(categoryName);
        });
        
        view.getSetPremiumButton().setOnAction(event -> {
            String premiumText = view.getPremiumInput().getText();
            view.getController().setPremium(premiumText);
        });
        
        view.getSetCommissionButton().setOnAction(event -> {
            String commissionText = view.getCommissionInput().getText();
            view.getController().setCommission(commissionText);
        });
        
        view.getChangeTimeButton().setOnAction(event -> {
        	try{
        		LocalDate date = view.getChangeTimePicker().getValue();
            	String timeString = view.getTimeField().getText();
            	LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
                view.getController().changeTime(date, timeString, time);
        	}
        	catch(Exception e){
        		view.getController().changeTimeError();
        	}
        });
        
        view.getResumeTimeButton().setOnAction(event -> {
            view.getController().resumeTime();
        });
        
        view.getPauseTimeButton().setOnAction(event -> {
        	view.getController().pauseTime();
        });
        
        view.getUnpauseTimeButton().setOnAction(event -> {
            view.getController().unpauseTime();
        });
	}
}
