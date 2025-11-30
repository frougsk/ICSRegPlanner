package screens;

// Package Imports
import bases.Course;
import bases.Account;

// JavaFX Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import fileHandlers.CourseLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

// Java Imports
import java.util.ArrayList;

public class CoursePage {
	private StackPane root;
	private VBox layout = new VBox();
	
	public StackPane showCourse(double width, double height) {
		//=========== LOAD COURSES  ===========
		CourseLoader loader = new CourseLoader();
		ArrayList<Course> bs = loader.getBS();
		ArrayList<Course> master = loader.getMasters();
		ArrayList<Course> phd = loader.getPHD();
		ArrayList<Course> mit = loader.getMITS();
		
		ObservableList<Course> allCourse = FXCollections.observableArrayList();
		allCourse.addAll(bs);
		allCourse.addAll(master);
		allCourse.addAll(phd);
		allCourse.addAll(mit);
		
		FilteredList<Course> searchList = new FilteredList<>(allCourse);
		
		// =========== COURSE SEARCH ===========
		// Table View
		TableView<Course> table = new TableView<>(searchList);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getStyleClass().add("course-table");
        
        // Sets the table columns
        TableColumn<Course, String> colCode   = new TableColumn<>("Course Code");
        TableColumn<Course, String> colCName      = new TableColumn<>("Course Name");
        TableColumn<Course, String> colUnit  = new TableColumn<>("Units");
        TableColumn<Course, String> colDescrip = new TableColumn<>("Description");
        TableColumn<Course, String> colType   = new TableColumn<>("Degree Program");
        
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        // colCode.setMinWidth(50); colCode.setMaxWidth(50);
		 colCode.setPrefWidth(120);
		 
		 colCName.setCellValueFactory(new PropertyValueFactory<>("CName"));
		 //colTitle.setMinWidth(100); colTitle.setMaxWidth(100);
		 colCName.setPrefWidth(250);
		 
		 colUnit.setCellValueFactory(new PropertyValueFactory<>("units"));
		// colUnit.setMinWidth(30); colUnit.setMaxWidth(30);
		 colUnit.setPrefWidth(60);
		 
		 colDescrip.setCellValueFactory(new PropertyValueFactory<>("desc"));
		 //colDescrip.setMinWidth(150); colDescrip.setMaxWidth(150);
		 colDescrip.setPrefWidth(500);
		 
		 colType.setCellValueFactory(new PropertyValueFactory<>("type"));
		 //colType.setMinWidth(30); colType.setMaxWidth(30);
		 colType.setPrefWidth(180);
        
        table.getColumns().addAll(colCode, colCName, colUnit, colDescrip, colType);
       
        // Search
		ComboBox<String> courseFilter = new ComboBox<>();
		courseFilter.getItems().addAll("All Degree Program", Course.BSCS, Course.MASTER, Course.PHD, Course.MIT);
		courseFilter.setValue("All Degree Program");
		courseFilter.getStyleClass().add("combo-box");
		
		TextField codeSearch = new TextField();
		codeSearch.setPromptText("Course Code");
		codeSearch.getStyleClass().add("login-textfields");
		
		TextField titleSearch = new TextField();
		titleSearch.setPromptText("Course Name");
		titleSearch.getStyleClass().add("login-textfields");
		titleSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			searchCourse(searchList, codeSearch.getText(), titleSearch.getText(), courseFilter.getValue(), table);
			});
		
		codeSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			searchCourse(searchList, codeSearch.getText(), titleSearch.getText(), courseFilter.getValue(), table);
			});
		
		courseFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
			searchCourse(searchList, codeSearch.getText(), titleSearch.getText(), courseFilter.getValue(), table);
			});
		
		// =========== Layout ===========
		HBox search = new HBox(10, codeSearch, titleSearch, courseFilter);
        search.setAlignment(Pos.TOP_LEFT);
        
        layout = new VBox(search, table);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("login-box");
        layout.setPadding(new Insets(20,140,20,100));
        layout.setStyle("-fx-background-color: #f7f8f7;");
        layout.setSpacing(25);
        
        root = new StackPane(layout);
        root.setPrefSize(width, height);
        return root;

	}
	
	// Helper Method: Returns matched course information
	static void searchCourse(FilteredList<Course> searchList, String code, String title, String degProg, TableView<Course> table) {
		searchList.setPredicate(c -> {
			String lower = code.toLowerCase();
			String lower2 = title.toLowerCase();
			
			boolean degprogMatched = degProg.equals("All Degree Program") || c.getType().equals(degProg);
			boolean codeMatched = code == null || code.isEmpty() || c.getCode().toLowerCase().contains(lower); 
			boolean titleMatched = title == null || title.isEmpty()	|| c.getCName().toLowerCase().contains(lower2);
			
			if (!degprogMatched && !codeMatched && !titleMatched) {
				table.setPlaceholder(new Label("No course found"));
			} 
			return degprogMatched && codeMatched && titleMatched;
		});
	}
}
