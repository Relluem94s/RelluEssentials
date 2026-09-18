package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper.isOwner;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerSetting;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import java.util.Arrays;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * Listener that handles interaction with protected openable blocks such as doors, trapdoors, and
 * gates.
 *
 * <p>Enforces access control by checking whether the interacting player
 * has rights to the protection associated with the clicked block. Supports flags such as
 * {@code ALLOW_PUBLIC} and {@code AUTO_CLOSE}, and grants moderators the ability to bypass
 * restrictions with a notification.
 * </p>
 *
 * @author rellu
 */
@ListenerName("BetterLock")
public class BetterLock implements ListenerConstruct {

  private final PlayerState[] protectionPlayerStates = {PlayerState.PROTECTION_INFO,
      PlayerState.PROTECTION_ADD, PlayerState.PROTECTION_REMOVE, PlayerState.PROTECTION_FLAG_ADD,
      PlayerState.PROTECTION_FLAG_REMOVE, PlayerState.PROTECTION_RIGHT_ADD,
      PlayerState.PROTECTION_RIGHT_REMOVE};

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Handles player interaction with blocks to enforce protection rules.
   *
   * <p>When a player interacts with an openable block, the associated
   * protection entry is evaluated. If the player lacks rights, access is denied unless the player
   * holds a moderator rank. For authorized owners, additional logic handles double-door
   * synchronization and auto-close scheduling based on active protection flags.
   * </p>
   *
   * <p>For non-openable but protectable blocks, access is similarly restricted
   * based on the player's rights and the active protection flags.
   * </p>
   *
   * @param e the {@link PlayerInteractEvent} triggered when a player interacts with a block
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInteract(@NotNull PlayerInteractEvent e) {
    Block clickedBlock = e.getClickedBlock();

    if (clickedBlock == null) {
      return;
    }
    Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock);
    ProtectionEntry protection = serviceContext.getProtectionService().getProtectionEntry(l);
    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(e.getPlayer());

    if (protection == null) {
      return;
    }

    if (pe == null) {
      return;
    }

    if (!ProtectionHelper.isOpenAble(clickedBlock)) {
      if (!serviceContext.getProtectionService().isProtectableMaterial(clickedBlock.getType())) {
        return;
      }

      handleProtectionInteract(e, pe, protection);
      return;
    }

    if (Arrays.stream(protectionPlayerStates)
        .anyMatch(playerState -> pe.getPlayerState() == playerState)) {
      return;
    }

    if (!ProtectionHelper.hasRights(protection, pe.getId())) {
      evaluateAccessAndNotify(e, protection);
      return;
    }

    if (notify(protection, pe.getId(), e.getPlayer())) {
      e.getPlayer().sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));
    }

    Openable openable = (Openable) clickedBlock.getBlockData();

    switch (openable) {
      case Door door -> {
        Block b2 = ProtectionHelper.getOtherPart(door, clickedBlock);
        if (b2 == null) {
          scheduleAutoCloseIfEnabled(protection, door, clickedBlock, e.getPlayer());
          return;
        }

        scheduleAutoCloseIfEnabledForDoubleDoor(e, door, b2, protection, clickedBlock);
      }
      case TrapDoor trapDoor ->
          scheduleAutoCloseIfEnabled(protection, trapDoor, clickedBlock, e.getPlayer());
      case Gate gate ->
          scheduleAutoCloseIfEnabled(protection, gate, clickedBlock, e.getPlayer());
      default -> {
      }
    }
  }

  private void scheduleAutoCloseIfEnabledForDoubleDoor(@NonNull PlayerInteractEvent e, Door door,
      Block b2, ProtectionEntry protection, Block clickedBlock) {
    if (!(b2.getBlockData() instanceof Door door2)) {
      return;
    }

    if (door2.getHinge() == door.getHinge()) {
      return;
    }

    if (door2.isOpen()) {
      door2.setOpen(false);
      return;
    }

    door2.setOpen(true);

    if (!ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTO_CLOSE)) {
      return;
    }

    serviceContext.getSchedulerService().runTaskLater(() -> {
      door.setOpen(false);
      door2.setOpen(false);

      clickedBlock.setBlockData(door);
      b2.setBlockData(door2);
      e.getPlayer().sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_AUTOCLOSE));
    }, 50);

    b2.setBlockData(door2);
  }

  private void handleProtectionInteract(PlayerInteractEvent e, @NonNull PlayerEntry pe,
      ProtectionEntry protection) {
    if (!pe.getPlayerState().equals(PlayerState.DEFAULT)) {
      return;
    }

    if (ProtectionHelper.hasRights(protection, pe.getId())) {
      if (notify(protection, pe.getId(), e.getPlayer())) {
        e.getPlayer().sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));
      }
      return;
    }

    evaluateAccessAndNotify(e, protection);
  }

  private void evaluateAccessAndNotify(@NonNull PlayerInteractEvent e, ProtectionEntry protection) {
    if (serviceContext.getGroupService().isSenderAuthorized(e.getPlayer(), "mod")) {
      e.setCancelled(false);
      e.getPlayer().sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW_ADMIN_OVERWRITE));
      return;
    }

    if (ProtectionHelper.hasFlag(protection, ProtectionFlags.ALLOW_PUBLIC)) {
      e.getPlayer().sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));
      return;
    }

    e.setCancelled(true);
    e.getPlayer().sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
  }

  private boolean notify(ProtectionEntry protection, int playerId, Player player) {
    boolean notify = true;
    if (isOwner(protection, playerId)) {
      notify = !serviceContext.getSettingPlayerService()
          .isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF);
    }
    return notify;
  }

  private void scheduleAutoCloseIfEnabled(ProtectionEntry protection, Openable openable,
      Block block, Player player) {
    if (!ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTO_CLOSE)) {
      return;
    }

    serviceContext.getSchedulerService().runTaskLater(() -> {
      openable.setOpen(false);
      block.setBlockData(openable);
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_AUTOCLOSE));
    }, 50);
  }
}