package de.relluem94.minecraft.server.spigot.essentials.events.protect;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_FLAGS;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ProtectionEntry;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class BlockRedstoneProtect implements Listener {

  @EventHandler
  public void onBlockRedstoneChange(@NotNull BlockRedstoneEvent e) {
    Block b = e.getBlock();
    ProtectionEntry protection;

    Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
    protection = RelluEssentials.getInstance().getProtectionRegistry().getProtectionEntry(l);

    if (protection != null) {
      JSONObject flags = protection.getFlags();
      boolean isAllowed = (!flags.isEmpty() && flags.has(PLUGIN_EVENT_PROTECT_FLAGS)
          && flags.getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS).toList()
          .contains(ProtectionFlags.ALLOW_REDSTONE.name()));
      if (!isAllowed) {
        e.setNewCurrent(e.getOldCurrent());
      }
    }
  }
}
