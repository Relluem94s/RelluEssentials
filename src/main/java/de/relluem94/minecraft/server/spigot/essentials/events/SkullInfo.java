package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/* Skull info on Click */
public class SkullInfo implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if (p.hasPermission("rellu.skull.info")) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    BlockState block = e.getClickedBlock().getState();
                    if (block instanceof Skull) {
                        Skull sk = (Skull) block;
                        p.sendTitle("§a" + sk.getOwner(), "§8~~~~~~~~~~~~~~~~~~~~~~~", 5, 80, 5);
                    }
                }
            }
        }
    }
}
