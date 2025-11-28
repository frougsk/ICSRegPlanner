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

public class Planner {
	
	private static ArrayList<Offering> allOfferings = new ArrayList<>();
	private static final Path BASKET_CSV = Path.of("accounts_data", "basket.csv");
	
	public static Scene makeCoursePlan(double width, double height, Stage mainStage, Account account) {
		// =========== TOP BAR ===========
		ImageView brand = new ImageView(Planner.class.getResource("/assets/brand.png").toExternalForm());
		double ogWidth = brand.getImage().getWidth();
		brand.setPreserveRatio(true); brand.setFitWidth(ogWidth * 0.05);
		
		// Make menu button
		ImageView borgir = new ImageView(Planner.class.getResource("/assets/menubutton.png").toExternalForm());
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
		Button subject = new Button("COURSES");
		
		// Directs the User to different tabs
		dash.setOnAction(e -> {mainStage.setScene(Smi_Dashboard.createDash(width, height, mainStage, account)); });
		subject.setOnAction(e -> {mainStage.setScene(CourseView.viewCourse(width, height, mainStage, account)); });
		
		dash.getStyleClass().add("sidebutton");
		planner.getStyleClass().add("sidebutton");
		subject.getStyleClass().add("sidebutton");

		sidebar.getChildren().addAll(dash, planner, subject);
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
		
		
		// =========== ADD TO BASKET ===========

		// =========== BASKET ===========
		// =========== PLANNER TAB ===========
		Planner_Sched top = new Planner_Sched(account);
		Planner_BasketView middle = new Planner_BasketView(account);
		Planner_SearchAdd bottom = new Planner_SearchAdd(account);
		
		bottom.setAddToBasket(o-> {
			boolean success = AddCourse.addCourse(account, o);
			
			if(!success) {
				top.error("Cannot add course: " + o.getCode());
				return;
			}
			top.updateSched(account);
			middle.refresh(account);
			
			top.success("Successfully added " + o.getCode());
		});
		
		middle.setRemoveToBasket(o -> {
			account.removeFromBasket(o.getCode());
			middle.refresh(account);
			top.updateSched(account);
			top.success("Successfully removed " + o.getCode());
			
		});
		
		// =========== LAYOUT ===========
		VBox content = new VBox(15, top.getNode(), middle.getNode(), bottom.getNode());
		content.setAlignment(Pos.CENTER);	
		content.setSpacing(25);
		
		ScrollPane scroll = new ScrollPane(content);
		scroll.setFitToWidth(true);
		
		BorderPane broot = new BorderPane();
		broot.setTop(topBar);
		broot.setLeft(sidebar); 
		broot.setCenter(scroll);
		
        StackPane root = new StackPane(broot);
        root.getStyleClass().add("welcome_bg");

        Scene scene = new Scene(root, width, height);
        Platform.runLater(() -> scene.getRoot().requestFocus());
        scene.getStylesheets().add(Planner.class.getResource("application.css").toExternalForm());
        return scene;
    }
		


}
