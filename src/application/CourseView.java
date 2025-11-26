package application;

//Import Packages
import bases.*;
import fileHandlers.*;
import javafx.animation.TranslateTransition;
// JavaFX Imports
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
// Java Imports
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseView {
		
	
	public static Scene viewCourse(double width, double height, Stage mainStage, Account account) {
		System.out.println("Entered CourseView.viewCourse()");
		// =========== TOP BAR ===========
		ImageView brand = new ImageView(Login.class.getResource("/assets/brand.png").toExternalForm());
		double ogWidth = brand.getImage().getWidth();
		brand.setPreserveRatio(true); brand.setFitWidth(ogWidth * 0.05);
		
		// Make menu button
		ImageView borgir = new ImageView(Login.class.getResource("/assets/menubutton.png").toExternalForm());
		borgir.setPreserveRatio(true); borgir.setFitWidth(ogWidth * 0.02);
		Button menuButton = new Button();
		menuButton.setGraphic(borgir);
		menuButton.setStyle("-fx-background-color: transparent;");
		
		HBox topBar = new HBox(20,menuButton,brand);
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setPadding(new Insets(0, 0, 0, 20));
		topBar.setPrefHeight(50);
		topBar.setStyle("-fx-background-color: #eaefdb;");
		
		// =========== SIDE BAR ===========
		VBox sidebar = new VBox(10);
		sidebar.setPadding(new Insets(30,0,0,0));
		sidebar.getStyleClass().add("sidebar");
		sidebar.setTranslateX(-140);
		
		Button dash = new Button("DASHBOARD");
		Button planner = new Button("PLANNER");
		Button course = new Button("COURSES");
		
		dash.setOnAction(e -> {
    		mainStage.setScene(Smi_Dashboard.createDash(width, height, mainStage, account));
    		});


		dash.getStyleClass().add("sidebutton");
		planner.getStyleClass().add("sidebutton");
		course.getStyleClass().add("sidebutton");

		sidebar.getChildren().addAll(dash, planner, course);
		sidebar.setAlignment(Pos.TOP_CENTER);

		// =========== ANIMATION ===========
		final boolean[] visible = {false};
		menuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
     		public void handle(MouseEvent arg0) {
     			TranslateTransition slide = new TranslateTransition(Duration.millis(250), sidebar);
     			if(!visible[0]) slide.setToX(0);
     			else slide.setToX(-(sidebar.getWidth()));
     			slide.play();
     			visible[0] = !visible[0];
     		}
     	});
		
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
		
		
		// =========== LAYOUT ===========
        HBox search = new HBox(10, codeSearch, titleSearch, courseFilter);
        search.setAlignment(Pos.TOP_LEFT);
        
        VBox tableSearch = new VBox(search, table);
        tableSearch.setAlignment(Pos.TOP_CENTER);
        tableSearch.getStyleClass().add("login-box");
        
        StackPane content = new StackPane(tableSearch);
        content.setAlignment(Pos.CENTER);
		content.setPadding(new Insets(5, 100, 10, 100));
        
		BorderPane broot = new BorderPane();
		broot.setTop(topBar);
		broot.setLeft(sidebar); 
		broot.setCenter(tableSearch);
		
        StackPane root = new StackPane(broot);
        root.getStyleClass().add("welcome_bg");

        Scene scene = new Scene(root, width, height);
        Platform.runLater(() -> scene.getRoot().requestFocus());
        scene.getStylesheets().add(Smi_Dashboard.class.getResource("application.css").toExternalForm());
        return scene;
    }
	
	
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
