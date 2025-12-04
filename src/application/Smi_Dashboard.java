package application;

//Package Imports
import bases.Account;
import screens.*;

// JavaFX Imports
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Smi_Dashboard {
	public static Scene createDash(double width, double height, Stage mainStage, Account account) {
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
		sidebar.setPadding(new Insets(10,0,0,0));
		sidebar.getStyleClass().add("sidebar");
		sidebar.setTranslateX(-140);
		
		Button dash = new Button("DASHBOARD");
		Button planner = new Button("PLANNER");
		Button course = new Button("COURSES");
		Button profile = new Button("PROFILE");

		dash.getStyleClass().add("sidebutton");
		planner.getStyleClass().add("sidebutton");
		course.getStyleClass().add("sidebutton");
		profile.getStyleClass().add("sidebutton");
		
		Region spacer = new Region();
		spacer.setPrefHeight(400);

		sidebar.getChildren().addAll(dash, planner, course, profile);
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

		// =========== LAYOUT ===========
		StackPane showArea = new StackPane();
		
		BorderPane broot = new BorderPane();
		broot.setTop(topBar);
		broot.setLeft(sidebar); 
		broot.setCenter(showArea);
		
        StackPane root = new StackPane(broot);
        root.getStyleClass().add("welcome_bg");
        
        // Link pages
        InfoPage info = new InfoPage();
        PlannerPage plan = new PlannerPage();
        CoursePage courses = new CoursePage();
        ProfilePage myPage = new ProfilePage();
        
        // Show dash page by default
        showArea.getChildren().setAll(info.showInfo(width, height, account));
        
        // Button actions
        dash.setOnAction(e -> showArea.getChildren().setAll(info.showInfo(width, height, account)));
        planner.setOnAction(e -> showArea.getChildren().setAll(plan.showPlanner(width, height, account)));
        course.setOnAction(e -> showArea.getChildren().setAll(courses.showCourse(width, height)));
        profile.setOnAction(e -> showArea.getChildren().setAll(myPage.showProfile(width, height, account)));

        Scene scene = new Scene(root, width, height);
        Platform.runLater(() -> scene.getRoot().requestFocus());
        scene.getStylesheets().add(Smi_Dashboard.class.getResource("application.css").toExternalForm());
        return scene;
    }
}
