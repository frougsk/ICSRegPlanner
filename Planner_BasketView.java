package application;

import bases.*;
import fileHandlers.*;

import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.nio.file.Paths;
import java.util.function.Consumer;

public class Planner_BasketView {
	private StackPane root = new StackPane();
	private TableView<Offering> table = new TableView<>();
	private Consumer<Offering> removeToBasket;
	
	public Planner_BasketView(Account acc) {
		//=========== TABLEVIEW  ===========
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getStyleClass().add("course-table");
        
		TableColumn<Offering, String> colCode   = new TableColumn<>("Course Code");
        TableColumn<Offering, String> colTitle      = new TableColumn<>("Course Title");
        TableColumn<Offering, String> colUnit  = new TableColumn<>("Units");
        TableColumn<Offering, String> colSec = new TableColumn<>("Section");
        TableColumn<Offering, String> colTime   = new TableColumn<>("Times");
        TableColumn<Offering, String> colDay   = new TableColumn<>("Days");
        TableColumn<Offering, String> colRoom   = new TableColumn<>("Rooms");
        
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("units"));
        colSec.setCellValueFactory(new PropertyValueFactory<>("section"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("day"));
        colRoom.setCellValueFactory(new PropertyValueFactory<>("room"));

        table.getColumns().addAll(colCode, colTitle, colUnit, colSec, colTime, colDay, colRoom);
        
        
        //=========== DELETE COURSE  ===========
        Button del = new Button("Delete");
        del.setOnMouseClicked(e -> {
        	Offering selected = table.getSelectionModel().getSelectedItem();
        	if(selected != null && removeToBasket != null) {
        		removeToBasket.accept(selected);
        	}
        });
        
        
     // =========== LAYOUT ===========
        Label basketLbl = new Label("YOUR BASKET ");
        basketLbl.getStyleClass().add("hello-style");
        basketLbl.setAlignment(Pos.TOP_LEFT);
        
        VBox basket = new VBox(10, basketLbl, table, del);
        //basket.setAlignment(Pos.CENTER);
        basket.setPadding(new Insets(10, 80, 10, 80));
        basket.getStyleClass().add("login-box");
        
       root.getChildren().add(basket);
   
        
        refresh(acc);
        
	}
	public Node getNode() {return root; }

    public void setRemoveToBasket(Consumer<Offering> handler) {
        this.removeToBasket = handler;
    }
	
	public void refresh(Account acc) {
		table.setItems(FXCollections.observableArrayList(acc.getBasket().values())); 
	}
}
