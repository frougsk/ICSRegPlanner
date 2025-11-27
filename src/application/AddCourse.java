package application;

import bases.Account;
import bases.Offering;

public class AddCourse {

    public static boolean addCourse(Account account, Offering newCourse) {
        // Check if possible duplicate course (except CMSC 290 and CMSC 291)
        if (account.getBasket().containsKey(newCourse.getCode())) {
            if (!newCourse.getCode().equalsIgnoreCase("CMSC 290") &&
                !newCourse.getCode().equalsIgnoreCase("CMSC 291")) {
                // INSERT CODE TO PROMPT USER THAT THIS ACTION CANNOT BE DONE
                return false;
            }
        }

        // Check for schedule conflicts (True if overlap)
        for (Offering existing : account.getBasket().values()) {
            if (conflicts(existing, newCourse)) {
            	// INSERT CODE TO PROMPT USER THAT THIS ACTION CANNOT BE DONE
                return false;
            }
        }

        // Add to basket if possible
        account.addToBasket(newCourse);
        // INSERT CODE TO PROMPT USER THAT ACTION WAS DONE SUCCESSFULLY
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
    private static int toMinutes(String time) {
        String[] parts = time.trim().split(":");
        int hour = Integer.parseInt(parts[0]);
        int min  = Integer.parseInt(parts[1]);
        return hour * 60 + min;
    }
}

