package application;

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
import java.util.ArrayList;
import javafx.util.Duration;


public class CreateAccount {
	
	private static ArrayList<Account> accounts = new ArrayList<>();
	private static final String CSV_PATH =  "accounts_data/accounts.csv";

	public static Scene createAccount(double width, double height, Stage mainStage) {	
		// =========== CREATE ACCOUNT BOX  ===========
		ImageView sign = new ImageView(new Image("file:assets/signin_placeholder_icon.png"));
		double ogWidth = sign.getImage().getWidth();
		sign.setPreserveRatio(true); sign.setFitWidth(ogWidth * 0.03);
		
		Text newAccount = new Text("Create an Account");
		newAccount.getStyleClass().add("login-font");
		newAccount.setFill(Color.web("#a6be5d"));
		
		Region spacer = new Region();
		spacer.setPrefHeight(20);
		
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
		degree.getItems().addAll("BSCS","MSCS","PhD CS", "MIT");
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
        
        VBox passBox = new VBox(8, passwordStack, confirmStack, showPassword);	
        
		// =========== BUTTONS  ===========
		Button back = new Button("Back");
		
		Button create = new Button("Create");
		create.getStyleClass().add("create-button");
		//create.setPrefSize(50, 30);
		
		HBox buttons = new HBox(100, back, create);
		buttons.setAlignment(Pos.BASELINE_CENTER);
		
		// =========== BUTTONS FEATURES ===========
		back.setOnMouseClicked(e -> mainStage.setScene(Login.welcomeShow(width, height, mainStage, new java.util.ArrayList<>())));
		
		// Hover Effect
		create.setOnMouseEntered(e -> {
			create.setTextFill(Color.web("#A6BE5D"));
        	}); 
        create.setOnMouseExited(e -> {
        	create.setTextFill(Color.web("#FFFFFF"));
        	});
        
        // Conditions if inputs are sufficient
		create.setOnMouseClicked(e -> {
			// Checks if all fields except middle name is filled
			if(email.getText().isEmpty() || firstName.getText().isEmpty() || lastName.getText().isEmpty()
					 ||  password.getText().isEmpty() || confirmPw.getText().isEmpty() || degree.getValue()==null) {
				new Alert(Alert.AlertType.WARNING,"Fill all required fields").show();
				return;
			} else if (!password.getText().equals(confirmPw.getText())) {
				new Alert(Alert.AlertType.ERROR,"Password does not match").show();
				return;
			} else {
				Account a = new Account(email.getText(),firstName.getText(), middleName.getText(), 
						lastName.getText(), degree.getValue(), password.getText());
				accounts.add(a);
				saveAccount(a);
				new Alert(Alert.AlertType.INFORMATION,"Account Successfully Created").showAndWait();
				mainStage.setScene(Login.welcomeShow(width, height, mainStage, accounts));
			}
		});

		// =========== LAYOUT ===========
		HBox fmName = new HBox(10, firstName, middleName);
		fmName.setAlignment(Pos.CENTER);
		fmName.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(firstName, Priority.ALWAYS);
		HBox.setHgrow(middleName, Priority.ALWAYS);
		
		VBox signup = new VBox(15);
		signup.setPadding(new Insets(50,30,0,30));
		
		signup.setAlignment(Pos.CENTER);
		signup.getStyleClass().add("login-box");
		signup.getChildren().addAll(sign, newAccount, email, fmName,
				lastName, degree, password, confirmPw, passBox, buttons);
			
        StackPane root = new StackPane(signup);
        root.setPadding(new Insets(30, 450, 20, 450));
		root.setPrefSize(width, height);
		root.setMinSize(width, height);
		root.setMaxSize(width, height);
        root.getStyleClass().add("welcome-bg");
                     
		Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(Login.class.getResource("application.css").toExternalForm());
        Platform.runLater(() -> root.requestFocus());
		return scene;			
		
	}

	private static void saveAccount(Account a) {
		File file = new File(CSV_PATH);
		file.getParentFile().mkdirs();
		try(FileWriter fw = new FileWriter(file, true)){
			fw.write(a.toFile() + "\n");
			//for (Account a: accounts) p.println(a.toFile());
		} catch(IOException e) {e.printStackTrace();}
	}

}

