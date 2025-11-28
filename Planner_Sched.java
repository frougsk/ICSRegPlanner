package application;

import bases.*;
import fileHandlers.*;

import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.nio.file.Paths;
import java.util.*;


public class Planner_Sched {
	
	private StackPane root = new StackPane();
	private GridPane schedGrid = new GridPane();
	private final Map<String, StackPane> dayTime = new HashMap<>();
	private Text warning = new Text("-- All Clear --");
	
	
	public Planner_Sched(Account acc) {
		// =========== WARNING PANEL ===========
		Text warningLbl = new Text("Warning Panel");
		warningLbl.getStyleClass().add("hello-style");

		//=========== SCHEDULE GRID  ===========
		Text plan = new Text("Course Plan");
		plan.getStyleClass().add("hello-style");
		
		String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		
		for (int d = 0; d < days.length; d++) {
			Label dayLbl = new Label(days[d]);
			dayLbl.setAlignment(Pos.CENTER);
			//dayLbl.getStyleClass().add("hello-style");
			schedGrid.add(dayLbl, d+1, 0);
		}
		
		int row =1;
		for (int h = 7; h <= 19; h++) {
			Label hour = new Label(h + ":00");
			hour.setAlignment(Pos.CENTER);
			schedGrid.add(hour, 0, row);
			row++;
		}
		
		// Grid Layout
		for (int d = 0; d < days.length; d++) {
			for (int h = 7; h <= 19; h++) {
				StackPane cell = new StackPane();
				cell.setMinSize(100, 20);
				//cell.getStyleClass().add("course-table");
				schedGrid.add(cell,  d+1, row);
				String key = days[d] + "-" + (h*60);
				dayTime.put(key, cell);
			}
		}
		schedGrid.setGridLinesVisible(true);
		
		
		// =========== LAYOUT ===========
		schedGrid.setHgap(5);
		schedGrid.setVgap(5);
		schedGrid.setPadding(new Insets(10));
					
		VBox sched = new VBox(plan, schedGrid);
		sched.setAlignment(Pos.CENTER);
				
		HBox panel = new HBox(50, sched, warningLbl);
		panel.setAlignment(Pos.TOP_LEFT);
		panel.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(plan, Priority.ALWAYS);
		HBox.setHgrow(warningLbl, Priority.ALWAYS);
		panel.setPadding(new Insets(10, 75, 10, 75));
		panel.getStyleClass().add("login-box");
	    	   
	    root.getChildren().add(panel);
		
	    
	}
	public Node getNode() {return root; }
	
	
	public void error(String msg) {
		warning.setText(msg);
		warning.setFill(Color.DARKRED);
	}
	
	public void success(String msg) {
		warning.setText(msg);
	}
	
	
	// =========== GRID UPDATE ===========
	public void updateSched(Account a) {
		//schedGrid.getChildren().removeIf(node -> node.getStyle() != null);
		
		// Clears the schedule on the grid
		for (var node : schedGrid.getChildren()) {
			Integer col = GridPane.getColumnIndex(node);
			Integer row = GridPane.getRowIndex(node);
			if(col != null && col > 0 && row != null && row > 0) {
				if(node instanceof StackPane) {
					StackPane cell = (StackPane) node;
					cell.getChildren().clear();
				}
			}
		}
		
		LinkedHashMap<String, Offering> basket = a.getBasket();
		for(Map.Entry<String, Offering> e: basket.entrySet()) {
			Offering o = e.getValue();
			placeOffering(o);
		}
	}
		
	
	private void placeOffering(Offering o) {
		if (o.getDay() == null || o.getTime() == null) return;
		
		int col = switch(o.getDay().trim().toLowerCase()) {
		case "mon" -> 1;
		case "tues" -> 2;
		case "wed" -> 3;
		case "thurs" -> 4;
		case "fri" -> 5;
		case "sat" -> 6;
		default -> -1;
		};
		
		if (col == -1) return;
		
		int startH = Integer.parseInt(o.getTime().split("-")[0].split(":")[0]);
		int row = (startH - 7) + 1;
		
		Label blockLbl = new Label(o.getCode());
		blockLbl.setStyle("-fx-background-color: green; -fx-padding: 3;");
		
		schedGrid.add(blockLbl, col, row);
	}
	
	
	
}
