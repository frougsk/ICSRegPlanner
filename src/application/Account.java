package application;

public class Account {
	// Attributes
    private final String emailAddress;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String program; 
    private final String password; 

    // Constructor
    public Account(String emailAddress, String firstName,
                String middleName, String lastName, String userType, String password) {
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.program = userType;
        this.password = password;
    }

    // Getters
    public String getEmailAddress() { return emailAddress; }
    public String getFirstName()    { return firstName; }
    public String getMiddleName()   { return middleName; }
    public String getLastName()     { return lastName; }
    public String getProgram()     { return program; }
    public String getPassword()     { return password; }

	//Converts to file format
	public String toFile() {
		return String.join(", ", emailAddress, firstName, middleName, lastName, program, password);
	}
	
}

