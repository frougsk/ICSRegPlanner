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
    public Planner_SearchAdd(Account acc, Planner_Sched top) {
        this.account = acc;
        this.schedView = top;

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
                addBtn.setOnAction(e -> {
                    Offering lec = rowItem.getLecture();
                    Offering lab = rowItem.getLab();

                    if (lab == null) {
                        schedView.error("[ERROR] No lab section in this row");
                        return;
                    }

                    boolean success = AddCourse.addBoundPair(account, lec, lab, schedView);
                    if (success && addToBasket != null) {
                        addToBasket.accept(lec);
                        addToBasket.accept(lab);
                    }
                });

                // A spacer to push the Add button to the right edge of the cell content
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // Pack lecture card and lab card into a VBox/HBox to better match screenshot styling
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

        TextField titleSearch = new TextField();
        titleSearch.setPromptText("Course Title");

        codeSearch.textProperty().addListener((a,b,c) ->
                filterRows(codeSearch.getText(), titleSearch.getText()));

        titleSearch.textProperty().addListener((a,b,c) ->
                filterRows(codeSearch.getText(), titleSearch.getText()));

        HBox searchRow = new HBox(10, codeSearch, titleSearch);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(10,
                new Label("COURSE SEARCH (Lecture + Lab)"),
                searchRow,
                table
        );
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
        box.setStyle("""
                -fx-background-color: #e8f0ff;
                -fx-background-radius: 8;
                -fx-border-radius: 8;
                -fx-border-color: #4a85ff;
                """);

        Label typeLbl = new Label(type + " (" + o.getUnits() + " units)");
        typeLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #0044aa;");

        Label sec = new Label(o.getSection() + " - (" + o.getTime() + ")");
        Label room = new Label("Location: " + safeString(o.getRoom()));
        Label day = new Label("Day: " + safeString(o.getDay()));

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
                // Use code + section prefix as key (so that lectures with same code but different sections are distinct)
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
}
