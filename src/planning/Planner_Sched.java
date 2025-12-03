package planning;

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

import application.AddCourse;


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
				schedGrid.add(cell,  d+1, row);
			}
			row++;
		}
		
		// =========== LAYOUT ===========
		schedGrid.setGridLinesVisible(true);
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
		schedGrid.getChildren().removeIf(node -> 
        node instanceof StackPane && node.getUserData() != null && node.getUserData().equals("courseBlock")
		);
		
		// Displace all courses from basket
		for (Offering o : a.getBasket().values()) {
	        placeOffering(o);
	    }
	}
	
	// Helper Method: Place blocks on calendar
	private void placeOffering(Offering o) {
	    if (o.getDay() == null || o.getTime() == null) return;

	    // Day-to-Column mapping
	    Map<String, Integer> dayToCol = new HashMap<>();
	    dayToCol.put("mon", 1);
	    dayToCol.put("tues", 2);
	    dayToCol.put("wed", 3);
	    dayToCol.put("thurs", 4);
	    dayToCol.put("fri", 5);
	    dayToCol.put("sat", 6);

	    // Parse multiple day courses
	    List<String> days = parseDaysDynamic(o.getDay());

	    try {
	        String[] times = o.getTime().split("-");
	        String startTimeStr = times[0];
	        String endTimeStr = times[1];

	        // Convert start time
	        int startMinutes = convertToMinutes(startTimeStr);
	        int endMinutes = convertToMinutes(endTimeStr);

	        // Catch invalid or zero-length spans
	        if (endMinutes <= startMinutes) endMinutes += 12 * 60;

	        // Calculate grid rows
	        int startHour = startMinutes / 60;
	        int startRow = (startHour - 7) + 1;
	        int rowSpan = (int) Math.ceil((endMinutes - startMinutes) / 60.0);

	        // Bounds check
	        if (startRow < 1) startRow = 1;
	        if (startRow + rowSpan - 1 > 13) rowSpan = 13 - startRow + 1;
	        if (rowSpan <= 0) return;

	        // ===== BLOCK CONTENT CREATION  =====
	        VBox blockContent = new VBox(2);
	        Label codeLabel = new Label(o.getCode() + " " + o.getSection());
	        Label roomLabel = new Label(o.getRoom() + " (" + o.getTime() + ")");
	        codeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");
	        roomLabel.setStyle("-fx-font-size: 9px;");
	        blockContent.getChildren().addAll(codeLabel, roomLabel);
	        blockContent.setAlignment(Pos.TOP_CENTER);
	        blockContent.setPadding(new Insets(3));

	        // ===== PLACE BLOCK ON SCHED  =====
	        for (String day : days) {
	            Integer col = dayToCol.get(day.toLowerCase());
	            if (col != null) {
	                StackPane dayBlock = new StackPane(blockContent); // new StackPane per day
	                dayBlock.setStyle("-fx-background-color: #4682B4; -fx-border-color: green; -fx-border-width: 1;");
	                dayBlock.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	                dayBlock.setUserData("courseBlock");

	                GridPane.setConstraints(dayBlock, col, startRow, 1, rowSpan);
	                schedGrid.getChildren().add(dayBlock);
	                dayBlock.toFront();
	            }
	        }
	    }catch (Exception e) {
	        System.out.println("Error parsing time for course " + o.getCode() + ": " + e.getMessage());
	    }
	}

	// Helper Method: Multi-Day Parsing
	private List<String> parseDaysDynamic(String dayStr) {
	    List<String> days = new ArrayList<>();
	    if (dayStr == null) return days;

	    dayStr = dayStr.trim().toUpperCase();

	    // Ordered abbreviations (longest first)
	    List<String> abbrOrder = List.of("TH", "M", "T", "W", "F", "S");

	    // Map to full day names
	    Map<String, String> dayMap = Map.of(
	        "TH", "thurs",
	        "M", "mon",
	        "T", "tues",
	        "W", "wed",
	        "F", "fri",
	        "S", "sat"
	    );

	    int i = 0;
	    while (i < dayStr.length()) {
	        boolean matched = false;
	        for (String abbr : abbrOrder) {
	            if (dayStr.startsWith(abbr, i)) {
	                days.add(dayMap.get(abbr));
	                i += abbr.length();
	                matched = true;
	                break;
	            }
	        }
	        if (!matched) i++;
	    }
	    return days;
	}

	// Convert HH:MM to minutes
	private int convertToMinutes(String timeStr) {
	    timeStr = timeStr.trim();
	    String[] parts = timeStr.split(":");
	    int hour = Integer.parseInt(parts[0]);
	    int min = 0;  

	    // Check if there is a minute part in the split array
	    if (parts.length > 1) min = Integer.parseInt(parts[1]);

	    // Handle PM times
	    if (hour >= 1 && hour <= 6) hour += 12; // 1-6 PM
	    else if (hour == 12) hour = 12;         // noon
	    return hour * 60 + min;
	}
}
