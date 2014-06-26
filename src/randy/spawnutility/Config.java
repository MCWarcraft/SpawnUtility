package randy.spawnutility;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	//File
	static File configfile = new File("plugins" + File.separator + "SpawnUtility" + File.separator + "config.yml");
	static FileConfiguration configuration = YamlConfiguration.loadConfiguration(configfile);

	//Load config
	public static void LoadConfig(){

		File directory = new File("plugins" + File.separator + "SpawnUtility");
		if(!directory.exists()){
			directory.mkdir();
		}

		//Create file if it does not exist
		if(!configfile.exists()) {
			try {
				configfile.createNewFile();
			} catch (IOException e) {
				System.out.print("The config file could not be created.");
			}
		}
		
		//General info
		if(configuration.contains("General.SpawnLocation")){
			String[] locSplit = configuration.getString("General.SpawnLocation").split(":");
			main.spawnLocation = new Location(Bukkit.getWorld("world"), Integer.parseInt(locSplit[0]), Integer.parseInt(locSplit[1]), Integer.parseInt(locSplit[2]), Float.parseFloat(locSplit[3]), 0);
		}
		if(configuration.contains("General.TaggedOwners")){
			String[] players = configuration.getString("General.TaggedOwners").split(",");
			for(int i = 0; i < players.length; i++){
				main.owners.add(players[i]);
			}
		}
		if(configuration.contains("General.TaggedStaff")){
			String[] players = configuration.getString("General.TaggedStaff").split(",");
			for(int i = 0; i < players.length; i++){
				main.staff.add(players[i]);
			}
		}
	}

	public static void SaveConfig(){
		
		//General info
		if(main.spawnLocation != null){
			Location minigameLobbyLoc = main.spawnLocation;
			configuration.set("General.SpawnLocation", minigameLobbyLoc.getBlockX() + ":" +  minigameLobbyLoc.getBlockY()+ ":" + minigameLobbyLoc.getBlockZ()+ ":" + minigameLobbyLoc.getYaw());
		}
		
		if(!main.owners.isEmpty()){
			String playerString = null;
			for(int i = 0; i < main.owners.size(); i++){
				String player = main.owners.get(i);
				if(playerString == null) playerString = player;
				else playerString += "," + player;
			}
			configuration.set("General.TaggedOwners", playerString);
		}
		
		if(!main.staff.isEmpty()){
			String playerString = null;
			for(int i = 0; i < main.staff.size(); i++){
				String player = main.staff.get(i);
				if(playerString == null) playerString = player;
				else playerString += "," + player;
			}
			configuration.set("General.TaggedStaff", playerString);
		}
		
		//Save file
		try {
			configuration.save(configfile);
		} catch (IOException e) {
			System.out.print("The config file could not be saved.");
		}
	}
}
