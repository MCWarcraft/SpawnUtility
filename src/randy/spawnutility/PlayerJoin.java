package randy.spawnutility;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Listener;

public class PlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		event.setQuitMessage(ChatColor.WHITE + player.getName() + ChatColor.GRAY + " left.");
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		System.out.println("Pre TP");
		main.TeleportPlayerToSpawn(event.getPlayer());
		System.out.println("Post TP");
		
		event.setJoinMessage(ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GRAY + " joined.");
		event.getPlayer().sendMessage(ChatColor.RED + "Welcome to " + ChatColor.GOLD + "Mc" + ChatColor.GRAY + "Warcraft" + ChatColor.RED + "! Type /help for help.");
	}
}
