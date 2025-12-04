package application;

// Package imports
import bases.Account;

// JavaFX imports
import javafx.util.Duration;
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

//Java Imports
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;

public class CreateAccount {
	
	private static final String CSV_PATH = "accounts_data/accounts.csv";

	public static Scene createAccount(double width, double height, Stage mainStage, Scene loginScene, ArrayList<Account> accounts) {
	    // =========== CREATE ACCOUNT BOX  ===========
	    ImageView sign = new ImageView(Login.class.getResource("/assets/icon.png").toExternalForm());
	    sign.setPreserveRatio(true);
	    sign.setFitWidth(100);
	    
	    Text newAccount = new Text("Create an Account");
	    newAccount.getStyleClass().add("login-font");
	    newAccount.setFill(Color.web("#a6be5d"));
	    
	    Region spacer = new Region();
	    spacer.setPrefHeight(0);
	    
	    TextField email = new TextField();
	    email.setPromptText("Email Address");
	    email.getStyleClass().add("login-textfields");
	    
	    TextField firstName = new TextField();
	    firstName.setPromptText("First Name");
	    firstName.getStyleClass().add("login-textfields");
	    
	    TextField middleName = new TextField();
	    middleName.setPromptText("Middle Name (Optional)");
	    middleName.getStyleClass().add("login-textfields");
	    
	    TextField lastName = new TextField();
	    lastName.setPromptText("Last Name");
	    lastName.getStyleClass().add("login-textfields");
	    
	    ComboBox<String> degree = new ComboBox<>();
	    degree.getItems().addAll("Bachelor of Science in Computer Science","Master of Science in Computer Science","Doctor of Philosophy in Computer Science", "Master of Information Technology");
	    degree.setPromptText("Degree Program");
	    degree.setMaxWidth(Double.MAX_VALUE);
	    degree.getStyleClass().add("combo-box");
	    
	    // Make a viewable and nonviewable password field		
	    PasswordField password = new PasswordField();
	    password.setPromptText("Password");
	    password.getStyleClass().add("login-textfields");
	    
	    PasswordField confirmPw = new PasswordField();
	    confirmPw.setPromptText("Confirm Password");
	    confirmPw.getStyleClass().add("login-textfields");
	    
	    TextField visiblePassword = new TextField();
	    visiblePassword.setPromptText("Password");
	    visiblePassword.setVisible(false);
	    visiblePassword.setManaged(false);
	    visiblePassword.getStyleClass().add("login-textfields");
	    
	    TextField visibleConfirm = new TextField();
	    visibleConfirm.setPromptText("Confirm Password");
	    visibleConfirm.setVisible(false);
	    visibleConfirm.setManaged(false);
	    visibleConfirm.getStyleClass().add("login-textfields");
	    
	    // Synchronize the two password fields + get field visibility
	    password.textProperty().bindBidirectional(visiblePassword.textProperty());
	    confirmPw.textProperty().bindBidirectional(visibleConfirm.textProperty());
	    StackPane passwordStack = new StackPane(password, visiblePassword);
	    StackPane confirmStack = new StackPane(confirmPw, visibleConfirm);
	    
	    CheckBox showPassword = new CheckBox("Show Password");
	    showPassword.getStyleClass().add("showpass-style");        
	            
	    showPassword.setOnAction(e -> {
	        boolean show = showPassword.isSelected();
	        visiblePassword.setVisible(show);
	        password.setVisible(!show);
	        visibleConfirm.setVisible(show);
	        confirmPw.setVisible(!show);
	        
	        visiblePassword.setManaged(show);
	        password.setManaged(!show);
	        visibleConfirm.setManaged(show);
	        confirmPw.setManaged(!show);
	    });
	    
	    VBox passBox = new VBox(15, passwordStack, confirmStack, showPassword);	
	    
	    // =========== BUTTONS  ===========
	    Button back = new Button("Back");
	    back.getStyleClass().add("back-button");
	    back.setStyle("-fx-cursor: hand;");
	    
	    Button create = new Button("Create");
	    create.getStyleClass().add("create-button");
	    create.setStyle("-fx-cursor: hand;");
	    create.setPrefSize(50, 30);
	    
	    HBox buttons = new HBox(100, back, create);
	    buttons.setAlignment(Pos.BASELINE_CENTER);
	    
	    // =========== BUTTONS FEATURES ===========
	    back.setOnMouseClicked(e -> mainStage.setScene(loginScene));
	    
	    // Hover Effect
	    back.setOnMouseEntered(e -> back.setTextFill(Color.web("#A6BE5D"))); 
	    back.setOnMouseExited(e -> back.setTextFill(Color.web("#FFFFFF")));
	    
	    create.setOnMouseEntered(e -> create.setTextFill(Color.web("#A6BE5D"))); 
	    create.setOnMouseExited(e -> create.setTextFill(Color.web("#FFFFFF")));
	    
	    // Conditions if inputs are sufficient
	    create.setOnMouseClicked(e -> {
	        if(email.getText().isEmpty() || firstName.getText().isEmpty() || lastName.getText().isEmpty()
	                 ||  password.getText().isEmpty() || confirmPw.getText().isEmpty() || degree.getValue()==null) {
	            Notifier.missingInput();
	            return;
	        } else if (!password.getText().equals(confirmPw.getText())) {
	            Notifier.passwordNotMatch();
	            return;
	        } else {
	            // Map degree to short code
	            String programCode = mapDegreeToCode(degree.getValue());
	            
	            Account a = new Account(email.getText(), firstName.getText(), middleName.getText(), 
	                    lastName.getText(), programCode, password.getText());
	            
	            // ADD TO THE ACCOUNTS LIST (this was missing!)
	            accounts.add(a);
	            
	            // Save to CSV
	            saveAccount(a);
	            
	            Notifier.successfullyCreated();
	            mainStage.setScene(loginScene);
	        }
	    });

	    // =========== LAYOUT ===========
	    HBox fmName = new HBox(10, firstName, middleName);
	    fmName.setAlignment(Pos.CENTER);
	    fmName.setMaxWidth(Double.MAX_VALUE);
	    HBox.setHgrow(firstName, Priority.ALWAYS);
	    HBox.setHgrow(middleName, Priority.ALWAYS);

	    VBox createAndTitle = new VBox(10, sign, newAccount);
	    createAndTitle.setAlignment(Pos.CENTER);

	    VBox signup = new VBox(15);
	    signup.setPadding(new Insets(50,30,50,30));
	    signup.setAlignment(Pos.CENTER);
	    signup.getStyleClass().add("login-box");
	    signup.getChildren().addAll(createAndTitle, spacer, email, fmName,
	            lastName, degree, passBox, buttons);
	            
	    StackPane root = new StackPane(signup);
	    root.setPadding(new Insets(30, 450, 30, 450));
	    root.setPrefSize(width, height);
	    root.setMinSize(width, height);
	    root.setMaxSize(width, height);
	    // Use a blurred/darkened background effect
	    root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
	                     
	    Scene scene = new Scene(root, width, height);
	    scene.getStylesheets().add(Login.class.getResource("application.css").toExternalForm());
	    Platform.runLater(() -> root.requestFocus());
	    return scene;			
	}

	private static boolean saveAccount(Account a) {
	    try {
	        File file = new File(CSV_PATH);
	        
	        // Create parent directories if they don't exist
	        File parentDir = file.getParentFile();
	        if (parentDir != null && !parentDir.exists()) {
	            parentDir.mkdirs();
	        }
	        
	        // Check if file exists and needs header
	        boolean fileExists = file.exists();
	        boolean needsHeader = !fileExists || file.length() == 0;
	        
	        // Append to file (create if doesn't exist)
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
	            // Write header if file is new or empty
	            if (needsHeader) {
	                writer.write("Email,First Name,Middle Name,Last Name,Program,Password");
	                writer.newLine();
	            }
	            
	            // Write account data
	            writer.write(a.toFile());
	            writer.newLine();
	            writer.flush();
	            
	            System.out.println("Account saved successfully: " + a.getEmailAddress());
	            return true;
	        }
	        
	    } catch (IOException e) {
	        System.err.println("Error saving account: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	    
	}
	
	// Helper method to map full degree names to codes
	private static String mapDegreeToCode(String fullDegreeName) {
	    switch(fullDegreeName) {
	        case "Bachelor of Science in Computer Science":
	            return "BSCS";
	        case "Master of Science in Computer Science":
	            return "MSCS";
	        case "Doctor of Philosophy in Computer Science":
	            return "PhD CS";
	        case "Master of Information Technology":
	            return "MIT";
	        default:
	            return fullDegreeName;
	    }
	}
}
