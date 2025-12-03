package planning;

// Package Imports
import bases.*;
import fileHandlers.*;
import application.AddCourse;

// JavaFX Imports
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

// Java Imports
import java.nio.file.Paths;
import java.util.*;

public class Planner_Sched {
	
	private StackPane root = new StackPane();
	private GridPane schedGrid = new GridPane();
	//private final Map<String, StackPane> dayTime = new HashMap<>();
	private Label warnings;
	
	
	public Planner_Sched(Account acc) {
		// =========== WARNING PANEL ===========
		Text warn = new Text("Warning Panel");
		warn.getStyleClass().add("hello-style");
		
		warnings = new Label("-- All Clear --");
		warnings.setWrapText(true);
		
		//=========== SCHEDULE GRID  ===========
		Text plan = new Text("Course Plan");
		plan.getStyleClass().add("hello-style");
		
		String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		
		for (int d = 0; d < days.length; d++) {
			Label dayLbl = new Label(days[d]);
			dayLbl.setAlignment(Pos.CENTER);
			schedGrid.add(dayLbl, d+1, 0);
		}
		
		int row =1;
		for (int h = 7; h <= 19; h++) {
			Label hour = new Label(h + ":00");
			hour.setAlignment(Pos.CENTER);
			hour.setMinWidth(40);
			schedGrid.add(hour, 0, row);
			row++;
		}
		
		// Grid Layout
		row = 1;
		for (int h = 7; h <= 19; h++) {
			for (int d = 0; d < days.length; d++) {
				StackPane cell = new StackPane();
				cell.setMinSize(100, 20);
				//cell.getStyleClass().add("course-table");
				schedGrid.add(cell,  d+1, row);
				//String key = days[d] + "-" + (h*60);
				//dayTime.put(key, cell);
			}
			row++;
		}
		schedGrid.setGridLinesVisible(true);
		
		
		// =========== LAYOUT ===========
		schedGrid.setHgap(5);
		schedGrid.setVgap(5);
		schedGrid.setPadding(new Insets(10));
		
		VBox warnPanel = new VBox(10, warn, warnings);
		warnPanel.setAlignment(Pos.TOP_LEFT);
		warnPanel.setMinWidth(250);
		
		VBox sched = new VBox(10, plan, schedGrid);
		sched.setAlignment(Pos.CENTER);
				
		HBox panel = new HBox(50, sched, warnPanel);
		panel.setAlignment(Pos.TOP_CENTER);
		HBox.setHgrow(sched, Priority.ALWAYS);
		panel.setPadding(new Insets(10, 75, 10, 75));
		panel.getStyleClass().add("login-box");
	    	   
		root.getChildren().add(panel);
		
		updateSched(acc);		
	    
	}
	public Node getNode() {return root; }
	
	// =========== WARNING PROMPTS ===========
	public void error(String msg) {
		warnings.setText(msg);
		warnings.setTextFill(Color.DARKRED);
	}
	
	public void success(String msg) {
		warnings.setText(msg);
	}
	
	public void setWarning(String text) {
		warnings.setText(text == null || text.trim().isEmpty() ? "-- All Clear --": text);
	}
	
	
	// =========== GRID UPDATE ===========
	public void updateSched(Account a) {	
		for (Node node : schedGrid.getChildren()) {
			Integer col = GridPane.getColumnIndex(node);
			Integer row = GridPane.getRowIndex(node);
			if(col != null && col > 0 && row != null && row > 0) {
				if(node instanceof StackPane) {
					StackPane cell = (StackPane) node;
					cell.getChildren().clear();
				}
			}
		}
		
		// Displace all courses from basket
		//LinkedHashMap<String, Offering> basket = a.getBasket();
		for(Offering o: a.getBasket().values()) {
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
		
		try {
			String[] times = o.getTime().split("-");
			String startTimeStr = times[0];
			String endTimeStr = times[1];
		
			int startH = Integer.parseInt(startTimeStr.split(":")[0]);
			int startM = Integer.parseInt(startTimeStr.split(":")[1]);
			
			int startMinutes = AddCourse.toMinutes(startTimeStr);
			int endMinutes = AddCourse.toMinutes(endTimeStr);
			
			if (endMinutes <= startMinutes) {
                endMinutes = startMinutes + 60; 
            }
			
			int startRow = (startH - 7) + 1; // Row 1 is 7:00, Row 2 is 8:00...
            double durationMinutes = endMinutes - startMinutes;
            
            int rowSpan = (int) Math.ceil(durationMinutes / 60.0);
            // Checks boundaries 
            if (startRow < 1 || startRow + rowSpan - 1 > 13) {
                System.err.println("Course " + o.getCode() + " time span exceeds planner bounds.");
                rowSpan = Math.min(rowSpan, 13 - startRow + 1);
                if (rowSpan <= 0) return; 
            }
			
            VBox blockContent = new VBox(2);
            Label codeLabel = new Label(o.getCode() + " " + o.getSection());
            Label roomLabel = new Label(o.getRoom() + " (" + o.getTime() + ")");
            
            codeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");
            roomLabel.setStyle("-fx-font-size: 9px;");
            
            blockContent.getChildren().addAll(codeLabel, roomLabel);
            blockContent.setAlignment(Pos.TOP_CENTER);
            blockContent.setPadding(new Insets(3));
            
            StackPane block = new StackPane(blockContent);
            block.setStyle("-fx-background-color: #4682B4; -fx-border-color: green; -fx-border-width: 1;");
            block.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            
            // Controls span
            GridPane.setConstraints(block, col, startRow, 1, rowSpan);
            
            // Add the block to the grid
            schedGrid.getChildren().add(block);
			
		} catch (Exception e) {
			System.out.println("Error parsing time for course " + o.getCode() + ": " + e.getMessage());
		}	
	}
}
