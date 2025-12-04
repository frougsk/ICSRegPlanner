package fileHandlers;

import bases.Account;
import bases.Offering;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BasketHandler {
    private static final Path ACCOUNTS_DIR = Path.of("accounts_data");
    
    // Save user's basket to their personal file
    public static boolean saveBasket(Account account) {
        try {
            // Create accounts_data directory if it doesn't exist
            if (!Files.exists(ACCOUNTS_DIR)) {
                Files.createDirectories(ACCOUNTS_DIR);
            }
            
            // Save to username_basket.csv
            Path basketFile = ACCOUNTS_DIR.resolve(account.getFirstName() + "_basket.csv");
            
            try (BufferedWriter writer = Files.newBufferedWriter(basketFile)) {
                // Write header
                writer.write("Course Code,Course Title,Units,Section,Times,Days,Rooms");
                writer.newLine();
                
                // Write each offering in basket
                for (Offering o : account.getBasket().values()) {
                    writer.write(String.format("%s,%s,%d,%s,%s,%s,%s",
                        o.getCode(),
                        o.getTitle(),
                        o.getUnits(),
                        o.getSection(),
                        o.getTime(),
                        o.getDay(),
                        o.getRoom()
                    ));
                    writer.newLine();
                }
            }
            
            return true;
        } catch (IOException e) {
            System.out.println("Error saving basket: " + e.getMessage());
            return false;
        }
    }
    
    // Load user's basket from their personal file
    public static boolean loadBasket(Account account) {
        try {
            Path basketFile = ACCOUNTS_DIR.resolve(account.getFirstName() + "_basket.csv");
            
            // If file doesn't exist, that's okay - user has no saved basket
            if (!Files.exists(basketFile)) {
                return true;
            }
            
            // Clear current basket
            account.getBasket().clear();
            
            try (BufferedReader reader = Files.newBufferedReader(basketFile)) {
                reader.readLine(); // Skip header
                
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    
                    if (parts.length >= 7) {
                        String code = parts[0];
                        String title = parts[1];
                        int units = Integer.parseInt(parts[2]);
                        String section = parts[3];
                        String time = parts[4];
                        String day = parts[5];
                        String room = parts[6];
                        
                        Offering offering = new Offering(code, title, units, section, time, day, room);
                        account.addToBasket(offering);
                    }
                }
            }
            
            return true;
        } catch (IOException e) {
            System.out.println("Error loading basket: " + e.getMessage());
            return false;
        }
    }
    
    // Delete user's saved basket
    public static boolean clearSavedBasket(Account account) {
        try {
            Path basketFile = ACCOUNTS_DIR.resolve(account.getFirstName() + "_basket.csv");
            if (Files.exists(basketFile)) {
                Files.delete(basketFile);
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error deleting basket file: " + e.getMessage());
            return false;
        }
    }
}
