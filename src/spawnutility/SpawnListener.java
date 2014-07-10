package spawnutility;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SpawnListener implements Listener
{
	private SpawnUtility plugin;

	public static Player stickPlayer;
	private int step = 0;
	
	
	public SpawnListener(SpawnUtility plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(stickPlayer != null && stickPlayer == player)
			{
				//Stick in hand?
				if(player.getItemInHand().getType() == Material.STICK)
				{
					if(step == 0)
					{
						Location blockLoc = event.getClickedBlock().getLocation();
						Location newLoc = new Location(blockLoc.getWorld(), blockLoc.getBlockX(), blockLoc.getBlockY() + 1, blockLoc.getBlockZ());
						plugin.setSpawnLocation(newLoc);
						stickPlayer.sendMessage(ChatColor.GREEN + "Spawn position set, right click in the direction you want the player to face when teleporting.");
						step++;
					}
					else if(step == 1)
					{
						plugin.setSpawnLocation(plugin.getSpawnLocation().setDirection(player.getLocation().getDirection()));
						stickPlayer.sendMessage(ChatColor.GREEN + "Spawn position and rotation set.");
						stickPlayer = null;
						step--;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		event.setQuitMessage(ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GRAY + " left.");
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		plugin.teleportToSpawn(event.getPlayer());
		
		event.setJoinMessage(ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GRAY + " joined.");
		event.getPlayer().sendMessage(ChatColor.RED + "Welcome to " + ChatColor.GOLD + "Mc" + ChatColor.GRAY + "Warcraft" + ChatColor.RED + "! Type /help for help.");
	}
}
