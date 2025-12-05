package screens;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class TutorialPage {

    public Parent showTutorial(double width, double height) {

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Video Tutorial");
        title.getStyleClass().add("hi-style");

        Media media = new Media(getClass().getResource("/assets/tutorial.m4v").toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        MediaView mediaView = new MediaView(player);

        mediaView.setFitWidth(width * 0.6);
        mediaView.setFitHeight(height * 0.6); 
        mediaView.setPreserveRatio(true);    

        player.setAutoPlay(true);

        layout.getChildren().addAll(title, mediaView);
        return layout;
    }
}
