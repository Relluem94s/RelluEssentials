package main.java.de.relluem94.minecraft.server.spigot.essentials.events.skills;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_EVENT_SKILL_SALVAGE_DONE;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import main.java.de.relluem94.minecraft.server.spigot.essentials.skills.enums.ItemValues;

public class Ev_Salvage implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Action a = e.getAction();
		Block b = e.getClickedBlock();
		Player p = e.getPlayer();
		PlayerInventory i = p.getInventory();
		if(a.equals(Action.RIGHT_CLICK_BLOCK)) {
			if(b.getType().equals(Material.GOLD_BLOCK)) {
				ItemStack is = i.getItemInMainHand();
				Material m = is.getType();
				ItemMeta im = is.getItemMeta();
				if(((Damageable) im) != null && !((Damageable) im).hasDamage()) {
					ItemValues iv = ItemValues.getByMaterial(m);
					if(iv != null) {
						is.setAmount(is.getAmount() -1);
						
						is = new ItemStack(iv.getMaterial(), iv.getValue());
						i.addItem(is);
						Map<Enchantment, Integer> enchants = im.getEnchants();
						if(enchants.size() != 0) {
							ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
							ItemMeta im_book = book.getItemMeta();
							for(Map.Entry<Enchantment, Integer> em : enchants.entrySet()) {
								im_book.addEnchant(em.getKey(), em.getValue(), true);
							}
							book.setItemMeta(im_book);
							i.addItem(book);
						}
						User u = User.getUserByPlayerName(p.getName());
						int score = u.salvage.getObjective().getScore("Salvage").getScore();
						u.salvage.getObjective().getScore("Salvage").setScore(score +1);
						p.sendMessage(String.format(PLUGIN_EVENT_SKILL_SALVAGE_DONE, is.getAmount() + "x " + is.getType().toString().replaceAll("_", " ").toLowerCase()));
						e.setCancelled(true);
					}
				}				
			}		
		}
	}
}


