package bases;

import java.util.LinkedHashMap;

public class Account {
	// Attributes
    private final String emailAddress;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String program; 
    private final String password; 
    
    private final LinkedHashMap<String, Offering> basketCourses;

    // Constructor
    public Account(String emailAddress, String firstName,
                String middleName, String lastName, String userType, String password) {
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.program = userType;
        this.password = password;
        
        this.basketCourses = new LinkedHashMap<>();
    }

    // Getters
    public String getEmailAddress() { return emailAddress; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }
    public String getProgram() { return program; }
    public String getPassword() { return password; }
    public LinkedHashMap<String, Offering> getBasket() { return basketCourses;}
    
    
    // Other Methods
    public void addToBasket(Offering offering) {
        String key = offering.getCode() + "-" + offering.getSection(); // UPDATE: Unique key per section
        basketCourses.put(key, offering);
    }

    public void removeFromBasket(String courseCode) {
        basketCourses.remove(courseCode); 
    }

	//Converts to file format
	public String toFile() {
		return String.join(",", emailAddress, firstName, middleName, lastName, program, password);
	}
}
