package application;

// FX Imports
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

// Java Imports
import java.util.ArrayList;
import javafx.util.Duration;

public class Login {
	public static Scene welcomeShow(double width, double height, Stage mainStage, ArrayList<Account> accounts) {
		// =========== TOP BAR ===========
		ImageView brand = new ImageView(Login.class.getResource("/assets/brand.png").toExternalForm());
		double ogWidth = brand.getImage().getWidth();
		brand.setPreserveRatio(true); brand.setFitWidth(ogWidth * 0.04);
		
		HBox topBar = new HBox(brand);
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setPadding(new Insets(0,0,0,20));
		topBar.setPrefHeight(50);
		topBar.setStyle("-fx-background-color: #eaefdb;");
		
		// =========== LOGIN BOX ===========
		Text login = new Text("Sign In");
		login.getStyleClass().add("login-font");
		login.setFill(Color.web("#a6be5d"));
        
		Region spacer = new Region();
		spacer.setPrefHeight(20);
		
		TextField email = new TextField();
        email.setPromptText("Email Address");
        email.getStyleClass().add("login-textfields");
        
        // Make a viewable and nonviewable password field
        PasswordField password = new PasswordField ();
        password.setPromptText("Password");
        password.getStyleClass().add("login-textfields");
        
        TextField visiblePassword = new TextField();
        visiblePassword.setPromptText("Password");
        visiblePassword.setVisible(false);
        visiblePassword.getStyleClass().add("login-textfields");
        
        // Synchronize the two password fields + get field visibility
        password.textProperty().bindBidirectional(visiblePassword.textProperty());
        StackPane passwordStack = new StackPane(password, visiblePassword);
        
        CheckBox showPassword = new CheckBox("Show Password");
        showPassword.setOnAction(e -> {
            boolean show = showPassword.isSelected();
            visiblePassword.setVisible(show);
            password.setVisible(!show);
        });
        showPassword.getStyleClass().add("showpass-style");
        
        // Password VBox
        VBox passbox = new VBox(8,passwordStack,showPassword);
        
        // =========== SIGN IN FEATURES ===========
        Image unhover = new Image(Login.class.getResource("/assets/normal_button.png").toExternalForm());
        Image hover = new Image(Login.class.getResource("/assets/Hovered_sign.png").toExternalForm());
        
        ImageView buttonIcon = new ImageView(unhover);
        buttonIcon.setFitWidth(40); 
		buttonIcon.setFitHeight(40);
        
        Button log = new Button();
		log.setGraphic(buttonIcon);
        log.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

		 // Hover effect
        log.setOnMouseEntered(e -> {
        	log.setTextFill(Color.web("#A6BE5D"));
        	buttonIcon.setImage(hover);   
        }); 
        log.setOnMouseExited(e -> {
        	log.setTextFill(Color.web("#FFFFFF"));
        	buttonIcon.setImage(unhover); 
        });
        
 		log.setOnMouseClicked(new EventHandler<MouseEvent>() {
 			public void handle(MouseEvent arg0) {
 				boolean found = false;
 				for(Account acc : accounts) {
 					// User is found and password matches account
 					if( acc.getEmailAddress().equals(email.getText()) && 
 						acc.getPassword().equals(password.getText())) {
 						found = true;
 						
 						Scene successScene = loginSuccess(width,height,mainStage,acc,topBar);
 						mainStage.setScene(successScene);
 						break;
 					}
 				}
 				
 				if(!found) {
// 					NotAuthenticated err = new NotAuthenticated();
// 					err.start(logStage);
 				}
 			}
 		});
        
        log.getStyleClass().add("login-button");
        log.setMaxWidth(Double.MAX_VALUE);
        
        Text normal = new Text("New Student?");
        Hyperlink clickable = new Hyperlink("Create an Account.");
        clickable.getStyleClass().add("clickable-style");
        TextFlow flow = new TextFlow(normal, clickable);
        flow.getStyleClass().add("textflow-custom");
        
        VBox bottomE = new VBox(7,log,flow);
        VBox.setMargin(bottomE, new Insets(10, 0, 0, 0));
		
		VBox signin = new VBox(20,login,spacer,email,passbox,bottomE);
		signin.setTranslateX(150);
		signin.setAlignment(Pos.TOP_CENTER);
		signin.setPadding(new Insets(50,40,0,40));
		signin.getStyleClass().add("login-box");
		
		// =========== LOGO ===========
		ImageView logo = new ImageView(Login.class.getResource("/assets/SMICS_logo.png").toExternalForm());
		double logoWidth = logo.getImage().getWidth();
		logo.setPreserveRatio(true); logo.setFitWidth(logoWidth * 0.15);
		
		Text motto = new Text("Your glowing registration assistant");
		motto.getStyleClass().add("tagline-style");
		motto.setFill(Color.web("#9ba381"));
		
		VBox welcome = new VBox(10,logo,motto);
		welcome.setAlignment(Pos.CENTER);
		welcome.setPadding(new Insets(50,40,0,40));
		
		// =========== LAYOUT ===========
		StackPane signinLeft = new StackPane(signin);
		signinLeft.setPadding(new Insets(90, 0, 90, 10));
		
		StackPane logoRight = new StackPane(welcome);
		logoRight.setPadding(new Insets(10,100,120,0));
		
		BorderPane broot = new BorderPane();
		broot.setPrefSize(width, height);
		broot.setTop(topBar);
		broot.setLeft(signinLeft);
		broot.setRight(logoRight);
		
		StackPane root = new StackPane(broot);
		root.setPrefSize(width, height);
		root.setMinSize(width, height);
		root.setMaxSize(width, height);
        root.getStyleClass().add("welcome-bg");
        
        root.setOnMousePressed(e -> root.requestFocus());
        
		Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(Login.class.getResource("application.css").toExternalForm());
        Platform.runLater(() -> root.requestFocus());
		return scene;
	}
	
	public static Scene loginSuccess(double width, double height, Stage mainStage, Account account, HBox topBar) {
		// =========== LOGIN SUCCESS SCENE ===========
		ImageView suc = new ImageView(Login.class.getResource("/assets/login_success.png").toExternalForm());
		double ogWidth = suc.getImage().getWidth();
		suc.setPreserveRatio(true); suc.setFitWidth(ogWidth * 0.25);
		
		Text welcome = new Text("Login successful! Welcome, " + account.getFirstName() + "!");
		
		VBox success = new VBox(20,suc,welcome);
		success.setAlignment(Pos.CENTER);
		success.setPadding(new Insets(0,0,10,0));
		
		StackPane centerBox = new StackPane(success);
		centerBox.getStyleClass().add("success-style");
		centerBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		
		
		// =========== LAYOUT ===========
		BorderPane broot = new BorderPane();
		broot.setPrefSize(width, height);
		broot.setTop(topBar);
		broot.setCenter(centerBox);
		
		StackPane root = new StackPane(broot);
		root.setPrefSize(width, height);
		root.setMinSize(width, height);
		root.setMaxSize(width, height);
        root.getStyleClass().add("welcome-bg");
        
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(ev -> {
        	FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), root);
    		fadeOut.setFromValue(1); fadeOut.setToValue(0);
    		
    		fadeOut.setOnFinished(e ->{
    			Scene dashScene = Smi_Dashboard.createDash(width, height, mainStage, account);
    		    mainStage.setScene(dashScene);
    		});
    		fadeOut.play();
		});
        
        pause.play();
        
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().add(Login.class.getResource("application.css").toExternalForm());
	    return scene;
	}
}

// References:
// Button with Image and Text: https://o7planning.org/11091/javafx-button
// Password Field Visibility: https://stackoverflow.com/questions/67476878/how-to-toggle-javafx-passwordfield-text-visibility
// Adding icons to a textfield: https://stackoverflow.com/questions/13159156/adding-a-small-picture-on-the-right-side-of-textfield-with-css?
// Alignment fixes: https://jenkov.com/tutorials/javafx/vbox.html
// TextFlow: https://stackoverflow.com/questions/20984209/javafx-how-to-make-a-clickable-text
// Remove focus from textfields: https://stackoverflow.com/questions/29051225/remove-default-focus-from-textfield-javafx
// Fade Transition: https://genuinecoder.com/javafx-splash-screen-loading-screen/
// Reference for end of animation: https://www.tutorialspoint.com/javafx/javafx_sequential_transition.htm