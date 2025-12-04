package planning;

// Package Imports
import bases.*;
import fileHandlers.*;
import application.AddCourse;
import application.Fonts;

//JavaFX Imports
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import planning.Planner_SearchAdd.*;

// Java Imports
import java.nio.file.Paths;
import java.util.function.Consumer;


public class Planner_BasketView {
	private StackPane root = new StackPane();
	private TableView<Offering> table = new TableView<>();
	private Consumer<Offering> removeToBasket;
	private Label warnings;
	
	public Planner_BasketView(Account acc) {
		//=========== TABLEVIEW  ===========
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setRowFactory(tv -> {
            TableRow<Offering> row = new TableRow<>();
            row.setPrefHeight(80); 
            return row;
        });
		
        TableColumn<Offering, Offering> colDetails = new TableColumn<>("BASKET");
        colDetails.setPrefWidth(500);
        colDetails.setStyle("-fx-font-size: 18px;");
        colDetails.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue()));

        colDetails.setCellFactory(col -> new TableCell<Offering, Offering>() {
            @Override
            protected void updateItem(Offering offeringItem, boolean empty) {
                super.updateItem(offeringItem, empty);

                if (empty || offeringItem == null) {
                    setGraphic(null);
                    return;
                }
                VBox card = buildCard(offeringItem, offeringItem.getCode() + " " + offeringItem.getSection()); 
                card.getStyleClass().add("mew-text");
  

                // Remove Course from Basket
                Button deleteBtn = new Button("Delete");
                deleteBtn.setMinWidth(50);
                deleteBtn.getStyleClass().add("adddel-button");
                
                deleteBtn.setOnAction(e -> {
                    if (removeToBasket != null) {
                        removeToBasket.accept(offeringItem);                        
                    }
                });

                HBox outer = new HBox(50);
                outer.setPadding(new Insets(6));
                outer.setAlignment(Pos.CENTER_LEFT);             
                outer.getChildren().addAll(card, deleteBtn);

                setGraphic(outer);
            }
        });
        table.getColumns().addAll(colDetails);
    
              
     // =========== WARNING PANEL ===========
		Text warn = new Text("MESSAGE");
		warn.getStyleClass().add("warn-style");
		warn.setStyle("-fx-text-fill: #192403;");
		
		warnings = new Label("No Warnings.");
		warnings.setWrapText(true);
		warnings.setFont(Fonts.loadCrimson(18));
		warnings.setStyle("-fx-font-size: 14px; ");
        
		
        // =========== LAYOUT ===========              
        VBox warnPanel = new VBox(5, warn, warnings);
        warnPanel.getStyleClass().add("warn-box");
		warnPanel.setAlignment(Pos.TOP_LEFT);
		warnPanel.setMinWidth(250);
		
		VBox basketBox = new VBox(table);
		basketBox.setPadding(new Insets(3));
		basketBox.setSpacing(5);

		VBox warnBasket = new VBox(10, warnPanel, basketBox);
		warnBasket.setAlignment(Pos.CENTER);
		warnBasket.setPadding(new Insets(10, 5, 0, 5));

        
        root.getChildren().add(warnBasket); 
        refresh(acc); 
	}
	
	public Node getNode() {return root; }
			
	// =========== BASKET ESSENTIALS ===========
    public void setRemoveToBasket(Consumer<Offering> handler) {
        this.removeToBasket = handler;
    }
	
	public void refresh(Account acc) {
		table.setItems(FXCollections.observableArrayList(acc.getBasket().values())); 
	}
	
	// =========== DETAILS CARD ===========
	private VBox buildCard(Offering o, String type) {
        VBox box = new VBox(6);
        box.setPadding(new Insets(10));
        box.setMinWidth(200);
        box.getStyleClass().add("meow-box");

        Label typeLbl = new Label(type + " (" + o.getUnits() + " units)");
        typeLbl.getStyleClass().add("meow-text");
        Label room = new Label("Location: " + safeString(o.getRoom()));
        room.getStyleClass().add("mew-text");
       

        box.getChildren().addAll(typeLbl, room);
        return box;
    }
	
    private String safeString(String s) {
        return s == null ? "" : s;
    }
	
    // =========== WARNING PROMPTS ===========
	public void error(String msg) {
		warnings.setText(msg);
		warnings.setTextFill(Color.DARKRED);
	}
	
	public void success(String msg) {
		warnings.setText(msg);
	}
	
	public void setWarning(String text) {
		warnings.setText(text == null || text.trim().isEmpty() ? "No Warnings.": text);
	}
}
