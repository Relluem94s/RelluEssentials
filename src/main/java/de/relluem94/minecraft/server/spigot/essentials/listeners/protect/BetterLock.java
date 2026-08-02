package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PermissionHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author rellu
 */
public class BetterLock implements ListenerConstruct {


  @Override
  public void injectContext(ServiceContext context) {

  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInteract(@NotNull PlayerInteractEvent e) {
    Block b = e.getClickedBlock();

    if (b != null) {
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      if (ProtectionHelper.isOpenAble(b)) {
        ProtectionEntry protection = RelluEssentials.getInstance().getProtectionRegistry()
            .getProtectionEntry(l);
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry()
            .getPlayerEntry(e.getPlayer());
        if (protection != null && pe != null && !(
            pe.getPlayerState().equals(PlayerState.PROTECTION_INFO) || pe.getPlayerState()
                .equals(PlayerState.PROTECTION_ADD) || pe.getPlayerState()
                .equals(PlayerState.PROTECTION_REMOVE) || pe.getPlayerState()
                .equals(PlayerState.PROTECTION_FLAG_ADD) || pe.getPlayerState()
                .equals(PlayerState.PROTECTION_FLAG_REMOVE) || pe.getPlayerState()
                .equals(PlayerState.PROTECTION_RIGHT_ADD) || pe.getPlayerState()
                .equals(PlayerState.PROTECTION_RIGHT_REMOVE))) {
          if (ProtectionHelper.hasRights(protection, pe.getId())) {
            if (ProtectionHelper.hasFlag(protection, ProtectionFlags.ALLOW_PUBLIC)) {
              e.getPlayer().sendMessage(
                  languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));
            } else {
              if (PermissionHelper.isAuthorized(e.getPlayer(),
                  GroupRegistry.getGroup("mod").getId())) {
                e.setCancelled(false);
                e.getPlayer().sendMessage(languageHelper.getWithPrefix(
                    MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW_ADMIN_OVERWRITE));
              } else {
                e.setCancelled(true);
                e.getPlayer().sendMessage(
                    languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
              }

            }
          } else {
            // If Notify protection self on
            e.getPlayer().sendMessage(
                languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));

            Openable openable = (Openable) b.getBlockData();

            switch (openable) {
              case Door _ -> {
                Door door = (Door) b.getBlockData();
                Block b2 = ProtectionHelper.getOtherPart(door, b);
                if (b2 != null) {
                  if (b2.getBlockData() instanceof Door door2) {
                    if (door2.getHinge() != door.getHinge()) {
                      if (door2.isOpen()) {
                        door2.setOpen(false);
                      } else {
                        door2.setOpen(true);

                        if (ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTO_CLOSE)) {
                          Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
                            door.setOpen(false);
                            door2.setOpen(false);

                            b.setBlockData(door);
                            b2.setBlockData(door2);
                            e.getPlayer().sendMessage(languageHelper.getWithPrefix(
                                MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_AUTOCLOSE));
                          }, 50);
                        }
                      }
                      b2.setBlockData(door2);
                    }
                  }
                } else {
                  if (ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTO_CLOSE)) {
                    Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
                      door.setOpen(false);

                      b.setBlockData(door);
                      e.getPlayer().sendMessage(languageHelper.getWithPrefix(
                          MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_AUTOCLOSE));
                    }, 50);
                  }
                }
              }
              case TrapDoor _ -> {
                TrapDoor door = (TrapDoor) b.getBlockData();
                if (ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTO_CLOSE)) {
                  Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
                    door.setOpen(false);

                    b.setBlockData(door);
                    e.getPlayer().sendMessage(languageHelper.getWithPrefix(
                        MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_AUTOCLOSE));
                  }, 50);
                }
              }
              case Gate _ -> {
                Gate door = (Gate) b.getBlockData();
                if (ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTO_CLOSE)) {
                  Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
                    door.setOpen(false);

                    b.setBlockData(door);
                    e.getPlayer().sendMessage(languageHelper.getWithPrefix(
                        MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_AUTOCLOSE));
                  }, 50);
                }
              }
              default -> {
              }
            }
            // ELSE Other Openable Objects (Future Implementations)
          }
        }
      } else if (RelluEssentials.getInstance().getProtectionRegistry()
          .isProtectableMaterial(b.getType())) {
        ProtectionEntry protection = RelluEssentials.getInstance().getProtectionRegistry()
            .getProtectionEntry(l);
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry()
            .getPlayerEntry(e.getPlayer());
        if (protection != null && pe != null && pe.getPlayerState().equals(PlayerState.DEFAULT)) {
          if (ProtectionHelper.hasRights(protection, pe.getId())) {
            if (ProtectionHelper.hasFlag(protection, ProtectionFlags.ALLOW_PUBLIC)) {
              e.getPlayer().sendMessage(
                  languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));
            } else {
              if (PermissionHelper.isAuthorized(e.getPlayer(),
                  GroupRegistry.getGroup("mod").getId())) {
                e.setCancelled(false);
                e.getPlayer().sendMessage(languageHelper.getWithPrefix(
                    MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW_ADMIN_OVERWRITE));
              } else {
                e.setCancelled(true);
                e.getPlayer().sendMessage(
                    languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
              }
            }
          } else {
            // If Notify protection self on
            e.getPlayer().sendMessage(
                languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));
          }
        }
      }
    }
  }
}