package application;

import java.util.ArrayList;
import java.util.List;

import bases.Account;
import bases.Offering;

public class AddCourse {
	
    public static boolean addCourse(Account account, Offering newCourse, Planner_Sched top) {
    	top.setWarning(null);
    	String newCode = newCourse.getCode().trim().toUpperCase();
        String newSection = newCourse.getSection().trim().toUpperCase();
        
        for (Offering existing: account.getBasket().values()) {
        	// Checks for exact duplicate of course and section
        	if (existing.getCode().equalsIgnoreCase(newCourse.getCode()) &&
        			existing.getSection().equalsIgnoreCase(newCourse.getSection())) {
				if (true) {
        		
        		// Check if possible duplicate course (except CMSC 290 and CMSC 291
	            /**if (!newCourse.getCode().equalsIgnoreCase("CMSC 290") &&
	                !newCourse.getCode().equalsIgnoreCase("CMSC 291")) {**/
	            		            	
					top.error("[ERROR] " + newCourse.getCode() + " " + newCourse.getSection() + " is already in your basket");
	                return false;
	            }
	    	}
    	}       

        List<Offering> withSameCode = new ArrayList<>();
        for (Offering existing : account.getBasket().values()) {
            if (existing.getCode().trim().toUpperCase().equals(newCode)) {
            	withSameCode.add(existing);
            }
                
            if (withSameCode.size() >= 2) {
            	top.error("[ERROR] You can only add a Lecture and Lab Class");
            }
        }
        
        if (withSameCode.size() == 1) {
            Offering existing = withSameCode.get(0);
            
            boolean existingLab = existing.getSection().toUpperCase().contains("L");
            boolean newIsLab = newSection.contains("L");

            if (existingLab == newIsLab) {
                top.error("[ERROR] You already have a Lab Class.");
                return false;
            }
        }
    
        // Check for schedule conflicts (True if overlap)
        for (Offering existing : account.getBasket().values()) {
            if (conflicts(existing, newCourse)) {
            	 top.error("[ERROR] " + newCourse.getCode() + " overlaps with your added courses");
            	return false;
            }
        }

        // Add to basket if possible
        account.addToBasket(newCourse);
        top.success("[SUCCESS] Successfully added " + newCourse.getCode());
       
		
        return true;
    }

    // Helper Method: Check if two offerings overlap in day/time
    private static boolean conflicts(Offering a, Offering b) {
        // Check if same day conflict
        if (!a.getDay().equalsIgnoreCase(b.getDay())) return false;

        // Parse times with format "HH:MM-HH:MM"
        String[] aTimes = a.getTime().split("-");
        String[] bTimes = b.getTime().split("-");

        int aStart = toMinutes(aTimes[0]);
        int aEnd = toMinutes(aTimes[1]);
        int bStart = toMinutes(bTimes[0]);
        int bEnd = toMinutes(bTimes[1]);

        // Overlap logic:
        // A cannot start before B ends == ASTART < BEND X
        // A cannot end after B starts == AEND > BSTART X
        return (aStart < bEnd && aEnd > bStart);
    }

    // Helper Method: Convert "HH:MM" to minutes since midnight
    static int toMinutes(String time) {
        time = time.trim();
        
        // Split into hours and minutes
        String[] parts = time.split(":");
        
        if (parts.length < 2) {
             try {
                 int hour = Integer.parseInt(time);
                 parts = new String[] {String.valueOf(hour), "00"};
             } catch (NumberFormatException e) {
                 return 0; 
             }
        }

        int hour = Integer.parseInt(parts[0]);
        int min  = Integer.parseInt(parts[1]);
      
        // Handles the 12:00 format to accomodate 24:00 format
        if (hour >= 1 && hour <= 6) { 
            hour += 12; 
        } else if (hour >= 1 && hour <= 11 && hour != 12) {
        	if (hour >= 1 && hour <= 7) { 
                hour += 12; // Converts 1 PM to 13, 7 PM to 19
        	}
        } 
        return hour * 60 + min;
    }
}

