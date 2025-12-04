package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bases.Account;
import bases.Offering;
import planning.Planner_Sched;

public class AddCourse {
    
    // Main add course method - checks for binding requirements
    public static boolean addCourse(Account account, Offering newCourse, Planner_Sched top) {
        return addCourse(account, newCourse, top, null);
    }
    
    public static boolean addCourse(Account account, Offering newCourse, Planner_Sched top, 
                                   Map<String, Offering> allOfferings) {
        top.setWarning(null);
        String newCode = newCourse.getCode().trim().toUpperCase();
        String newSection = newCourse.getSection().trim().toUpperCase();

        // Check if offering has a binding requirement
        if (newCourse.hasBinding()) {
            String boundKey = newCourse.getBoundKey();
            
            // Check if the bound course is already in basket
            boolean boundInBasket = account.getBasket().containsKey(boundKey);
            
            if (!boundInBasket) {
                // Bound course is NOT in basket
                if (allOfferings != null) {
                    Offering boundOffering = allOfferings.get(boundKey);
                    if (boundOffering != null) {
                        // Determine which is lecture and which is lab
                        Offering lecture = newCourse.isLab() ? boundOffering : newCourse;
                        Offering lab = newCourse.isLab() ? newCourse : boundOffering;
                        
                        top.error("[BINDING REQUIRED] " + lecture.getCode() + " " + 
                                lecture.getSection() + " requires " + 
                                lab.getSection() + ". Use 'Add Lecture + Lab' button.");
                    } else {
                        top.error("[BINDING ERROR] Required paired section not found: " + boundKey);
                    }
                } else {
                    top.error("[BINDING REQUIRED] This section requires its paired lecture/lab. " +
                            "Required section: " + boundKey);
                }
                return false;
            }
        }

        // Gather existing offerings with the same course code
        List<Offering> sameCode = new ArrayList<>();
        for (Offering existing : account.getBasket().values()) {
            if (existing.getCode().equalsIgnoreCase(newCode)) {
                sameCode.add(existing);
            }
        }

        // Only 1 Lecture + 1 Lab match allowed (or just 1 lecture for courses without labs)
        if (sameCode.size() >= 2) {
            top.error("[ERROR] You can only add one Lecture and one Lab for " + newCode);
            return false;
        }

        // If one exists, ensure valid lecture-lab pairing
        if (sameCode.size() == 1) {
            Offering existing = sameCode.get(0);

            String existingSec = existing.getSection().toUpperCase();
            String newSec = newSection.toUpperCase();

            // Most lab sections contain L at the end
            boolean existingIsLab = existingSec.endsWith("L");
            boolean newIsLab = newSec.endsWith("L");

            if (existingIsLab == newIsLab) {
                top.error("[ERROR] You already added a " + (existingIsLab ? "Lab" : "Lecture") + " for " + newCode);
                return false;
            }

            // Extract prefix (e.g., "G" from "G-1L")
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
                top.error("[ERROR] " + newCourse.getCode() + " " + newCourse.getSection() + 
                        " overlaps with " + existing.getCode() + " " + existing.getSection());
                return false;
            }
        }

        // Add to basket
        account.addToBasket(newCourse);
        top.success("[SUCCESS] Successfully added " + newCourse.getCode() + " " + newCourse.getSection());
        return true;
    }

    // Add both lecture and lab together
    public static boolean addBoundPair(Account account, Offering offering1, Offering offering2, 
                                      Planner_Sched top) {
        top.setWarning(null);
        
        // Determine which is lecture and which is lab
        Offering lecture = offering1.isLab() ? offering2 : offering1;
        Offering lab = offering1.isLab() ? offering1 : offering2;
        
        String code = lecture.getCode();
        
        // Check if either already exists
        if (account.getBasket().containsKey(lecture.getKey())) {
            top.error("[ERROR] Lecture " + lecture.getSection() + " is already in your basket");
            return false;
        }
        if (account.getBasket().containsKey(lab.getKey())) {
            top.error("[ERROR] Lab " + lab.getSection() + " is already in your basket");
            return false;
        }
        
        // Check if user already has any section of this course
        for (Offering existing : account.getBasket().values()) {
            if (existing.getCode().equalsIgnoreCase(code)) {
                top.error("[ERROR] You already have " + existing.getCode() + " " + 
                        existing.getSection() + " in your basket");
                return false;
            }
        }
        
        // Check for conflicts with lecture
        for (Offering existing : account.getBasket().values()) {
            if (conflicts(existing, lecture)) {
                top.error("[CONFLICT] Lecture " + lecture.getSection() + 
                        " conflicts with " + existing.getCode() + " " + existing.getSection());
                return false;
            }
        }
        
        // Check for conflicts with lab
        for (Offering existing : account.getBasket().values()) {
            if (conflicts(existing, lab)) {
                top.error("[CONFLICT] Lab " + lab.getSection() + 
                        " conflicts with " + existing.getCode() + " " + existing.getSection());
                return false;
            }
        }
        
        // Add both to basket
        account.addToBasket(lecture);
        account.addToBasket(lab);
        
        top.success("[SUCCESS] Added " + lecture.getCode() + " " + 
                lecture.getSection() + " (Lecture) and " + 
                lab.getSection() + " (Lab)");
        return true;
    }

    // Check if two offerings overlap in day/time
    private static boolean conflicts(Offering a, Offering b) {
        // Skip if either has no time/day info
        if (a.getTime() == null || a.getDay() == null || 
            b.getTime() == null || b.getDay() == null) {
            return false;
        }
        
        // Check if they share any common days
        if (!daysOverlap(a.getDay(), b.getDay())) {
            return false;
        }

        // Parse times with format "HH:MM-HH:MM" or "H:MM-H:MM"
        try {
            String[] aTimes = a.getTime().split("-");
            String[] bTimes = b.getTime().split("-");

            int aStart = toMinutes(aTimes[0]);
            int aEnd = toMinutes(aTimes[1]);
            int bStart = toMinutes(bTimes[0]);
            int bEnd = toMinutes(bTimes[1]);

            // Overlap logic: A and B overlap if A starts before B ends AND B starts before A ends
            return (aStart < bEnd && bStart < aEnd);
        } catch (Exception e) {
            return false; // If parsing fails, assume no conflict
        }
    }
    
    // Check if two day strings have any overlap
    private static boolean daysOverlap(String days1, String days2) {
        days1 = days1.toUpperCase();
        days2 = days2.toUpperCase();
        
        // Common day abbreviations
        String[] dayAbbr = {"MON", "TUES", "WED", "THURS", "FRI", "SAT", "SUN", "M", "T", "W", "TH", "F", "S"};
        
        for (String day : dayAbbr) {
            if (days1.contains(day) && days2.contains(day)) {
                return true;
            }
        }
        return false;
    }

    // Helper Method: Convert "HH:MM" or "H:MM" to minutes since midnight
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
        int min = Integer.parseInt(parts[1]);
      
        // Handle PM times (1-6 are assumed PM, 7-12 stay as is)
        if (hour >= 1 && hour <= 6) { 
            hour += 12; 
        }
        
        return hour * 60 + min;
    }
}
