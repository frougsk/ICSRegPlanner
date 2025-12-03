package fileHandlers;

import bases.Course;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CourseLoader {

    private ArrayList<Course> bs = new ArrayList<>();
    private ArrayList<Course> master = new ArrayList<>();
    private ArrayList<Course> phd = new ArrayList<>();
    private ArrayList<Course> mit = new ArrayList<>();

    public ArrayList<Course> getBS() {
        bs = getCourses(Path.of("data", "ics_cmsc_courses.csv"));
        return bs;
    }

    public ArrayList<Course> getMasters() {
        master = getCourses(Path.of("data", "ics_mscs_courses.csv"));
        return master;
    }

    public ArrayList<Course> getPHD() {
        phd = getCourses(Path.of("data", "ics_phd_courses.csv"));
        return phd;
    }

    public ArrayList<Course> getMITS() {
        mit = getCourses(Path.of("data", "ics_mit_courses.csv"));
        return mit;
    }

    public ArrayList<Course> getCourses(Path path) {
        ArrayList<Course> courses = new ArrayList<>();
        System.out.println("Loading CSV from: " + path.toAbsolutePath());

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                // split raw into tokens
                String[] raw = line.split(",");

                if (raw.length < 3) {
                    System.out.println("Skipping invalid row: " + line);
                    continue;
                }

                String code = raw[0].trim();
                StringBuilder nameSB = new StringBuilder();
                int unitIndex = -1;

                // find first integer/ units col
                for (int i = 1; i < raw.length; i++) {
                    try {
                        Integer.parseInt(raw[i].trim());
                        unitIndex = i;
                        break;
                    } catch (NumberFormatException ignore) {
                        nameSB.append(raw[i]).append(", ");
                    }
                }

                if (unitIndex == -1) {
                    System.out.println("Skipping row with invalid units: " + line);
                    continue;
                }

                // clean
                String name = nameSB.toString().trim();
                if (name.endsWith(",")) {
                    name = name.substring(0, name.length() - 1);
                }

                int units = Integer.parseInt(raw[unitIndex].trim());

                // all after integer is description
                StringBuilder descSB = new StringBuilder();
                for (int i = unitIndex + 1; i < raw.length; i++) {
                    descSB.append(raw[i]).append(", ");
                }

                String description = descSB.toString().trim();
                if (description.endsWith(",")) {
                    description = description.substring(0, description.length() - 1);
                }

                // determine type
                String file = path.getFileName().toString();
                String type =
                        file.equals("ics_cmsc_courses.csv") ? Course.BSCS :
                        file.equals("ics_mit_courses.csv") ? Course.MIT :
                        file.equals("ics_mscs_courses.csv") ? Course.MASTER :
                        Course.PHD;

                courses.add(new Course(code, name, units, description, type));
            }

        } catch (IOException e) {
            System.out.println("Error reading courses: " + e.getMessage());
        }

        return courses;
    }

}
