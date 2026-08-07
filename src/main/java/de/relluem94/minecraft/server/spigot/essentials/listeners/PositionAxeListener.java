package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_POSITION_AXE;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PositionAxeListener implements ListenerConstruct {


  private final ItemHelper positionAxeItem = ItemRegistry.find(
      RegistryKey.of(PLUGIN_ITEM_NAMESPACE_POSITION_AXE)).orElseThrow();

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
    if (event.getHand() == null || event.getHand() != EquipmentSlot.HAND) {
      return;
    }

    Player player = event.getPlayer();
    ItemStack item = player.getInventory().getItemInMainHand();

    if (!positionAxeItem.almostEquals(item)) {
      return;
    }

    if (event.getAction() == Action.LEFT_CLICK_BLOCK
        || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
      event.setCancelled(true);
    } else {
      return;
    }

    if (event.getClickedBlock() == null) {
      return;
    }

    DoubleStore<Location, Location> positions = RelluEssentials.getInstance().position.computeIfAbsent(
        player, _ -> new DoubleStore<>(null, null));

    Location clickedLocation = event.getClickedBlock().getLocation();

    if (player.isSneaking()) {
      if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
        positions.setValue(null);
        player.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_POSITION_AXE_FIRST_RESET));
      } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        positions.setSecondValue(null);
        player.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_POSITION_AXE_SECOND_RESET));
      }
    } else {
      if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
        positions.setValue(clickedLocation);
        player.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_POSITION_AXE_FIRST_SET,
                    clickedLocation.getBlockX(), clickedLocation.getBlockY(),
                    clickedLocation.getBlockZ()));
      } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        positions.setSecondValue(clickedLocation);
        player.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_POSITION_AXE_SECOND_SET,
                    clickedLocation.getBlockX(), clickedLocation.getBlockY(),
                    clickedLocation.getBlockZ()));
      }
    }
  }
}