package main.java.de.relluem94.minecraft.server.spigot.essentials.events.skills;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class Ev_AutoReplant implements Listener{

	private Material[] plants = {
			Material.CARROTS, 
			Material.POTATOES, 
			Material.WHEAT, 
			Material.BEETROOTS
	};	
	
	private Material[] seeds = {
			Material.CARROT, 
			Material.POTATO, 
			Material.WHEAT_SEEDS, 
			Material.BEETROOT_SEEDS
	};	
	
	@EventHandler
	public void onHarvest(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		PlayerInventory i = p.getInventory();
		if(e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
			if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				for(int x = 0; x < plants.length; x++) {
					check(e.getClickedBlock(), i, x);
				}
			}
		}
	}
	
	private void check(Block b, PlayerInventory i, int x) {
		if(b.getType().equals(plants[x]) && isMature(b)){
			ItemStack seed = new ItemStack(seeds[x], 1);
			int inv_size = i.getSize();
			if(inv_size != 0) {
				for(ItemStack is : i.getContents()) {
					if(is != null && is.getType().equals(seeds[x])) {
						try {
							i.removeItem(seed);
						}
						catch (AssertionError e) {
							System.out.println(e.getMessage());
						}
						b.breakNaturally();
						b.setType(plants[x]);
					}
				}
			}
		}
	}
	
	private boolean isMature(Block block) {
		Ageable a = ((Ageable) block.getBlockData());
		if(a.getAge() == a.getMaximumAge()) {
			return true;
		}
		else {
			return false;
		}
	}
}
