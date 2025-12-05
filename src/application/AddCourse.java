package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bases.Account;
import bases.Offering;
import planning.Planner_BasketView;

public class AddCourse {

    // Main add course method - checks for binding requirements
    @SuppressWarnings("exports")
	public static boolean addCourse(Account account, Offering newCourse, Planner_BasketView mid) {
        return addCourse(account, newCourse, mid, null);
    }
    
    @SuppressWarnings("exports")
	public static boolean addCourse(Account account, Offering newCourse, Planner_BasketView mid,
                                    Map<String, Offering> allOfferings) {

        mid.setWarning(null);
        String newCode = newCourse.getCode().trim().toUpperCase();

        // Check if offering has a binding requirement
        // Binding meaning if that lecture has a lab
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

                        mid.error("[BINDING REQUIRED] " + lecture.getCode() + " " +
                                lecture.getSection() + " requires " +
                                lab.getSection());

                    } else {
                        mid.error("[BINDING ERROR] Required paired section not found: " + boundKey);
                    }
                } else {
                    mid.error("[BINDING REQUIRED] This section requires its paired lecture/lab. " +
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

        // Check if this exact offering is already in basket
        if (account.getBasket().containsKey(newCourse.getKey())) {
            mid.error("[ERROR] This course is already in your basket.");
            return false;
        }

        // If course already exists with different section, NOO
        if (!sameCode.isEmpty()) {
            Offering existing = sameCode.get(0);
            mid.error("[ERROR] You already have " + existing.getCode() + " in your basket.");
            return false;
        }

        // Schedule conflict check iF TBA then skip
        if (newCourse.getTime() != null && !newCourse.getTime().equalsIgnoreCase("TBA")) {
            for (Offering existing : account.getBasket().values()) {
                if (conflicts(existing, newCourse)) {
                    mid.error("[ERROR] " + newCourse.getCode() + " " + newCourse.getSection() +
                            " overlaps with " + existing.getCode() + " " + existing.getSection());
                    return false;
                }
            }
        }

        // Add to basket
        account.addToBasket(newCourse);
        mid.success("[SUCCESS] Successfully added " + newCourse.getCode() + " " + newCourse.getSection());
        return true;
    }


    // Add both lecture and lab together
    @SuppressWarnings("exports")
	public static boolean addBoundPair(Account account, Offering offering1, Offering offering2,
                                       Planner_BasketView mid) {

        mid.setWarning(null);

        // Determine which is lecture and which is lab
        Offering lecture;
        Offering lab;

        if (offering1.isLab()) {
            // offering1 is the lab, so offering2 must be the lecture
            lecture = offering2;
            lab = offering1;
        } else {
            // offering1 is the lecture, so offering2 must be the lab
            lecture = offering1;
            lab = offering2;
        }

        String code = lecture.getCode();
        String lectureSection = lecture.getSection().toUpperCase();
        String labSection = lab.getSection().toUpperCase();

        // Extract lab prefix (e.g., "G" from "G-1L")
        String labPrefix;
        if (labSection.contains("-")) {
            // Example: CMSC22-UV5L to CMSC22
            labPrefix = labSection.split("-")[0];
        } else {
            // Example: CMSC22UV5L-CMSC22
            int index = labSection.lastIndexOf('L');
            labPrefix = (index > 0) ? labSection.substring(0, index) : labSection;
        }


        // Verify the lecture and lab match each other
        if (!lectureSection.equals(labPrefix)) {
            mid.error("[ERROR] Section mismatch. Lecture " + lectureSection +
                    " doesn't match Lab " + labSection);
            return false;
        }

        // Check if this exact lecture is already in basket
        if (account.getBasket().containsKey(lecture.getKey())) {
            mid.error("[ERROR] Lecture " + lecture.getSection() + " is already in your basket");
            return false;
        }

        // Check if this exact lab is already in basket
        if (account.getBasket().containsKey(lab.getKey())) {
            mid.error("[ERROR] Lab " + lab.getSection() + " is already in your basket");
            return false;
        }

        // Count existing offerings of this course code
        int lectureCount = 0;
        int labCount = 0;

        for (Offering existing : account.getBasket().values()) {
            if (existing.getCode().equalsIgnoreCase(code)) {
                String existingSection = existing.getSection().toUpperCase();
                if (existingSection.endsWith("L")) {
                    labCount++;
                } else {
                    lectureCount++;
                }
            }
        }

        // If we already have both a lecture and a lab for this course, reject
        if (lectureCount > 0 && labCount > 0) {
            mid.error("[ERROR] You already have this course in your planner.");
            return false;
        }

        // If we have a lecture but no lab, make sure the new lab matches
        if (lectureCount > 0 && labCount == 0) {
            for (Offering existing : account.getBasket().values()) {
                if (existing.getCode().equalsIgnoreCase(code)
                        && !existing.getSection().toUpperCase().endsWith("L")) {

                    String existingLectureSection = existing.getSection().toUpperCase();

                    if (!existingLectureSection.equals(labPrefix)) {
                        mid.error("[ERROR] Lab " + labSection + " doesn't match existing lecture " +
                                existingLectureSection);
                        return false;
                    }
                }
            }
        }

        // If we have a lab but no lecture, make sure the new lecture matches
        if (labCount > 0 && lectureCount == 0) {
            for (Offering existing : account.getBasket().values()) {
                if (existing.getCode().equalsIgnoreCase(code)
                        && existing.getSection().toUpperCase().endsWith("L")) {

                    String existingLabSection = existing.getSection().toUpperCase();
                    String existingLabPrefix = existingLabSection.contains("-")
                            ? existingLabSection.split("-")[0]
                            : existingLabSection.replace("L", "");

                    if (!existingLabPrefix.equals(lectureSection)) {
                        mid.error("[ERROR] Lecture " + lectureSection +
                                " doesn't match existing lab " + existingLabSection);
                        return false;
                    }
                }
            }
        }

        // Check for time conflicts with lecture
        for (Offering existing : account.getBasket().values()) {
            if (conflicts(existing, lecture)) {
                mid.error("[CONFLICT] Lecture " + lecture.getSection() +
                        " conflicts with " + existing.getCode() + " " + existing.getSection());
                return false;
            }
        }

        // Check for time conflicts with lab
        for (Offering existing : account.getBasket().values()) {
            if (conflicts(existing, lab)) {
                mid.error("[CONFLICT] Lab " + lab.getSection() +
                        " conflicts with " + existing.getCode() + " " + existing.getSection());
                return false;
            }
        }

        // All checks passed - add both to basket
        account.addToBasket(lecture);
        account.addToBasket(lab);

        mid.success("[SUCCESS] Added " + lecture.getCode() + " " +
                lecture.getSection() + " - " +
                lab.getSection());

        return true;
    }


    // Check if two offerings overlap in day/time
    private static boolean conflicts(Offering a, Offering b) {

        // Skip if either has no time/day info or if times are TBA
        if (a.getTime() == null || a.getDay() == null ||
                b.getTime() == null || b.getDay() == null ||
                a.getTime().equalsIgnoreCase("TBA") ||
                b.getTime().equalsIgnoreCase("TBA")) {
            return false;
        }

        // Check if they share any common days
        if (!daysOverlap(a.getDay(), b.getDay())) {
            return false;
        }

        // Parse times "HH:MM-HH:MM"
        try {
            String[] aTimes = a.getTime().split("-");
            String[] bTimes = b.getTime().split("-");

            int aStart = toMinutes(aTimes[0]);
            int aEnd = toMinutes(aTimes[1]);
            int bStart = toMinutes(bTimes[0]);
            int bEnd = toMinutes(bTimes[1]);

            return (aStart < bEnd && bStart < aEnd);

        } catch (Exception e) {
            System.out.println("Error checking conflict between " +
                    a.getCode() + " and " + b.getCode() + ": " + e.getMessage());
            return false;
        }
    }


    // Check if two day strings overlap
    private static boolean daysOverlap(String days1, String days2) {

        if (days1 == null || days2 == null) return false;

        days1 = days1.toUpperCase().trim();
        days2 = days2.toUpperCase().trim();

        List<String> parsed1 = parseDays(days1);
        List<String> parsed2 = parseDays(days2);

        for (String d1 : parsed1) {
            for (String d2 : parsed2) {
                if (d1.equals(d2)) return true;
            }
        }

        return false;
    }


    // Parse day strings into list
    private static List<String> parseDays(String dayStr) {

        List<String> days = new ArrayList<>();
        if (dayStr == null || dayStr.isEmpty()) return days;

        dayStr = dayStr.toUpperCase().trim();

        List<String> abbrOrder = List.of(
                "THURS", "TUES", "WED", "FRI", "SAT", "SUN",
                "MON", "TH", "T", "W", "F", "S", "M"
        );

        Map<String, String> dayMap = Map.ofEntries(
                Map.entry("MONDAY", "MON"),
                Map.entry("TUESDAY", "TUES"),
                Map.entry("WEDNESDAY", "WED"),
                Map.entry("THURSDAY", "THURS"),
                Map.entry("FRIDAY", "FRI"),
                Map.entry("SATURDAY", "SAT"),
                Map.entry("SUNDAY", "SUN"),
                Map.entry("THURS", "THURS"),
                Map.entry("TUES", "TUES"),
                Map.entry("WED", "WED"),
                Map.entry("FRI", "FRI"),
                Map.entry("SAT", "SAT"),
                Map.entry("SUN", "SUN"),
                Map.entry("MON", "MON"),
                Map.entry("TH", "THURS"),
                Map.entry("T", "TUES"),
                Map.entry("W", "WED"),
                Map.entry("F", "FRI"),
                Map.entry("S", "SAT"),
                Map.entry("M", "MON")
        );

        int i = 0;
        while (i < dayStr.length()) {
            boolean matched = false;

            for (String abbr : abbrOrder) {
                if (dayStr.startsWith(abbr, i)) {
                    String normalized = dayMap.get(abbr);

                    if (normalized != null && !days.contains(normalized)) {
                        days.add(normalized);
                    }

                    i += abbr.length();
                    matched = true;
                    break;
                }
            }

            if (!matched) i++;
        }

        return days;
    }


    // Convert "HH:MM" to minutes from midnight
    static int toMinutes(String time) {
        time = time.trim();
        String[] parts = time.split(":");

        int hour = Integer.parseInt(parts[0].trim());
        int min = (parts.length > 1) ? Integer.parseInt(parts[1].trim()) : 0;

        return hour * 60 + min;  // direct conversion
    }
}
