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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.nio.file.Paths;
import java.util.function.Consumer;

public class Planner_SearchAdd {
	
	private final Account account;
	private StackPane root;// = new StackPane() ;
	private Consumer<Offering> addToBasket;
	private FilteredList<Offering> filtered;
	
	public Planner_SearchAdd(Account account) {
		this.account = account;
		this.root  = new StackPane();
		searchAdd();
	}
	
	private void searchAdd() {
		//=========== LOAD OFFERED COURSES  ===========
		OfferingLoader loader = new OfferingLoader();
		YearSet ay = loader.getOfferings(Paths.get("courses/course_offerings.csv"));
		ObservableList<Offering> offerings = FXCollections.observableArrayList(ay.getOfferings());
		
		FilteredList<Offering> searchList = new FilteredList<>(offerings, s -> true);
		
		
		//=========== TABLEVIEW  ===========
		TableView<Offering> table = new TableView<>(searchList);
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
        
        //=========== SEARCH COURSE  ===========
        TextField codeSearch = new TextField();
		codeSearch.setPromptText("Course Code");
		codeSearch.getStyleClass().add("login-textfields");
		
		TextField titleSearch = new TextField();
		titleSearch.setPromptText("Course Name");
		titleSearch.getStyleClass().add("login-textfields");
		titleSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			searchCourse(searchList, codeSearch.getText(), titleSearch.getText(), table);
			});
		
		codeSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			searchCourse(searchList, codeSearch.getText(), titleSearch.getText(), table);
			});
		
		HBox search = new HBox(10, codeSearch, titleSearch);
        search.setAlignment(Pos.TOP_LEFT);
        
        
        //=========== ADD COURSE  ===========       
        Button add = new Button("Add");
        add.setOnMouseClicked(e -> {
        	Offering selected = table.getSelectionModel().getSelectedItem();
        	if(selected != null && addToBasket != null) {
        		addToBasket.accept(selected);
        	}
        });   
        
        
        // =========== LAYOUT ===========
        Label seachLbl = new Label("COURSE SEARCH");
        seachLbl.getStyleClass().add("hello-style");
        seachLbl.setAlignment(Pos.TOP_LEFT);
        
        VBox searchAdd = new VBox(10, search, table, add);
        //searchAdd.setAlignment(Pos.CENTER);
        searchAdd.setPadding(new Insets(10, 80, 10, 80));
        searchAdd.setMaxHeight(Double.MAX_VALUE);
        searchAdd.getStyleClass().add("login-box");
        
        root.getChildren().add(searchAdd);
        }
	    
    public Node getNode() {return root; }
    
    public void setAddToBasket(Consumer<Offering> handler) {
    	this.addToBasket = handler;
    }
    
    // For Search Function
	static void searchCourse(FilteredList<Offering> searchList, String code, String title, TableView<Offering> table) {
		searchList.setPredicate(o -> {
			String lower = code.toLowerCase();
			String lower2 = title.toLowerCase();
			
			boolean codeMatched = code == null || code.isEmpty() || o.getCode().toLowerCase().contains(lower); 
			boolean titleMatched = title == null || title.isEmpty()	|| o.getTitle().toLowerCase().contains(lower2);
			
			if (!codeMatched && !titleMatched) {
				table.setPlaceholder(new Label("No course found"));
			} 
			return codeMatched && titleMatched;
		});
	}
}
