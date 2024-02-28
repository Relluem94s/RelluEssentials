package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_SKULL_INFO_SPACER;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

/* Skull info on Click */
public class SkullInfo implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND) && (Permission.isAuthorized(p, Groups.getGroup("vip").getId()) && (e.getAction() == Action.RIGHT_CLICK_BLOCK))) {
            BlockState block = e.getClickedBlock().getState();
            if (block instanceof Skull sk) {
                p.sendTitle("§a" + sk.getOwnerProfile().getName(), PLUGIN_EVENT_SKULL_INFO_SPACER, 5, 80, 5);
            }
        }
    }
}