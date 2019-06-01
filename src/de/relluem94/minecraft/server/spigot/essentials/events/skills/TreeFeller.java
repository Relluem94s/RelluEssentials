package de.relluem94.minecraft.server.spigot.essentials.events.skills;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;


public class TreeFeller implements Listener{

	private Material[] logs = {
			Material.OAK_LOG, 
			Material.ACACIA_LOG, 
			Material.BIRCH_LOG, 
			Material.DARK_OAK_LOG, 
			Material.JUNGLE_LOG, 
			Material.SPRUCE_LOG
	};
	
	private Material[] leaves = {
			Material.OAK_LEAVES,
			Material.ACACIA_LEAVES,
			Material.BIRCH_LEAVES,
			Material.DARK_OAK_LEAVES,
			Material.JUNGLE_LEAVES,
			Material.SPRUCE_LEAVES
	};
	
	
	@EventHandler
	public void onFell(BlockBreakEvent e) {
		ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();
		ItemMeta im = itemInHand.getItemMeta();
		if(itemInHand.getType().equals(Material.DIAMOND_AXE)) {
			for(Material b2d: logs) {
				if(e.getBlock().getType().equals(b2d)){
					int damage = fellTree(e.getBlock());
					if(((Damageable) im) != null) {
						((Damageable) im).setDamage(damage);
					}
				}
			}
		}
		itemInHand.setItemMeta(im);
	}
	
	private int fellTree(Block b) {
		int blocks_broken = 0;
		for(int x = -1; x <= 30; x++) {
			Block b2b = b.getRelative(BlockFace.UP, x);
			for(int y = 0; y <= x; y++) {
				Block b2b_w = b2b.getRelative(BlockFace.WEST, y);
				Block b2b_e = b2b.getRelative(BlockFace.EAST, y);
				Block b2b_s = b2b.getRelative(BlockFace.SOUTH, y);
				Block b2b_n = b2b.getRelative(BlockFace.NORTH, y);
				
				blocks_broken += checkBlock(b2b_w);
				blocks_broken += checkBlock(b2b_e);
				blocks_broken += checkBlock(b2b_s);
				blocks_broken += checkBlock(b2b_n);
				blocks_broken += checkBlock(b2b);
			}
		}
		return blocks_broken;
	}
	
	private int checkBlock(Block block) {
		int blocks_broken = 0;
		Block b2b_w = block.getRelative(BlockFace.WEST, 1);
		Block b2b_e = block.getRelative(BlockFace.EAST, 1);
		Block b2b_s = block.getRelative(BlockFace.SOUTH, 1);
		Block b2b_n = block.getRelative(BlockFace.NORTH, 1);
		
		blocks_broken += breakBlocks(logs, block, b2b_w, b2b_e, b2b_s, b2b_n);
		breakBlocks(leaves, block, b2b_w, b2b_e, b2b_s, b2b_n);
		
		return blocks_broken;
	}
	
	private int breakBlocks(Material[] materials, Block block, Block b2b_w, Block b2b_e, Block b2b_s, Block b2b_n) {
		int blocks_broken = 0;
		for(Material b2d: materials) {
			if(block.getType().equals(b2d)){
				block.breakNaturally();
				spawnParticle(block);
				blocks_broken++;
			}
			
			if(b2b_w.getType().equals(b2d)){
				b2b_w.breakNaturally();
				spawnParticle(b2b_w);
				blocks_broken++;
			}
			
			if(b2b_e.getType().equals(b2d)){
				b2b_e.breakNaturally();
				spawnParticle(b2b_e);
				blocks_broken++;
			}
			
			if(b2b_s.getType().equals(b2d)){
				b2b_s.breakNaturally();
				spawnParticle(b2b_s);
				blocks_broken++;
			}
			
			if(b2b_n.getType().equals(b2d)){
				b2b_n.breakNaturally();
				spawnParticle(b2b_n);
				blocks_broken++;
			}
		}
		return blocks_broken;
	}
	
	private void spawnParticle(Block block) {
		block.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation(), 10, block.getType().createBlockData());
	}
}
