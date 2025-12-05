package application;

import javafx.scene.text.Font;

public class Fonts {
    
    public static final String DOTEMP = "/fonts/dotemp-8bit.otf";
    public static final String CRIMSON = "/fonts/CrimsonPro-VariableFont_wght.ttf";
    
    private Fonts() {}
    
    // Getters
    @SuppressWarnings("exports")
	public static Font load(String uri, double size) {
        Font f = Font.loadFont(Fonts.class.getResourceAsStream(uri), size);
        if (f == null) {
            try {
                return Font.font("Montserrat", size); // defaults
            } catch (Exception ex) {
                return Font.getDefault();
            }
        }
        return f;
    }
    
    @SuppressWarnings("exports")
	public static Font loadDotemp(int size) {
        return load(DOTEMP, size);
    }
    
    @SuppressWarnings("exports")
	public static Font loadCrimson(int size) {
    	return load(CRIMSON, size);
    }
}
