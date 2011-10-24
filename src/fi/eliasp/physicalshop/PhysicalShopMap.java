package fi.eliasp.physicalshop;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import org.bukkit.Material;

public class PhysicalShopMap {
	
	public static File folder;
	
	public static void addShop(String playerName, Material material, Rate buyRate, Rate sellRate, String worldName, double x, double y, double z) {
		// Read in shop list
		HashMap<String, HashMap<String,String>> shopMap = getMap();
		
		// Create comma separated shop string
		String location = worldName + "," + x + "," + y + "," + z;
		int buy = 0;
		int sell = 0;
		if (buyRate != null) {
			buy = buyRate.getPrice();
		}
		if (sellRate != null) {
			sell = sellRate.getPrice();
		}
		String shop = material.toString() + "," + buy + "," + sell;
		
		// check to see if shop is already in list
		// First check if player exists in list
		if (shopMap.containsKey(playerName)) {
			// Shop is in the list
			if (shopMap.get(playerName).containsKey(location)) {
				// A shop already exists in database at this location
				// This shouldn't happen
				// TODO: Remove old entry, add this one?
				shopMap.get(playerName).remove(location);
				shopMap.get(playerName).put(location, shop);
			}
			// Shop is not in list
			else {
				shopMap.get(playerName).put(location,shop);				
			}
		}
		// If player doesn't exist, create new entry and add shop
		else {
			shopMap.put(playerName, new HashMap<String,String>());
			shopMap.get(playerName).put(location,shop);
		}
		// Write the file out
		saveMap(shopMap);
		
	}
	
	public static void removeShop(String playerName, String worldName, double x, double y, double z) {
		// Read in shop list
		HashMap<String, HashMap<String,String>> shopMap = getMap();
		
		// Create comma separated shop location string
		String location = worldName + "," + x + "," + y + "," + z;
		
		// check to see if shop is already in list
		// First check if player exists in list
		if (shopMap.containsKey(playerName)) {
			// Shop is in the list
			if (shopMap.get(playerName).containsKey(location)) {
				shopMap.get(playerName).remove(location);
			}
			// Shop is not in list
			else {
				// Shop doesn't exist in list
			    // It's already removed, so there's nothing to do here.
			}
			
			// Check to see if player has any remaining shops
			// If they do not, remove them from the list
			if (shopMap.get(playerName).isEmpty()) {
				shopMap.remove(playerName);
			}
		}
		// If player doesn't exist, this player has no shops
		else {
			// This player has no shops, nothing to remove
		}
		
		// Write the file out
		saveMap(shopMap);		
	}
	
	private static void saveMap(HashMap<String, HashMap<String,String>> shopMap) {
			
		try {
			// Open the file
			if (!folder.exists()) {
				folder.mkdir();
			}			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(folder, "shopMap.csv")));
			
			// Iterate through HashMap and output to file
			// Format is: playerName:worldName,x,y,z:material,buyRate,sellRate
			//            One shop per line.
			//            Assume only 1 shop allowed per location (world,x,y,z)
			
			// For each key in HashMap
			for (String player: shopMap.keySet()) {
				// Iterate through it's list of shops
				for (String location: shopMap.get(player).keySet()) {
					// Write out one shop per line prepended with player name
					writer.write(player + ":" + location + ":" + shopMap.get(player).get(location));
					// Append newline
					writer.newLine();
				}
			}
			// Close the File
			writer.close();
		}
		// Catch any exceptions
		// TODO: Catch specific exceptions and add specific fail msgs
		catch (Exception e) {
			e.printStackTrace();			
		}
		
	}

	private static HashMap<String, HashMap<String,String>> getMap() {
		
		HashMap<String, HashMap<String,String>> map = new HashMap<String, HashMap<String,String>>();
		String[] tokens;
		String line = null;
		
		try {
			// Open the file
			if (!folder.exists()) {
				folder.mkdir();
			}
			File shopList = new File(folder, "shopMap.csv");
			if (!shopList.exists()) {
				shopList.createNewFile();
			}
			BufferedReader reader = new BufferedReader(new FileReader(shopList));
			// Read in file one line at a time.
			// Format is playerName:worldName,x,y,z:material,buyRate,sellRate
			while ((line = reader.readLine()) != null) {
				//Split line on ':' to get player name and the shops that player has
				tokens = line.split(":");
				//check to see if this player exists
				if (map.containsKey(tokens[0])) {
					//if they do, add shop
					if (!map.get(tokens[0]).containsKey(tokens[1])) {
						map.get(tokens[0]).put(tokens[1], tokens[2]);
					}
					else {
						// A shop already exists at this location
					}
				}
				else {
					//if they don't, add the player and a new shop list
					map.put(tokens[0], new HashMap<String,String>());
					// then add the shop to the list.
					map.get(tokens[0]).put(tokens[1], tokens[2]);
				}						
			}
			// Close the file
			reader.close();
			// Return the HashMap
			return map;
		}
		// Catch exceptions
		// TODO: add specific exceptions and fail messages
		catch (Exception e) {
			e.printStackTrace();
			return map;
		}
		
	}
	
}
