package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_WORLDSELECTOR;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.commands.Worlds;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

@ListenerName("OpenWorldSelector")
public class OpenWorldSelector implements ListenerConstruct {


  private final RegistryKey worldSelectorKey;

  public OpenWorldSelector() {
    this.worldSelectorKey = RegistryKey.of(PLUGIN_ITEM_NAMESPACE_WORLDSELECTOR);
  }

  @Override
  public void injectContext(ServiceContext context) {

  }

  @EventHandler
  public void onWorldSelectorUse(@NotNull PlayerInteractEvent event) {
    if (event.getHand() == null || !event.getHand().equals(EquipmentSlot.HAND)) {
      return;
    }

    boolean isRightClick = event.getAction() == Action.RIGHT_CLICK_BLOCK
        || event.getAction() == Action.RIGHT_CLICK_AIR;

    if (!isRightClick || event.getItem() == null) {
      return;
    }

    ItemRegistry.find(worldSelectorKey).ifPresent(worldSelectorItem -> {
      if (worldSelectorItem.equalsName(event.getItem())) {
        Worlds.openWorldMenu(event.getPlayer());
        event.setCancelled(true);
      }
    });
  }
}