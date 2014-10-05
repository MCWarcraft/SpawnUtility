package spawnutility;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import core.Event.PlayerVoidDamageEvent;

public class SpawnListener implements Listener
{
	private SpawnUtility plugin;	
	
	public SpawnListener(SpawnUtility plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		event.setQuitMessage(ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GRAY + " left.");
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		//plugin.teleportToSpawn(event.getPlayer());
		
		event.setJoinMessage(ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GRAY + " joined.");
		event.getPlayer().sendMessage(ChatColor.RED + "Welcome to " + ChatColor.GOLD + "Mc" + ChatColor.GRAY + "Warcraft" + ChatColor.RED + "! Type /help for help.");
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerVoidDamage(PlayerVoidDamageEvent event)
	{
		if (event.isUsed())
			return;
		plugin.teleportToSpawn(event.getPlayer());
		event.use();
	}
}
