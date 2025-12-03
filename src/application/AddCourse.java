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

    	// Gather existing offerings with the same course code
    	List<Offering> sameCode = new ArrayList<>();
    	for (Offering existing : account.getBasket().values()) {
    	    if (existing.getCode().equalsIgnoreCase(newCode)) {
    	        sameCode.add(existing);
    	    }
    	}

    	// Only 1 Lecture + 1 Lab match allowed
    	if (sameCode.size() >= 2) {
    	    top.error("[ERROR] You can only add one Lecture and one Lab for " + newCode);
    	    return false;
    	}

    	// If one exists, ensure valid lecture-lab pairing
    	if (sameCode.size() == 1) {
    	    Offering existing = sameCode.get(0);

    	    String existingSec = existing.getSection().toUpperCase();
    	    String newSec = newSection.toUpperCase();

			// Most lab sections contain L at the end !! Lazy checker, find alternative if possible
    	    boolean existingIsLab = existingSec.contains("L");
    	    boolean newIsLab = newSec.contains("L");

    	    if (existingIsLab == newIsLab) {
    	        top.error("[ERROR] You already added a " + (existingIsLab ? "Lab" : "Lecture") + " for " + newCode);
    	        return false;
    	    }

    	    String existingPrefix = existingIsLab ? existingSec.split("-")[0] : existingSec;
    	    String newPrefix = newIsLab ? newSec.split("-")[0] : newSec;

    	    if (!existingPrefix.equalsIgnoreCase(newPrefix)) {
    	        top.error("[ERROR] Section mismatch. Lab must match its lecture prefix.");
    	        return false;
    	    }
    	}

    	// Schedule conflict check
    	for (Offering existing : account.getBasket().values()) {
    	    if (conflicts(existing, newCourse)) {
    	        top.error("[ERROR] " + newCourse.getCode() + " overlaps with your added courses");
    	        return false;
    	    }
    	}

    	// Add to basket
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



