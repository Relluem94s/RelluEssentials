package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_SKULL_INFO_SPACER;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.profile.PlayerProfile;

/* Skull info on Click */
@ListenerName("SkullInfo")
public class SkullInfo implements ListenerConstruct {


  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void onClick(PlayerInteractEvent e) {
    Player p = e.getPlayer();
    if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)
        && serviceContext.getGroupService().isSenderAuthorized(p, "vip")
        && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
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