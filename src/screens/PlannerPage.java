package screens;

// Package Imports
import application.AddCourse;
import bases.Account;
import planning.*;

// JavaFX Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
		// =========== PLANNER TAB ===========
		Planner_Sched top = new Planner_Sched(account);
		Planner_BasketView middle = new Planner_BasketView(account);
		Planner_SearchAdd bottom = new Planner_SearchAdd(account, top);
		
		bottom.setAddToBasket(o-> {
			boolean success = AddCourse.addCourse(account, o, top);
			
			if (success) {
				middle.refresh(account);
				top.updateSched(account);
				top.success("[SUCCESS] Successfully added " + o.getCode());
			}
		});
		
		middle.setRemoveToBasket(o -> {
			account.removeFromBasket(o.getCode().toString());					// MADE METHOD COMPATIBLE W/ ARGUMENT
			middle.refresh(account);
			top.updateSched(account);
			top.success("[SUCCESS] Successfully removed " + o.getCode());
			
		});
		
		// =========== Layout ===========
		layout = new VBox(15, top.getNode(), middle.getNode(), bottom.getNode());
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
