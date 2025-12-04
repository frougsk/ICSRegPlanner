package bases;

public class Offering {
    private String code;
    private String title;
    private int units;
    private String section;
    private String time;
    private String day;
    private String room;
    
    // New fields for lecture-lab binding
    private String coReq;  // Co-requisite section (e.g., "G-1L" for lecture "G")
    private String preReq; // Pre-requisite section (e.g., "G" for lab "G-1L")
    private boolean isLab; // Flag to identify if this is a lab section
    
    // Original constructor
    public Offering(String code, String title, int units, String section, 
                   String time, String day, String room) {
        this.code = code;
        this.title = title;
        this.units = units;
        this.section = section;
        this.time = time;
        this.day = day;
        this.room = room;
        this.isLab = section != null && section.toUpperCase().endsWith("L");
    }
    
    // Extended constructor with binding info
    public Offering(String code, String title, int units, String section, 
                   String time, String day, String room, String coReq, String preReq) {
        this(code, title, units, section, time, day, room);
        this.coReq = coReq;
        this.preReq = preReq;
    }
    
    // Getters
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getUnits() { return units; }
    public String getSection() { return section; }
    public String getTime() { return time; }
    public String getDay() { return day; }
    public String getRoom() { return room; }
    public String getCoReq() { return coReq; }
    public String getPreReq() { return preReq; }
    public boolean isLab() { return isLab; }
    
    // Setters for binding
    public void setCoReq(String coReq) { this.coReq = coReq; }
    public void setPreReq(String preReq) { this.preReq = preReq; }
    
    // Helper method to check if this offering has a required pair
    public boolean hasBinding() {
        return (coReq != null && !coReq.isEmpty()) || 
               (preReq != null && !preReq.isEmpty());
    }
    
    // Get the full key for this offering
    public String getKey() {
        return code + "-" + section;
    }
    
    // Get the key of the bound offering
    public String getBoundKey() {
        if (coReq != null && !coReq.isEmpty()) {
            return code + "-" + coReq;
        } else if (preReq != null && !preReq.isEmpty()) {
            return code + "-" + preReq;
        }
        return null;
    }
}
