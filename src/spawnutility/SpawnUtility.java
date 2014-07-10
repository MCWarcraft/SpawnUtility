package spawnutility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import core.Custody.Custody;
import core.HonorPoints.HonorConnector;
import core.Scoreboard.CoreScoreboardManager;
import core.Scoreboard.DisplayBoard;
import core.Utilities.CoreItems;
import core.Utilities.LocationParser;

public class SpawnUtility extends JavaPlugin
{
	//Location
	private Location spawnLocation;

	private HonorConnector honorConnector = new HonorConnector();
	
	@Override
	public void onEnable()
	{
		//Register events
		getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
		//Register commands
		SpawnCommandExecutor spawnCommandExecutor = new SpawnCommandExecutor(this);
		getCommand("spawn").setExecutor(spawnCommandExecutor);
		getCommand("setspawn").setExecutor(spawnCommandExecutor);
		
		loadData();
		
		getServer().getLogger().info("[SpawnUtility] Succesfully enabled.");
	}
	
	@Override
	public void onDisable()
	{
		Bukkit.getScheduler().cancelAllTasks();
		
		saveData();
		
		this.getServer().getLogger().info("[SpawnUtility] Succesfully disabled.");
	}
	
	public Location getSpawnLocation()
	{
		return this.spawnLocation;
	}
	
	public void setSpawnLocation(Location spawnLocation)
	{
		this.spawnLocation = spawnLocation;
	}
	
	public void teleportToSpawn(Player player)
	{
		if (spawnLocation == null)
		{
			player.sendMessage(ChatColor.RED + "The spawn hasn't been set");
			return;
		}
		
		//Custody switch
		Custody.switchCustody(player, "spn");
		//Teleport
		player.teleport(spawnLocation);
		//Give proper inventory
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.getInventory().addItem(CoreItems.COMPASS);
		//Generate scoreboard format
		generateScoreboard(player);
		//Update and show
		CoreScoreboardManager.getDisplayBoard(player).update(true);
	}
	
	public void generateScoreboard(Player player)
	{
		DisplayBoard tempBoard = CoreScoreboardManager.getDisplayBoard(player);
		tempBoard.setTitle("Welcome to MCWarcraft " + player.getName() + "!", "");
		tempBoard.putHeader("---------------");
		tempBoard.putField(ChatColor.GREEN + "Honor: ", honorConnector, player.getName());
		tempBoard.putHeader("---------------");
	}
	
	public void saveData()
	{
		getConfig().set("spawn", LocationParser.locationToString(spawnLocation));
		saveConfig();
	}
	
	public void loadData()
	{
		setSpawnLocation(LocationParser.parseLocation(getConfig().getString("spawn")));
	}
}