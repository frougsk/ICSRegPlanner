package application;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Smi_Dashboard {
	public static Scene createDash(double width, double height, Stage mainStage, Account account) {
		// =========== TOP BAR ===========
		ImageView brand = new ImageView(new Image("file:assets/smiCS_8bit.png"));
		double ogWidth = brand.getImage().getWidth();
		brand.setPreserveRatio(true); brand.setFitWidth(ogWidth * 0.05);
		
		// Make menu button
		ImageView borgir = new ImageView(new Image("file:assets/menubutton.png"));
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
		
		Button manual = new Button("HOME");
		Button settings = new Button("USER MANUAL");
		Button about = new Button("COMPUTE");

		manual.getStyleClass().add("sidebutton");
		settings.getStyleClass().add("sidebutton");
		about.getStyleClass().add("sidebutton");

		sidebar.getChildren().addAll(manual, settings, about);
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
		BorderPane broot = new BorderPane();
		broot.setTop(topBar);
		broot.setLeft(sidebar); 
		
        StackPane root = new StackPane(broot);
        root.getStyleClass().add("welcome_bg");

        Scene scene = new Scene(root, width, height);
        Platform.runLater(() -> scene.getRoot().requestFocus());
        scene.getStylesheets().add(Smi_Dashboard.class.getResource("application.css").toExternalForm());
        return scene;
    }
}
