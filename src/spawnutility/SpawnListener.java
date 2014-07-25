package spawnutility;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
		plugin.teleportToSpawn(event.getPlayer());
		
		event.setJoinMessage(ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GRAY + " joined.");
		event.getPlayer().sendMessage(ChatColor.RED + "Welcome to " + ChatColor.GOLD + "Mc" + ChatColor.GRAY + "Warcraft" + ChatColor.RED + "! Type /help for help.");
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event)
	{
		if (event.getCause() != DamageCause.VOID)
			return;
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		plugin.teleportToSpawn(player);
	}
}
