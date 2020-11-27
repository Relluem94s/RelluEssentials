package main.java.de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;



public class BetterBlockDrop implements Listener{
	
	private Material[] blocks2Drop = {
			Material.GLASS, 
			Material.GLASS_PANE,
			Material.BLACK_STAINED_GLASS,
			Material.BLACK_STAINED_GLASS_PANE,
			Material.BLUE_STAINED_GLASS,
			Material.BLUE_STAINED_GLASS_PANE,
			Material.BROWN_STAINED_GLASS,
			Material.BROWN_STAINED_GLASS_PANE,
			Material.CYAN_STAINED_GLASS,
			Material.CYAN_STAINED_GLASS_PANE,
			Material.GRAY_STAINED_GLASS,
			Material.GRAY_STAINED_GLASS_PANE,
			Material.GREEN_STAINED_GLASS,
			Material.GREEN_STAINED_GLASS_PANE,
			Material.LIGHT_BLUE_STAINED_GLASS,
			Material.LIGHT_BLUE_STAINED_GLASS_PANE,
			Material.LIGHT_GRAY_STAINED_GLASS,
			Material.LIGHT_GRAY_STAINED_GLASS_PANE,
			Material.LIME_STAINED_GLASS,
			Material.LIME_STAINED_GLASS_PANE,
			Material.MAGENTA_STAINED_GLASS,
			Material.MAGENTA_STAINED_GLASS_PANE,
			Material.ORANGE_STAINED_GLASS,
			Material.ORANGE_STAINED_GLASS_PANE,
			Material.PINK_STAINED_GLASS,
			Material.PINK_STAINED_GLASS_PANE,
			Material.PURPLE_STAINED_GLASS,
			Material.PURPLE_STAINED_GLASS_PANE,
			Material.RED_STAINED_GLASS,
			Material.RED_STAINED_GLASS_PANE,
			Material.WHITE_STAINED_GLASS,
			Material.WHITE_STAINED_GLASS_PANE,
			Material.YELLOW_STAINED_GLASS,
			Material.YELLOW_STAINED_GLASS_PANE
			};
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Material m = e.getBlock().getBlockData().getMaterial();
		for(Material b2d: blocks2Drop) {
			if(m == b2d) {
				ItemStack is = new ItemStack(b2d , 1);
				Player p = (Player) e.getPlayer();
				if(Permission.isAuthorized(p, 2)) {
					p.getWorld().dropItem(e.getBlock().getLocation(), is);
					e.setDropItems(false);
				}
			}
		}
	}
}
