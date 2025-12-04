package screens;

// Package Imports
import application.AddCourse;
import bases.Account;
import fileHandlers.BasketHandler;
import planning.*;

// JavaFX Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

//Java Imports
import java.nio.file.Path;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlannerPage {
	private StackPane root;
	private VBox layout = new VBox();
	
	private static final Path BASKET_CSV = Path.of("accounts_data", "basket.csv");
	
	public StackPane showPlanner(double width, double height, Account account) {
	    // Load saved basket when planner opens
	    BasketHandler.loadBasket(account);
	    
	    // =========== PLANNER TAB ===========
	    Planner_Sched top = new Planner_Sched(account);
	    Planner_BasketView middle = new Planner_BasketView(account);
	    Planner_SearchAdd bottom = new Planner_SearchAdd(account, top, middle);

	    middle.setRemoveToBasket(o -> {
	        String code = o.getCode();
	        String section = o.getSection().toUpperCase();
	        boolean isLab = section.endsWith("L");
	        
	        // Remove the selected offering
	        account.removeFromBasket(o.getKey());
	        
	        // Find and remove the paired offering
	        if (isLab) {
	            // If removing a lab (e.g., "G-1L"), find and remove the matching lecture (e.g., "G")
	            String labPrefix = section.contains("-") ? section.split("-")[0] : section.replace("L", "");
	            String lectureKey = code + "-" + labPrefix;
	            account.removeFromBasket(lectureKey);
	            top.success("[SUCCESS] Removed " + code + " " + section + " and its lecture " + labPrefix);
	        } else {
	            // If removing a lecture (e.g., "G"), find and remove all matching labs (e.g., "G-1L", "G-2L", etc.)
	            List<String> keysToRemove = new ArrayList<>();
	            for (String key : account.getBasket().keySet()) {
	                if (key.startsWith(code + "-" + section + "-") || 
	                    (key.startsWith(code + "-") && key.toUpperCase().contains(section + "-") && key.toUpperCase().endsWith("L"))) {
	                    keysToRemove.add(key);
	                }
	            }
	            for (String key : keysToRemove) {
	                account.removeFromBasket(key);
	            }
	            top.success("[SUCCESS] Removed " + code + " " + section + " and its lab(s)");
	        }
	        
	        middle.refresh(account);
	        top.updateSched(account);
	    });
	    
	    // =========== SAVE AND CLEAR BUTTONS ===========
	    Button saveBtn = new Button("Save Schedule");
	    saveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
	    saveBtn.setOnAction(e -> {
	        boolean success = BasketHandler.saveBasket(account);
	        if (success) {
	            top.success("[SUCCESS] Schedule saved successfully!");
	        } else {
	            top.error("[ERROR] Failed to save schedule");
	        }
	    });
	    
	    Button clearBtn = new Button("Clear All");
	    clearBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
	    clearBtn.setOnAction(e -> {
	        account.getBasket().clear();
	        BasketHandler.clearSavedBasket(account);
	        middle.refresh(account);
	        top.updateSched(account);
	        top.success("[SUCCESS] Basket cleared");
	    });
	    
	    HBox buttonBox = new HBox(15, saveBtn, clearBtn);
	    buttonBox.setAlignment(Pos.CENTER);
	    buttonBox.setPadding(new Insets(10));
	    
	    // =========== Layout ===========
	    layout = new VBox(15, buttonBox, top.getNode(), middle.getNode(), bottom.getNode());
	    layout.setAlignment(Pos.TOP_CENTER);
	    layout.setPadding(new Insets(20,40,20,40));
	    layout.setStyle("-fx-background-color: white;");
	    layout.setSpacing(25);
	   
	    ScrollPane scroll = new ScrollPane(layout);
	    scroll.setFitToWidth(true);
	    scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	    scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
	    scroll.setStyle("-fx-background-color: #f7f8f7;");
	    
	    root = new StackPane(scroll);
	    root.setPrefSize(width, height);
	    return root;
	}
}
