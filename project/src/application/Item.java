//Just holds item specific info
package application;

import java.time.LocalDateTime;

public class Item {
	private String title;
    private String weight;
    private String description;
    private String condition;
    
    public Item(String title, String weight, String description, String condition){
    	this.title = title;
    	this.weight = weight;
        this.description = description;
        this.condition = condition;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getWeight() {
        return weight;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getCondition() {
        return condition;
    }
}
