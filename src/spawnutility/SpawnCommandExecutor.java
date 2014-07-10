package spawnutility;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		else if (commandName.equalsIgnoreCase("setspawn"))
		{
			if(player.hasPermission("SpawnUtility.setspawn"))
			{
				SpawnListener.stickPlayer = player;
				player.sendMessage(ChatColor.GREEN + "Right click a block with a stick to set the spawn.");
			}
			else
				player.sendMessage(ChatColor.RED + "You don't have permission to do that.");
			
			return true;
		}
		
		return false;
	}
}
