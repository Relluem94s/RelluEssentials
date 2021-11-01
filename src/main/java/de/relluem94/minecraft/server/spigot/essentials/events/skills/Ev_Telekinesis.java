package de.relluem94.minecraft.server.spigot.essentials.events.skills;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.autosmelt;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.telekinesis;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Ev_Telekinesis implements Listener {

    @EventHandler
    public void onDeathCollect(EntityDeathEvent e) {
        if (e.getEntity().getKiller() instanceof Player) {
            Player p = (Player) e.getEntity().getKiller();
            if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(telekinesis)) {
                e.getDrops().forEach(im -> {
                    if (p.getInventory().firstEmpty() >= 0) {
                        p.getInventory().addItem(im);
                    } else {
                        p.getWorld().dropItem(p.getLocation(), im);
                    }
                });
                e.getDrops().clear();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreakCollect(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();

        if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(telekinesis)) {
            if (!p.getInventory().getItemInMainHand().getEnchantments().containsKey(autosmelt)) {
                Block bl = b.getRelative(BlockFace.UP);

                List<ItemStack> lis = new ArrayList();
                lis.addAll(b.getDrops());

                while (bl.getType().equals(Material.SUGAR_CANE)) {
                    lis.addAll(bl.getDrops());
                    bl.getDrops().clear();

                    bl.setType(Material.AIR);
                    bl = bl.getRelative(BlockFace.UP);
                }

                lis.forEach(im -> {
                    if (p.getInventory().firstEmpty() >= 0) {
                        p.getInventory().addItem(im);
                    } else {
                        p.getWorld().dropItem(p.getLocation(), im);
                    }
                });
                e.setDropItems(false);
            } else {
                e.setDropItems(false); //HANDLED IN AUTOSMELT EVENT
            }
        }

    }
}
