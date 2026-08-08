package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN_DATE_FORMAT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_FLAGS;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlayerInteractProtect implements ListenerConstruct {

  ServiceContext context;

  @Override
  public void injectContext(ServiceContext context) {
    this.context = context;
  }

  @EventHandler
  public void onPlayerProtectionChange(@NotNull PlayerInteractEvent e) {
    PlayerEntry pe = context.getPlayerService().getPlayerEntry(e.getPlayer());

    if (pe.getPlayerState().equals(PlayerState.PROTECTION_ADD) && e.getClickedBlock() != null) {
      context.getProtectionActionService().protectBlock(e.getPlayer(), e.getClickedBlock());
      pe.setPlayerState(PlayerState.DEFAULT);
      pe.setPlayerStateParameter(null);
      e.setCancelled(true);
    } else if (pe.getPlayerState().equals(PlayerState.PROTECTION_REMOVE) && e.getClickedBlock() != null) {
      context.getProtectionActionService()
          .removeProtectionFromBlock(e.getPlayer(), e.getClickedBlock());
      pe.setPlayerState(PlayerState.DEFAULT);
      pe.setPlayerStateParameter(null);
      e.setCancelled(true);
    } else if (pe.getPlayerState().equals(PlayerState.PROTECTION_INFO)) {
      Block b = e.getClickedBlock();
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      ProtectionEntry pre = context.getProtectionRegistry()
          .getProtectionEntry(l);
      if (pre != null) {
        Player p = e.getPlayer();
        Location loc = pre.getLocationEntry().getLocation();

        p.sendMessage("");
        p.sendMessage(
            context.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO));
        p.sendMessage("");
        p.sendMessage(
            context.getTranslationService()
                .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_ID, pre.getId()));
        p.sendMessage(context.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_CREATED,
                pre.getCreated()));
        p.sendMessage(context.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_UPDATED,
                pre.getUpdated()));
        p.sendMessage(
            context.getTranslationService()
                .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_LOCATION,
                    loc.getX(),
                    loc.getY(), loc.getZ(), Objects.requireNonNull(loc.getWorld()).getName()));
        p.sendMessage(context.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_ID,
                pre.getLocationEntry().getPlayerId()));

        PlayerEntry owner = context.getPlayerService()
            .getPlayerEntryByInternalId(pre.getLocationEntry().getPlayerId());
        if (owner != null) {
          OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(owner.getUuid()));

          Calendar cal = Calendar.getInstance();
          SimpleDateFormat df = new SimpleDateFormat(
              PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN_DATE_FORMAT);
          cal.setTimeInMillis(op.getLastPlayed());

          p.sendMessage(
              context.getTranslationService()
                  .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_UUID,
                      owner.getUuid()));
          p.sendMessage(
              context.getTranslationService()
                  .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_NAME,
                      op.getName()));
          p.sendMessage(
              context.getTranslationService().get(
                  MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN,
                  df.format(cal.getTime())));
        }
        p.sendMessage(context.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_MATERIAL,
                pre.getMaterialName()));
        p.sendMessage(
            context.getTranslationService()
                .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_FLAGS,
                    pre.getFlags().toString()));
        p.sendMessage(
            context.getTranslationService()
                .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_RIGHTS,
                    pre.getRights().toString()));
        p.sendMessage("");
        p.sendMessage("");
      }
      pe.setPlayerState(PlayerState.DEFAULT);
      pe.setPlayerStateParameter(null);
      e.setCancelled(true);
    } else if (pe.getPlayerState().equals(PlayerState.PROTECTION_FLAG_REMOVE)) {
      Block b = e.getClickedBlock();
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      ProtectionEntry pre = context.getProtectionRegistry()
          .getProtectionEntry(l);
      if (pre != null && ProtectionHelper.hasPermission(pre, e.getPlayer())) {
        e.getPlayer()
            .sendMessage(
                context.getTranslationService()
                    .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));
        boolean update = false;
        if (pre.getFlags().has(PLUGIN_EVENT_PROTECT_FLAGS)) {
          JSONArray flagJSON = pre.getFlags().getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS);

          String flag = ProtectionFlags.valueOf(
              ((String) pe.getPlayerStateParameter()).toUpperCase()).name();
          List<Object> list = flagJSON.toList();
          if (list.contains(flag)) {
            update = true;
            list.remove(flag);
            JSONObject flags = new JSONObject();
            flags.put(PLUGIN_EVENT_PROTECT_FLAGS, list);

            pre.setFlags(flags);
          }
        }

        if (update) {
          context.getDatabaseHelper().updateProtectionFlag(pre);
          context.getProtectionRegistry().removeProtectionEntry(l);
          context.getProtectionRegistry().putProtectionEntry(l, pre);
          e.getPlayer().sendMessage(
              context.getTranslationService()
                  .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE));
        } else {
          e.getPlayer().sendMessage(context.getTranslationService().getWithPrefix(
              MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE_FAILED));
        }

        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);

        e.setCancelled(true);
      } else {
        e.getPlayer().sendMessage(
            context.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
      }
    } else if (pe.getPlayerState().equals(PlayerState.PROTECTION_FLAG_ADD)) {
      Block b = e.getClickedBlock();
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      ProtectionEntry pre = context.getProtectionRegistry()
          .getProtectionEntry(l);
      if (pre != null && ProtectionHelper.hasPermission(pre, e.getPlayer())) {
        e.getPlayer()
            .sendMessage(
                context.getTranslationService()
                    .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));
        boolean update = false;
        if (pre.getFlags().has(PLUGIN_EVENT_PROTECT_FLAGS)) {
          JSONArray flagJSON = pre.getFlags().getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS);

          List<Object> list = flagJSON.toList();
          String flag = ProtectionFlags.valueOf(
              ((String) pe.getPlayerStateParameter()).toUpperCase()).name();

          if (!list.contains(flag)) {
            update = true;
            list.add(ProtectionFlags.valueOf(((String) pe.getPlayerStateParameter()).toUpperCase())
                .name());
          }

          JSONObject flags = new JSONObject();
          flags.put(PLUGIN_EVENT_PROTECT_FLAGS, list);

          pre.setFlags(flags);
        } else {
          JSONObject flags = new JSONObject();
          if (Objects.requireNonNull(b).getType().equals(Material.LEVER) || b.getType()
              .equals(Material.IRON_DOOR)) {
            JSONArray flagArray = new JSONArray();
            flagArray.put(ProtectionFlags.ALLOW_REDSTONE.name());
            flagArray.put(
                ProtectionFlags.valueOf(((String) pe.getPlayerStateParameter()).toUpperCase())
                    .name());
            flags.put(PLUGIN_EVENT_PROTECT_FLAGS, flagArray);
          } else {
            JSONArray flagArray = new JSONArray();
            flagArray.put(
                ProtectionFlags.valueOf(((String) pe.getPlayerStateParameter()).toUpperCase())
                    .name());
            flags.put(PLUGIN_EVENT_PROTECT_FLAGS, flagArray);
          }
          update = true;
          pre.setFlags(flags);
        }

        if (update) {
          context.getDatabaseHelper().updateProtectionFlag(pre);
          context.getProtectionRegistry().removeProtectionEntry(l);
          context.getProtectionRegistry().putProtectionEntry(l, pre);
          e.getPlayer().sendMessage(
              context.getTranslationService()
                  .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD));
        } else {
          e.getPlayer().sendMessage(
              context.getTranslationService().getWithPrefix(
                  MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD_FAILED));
        }

        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
        e.setCancelled(true);
      } else {
        e.getPlayer().sendMessage(
            context.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
      }
    } else if (pe.getPlayerState().equals(PlayerState.PROTECTION_RIGHT_ADD)) {
      Block b = e.getClickedBlock();
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      ProtectionEntry pre = context.getProtectionRegistry()
          .getProtectionEntry(l);
      if (pre != null && ProtectionHelper.hasPermission(pre, e.getPlayer())) {
        e.getPlayer()
            .sendMessage(
                context.getTranslationService()
                    .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));

        UUID uuid = UUID.fromString((String) pe.getPlayerStateParameter());
        int id = context.getPlayerService().getPlayerEntry(uuid).getId();

        context.getProtectionActionService().addRight(e.getPlayer(), pre, id, false);

        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
        e.setCancelled(true);
      } else {
        e.getPlayer().sendMessage(
            context.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
      }
    } else if (pe.getPlayerState().equals(PlayerState.PROTECTION_RIGHT_REMOVE)) {
      Block b = e.getClickedBlock();
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      ProtectionEntry pre = context.getProtectionRegistry()
          .getProtectionEntry(l);
      if (pre != null && ProtectionHelper.hasPermission(pre, e.getPlayer())) {
        e.getPlayer()
            .sendMessage(
                context.getTranslationService()
                    .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));

        UUID uuid = UUID.fromString((String) pe.getPlayerStateParameter());
        int id = context.getPlayerService().getPlayerEntry(uuid).getId();

        context.getProtectionActionService().removeRight(e.getPlayer(), pre, id, false);

        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
        e.setCancelled(true);
      } else {
        e.getPlayer().sendMessage(
            context.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
      }
    }
  }
}
