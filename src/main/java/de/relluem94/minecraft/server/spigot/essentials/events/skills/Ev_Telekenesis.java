package main.java.de.relluem94.minecraft.server.spigot.essentials.events.skills;

import main.java.de.relluem94.minecraft.server.spigot.essentials.Strings;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Ev_Telekenesis implements Listener {

    @EventHandler
    public void onBreakCollect(BlockBreakEvent e) {
        Player p = e.getPlayer();

        Block b = e.getBlock();

        if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "telekenesis")))) {
            if (!p.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "autosmelt")))) {
                for (ItemStack im : b.getDrops()) {
                    if (p.getInventory().firstEmpty() >= 0) {
                        p.getInventory().addItem(im);
                    } else {
                        p.getWorld().dropItem(p.getLocation(), im);
                    }
                }
                e.setDropItems(false);
            } else {
                e.setDropItems(false); //HANDLED IN AUTOSMELT EVENT
            }
        }

    }
}
