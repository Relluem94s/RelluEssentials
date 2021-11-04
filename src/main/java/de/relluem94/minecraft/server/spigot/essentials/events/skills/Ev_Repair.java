package de.relluem94.minecraft.server.spigot.essentials.events.skills;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import de.relluem94.minecraft.server.spigot.essentials.skills.enums.ItemValues;

import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_SKILL_REPAIR_DONE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_SKILL_REPAIR_WARNING;

public class Ev_Repair implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Action a = e.getAction();
        Block b = e.getClickedBlock();
        Player p = e.getPlayer();
        PlayerInventory i = p.getInventory();
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
                if (b.getType().equals(Material.IRON_BLOCK)) {
                    ItemStack is = i.getItemInMainHand();
                    ItemMeta im = is.getItemMeta();
                    Material m = is.getType();
                    if (((Damageable) im) != null && ((Damageable) im).hasDamage()) {
                        ItemValues iv = ItemValues.getByMaterial(m);
                        if (iv != null) {
                            Material cost_material = iv.getMaterial();
                            ItemStack cost_itemstack = new ItemStack(cost_material, 1);
                            if (i.contains(cost_material)) {
                                p.getInventory().removeItem(cost_itemstack);
                                int damage = ((Damageable) im).getDamage();
                                ((Damageable) im).setDamage(damage - (45 * iv.getFactor()));
                                p.sendMessage(PLUGIN_EVENT_SKILL_REPAIR_DONE);
                                is.setItemMeta(im);
                                User u = User.getUserByPlayerName(p.getName());
                                int score = u.repair.getObjective().getScore("Repair").getScore();
                                u.repair.getObjective().getScore("Repair").setScore(score + 1);
                            } else {
                                p.sendMessage(String.format(PLUGIN_EVENT_SKILL_REPAIR_WARNING, iv.getMaterial().toString().replaceAll("_", " ").toLowerCase()));
                            }
                        }
                    }
                }
            }
        }
    }
}