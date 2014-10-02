package spawnutility;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import core.Utilities.LocationSelector;

public class SpawnCommandExecutor implements CommandExecutor
{
	private SpawnUtility plugin;
	
	public SpawnCommandExecutor(SpawnUtility plugin)
	{
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandName, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only player can use spawn commands.");
			return true;
		}
			
		Player player = (Player) sender;

		//If the player is going to spawn
		if(commandName.equalsIgnoreCase("spawn"))
		{
			plugin.teleportToSpawn(player);
			return true;
		}
		//If the player is trying to set spawn
		else if (commandName.equalsIgnoreCase("setspawn") && player.hasPermission("spawnutility.setspawn"))
		{
			if (LocationSelector.getSelectedLocation(player.getUniqueId()) != null)
			{
				plugin.setSpawnLocation(LocationSelector.getSelectedLocation(player.getUniqueId()));
				player.sendMessage(ChatColor.GREEN + "The spawn has been set.");
			}
			else
				player.sendMessage(ChatColor.RED + "Please select a location with a stick.");
			
			return true;
		}
		
		return false;
	}
}
