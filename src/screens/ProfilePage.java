package screens;

// Java Imports
import java.nio.file.Paths;
import java.util.ArrayList;

// Package Imports
import application.Fonts;
import application.Login;
import bases.Account;
import fileHandlers.AccountLoader;

// JavaFX Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProfilePage {
	private StackPane root;
    private VBox layout = new VBox();
    
	public StackPane showProfile(double width, double height, Stage mainStage, Account account) {
		ImageView pfp = new ImageView(Login.class.getResource("/assets/icon.png").toExternalForm());
		pfp.setPreserveRatio(true);
		pfp.setFitWidth(150);
		
		Text display = new Text(account.getFirstName().toUpperCase() + " " + account.getLastName().toUpperCase());
		display.setFont(Fonts.loadDotemp(40));
		display.setFill(Color.web("#4E7711"));
		
		// =========== Info ===========
		// Name
		String fullName;
		if (account.getMiddleName().isEmpty()) fullName = account.getFirstName() + " " + account.getLastName();
		else fullName = account.getFirstName() + " " + account.getMiddleName() + " " + account.getLastName();

		Text name = new Text("Name:  " + fullName);
		name.getStyleClass().add("info-style");

		// Email
		Text email = new Text("Email:  " + account.getEmailAddress());
		email.getStyleClass().add("info-style");

		// Program
		String acc_prog = account.getProgram();
		String progStr;
		if (acc_prog.equalsIgnoreCase("BSCS")) {
		    progStr = "Bachelor of Science in Computer Science";
		} else if (acc_prog.equalsIgnoreCase("MSCS")) {
		    progStr = "Master of Science in Computer Science";
		} else if (acc_prog.equalsIgnoreCase("PhD CS")) {
		    progStr = "Doctor of Philosophy in Computer Science";
		} else {
		    progStr = "Master in Information Technology";
		}

		Text program = new Text("Program:  " + progStr);
		program.getStyleClass().add("info-style");
		
		VBox info = new VBox(10, name, email, program);
		info.setPadding(new Insets(0,0,0,200));
		info.setAlignment(Pos.TOP_LEFT);
		info.getStyleClass().add("info-box");
		
		// =========== Buttons ===========
		// Leads back to log in page
		AccountLoader accload = new AccountLoader();
		ArrayList<Account> accounts = accload.getAccounts(Paths.get("accounts_data/accounts.csv"));
		
		Button out = new Button("Sign Out");
		out.getStyleClass().add("extra-button");
		out.setOnAction(e -> mainStage.setScene(Login.welcomeShow(width, height, mainStage, accounts)));

		// Exits the program
		Button exit = new Button("Exit");
		exit.getStyleClass().add("extra-button");
		exit.setOnAction(e -> System.exit(0));
		
		HBox btn = new HBox(5, out, exit);
		btn.setAlignment(Pos.CENTER_RIGHT);

		// =========== Layout ===========
		Region moreSpace = new Region();
		moreSpace.setPrefHeight(30);
		
		layout = new VBox(10, pfp, display, info, moreSpace, btn);
        layout.setPadding(new Insets(20,200,20,200));
        layout.setStyle("-fx-background-color: #f7f8f7;");
        layout.setAlignment(Pos.TOP_CENTER);	
        layout.setSpacing(25);

        root = new StackPane(layout);
        root.setPrefSize(width, height);
        return root;
	}
}
