package fileHandlers;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import bases.Account;
import bases.Offering;

public class BasketLoader {
	public void loadBasket(ArrayList<Account> accounts, Path path) {
		try(BufferedReader br = Files.newBufferedReader(path)){
			String line;
			br.readLine();	// Skip header
			
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if(parts.length < 8) continue;	// Precaution for weird data
				
				String email = parts[0];
				String code = parts[1];
				String title = parts[2];
				int units = Integer.parseInt(parts[0]);
				String section = parts[4];
				String time = parts[5];
				String day = parts[6];
				String room = parts[7];
				
				Offering offering = new Offering(code, title, units, section, time, day, room);
				
				// Add the course to the planner of the user with matched account
				for(Account acc : accounts) {
					if(acc.getEmailAddress().equalsIgnoreCase(email)) acc.addToBasket(offering);;
					break;
				}
			}
			
			br.close();
			
		}catch (IOException e) {
            System.out.println("Error reading basket: " + e.getMessage());
        }
	}
}
