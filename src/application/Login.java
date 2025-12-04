package application;

// Package imports
import bases.Account;

// JavaFX Imports
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

// Other Java Imports
import java.util.ArrayList;

public class Login {

    public static Scene welcomeShow(double width, double height, Stage mainStage, ArrayList<Account> accounts) {

        // =========== TOP BAR ===========
        ImageView brand = new ImageView(Login.class.getResource("/assets/brand.png").toExternalForm());
        double ogWidth = brand.getImage().getWidth();
        brand.setPreserveRatio(true);
        brand.setFitWidth(ogWidth * 0.05);

        ImageView borgir = new ImageView(Login.class.getResource("/assets/menubutton.png").toExternalForm());
        borgir.setPreserveRatio(true);
        borgir.setFitWidth(ogWidth * 0.02);

        Button menuButton = new Button();
        menuButton.setGraphic(borgir);
        menuButton.setStyle("-fx-background-color: transparent;");

        HBox topBar = new HBox(20, menuButton, brand);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 0, 20));
        topBar.setPrefHeight(50);
        topBar.setStyle("-fx-background-color: #eaefdb;");

        // =========== LOGIN BOX ===========
        ImageView smiski = new ImageView(Login.class.getResource("/assets/icon.png").toExternalForm());
        smiski.setPreserveRatio(true);
        smiski.setFitWidth(100);

        javafx.scene.effect.DropShadow dropShadow = new javafx.scene.effect.DropShadow();
        dropShadow.setColor(Color.rgb(166, 190, 93, 0.3));
        dropShadow.setRadius(40);
        smiski.setEffect(dropShadow);

        Text login = new Text("Log In");
        login.setFont(Fonts.loadDotemp(55));
        login.setFill(Color.web("#a6be5d"));
        login.setStyle("-fx-effect: dropshadow( one-pass-box , #a6be5d , 5, 0, 0 , 0 );");

        Region spacer = new Region();
        spacer.setPrefHeight(18);

        TextField email = new TextField();
        email.setPromptText("Email Address");
        email.getStyleClass().add("login-textfields");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.getStyleClass().add("login-textfields");

        TextField visiblePassword = new TextField();
        visiblePassword.setPromptText("Password");
        visiblePassword.setVisible(false);
        visiblePassword.getStyleClass().add("login-textfields");

        password.textProperty().bindBidirectional(visiblePassword.textProperty());

        StackPane passwordStack = new StackPane(password, visiblePassword);

        CheckBox showPassword = new CheckBox("Show Password");
        showPassword.setOnAction(e -> {
            boolean show = showPassword.isSelected();
            visiblePassword.setVisible(show);
            password.setVisible(!show);
        });
        showPassword.getStyleClass().add("showpass-style");

        VBox passbox = new VBox(8, passwordStack, showPassword);

        // =========== LOG IN FEATURES ===========
        Image unhover = new Image(Login.class.getResource("/assets/Unhovered_sign.png").toExternalForm());
        Image hover = new Image(Login.class.getResource("/assets/Hovered_sign.png").toExternalForm());

        ImageView buttonIcon = new ImageView(unhover);
        buttonIcon.setFitWidth(45);
        buttonIcon.setFitHeight(50);

        Button log = new Button("Log In", buttonIcon);
        log.setTextFill(Color.web("#FFFFFF"));
        log.setStyle("-fx-cursor: hand; -fx-padding:0; -fx-effect: dropshadow( one-pass-box , #FFFFFF, 3, 0, 0 , 0 );");
        log.setContentDisplay(ContentDisplay.RIGHT);

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
                for (Account acc : accounts) {

                    if (acc.getEmailAddress().equals(email.getText())
                            && acc.getPassword().equals(password.getText())) {

                        found = true;
                        Scene successScene = loginSuccess(width, height, mainStage, acc, topBar);
                        mainStage.setScene(successScene);
                        break;
                    }
                }

                if (!found) {
                    Notifier.error();
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

        VBox bottomE = new VBox(7, log, flow);
        VBox.setMargin(bottomE, new Insets(10, 0, 0, 0));

        VBox smiskiAndTitle = new VBox(0, smiski, login);
        smiskiAndTitle.setAlignment(Pos.CENTER);

        VBox signin = new VBox(5, smiskiAndTitle, spacer, email, passbox, bottomE);
        signin.setAlignment(Pos.TOP_CENTER);
        signin.setPadding(new Insets(50, 40, 0, 40));
        signin.getStyleClass().add("login-box");

        Image hangingImg = new Image(Login.class.getResource("/assets/Hang.gif").toExternalForm());
        Image hoverImg = new Image(Login.class.getResource("/assets/Hng2.gif").toExternalForm());

        ImageView smiskiView = new ImageView(hangingImg);
        smiskiView.setPreserveRatio(true);
        smiskiView.setFitWidth(290);
        smiskiView.setTranslateX(330);
        smiskiView.setTranslateY(-200);
        smiskiView.setStyle("-fx-cursor: hand;");

        smiskiView.setOnMouseEntered(e -> smiskiView.setImage(hoverImg));
        smiskiView.setOnMouseExited(e -> smiskiView.setImage(hangingImg));

        StackPane signinWithHanging = new StackPane(signin, smiskiView);
        signinWithHanging.setAlignment(Pos.CENTER_LEFT);
        signinWithHanging.setTranslateX(150);

        // =========== LOGO ===========
        ImageView logo = new ImageView(Login.class.getResource("/assets/LandingPage.gif").toExternalForm());
        double logoWidth = logo.getImage().getWidth();
        logo.setPreserveRatio(true);
        logo.setFitWidth(logoWidth * 0.7);

        VBox welcome = new VBox(logo);
        welcome.setAlignment(Pos.CENTER);
        welcome.setPadding(new Insets(10, 50, 0, 50));

        HBox centerContent = new HBox(210, signinWithHanging, welcome);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(40, 50, 40, 50));

        StackPane centerArea = new StackPane(centerContent);

        BorderPane broot = new BorderPane();
        broot.setPrefSize(width, height);
        broot.setTop(topBar);
        broot.setCenter(centerArea);

        StackPane root = new StackPane(broot);
        root.setPrefSize(width, height);
        root.getStyleClass().add("welcome-bg");

        root.setOnMousePressed(e -> root.requestFocus());

        // **CREATE SCENE HERE FIRST**
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(Login.class.getResource("application.css").toExternalForm());

        // **NOW clickable action can safely use 'scene'**
        clickable.setOnAction(e -> {
            mainStage.setScene(CreateAccount.createAccount(width, height, mainStage, scene, accounts));
        });

        Platform.runLater(() -> root.requestFocus());

        return scene;
    }

    public static Scene loginSuccess(double width, double height, Stage mainStage, Account account, HBox topBar) {

        ImageView suc = new ImageView(Login.class.getResource("/assets/login_success.png").toExternalForm());
        double ogWidth = suc.getImage().getWidth();
        suc.setPreserveRatio(true);
        suc.setFitWidth(ogWidth * 0.25);

        Text welcome = new Text("Login successful! Welcome, " + account.getFirstName() + "!");

        VBox success = new VBox(20, suc, welcome);
        success.setAlignment(Pos.CENTER);

        StackPane centerBox = new StackPane(success);
        centerBox.getStyleClass().add("success-style");

        BorderPane broot = new BorderPane();
        broot.setTop(topBar);
        broot.setCenter(centerBox);

        StackPane root = new StackPane(broot);
        root.getStyleClass().add("welcome-bg");
        root.setStyle("-fx-cursor: wait;");

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(ev -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), root);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            fadeOut.setOnFinished(e -> {
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
