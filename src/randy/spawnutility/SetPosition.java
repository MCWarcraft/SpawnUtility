package randy.spawnutility;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SetPosition implements Listener {
	
	public static Player stickPlayer;
	public static int step = 0;
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		
		Player player = event.getPlayer();
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(stickPlayer != null && stickPlayer == player){
				
				//Stick in hand?
				if(player.getItemInHand().getType() == Material.STICK){
					if(step == 0){
						Location blockLoc = event.getClickedBlock().getLocation();
						Location newLoc = new Location(blockLoc.getWorld(), blockLoc.getBlockX(), blockLoc.getBlockY() + 1, blockLoc.getBlockZ());
						main.spawnLocation = newLoc;
						stickPlayer.sendMessage(ChatColor.GREEN + "Spawn position set, right click in the direction you want the player to face when teleporting.");
						step++;
					}else if(step == 1){
						main.spawnLocation = new Location(player.getWorld(), main.spawnLocation.getBlockX(),  main.spawnLocation.getBlockY(), main.spawnLocation.getBlockZ(), player.getLocation().getYaw(), 0);
						stickPlayer.sendMessage(ChatColor.GREEN + "Spawn position and rotation set.");
						stickPlayer = null;
						step--;
					}
				}
			}
		}
	}
	
}
