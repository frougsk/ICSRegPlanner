package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;

public class Notifier {

	public static void error() {
	    Stage popup = new Stage();
	    popup.setTitle("Error!");

	    // stops main window be accessed when an error is present
	    popup.initModality(Modality.APPLICATION_MODAL);

	    VBox box = new VBox(0);
	    box.setAlignment(Pos.CENTER);
	    box.setPadding(new Insets(30, 20, 40, 20));

	    Label message = new Label("Account not found!");
	    message.getStyleClass().add("error-message");
	    message.setStyle("-fx-padding: 0;");

	    Label subText = new Label("Please try again.");
	    subText.getStyleClass().add("error-message");
	    subText.setStyle("-fx-font-size: 36px; -fx-font-style: italic; -fx-text-fill: #395c06; -fx-padding: 0;");

	    Button sorry = new Button("Sorry!");
	    sorry.getStyleClass().add("error-button");
	    sorry.setOnMouseClicked(e1 -> popup.close());

	    Image lay = new Image(Login.class.getResource("/assets/lay.png").toExternalForm());
	    ImageView layView = new ImageView(lay);
	    layView.setPreserveRatio(true);
	    layView.setFitWidth(290);
	    layView.setLayoutX(200);
	    layView.setLayoutY(-120);
	    layView.setStyle("-fx-cursor: hand;");

	    Pane smiskiPane = new Pane();
	    smiskiPane.setPrefHeight(70);
	    smiskiPane.getChildren().add(layView);
	    smiskiPane.setMouseTransparent(true);

	    box.getChildren().addAll(message, subText, sorry, smiskiPane);

	    Scene popupScene = new Scene(box, 500, 250);
	    popupScene.getStylesheets().add(Notifier.class.getResource("application.css").toExternalForm());
	    popup.setScene(popupScene);
	    popup.setResizable(false);
	    popup.showAndWait();
	}
}
