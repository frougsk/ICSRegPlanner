package fileHandlers;

// Package imports
import bases.Course;

// Java imports
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CourseLoader {
	// Attributes / Temp Storage
	private final ArrayList<Course> bs = new ArrayList<>();
	private final ArrayList<Course> master = new ArrayList<>();
	private final ArrayList<Course> phd = new ArrayList<>();
	private final ArrayList<Course> mit = new ArrayList<>();
	
	// Getters
	public ArrayList<Course> getBS() { return bs; }
	public ArrayList<Course> getMasers() { return master; }
	public ArrayList<Course> getPHD() { return phd; }
	public ArrayList<Course> getMITS() { return mit; }
	
	// Get Courses
	public static ArrayList<Course> getCoures(Path path){
		ArrayList<Course> courses = new ArrayList<>();
		
		try(BufferedReader br = Files.newBufferedReader(path)){
			String line;
			br.readLine();	// Skip header
			
			while((line = br.readLine()) != null) {
				String[] cDetails = line.split(",");
				
				String code = cDetails[0];
				String name = cDetails[1];
				int unit;
				String description;
				
				// Handle CSV auto separation on name
				if(cDetails.length != 4) {
					name = name + " " + cDetails[2];
					unit = Integer.parseInt(cDetails[3]);
					description = cDetails[4];
				}else {
					unit = Integer.parseInt(cDetails[2]);
					description = cDetails[3];
				}
				
				// Check path for type
				String file = path.getFileName().toString();
				String type;
				
				if(file.equals("ics_cmsc_courses.csv")) type = Course.BSCS;
				else if(file.equals("ics_mit_courses.csv")) type = Course.MIT;
				else if(file.equals("ics_mscs_course.csv")) type = Course.MASTER;
				else type = Course.PHD;
				
				// Create course
				Course c = new Course(code, name, unit, description, type);
				courses.add(c);
				
				// Add to storage arraylist
				if(type == Course.BSCS) bs.add(c);
				else if(type == Course.MIT) mit.add(c);
				else if(type == Course.MASTER) master.add(c);
				else phd.add(c);
			}
			
			return courses;
			
		}catch (IOException e) {
            System.out.println("Error reading courses: " + e.getMessage());
        }
		
		return courses;
	}
}
