package application;
	
// Import Packages
import bases.*;
import fileHandlers.*;

// Import JavaFX
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

// Other Java imports
import java.util.ArrayList;
import java.nio.file.Paths;

public class Main extends Application {
	@Override
	public void start(@SuppressWarnings("exports") Stage mainStage) {
		// Get fonts
		Font.loadFont(getClass().getResource("/fonts/dotemp-8bit.otf").toExternalForm(), 10);
		Font.loadFont(getClass().getResource("/fonts/CrimsonPro-VariableFont_wght.ttf").toExternalForm(), 10);
		
		// App logo
		try {
        	Image icon = new Image(getClass().getResource("/assets/icon.png").toExternalForm());
        	mainStage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Icon not found");
        }

		// Get screen size
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double width = screenBounds.getWidth();
		double height = screenBounds.getHeight();
		
		mainStage.setX(screenBounds.getMinX());
		mainStage.setY(screenBounds.getMinY());
		mainStage.setWidth(width);
		mainStage.setHeight(height);
		
		// Get accounts
		AccountLoader accload = new AccountLoader();
		ArrayList<Account> accounts = accload.getAccounts(Paths.get("accounts_data/accounts.csv"));
		
		// Show login scene
		mainStage.setScene(Login.welcomeShow(width, height, mainStage, accounts));
		mainStage.setTitle("smiCS");
		mainStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

// Import Google Font to CSS: https://www.w3docs.com/snippets/css/how-to-import-google-fonts-in-css-file.html
