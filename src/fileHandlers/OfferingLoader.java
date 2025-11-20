package fileHandlers;

//Package imports
import bases.Offering;
import bases.YearSet;

//Java imports
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class OfferingLoader {
	public YearSet getOfferings(Path path){
		String acadYear = null;
		ArrayList<Offering> offerings = new ArrayList<>();
		
		try(BufferedReader br = Files.newBufferedReader(path)){	
			acadYear = br.readLine();	// Read the first line
			br.readLine();						// Skip header
			
			// Get offering details
			String line;
			while((line = br.readLine()) != null) {
				String[] oDetails = line.split(",");
				
				String code = oDetails[0];
				String title = oDetails[1];
				int units = Integer.parseInt(oDetails[2]);
				String section = oDetails[3];
				String time = oDetails[4];
				String day = oDetails[5];
				String room = oDetails[6];
				
				// Add offering to arraylist
				Offering offer = new Offering(code, title, units, section, time, day, room);
				offerings.add(offer);
			}
		}catch (IOException e) {
            System.out.println("Error reading courses: " + e.getMessage());
        }
		
		// Create set of offerings for the academic year
		YearSet ay = new YearSet(acadYear, offerings);
		return ay;
	}
}
