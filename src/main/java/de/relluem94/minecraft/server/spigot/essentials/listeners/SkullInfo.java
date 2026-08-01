package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_SKULL_INFO_SPACER;

import de.relluem94.minecraft.server.spigot.essentials.helpers.PermissionHelper;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.profile.PlayerProfile;

/* Skull info on Click */
public class SkullInfo implements Listener {

  @EventHandler
  public void onClick(PlayerInteractEvent e) {
    Player p = e.getPlayer();
    if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND) && PermissionHelper.isAuthorized(p,
        GroupRegistry.getGroup("vip").getId()) && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
      Block clickedBlock = e.getClickedBlock();
      if (clickedBlock == null) {
        return;
      }
      BlockState clickedBlockState = clickedBlock.getState();
      if (clickedBlockState instanceof Skull sk) {
        PlayerProfile ownerProfile = sk.getOwnerProfile();
        if (ownerProfile == null) {
          return;
        }
        p.sendTitle("§a" + ownerProfile.getName(), PLUGIN_EVENT_SKULL_INFO_SPACER, 5, 80, 5);
      }
    }
  }
}