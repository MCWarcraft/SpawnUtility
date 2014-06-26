package randy.spawnutility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import randy.core.CoreAPI;
import randy.core.CoreScoreboard;
import randy.core.tools.CoreDatabase;

public class main extends JavaPlugin implements Listener{
	
	//Location
	public static Location spawnLocation;
	
	//Scoreboard
	public static HashMap<String, ScrollingText> scrollingTextMap = new HashMap<String, ScrollingText>();
	public static HashMap<String, Integer> textRank = new HashMap<String, Integer>();
	public static HashMap<String, Integer> textHonor = new HashMap<String, Integer>();
	
	//Text scrolling
	public static ArrayList<ScrollingText> scrollingTextList = new ArrayList<ScrollingText>();
	
	//Listeners
	private final PlayerJoin playerJoinListener = new PlayerJoin();
	private final SetPosition setPositionListener = new SetPosition();
	
	//WHO
	public static ArrayList<String> owners = new ArrayList<String>();
	public static ArrayList<String> staff = new ArrayList<String>();
	
	//Timer
	Timer timer = new Timer();
	TimerTask timerTask;
	
	
	@Override
	public void onDisable() {
		Config.SaveConfig();
		
		timer.cancel();		
		timerTask.cancel();
		Bukkit.getScheduler().cancelAllTasks();
		
		System.out.print("[SpawnUtility] Succesfully disabled.");
	}
	
	@Override
	public void onEnable() {
		
		//Register events
		getServer().getPluginManager().registerEvents(playerJoinListener, this);
		getServer().getPluginManager().registerEvents(setPositionListener, this);
		getServer().getPluginManager().registerEvents(this, this);
		
		Config.LoadConfig();
		
		startTimer();
		
		scrollingTextList.add(new ScrollingText("Purchase a Rank for multipliers and more!", ""+ChatColor.WHITE));
		scrollingTextList.add(new ScrollingText("[LEGION]", ChatColor.RED + "Rank: " + ChatColor.AQUA));
		scrollingTextList.add(new ScrollingText("www.mcwarcraft.com",""+ChatColor.WHITE));
		
		System.out.print("[SpawnUtility] Succesfully enabled.");
	}

	public boolean onCommand(CommandSender sender, Command command, String commandName, String[] args){
		if(sender instanceof Player){
			Player player = (Player)sender;
			
			//Spawn
			if(commandName.equalsIgnoreCase("spawn")){
				CoreAPI.ExitGameModes(player);
				TeleportPlayerToSpawn(player);
				
				//CreateScoreboard(player.getName());
				return true;
			}
			
			//Setspawn	
			if(commandName.equalsIgnoreCase("setspawn")){
				if(player.hasPermission("SpawnUtility.setspawn")){
					SetPosition.stickPlayer = player;
					player.sendMessage(ChatColor.GREEN + "Right click a block with a stick to set the spawn.");
				}else{
					player.sendMessage(ChatColor.RED + "You don't have permission to do that.");
				}
				return true;
			}
			
			//Disable kill
			if(commandName.equalsIgnoreCase("kill")){
				player.setHealth(20d);
				player.sendMessage(ChatColor.WHITE + "Unknown command. Type '/help' for help.");
				return true;
			}
			
			//Tag owners
			if(commandName.equalsIgnoreCase("owner")){
				if(player.hasPermission("SpawnUtility.tagowner")){
					if(args.length == 1){
						//if(player.hasPermission("SpawnUtility.tag")){
							String otherPlayerName = args[0];
							Player otherPlayer = Bukkit.getPlayer(otherPlayerName);
							if(otherPlayer != null){
								if(!owners.contains(otherPlayerName)){
									owners.add(otherPlayerName);
									player.sendMessage(ChatColor.GREEN + "Player succesfully tagged as owner!");
									
									//Sort the list right away
									Collections.sort(owners, new Comparator<String>() {
		
										@Override
										public int compare(String p1, String p2) {
											return p1.compareTo(p2);
										}
										
									});
								}else{
									player.sendMessage(ChatColor.RED + "That player is already an owner!");
								}
							}else{
								player.sendMessage(ChatColor.RED + "That player is not found!");
							}
						//}else{
						//	player.sendMessage(ChatColor.RED + "You don't have permission to do that.");
						//}
						return true;
					} else if (args.length == 2){
						if(args[0].equalsIgnoreCase("remove")){
							String otherPlayerName = args[0];
							Player otherPlayer = Bukkit.getPlayer(otherPlayerName);
							if(otherPlayer != null){
								if(owners.contains(otherPlayerName)){
									owners.remove(otherPlayerName);
									player.sendMessage(ChatColor.GREEN + "Player succesfully removed as owner!");
									
									//Sort the list right away
									Collections.sort(owners, new Comparator<String>() {
		
										@Override
										public int compare(String p1, String p2) {
											return p1.compareTo(p2);
										}
										
									});
								}else{
									player.sendMessage(ChatColor.RED + "That player is not an owner!");
								}
							}else{
								player.sendMessage(ChatColor.RED + "That player is not found!");
							}
						}
					}
				}
				
				return true;
			}
			
			//Tag staff
			if(commandName.equalsIgnoreCase("staff")){
				if(player.hasPermission("SpawnUtility.tagstaff")){
					if(args.length == 1){
						//if(player.hasPermission("SpawnUtility.tag")){
						String otherPlayerName = args[0];
						Player otherPlayer = Bukkit.getPlayer(otherPlayerName);
						if(otherPlayer != null){
							if(!staff.contains(otherPlayerName)){
								staff.add(otherPlayerName);
								player.sendMessage(ChatColor.GREEN + "Player succesfully tagged as staff!");
								
								//Sort the list right away
								Collections.sort(staff, new Comparator<String>() {
	
									@Override
									public int compare(String p1, String p2) {
										return p1.compareTo(p2);
									}
									
								});
							}else{
								player.sendMessage(ChatColor.RED + "That player is already staff!");
							}
						}else{
							player.sendMessage(ChatColor.RED + "That player is not found!");
						}
						//}else{
						//	player.sendMessage(ChatColor.RED + "You don't have permission to do that.");
						//}
						return true;
					} else if (args.length == 2){
						if(args[0].equalsIgnoreCase("remove")){
							String otherPlayerName = args[0];
							Player otherPlayer = Bukkit.getPlayer(otherPlayerName);
							if(otherPlayer != null){
								if(staff.contains(otherPlayerName)){
									staff.remove(otherPlayerName);
									player.sendMessage(ChatColor.GREEN + "Player succesfully removed as staff!");
									
									//Sort the list right away
									Collections.sort(staff, new Comparator<String>() {
		
										@Override
										public int compare(String p1, String p2) {
											return p1.compareTo(p2);
										}
										
									});
								}else{
									player.sendMessage(ChatColor.RED + "That player is not an staff!");
								}
							}else{
								player.sendMessage(ChatColor.RED + "That player is not found!");
							}
						}
					}
				}
				return true;
			}
			
			//Who
			if(commandName.equalsIgnoreCase("who")){
				player.sendMessage(ChatColor.RED + "Owner " + ChatColor.GOLD + "Staff " + ChatColor.DARK_PURPLE + "HERO " + ChatColor.AQUA + "LEGION " + ChatColor.GRAY + "Normal");
				
				Player[] op = Bukkit.getOnlinePlayers();
				ArrayList<String> onlinePlayers = new ArrayList<String>();
				for(int i = 0; i < op.length; i++){
					onlinePlayers.add(op[i].getName());
				}
				String whoString = ChatColor.GRAY + "(" + onlinePlayers.size() + "/100): " + ChatColor.RED;
				
				//Owners
				for(int i = 0; i < owners.size(); i++){
					if(onlinePlayers.contains(owners.get(i))){
						whoString += owners.get(i) + " ";
					}
				}
				whoString += ChatColor.GOLD;
				
				//Staff
				for(int i = 0; i < staff.size(); i++){
					if(onlinePlayers.contains(staff.get(i))){
						whoString += staff.get(i) + " ";
					}
				}
				
				//Sort online player list
				if(onlinePlayers.isEmpty()){
					Collections.sort(onlinePlayers, new Comparator<String>() {

						@Override
						public int compare(String p1, String p2) {
							return p1.compareTo(p2);
						}
					
					});
				}
				
				ArrayList<String> heroes = new ArrayList<String>();
				ArrayList<String> legionaires = new ArrayList<String>();
				for(int i = 0; i < onlinePlayers.size(); i++){
					String pl = onlinePlayers.get(i);
					int rank = textRank.get(pl);
					if(rank == 2){
						legionaires.add(pl);
					}else if(rank == 3){
						heroes.add(pl);
					}
				}
				
				whoString += ChatColor.DARK_PURPLE;
				for(int i = 0; i < heroes.size(); i++){
					whoString += heroes.get(i) + " ";
				}
				
				whoString += ChatColor.AQUA;
				for(int i = 0; i < legionaires.size(); i++){
					whoString += legionaires.get(i) + " ";
				}
				
				whoString += ChatColor.GRAY;
				for(int i = 0; i < onlinePlayers.size(); i++){
					String plyr = onlinePlayers.get(i);
					if(!owners.contains(plyr) && !staff.contains(plyr) && !legionaires.contains(plyr) && !heroes.contains(plyr)){
						whoString += plyr + " ";
					}
				}
				
				player.sendMessage(whoString);
				return true;
			}
		}
		return false;
	}
	
	 @EventHandler(priority = EventPriority.HIGHEST)
	 public void onPreprocess(PlayerCommandPreprocessEvent event) {
		 String command = event.getMessage().substring(1);
		 if(!(event.getPlayer().hasPermission("SpawnUtility.tag") || owners.contains(event.getPlayer().getName()) ||
				 (!command.equalsIgnoreCase("help") ||
						 !command.equalsIgnoreCase("who") ||
						 !command.contains("versus") ||
						 !command.contains("minigame") ||
						 !command.equalsIgnoreCase("spawn") ||
						 !command.contains("honor multiplier") ||
						 !command.contains("ffa") ||
						 !command.contains("minigame") ||
						 !command.contains("msg")))) {
			 event.getPlayer().sendMessage("Unknown command. Type '/help' for help.");
		 }
	 }
	
	public static void TeleportPlayerToSpawn(Player player){
		
		//Teleport
		player.teleport(spawnLocation);
		
		//Clear inventory
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[4]);
		
		//Give compass
		ItemStack item = new ItemStack(Material.COMPASS, 1);
		ItemMeta itemMeta = item.getItemMeta();
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + "Right click to go");
		
		itemMeta.setDisplayName(""+ ChatColor.GREEN + ChatColor.BOLD + "Game Menu");
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		
		player.getInventory().addItem(item);
		
		//Remove player from other plugins
		CoreAPI.ExitGameModes(player);
		
		CoreScoreboard.UpdateScoreboard(player.getName());
	}
	
	public static void UpdateScoreboard(String player){	
		
		//System.out.print("Updating scoreboard of " + player);
		ScrollingText scrollingText = scrollingTextMap.get(player);
		if(scrollingText == null){
			scrollingText = new ScrollingText("Welcome " + player, ""+ChatColor.GOLD + ChatColor.BOLD);
			scrollingTextMap.put(player, scrollingText);
			scrollingText.Update();
			scrollingTextList.add(scrollingText);
		}
		
		textRank.put(player, (int)CoreDatabase.GetCurrencyMultiplier(player));
		textHonor.put(player, CoreDatabase.GetCurrency(player));
		
		//Dynamic rank stuff
		/*int rank = textRank.get(player);
		String rankString = null;
		String rankMessage = null;
		if(rank == 1){
			rankMessage = scrollingTextList.get(0).GetString();
			rankString = ""+ChatColor.RED + ChatColor.BOLD + "No Rank";
		}else if(rank == 2){
			rankString = scrollingTextList.get(1).GetString();
			rankMessage = " ";
		}else{
			rankString = ChatColor.RED + "Rank: " + ChatColor.DARK_PURPLE + "[HERO]";
			rankMessage = "  ";
		}*/
		
		CoreScoreboard.SetTitle(player, scrollingText.GetString());
		CoreScoreboard.SetScore(player, ""+ChatColor.GREEN + ChatColor.BOLD + "Honor Points", "spn", 15);
		CoreScoreboard.SetScore(player, ""+ChatColor.WHITE + ChatColor.BOLD + textHonor.get(player), "spn", 14);
		CoreScoreboard.SetScore(player, "   ", "spn", 13);
		CoreScoreboard.SetScore(player, ""+ChatColor.GRAY + "Total " + ChatColor.GOLD + "kills", "spn", 12);
		CoreScoreboard.SetScore(player, ""+ChatColor.WHITE + CoreDatabase.GetTotalKills(player), "spn", 11);
		CoreScoreboard.SetScore(player, "    ", "spn", 10);
		CoreScoreboard.SetScore(player, ""+ChatColor.GRAY + "Total " + ChatColor.GOLD + "deaths", "spn", 9);
		CoreScoreboard.SetScore(player, ""+ChatColor.WHITE + CoreDatabase.GetTotalDeaths(player), "spn", 8);
		CoreScoreboard.SetScore(player, "     ", "spn", 7);
		CoreScoreboard.SetScore(player, ""+ChatColor.WHITE + "--------------", "spn", 6);
	}
	
	public static void SetRank(String player, int rank){
		if(player.length() > 14)
			player = player.substring(0, 14);
		textRank.put(player, rank);
	}
	
	public static void SetHonor(String player, int honor){
		if(player.length() > 14)
			player = player.substring(0, 14);
		textHonor.put(player, honor);
	}
	
	private void startTimer(){

		//Start timer, triggers every 0.1th of a second
		timerTask = new TimerTask() {
			public void run() {
				List<String> players = CoreScoreboard.GetGamemodePlayers("spn");
				//System.out.print("Player list size: " + players.size());
				for(int e = 0; e < players.size(); e++){
					String playerName = players.get(e);
					Player player = Bukkit.getPlayer(playerName);
					if(player != null){
						CoreScoreboard.UpdateScoreboard(playerName);
					}
				}
				
				for(int i = 0; i < scrollingTextList.size(); i++){
					scrollingTextList.get(i).Update();
				}
			}
		};
		timer.schedule(timerTask, 100, 100);
	}
}
