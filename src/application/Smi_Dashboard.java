package application;

//Package imports
import bases.Account;

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
		ImageView borgir = new ImageView(new Image("/assets/menubutton.png"));
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
		
			course.setOnAction(e -> {
				System.out.println("Courses button clicked!");
	    		mainStage.setScene(CourseView.viewCourse(width, height, mainStage, account));
	    		});

		dash.getStyleClass().add("sidebutton");
		planner.getStyleClass().add("sidebutton");
		course.getStyleClass().add("sidebutton");

		sidebar.getChildren().addAll(dash, planner, course);
		sidebar.setAlignment(Pos.TOP_CENTER);
		
		// =========== INTRODUCTION ===========
		Text hello = new Text("Hello, " + account.getFirstName().toUpperCase() + 
				" " + account.getLastName().toUpperCase() + "!");
		hello.getStyleClass().add("hello-style");
		
		Text introText = new Text("Welcome to SMICS Course Planning Portal, your glowing registration assistant! "); 
				/**+ "faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis " + 
				"convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus " + 
				"nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. " + 
				"Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia " +
				"nostra inceptos himenaeos.");**/
				
		TextFlow introParagraph = new TextFlow(introText);
		introParagraph.setPadding(new Insets(10, 20, 10, 20));
		introParagraph.setMaxWidth(width);
		introParagraph.getStyleClass().add("paragraph-style");
		
		VBox introBox = new VBox(20, hello, introParagraph);
		introBox.setAlignment(Pos.TOP_LEFT);
		introBox.setPadding(new Insets(25, 40, 0, 50));
		introBox.setMaxWidth(width);
		introBox.getStyleClass().add("login-box");
		
		StackPane intro = new StackPane(introBox);
		intro.setAlignment(Pos.CENTER);
		intro.setPadding(new Insets(25, 250, 0, 250));
		
		// =========== ABOUT ===========
		Text ab = new Text("ABOUT");
		ab.getStyleClass().add("hello-style");
		
		// Paragraph 1
		Text abText1 = new Text("SMICS is designed to make your course planning simple, convenient, and " + 
				"easy. SMICS have three (3) tabs — Dashboard, Planner, and Courses. In this section, you will " + 
				"learn the function of each tab to guide you for a better navigating experience in this portal. ");
		
		TextFlow aboutPara1 = new TextFlow(abText1);
		aboutPara1.setPadding(new Insets(20, 20, 5, 20));
		aboutPara1.setMaxWidth(width);
		aboutPara1.getStyleClass().add("paragraph-style");
		
		// Paragraph 2
		Text abText2 = new Text("The Dashboard Tab is where you are right now. It contains the the guide on how " + 
				"you can navigate this Portal. It provides detailed information on each tab that you will " + 
				"encounter. Furthermore, in the Credits Section, you will find the information about the creators " + 
				"and developers of this Portal. It also contains the references used, such as images or other " + 
				"obtained from the web");
		
		TextFlow aboutPara2 = new TextFlow(abText2);
		aboutPara2.setPadding(new Insets(5, 20, 5, 20));
		aboutPara2.setMaxWidth(width);
		aboutPara2.getStyleClass().add("paragraph-style");
		
		// Paragraph 3
		Text abText3 = new Text("The Planner Tab is where you can create your planner. You can only plan courses that " + 
				"are offered during this coming semester and offered within your degree program. You can add, delete, " + 
				"and edit courses in your planner. Your added courses are automatically added to your calendar to see " + 
				"your planned schedule. ");
		
		TextFlow aboutPara3 = new TextFlow(abText3);
		aboutPara3.setPadding(new Insets(5, 20, 5, 20));
		aboutPara3.setMaxWidth(width);
		aboutPara3.getStyleClass().add("paragraph-style");
				
		// Paragraph 4
		Text abText4 = new Text("The Courses Tab is where you can see all courses offered by the Institute of Computer " + 
				"Science (ICS). You can filter the the courses you want to see to a specific degree program. You can " + 
				"also search for a specific course that you want to find  using its course code. Aside from this, the " + 
				"courses that you will see includes its course code, course title, number of units, and description. This " +
				"is to provide you a more detailed information about the course you are planning to take to help you plan better.");
		
		TextFlow aboutPara4 = new TextFlow(abText4);
		aboutPara4.setPadding(new Insets(5, 20, 10, 20));
		aboutPara4.setMaxWidth(width);
		aboutPara4.getStyleClass().add("paragraph-style");
		
		VBox aboutBox = new VBox(50, ab, aboutPara1, aboutPara2, aboutPara3, aboutPara4);
		aboutBox.setAlignment(Pos.TOP_LEFT);
		aboutBox.setPadding(new Insets(10, 40, 10, 50));
		aboutBox.setMaxWidth(width);
		aboutBox.setSpacing(10);
		aboutBox.getStyleClass().add("login-box");
		
		StackPane about = new StackPane(aboutBox);
		about.setAlignment(Pos.CENTER);
		about.setPadding(new Insets(10, 250, 0, 250));
		
		// =========== CREDITS ===========
		Text cred = new Text("CREDITS");
		cred.getStyleClass().add("hello-style");
		
		// Paragraph 1 - cred
		Text credText1 = new Text("This Portal is created by the group JELlyAce, students of the CMSC 22 " + 
				"UV-5L of the University of the Philippines Los Baños (UPLB). They are Jodi Antonette Calleja, " + 
				"Maria Eliza Gevaña, and Lian Francine Hidalgo." ); 
						
		TextFlow credPara1 = new TextFlow(credText1);
		credPara1.setPadding(new Insets(20, 20, 5, 20));
		credPara1.setMaxWidth(width);
		credPara1.getStyleClass().add("paragraph-style");
		
		// References
		Text credRef = new Text("References");
		credRef.getStyleClass().add("hello-style");
		
		TextFlow ref = new TextFlow(credRef);
		ref.setPadding(new Insets(20, 20, 5, 20));
		 
		Text credText2 = new Text("Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque " + 
				"faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis " + 
				"convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus " + 
				"nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. " + 
				"Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia " + 
				"nostra inceptos himenaeos.");
		
		TextFlow credPara2 = new TextFlow(credText2);
		credPara2.setPadding(new Insets(5, 20, 10, 20));
		credPara2.setMaxWidth(width);
		credPara2.getStyleClass().add("paragraph-style");

		VBox creditBox = new VBox(50, cred, credPara1, ref, credPara2);
		creditBox.setAlignment(Pos.TOP_LEFT);
		creditBox.setPadding(new Insets(25, 40, 0, 50));
		creditBox.setMaxWidth(width);
		creditBox.setSpacing(10);
		creditBox.getStyleClass().add("login-box");
		
		StackPane credit = new StackPane(creditBox);
		credit.setAlignment(Pos.CENTER);
		credit.setPadding(new Insets(10, 250, 0, 250));
		
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
		VBox content = new VBox(intro, about ,credit);
		content.setAlignment(Pos.CENTER);	
		content.setSpacing(25);
		
		ScrollPane scroll = new ScrollPane(content);
		scroll.setFitToWidth(true);
		
		
		BorderPane broot = new BorderPane();
		broot.setTop(topBar);
		broot.setLeft(sidebar); 
		broot.setCenter(scroll);
		
        StackPane root = new StackPane(broot);
        //root.setPadding(new Insets(10,200,0,200));
        root.getStyleClass().add("welcome_bg");

        Scene scene = new Scene(root, width, height);
        Platform.runLater(() -> scene.getRoot().requestFocus());
        scene.getStylesheets().add(Smi_Dashboard.class.getResource("application.css").toExternalForm());
        return scene;
    }
}


