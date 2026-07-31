package de.relluem94.minecraft.server.spigot.essentials.events.npc;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcHelper;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;


public class PlaceNpc implements Listener {

  @EventHandler
  public void onNPCPlacement(@NotNull PlayerInteractEvent e) {
    if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
      if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK)
          && e.getItem() != null && RelluEssentials.getInstance().getNpcAPI().getNPCItemStackList()
          .contains(e.getItem())) {
        e.setCancelled(true);

        if (e.getClickedBlock() == null) {
          return;
        }

        Location location = e.getClickedBlock().getLocation().add(0, 1, 0);
        location.setYaw(e.getPlayer().getLocation().getYaw());

        for (int i = 0; i < RelluEssentials.getInstance().getNpcAPI().getNPCItemStackList().size();
            i++) {
          if (RelluEssentials.getInstance().getNpcAPI().getNPCItemStackList().get(i)
              .equals(e.getItem())) {
            NpcHelper nh = new NpcHelper(location,
                RelluEssentials.getInstance().getNpcAPI().getNPC(i));
            nh.spawn();
            e.getPlayer().sendMessage(
                languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_SPAWN,
                    nh.getCustomName()));
          }
        }
      }
    }
  }
}
