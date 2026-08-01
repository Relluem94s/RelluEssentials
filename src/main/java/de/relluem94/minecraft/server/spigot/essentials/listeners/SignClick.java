package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Home;
import de.relluem94.minecraft.server.spigot.essentials.commands.Spawn;
import de.relluem94.minecraft.server.spigot.essentials.constants.SignConstants;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AnnotationHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.model.SignAction;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.SignRegistry;
import java.util.Map.Entry;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class SignClick implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChangeSignCreateActionSign(@NotNull PlayerInteractEvent e) {
    PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry()
        .getPlayerEntry(e.getPlayer().getUniqueId());
    if (!pe.getPlayerState().equals(PlayerState.DEFAULT) || e.getHand() == null || !e.getHand()
        .equals(EquipmentSlot.HAND) || e.getPlayer().isSneaking()) {
      return;
    }

    if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK) {
      return;
    }

    Block clickedBlock = e.getClickedBlock();
    if (clickedBlock == null || !SignHelper.isBlockSign(clickedBlock)) {
      return;
    }

    Sign sign = (Sign) clickedBlock.getState();
    final Player player = e.getPlayer();
    final String[] frontLines = new String[]{
        sign.getSide(Side.FRONT).getLine(0),
        sign.getSide(Side.FRONT).getLine(1),
        sign.getSide(Side.FRONT).getLine(2),
        sign.getSide(Side.FRONT).getLine(3)
    };

    Optional<Entry<RegistryKey, SignAction>> foundEntry =
        SignRegistry.findEntryByLine(frontLines[1]);

    if (foundEntry.isEmpty()) {
      return;
    }
    cancelInteraction(e);

    String actionKey = foundEntry.get().getKey().key();

    switch (actionKey) {
      case SignConstants.PLUGIN_SIGN_ACTION_SPAWN -> handleSpawn(player);
      case SignConstants.PLUGIN_SIGN_ACTION_UP -> handleUp(player, clickedBlock);
      case SignConstants.PLUGIN_SIGN_ACTION_DOWN -> handleDown(player, clickedBlock);
      case SignConstants.PLUGIN_SIGN_ACTION_COMMAND -> handleCommand(player, frontLines[2]);
      case SignConstants.PLUGIN_SIGN_ACTION_TELEPORT -> handleTeleport(player, frontLines[2]);
      case SignConstants.PLUGIN_SIGN_ACTION_HOME -> handleHome(player, frontLines[2]);
    }
  }

  private void handleSpawn(Player player) {
    String spawnCommand = AnnotationHelper.getCommandName(Spawn.class);
    if (spawnCommand == null) {
      return;
    }
    player.performCommand(spawnCommand);
  }

  private void handleUp(Player player, Block signBlock) {
    Location signLocation = signBlock.getLocation();
    if (signLocation.getWorld() == null) {
      return;
    }

    int maxHeight = signLocation.getWorld().getMaxHeight();
    boolean endPointFound = false;

    for (int y = signLocation.getBlockY(); y <= maxHeight; y++) {
      Block candidateBlock = signLocation.add(0, 1, 0).getBlock();
      if (!SignHelper.isBlockSign(candidateBlock)) {
        continue;
      }

      String candidateLine1 = ((Sign) candidateBlock.getState()).getSide(Side.FRONT).getLine(1);
      if (SignRegistry.findEntryByLine(candidateLine1).isEmpty()) {
        continue;
      }

      Location destination = player.getLocation().clone();
      destination.setY(y);

      if (!destination.clone().add(0, -1, 0).getBlock().getType().equals(Material.AIR)) {
        endPointFound = true;
        player.teleport(destination, TeleportCause.COMMAND);
        break;
      }
    }

    if (!endPointFound) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_SIGN_UP_OR_DOWN_NO_END_POINT));
    }
  }

  private void handleDown(Player player, Block signBlock) {
    Location signLocation = signBlock.getLocation();
    if (signLocation.getWorld() == null) {
      return;
    }

    int minHeight = signLocation.getWorld().getMinHeight();
    boolean endPointFound = false;

    for (int y = signLocation.getBlockY(); y >= minHeight; y--) {
      Block candidateBlock = signLocation.add(0, -1, 0).getBlock();
      if (!SignHelper.isBlockSign(candidateBlock)) {
        continue;
      }

      String candidateLine1 = ((Sign) candidateBlock.getState()).getSide(Side.FRONT).getLine(1);
      if (SignRegistry.findEntryByLine(candidateLine1).isEmpty()) {
        continue;
      }

      Location destination = player.getLocation().clone();
      destination.setY((double) y - 2);

      if (!destination.clone().add(0, -1, 0).getBlock().getType().equals(Material.AIR)) {
        endPointFound = true;
        player.teleport(destination, TeleportCause.COMMAND);
        break;
      }
    }

    if (!endPointFound) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_SIGN_UP_OR_DOWN_NO_END_POINT));
    }
  }

  private void handleCommand(Player player, String customInput) {
    player.performCommand(customInput);
  }

  private void handleTeleport(Player player, String customInput) {
    String[] coordinates = customInput.split(",");
    Location destination = new Location(
        player.getWorld(),
        Integer.parseInt(coordinates[0]),
        Integer.parseInt(coordinates[1]),
        Integer.parseInt(coordinates[2])
    );
    player.teleport(destination);
  }

  private void handleHome(Player player, String customInput) {
    String homeCommand = AnnotationHelper.getCommandName(Home.class);
    if (homeCommand == null) {
      return;
    }
    player.performCommand(homeCommand + " " + customInput);
  }

  private void cancelInteraction(@NotNull PlayerInteractEvent event) {
    event.setCancelled(true);
    event.setUseInteractedBlock(Event.Result.DENY);
    event.setUseItemInHand(Event.Result.DENY);
  }
}