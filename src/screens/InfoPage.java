package screens;

import application.Fonts;
// Package Imports
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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class InfoPage {
    private StackPane root;
    private VBox layout = new VBox();

    public StackPane showInfo(double width, double height, Account account) {
    	// =========== INTRODUCTION ===========
		Text hello = new Text("Hello, " + account.getFirstName().toUpperCase() + 
				" " + account.getLastName().toUpperCase());
		hello.setFont(Fonts.loadDotemp(40));
		hello.setFill(Color.web("#4E7711"));

		Text introText = new Text("Welcome to the Scheduling & Management for the Institute of Computer Science!\r\n"
				+ "This application helps you plan your classes for the First Semester of A.Y. 2025–2026 in an easy and organized way.\r\n"
				+ "\r\n"
				+ "On this platform, you can create an account, log in, view your weekly calendar, and manage the courses allowed in your degree program. "
				+ "You can add, edit, or delete courses, and the schedule will highlight the time slots for you. "
				+ "The system also shows details about each course to help you decide better. ");
				
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
		intro.setPadding(new Insets(10, 100, 0, 100));
		
		// =========== ABOUT ===========
		Text ab = new Text("About smiCS");
		ab.setFont(Fonts.loadDotemp(40));
		ab.setFill(Color.web("#4E7711"));
		
		// Paragraph 1
		Text abText1 = new Text("SMICS is designed to make your course planning simple, convenient, and " + 
				"easy. SMICS have three (3) tabs — Dashboard, Planner, and Courses. In this section, you will" + 
				" learn the function of each tab to guide you for a better navigating experience in this portal. ");
		
		TextFlow aboutPara1 = new TextFlow(abText1);
		aboutPara1.setPadding(new Insets(20, 20, 5, 20));
		aboutPara1.setMaxWidth(width);
		aboutPara1.getStyleClass().add("paragraph-style");
		
		// Paragraph 2
		Text abText2 = new Text("The Dashboard Tab is where you are right now. It contains the the guide on how " + 
				"you can navigate this Portal. It provides detailed information on each tab that you can you will " + 
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
		about.setPadding(new Insets(10, 100, 0, 100));
		
		// =========== CREDITS ===========
		Text cred = new Text("Behind the Scenes");
		cred.setFont(Fonts.loadDotemp(40));
		cred.setFill(Color.web("#4E7711"));
		
		// Paragraph 1 - cred
		Text credText1 = new Text("This Portal is created by the group JELlyAce, students of the CMSC 22 " + 
				"UV-5L of the University of the Philippines Los Baños (UPLB). ");
		
		TextFlow credPara1 = new TextFlow(credText1);
		credPara1.setPadding(new Insets(20, 20, 5, 20));
		credPara1.setMaxWidth(width);
		credPara1.getStyleClass().add("paragraph-style");
		
		// References
		Text credRef = new Text("References");
		credRef.setFont(Fonts.loadDotemp(40));
		credRef.setFill(Color.web("#4E7711"));
		
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
		credit.setPadding(new Insets(10, 100, 0, 100));

        // =========== Layout ===========
		layout = new VBox(intro, about ,credit);
        layout.setPadding(new Insets(20,50,20,50));
        layout.setStyle("-fx-background-color: #f7f8f7;");
        layout.setAlignment(Pos.CENTER);	
        layout.setSpacing(25);

        // Make scrollpane but only vertical
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: #f7f8f7;");

        root = new StackPane(scrollPane);
        root.setPrefSize(width, height);
        return root;
    }
}
