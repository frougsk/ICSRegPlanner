package fileHandlers;

import bases.Account;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AccountLoader {

	// Read user data
	public ArrayList<Account> getAccounts(Path path){
		ArrayList<Account> accounts = new ArrayList<>();
		
		try(BufferedReader br = Files.newBufferedReader(path)){
			String line;
			line = br.readLine();	// Skip header
			
			while((line = br.readLine()) != null) {
				String[] accDetails = line.split(",");
				String email = accDetails[0]; 
				String first = accDetails[1]; 
				String middle = accDetails[2]; 
				String last = accDetails[3]; 
				String program = accDetails[4]; 
				String password = accDetails[5];
				
				// Add new account
				Account newAcc = new Account(email, first, middle, last, program, password);
				accounts.add(newAcc);
			}
			
			br.close();
			
		}catch (IOException e) {
            System.out.println("Error reading albums: " + e.getMessage());
        }
		
		return accounts;
	}
		
}

