package bases;

public class Offering {
	// Attributes
	private final String code;
	private final String title;
	private final int units;
	private final String section;
	private final String time;
	private final String day;
	private final String room;
	
	// Constructor
	public Offering(String code, String title, int units, String section, String time, String day, String room) {
		this.code = code;
		this.title = title;
		this.units = units;
		this.section = section;
		this.time = time;
		this.day = day;
		this.room = room;
	}
	
	// Getters
	public String getCode() { return code; }
	public String getTitle() { return title; }
	public int getUnits() { return units; }
	public String getSection() { return section; }
	public String getTime() { return time; }
	public String getDay() { return day; }
	public String getRoom() { return room; }
}
