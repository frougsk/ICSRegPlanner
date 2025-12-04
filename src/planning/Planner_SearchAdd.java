package planning;

import bases.*;
import fileHandlers.*;
import application.AddCourse;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

public class Planner_SearchAdd {
    private StackPane root = new StackPane();
    private Consumer<Offering> addToBasket;
    private Account account;
    private Planner_Sched schedView;
    private FilteredList<CourseRow> filteredRows;
	private Planner_BasketView basketView;

    // ==========================================================
    // Inner class representing one table row 
    // ==========================================================
    public static class CourseRow {
        private String code;
        private String title;
        private int units;
        private Offering lecture;
        private Offering lab;

        public CourseRow(String code, String title, int units, Offering lecture, Offering lab) {
            this.code = code;
            this.title = title;
            this.units = units;
            this.lecture = lecture;
            this.lab = lab;
        }

        public String getCode() { return code; }
        public String getTitle() { return title; }
        public int getUnits() { return units; }
        public Offering getLecture() { return lecture; }
        public Offering getLab() { return lab; }
    }

    // ==========================================================
    // Constructor
    // ==========================================================
    public Planner_SearchAdd(Account acc, Planner_Sched top, Planner_BasketView basket) {
        this.account = acc;
        this.schedView = top;
        this.basketView = basket;

        OfferingLoader loader = new OfferingLoader();
        YearSet ay = loader.getOfferings(Paths.get("data/course_offerings.csv"));
        ObservableList<Offering> offerings =
                FXCollections.observableArrayList(ay.getOfferings());

        ObservableList<CourseRow> rows = groupOfferingsAsRows(offerings);
        filteredRows = new FilteredList<>(rows, r -> true);

        // TABLE
        TableView<CourseRow> table = new TableView<>(filteredRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setRowFactory(tv -> {
            TableRow<CourseRow> row = new TableRow<>();
            row.setPrefHeight(140);
            return row;
        });

        // COLUMN 1 — Course Code
        TableColumn<CourseRow, String> colCode = new TableColumn<>("Code");
        colCode.setPrefWidth(120);
        colCode.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCode()));

        // COLUMN 2 — Lecture, Lab, Add button
        TableColumn<CourseRow, CourseRow> colDetails = new TableColumn<>("Class Details");
        colDetails.setPrefWidth(700);

        colDetails.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue()));

        colDetails.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(CourseRow rowItem, boolean empty) {
                super.updateItem(rowItem, empty);

                if (empty || rowItem == null) {
                    setGraphic(null);
                    return;
                }

                HBox outer = new HBox(12);
                outer.setPadding(new Insets(6));
                outer.setAlignment(Pos.CENTER_LEFT);

                VBox lectureCard = buildCard(rowItem.getLecture(), "Lecture/Main");
                VBox labCard = null;
                if (rowItem.getLab() != null) {
                    labCard = buildCard(rowItem.getLab(), "Lab/Recit");
                } else {
                    labCard = new VBox();
                }

                Button addBtn = new Button("Add");
                addBtn.setMinWidth(60);
                addBtn.getStyleClass().add("adddel-button");
                addBtn.setOnAction(e -> {
                    Offering lec = rowItem.getLecture();
                    Offering lab = rowItem.getLab();

                    boolean success = false; 
                    
                    if (!programCheck(account, lec)) {
                    	basket.error("[ERROR] " + lec.getCode() + " is not offered in your program");
                    } else if (lab == null) {
                        // No lab
                        success = AddCourse.addCourse(account, lec, basket, null);
                    } else {
                        // Has lab
                        success = AddCourse.addBoundPair(account, lec, lab, basket);
                    }
                    
                    if (success) {
                        // Refresh both views directly
                        schedView.updateSched(account);
                        basketView.refresh(account);
                    }
                });
                
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // Pack lecture card and lab card into a Hbox
                HBox cardsBox = new HBox(10, lectureCard, labCard);
                cardsBox.setAlignment(Pos.CENTER_LEFT);

                outer.getChildren().addAll(cardsBox, spacer, addBtn);
                setGraphic(outer);
            }
        });

        table.getColumns().addAll(colCode, colDetails);

        // SEARCH BAR
        TextField codeSearch = new TextField();
        codeSearch.setPromptText("Course Code");
        codeSearch.getStyleClass().add("course-textfields");

        TextField titleSearch = new TextField();
        titleSearch.setPromptText("Course Title");
        titleSearch.getStyleClass().add("course-textfields");

        codeSearch.textProperty().addListener((a,b,c) ->
                filterRows(codeSearch.getText(), titleSearch.getText()));

        titleSearch.textProperty().addListener((a,b,c) ->
                filterRows(codeSearch.getText(), titleSearch.getText()));

        HBox searchRow = new HBox(10, codeSearch, titleSearch);
        searchRow.setAlignment(Pos.CENTER_LEFT);

 		Label searchLbl = new Label("ADD CLASSES");
        searchLbl.getStyleClass().add("hello-style");
        searchLbl.setAlignment(Pos.TOP_CENTER);
        
        VBox lay = new VBox(10, searchLbl, searchRow);
        lay.getStyleClass().add("gen-box");
		
        VBox layout = new VBox(20, lay, table);
        layout.setPadding(new Insets(16));

        root = new StackPane(layout);
    }

    // ==========================================================
    // Build card UI
    // ==========================================================
    private VBox buildCard(Offering o, String type) {
        VBox box = new VBox(6);
        box.setPadding(new Insets(10));
        box.setMinWidth(320);
        box.getStyleClass().add("meow-box");

        Label typeLbl = new Label(type + " (" + o.getUnits() + " units)");
        typeLbl.getStyleClass().add("meow-text");

        Label sec = new Label(o.getSection() + " - (" + o.getTime() + ")");
        sec.getStyleClass().add("mew-text");
        
        Label room = new Label("Location: " + safeString(o.getRoom()));
        room.getStyleClass().add("mew-text");
        
        Label day = new Label("Day: " + safeString(o.getDay()));
        day.getStyleClass().add("mew-text");

        box.getChildren().addAll(typeLbl, sec, day, room);
        return box;
    }

    private String safeString(String s) {
        return s == null ? "" : s;
    }

    // ==========================================================
    // Group lecture + labs into rows
    // ==========================================================
    private ObservableList<CourseRow> groupOfferingsAsRows(ObservableList<Offering> offerings) {
        // Collect lectures and labs by key
        Map<String, Offering> lectures = new LinkedHashMap<>();
        Map<String, List<Offering>> labsByLectureKey = new HashMap<>();

        // Normalize section upper case for checks
        for (Offering o : offerings) {
            String section = o.getSection() == null ? "" : o.getSection().toUpperCase();
            String lectKey = o.getCode() + "-" + (o.getSection() == null ? "" : o.getSection());

            if (!section.endsWith("L")) {
                // treat as lecture/main
                // Use code + section prefix as key so that lectures with same code but different sections are distinct
                String key = o.getCode() + "-" + o.getSection();
                lectures.put(key, o);
            }
        }

        // Collect lab offerings mapped to their lecture prefix
        for (Offering o : offerings) {
            String section = o.getSection() == null ? "" : o.getSection().toUpperCase();
            if (section.endsWith("L")) {
                // example: if section is "G-1L", prefix = "G"
                String[] parts = section.split("-");
                String prefix = parts.length > 0 ? parts[0] : section;
                String key = o.getCode() + "-" + prefix; // match the lecture key above
                labsByLectureKey.computeIfAbsent(key, k -> new ArrayList<>()).add(o);
            }
        }

        ObservableList<CourseRow> result = FXCollections.observableArrayList();

        // For each lecture key that has labs, create one row per lab
        for (Map.Entry<String, Offering> lectEntry : lectures.entrySet()) {
            String lectKey = lectEntry.getKey();
            Offering lec = lectEntry.getValue();

            List<Offering> labs = labsByLectureKey.getOrDefault(lectKey, Collections.emptyList());
            if (!labs.isEmpty()) {
                // sort labs by section string for stable ordering (optional)
                labs.sort(Comparator.comparing(Offering::getSection));
                for (Offering lab : labs) {
                    result.add(new CourseRow(lec.getCode(), lec.getTitle(), lec.getUnits(), lec, lab));
                }
            } else {
                result.add(new CourseRow(lec.getCode(), lec.getTitle(), lec.getUnits(), lec, null));
            }
        }

        return result;
    }

    // ==========================================================
    // Search Filter
    // ==========================================================
    private void filterRows(String code, String title) {
        String codeLower = code == null ? "" : code.toLowerCase();
        String titleLower = title == null ? "" : title.toLowerCase();

        filteredRows.setPredicate(r ->
                (codeLower.isEmpty() || r.getCode().toLowerCase().contains(codeLower)) &&
                        (titleLower.isEmpty() || r.getTitle().toLowerCase().contains(titleLower))
        );
    }

    public Node getNode() { return root; }

    public void setAddToBasket(Consumer<Offering> handler) {
        this.addToBasket = handler;
    }
    
    public static boolean programCheck(Account a, Offering o) {
    	CourseLoader loader = new CourseLoader();
		ArrayList<Course> bs = loader.getBS();
		ArrayList<Course> master = loader.getMasters();
		ArrayList<Course> phd = loader.getPHD();
		ArrayList<Course> mit = loader.getMITS();
    	
    	String userProg = a.getProgram().toUpperCase();
    	String offeringCode = o.getCode();
    	
    	ArrayList<Course> allowedCourses = null;
    	if (userProg.contains("BS")) {
    		allowedCourses = bs;    		
    	} else if (userProg.contains("MS")) {
    		allowedCourses = master;
    	} else if (userProg.contains("PHD")) {
    		allowedCourses = phd;
    	} else if (userProg.contains("MIT")) {
    		allowedCourses = mit;
    	} else return false;	   	
   
    	for (Course c: allowedCourses) {
    		String allowedCode = c.getCode().trim();
    		if (allowedCode.equals(offeringCode)) {
    			return true;
    		}
    	}
		return false;   
    }
}
