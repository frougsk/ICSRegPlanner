module ICSRegPlanner {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
	requires javafx.media;
    
    opens application to javafx.graphics, javafx.base;
    opens bases to javafx.base;
    opens fileHandlers to javafx.base;
    opens screens to javafx.base;
    
    exports application;
    exports bases;
}
