package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN_DATE_FORMAT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_FLAGS;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
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

/**
 * Listener that handles player interactions with protected blocks.
 *
 * <p>Evaluates the current {@link PlayerState} of the interacting player and dispatches
 * the corresponding protection action, including adding or removing protections, displaying
 * protection info, and managing flags and access rights.
 */
@ListenerName("PlayerInteractProtect")
public class PlayerInteractProtect implements ListenerConstruct {

  ServiceContext context;

  /**
   * Injects the {@link ServiceContext} into this listener.
   *
   * @param context the service context providing access to all required services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.context = context;
  }

  /**
   * Handles player block interactions and routes them to the appropriate protection action based on
   * the player's current {@link PlayerState}.
   *
   * <p>The following states are handled:
   * <ul>
   *   <li>{@link PlayerState#PROTECTION_ADD} – protects the clicked block</li>
   *   <li>{@link PlayerState#PROTECTION_REMOVE} – removes protection from the clicked block</li>
   *   <li>{@link PlayerState#PROTECTION_INFO} – displays detailed protection information</li>
   *   <li>{@link PlayerState#PROTECTION_FLAG_ADD} – adds a protection flag to
   *   the clicked block</li>
   *   <li>{@link PlayerState#PROTECTION_FLAG_REMOVE} – removes a protection flag from
   *   the clicked block</li>
   *   <li>{@link PlayerState#PROTECTION_RIGHT_ADD} – grants access rights to a player for
   *   the clicked block</li>
   *   <li>{@link PlayerState#PROTECTION_RIGHT_REMOVE} – revokes access rights from a player for
   *   the clicked block</li>
   * </ul>
   *
   * <p>After each handled state, the player's state is reset to {@link PlayerState#DEFAULT}
   * and the state parameter is cleared. The event is canceled to
   * prevent default interaction behavior.
   *
   * @param e the {@link PlayerInteractEvent} triggered when a player interacts with a block
   */
  @EventHandler
  public void onPlayerProtectionChange(@NotNull PlayerInteractEvent e) {
    PlayerEntry pe = context.getPlayerService().getPlayerEntry(e.getPlayer());

    if (pe.getPlayerState().equals(PlayerState.PROTECTION_ADD) && e.getClickedBlock() != null) {
      context.getProtectionActionService().protectBlock(e.getPlayer(), e.getClickedBlock());
      pe.setPlayerState(PlayerState.DEFAULT);
      pe.setPlayerStateParameter(null);
      e.setCancelled(true);
    } else if (pe.getPlayerState().equals(PlayerState.PROTECTION_REMOVE)
        && e.getClickedBlock() != null) {
      context.getProtectionActionService()
          .removeProtectionFromBlock(e.getPlayer(), e.getClickedBlock());
      pe.setPlayerState(PlayerState.DEFAULT);
      pe.setPlayerStateParameter(null);
      e.setCancelled(true);
    } else if (pe.getPlayerState().equals(PlayerState.PROTECTION_INFO)) {
      Block b = e.getClickedBlock();
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      ProtectionEntry pre = context.getProtectionService().getProtectionEntry(l);
      if (pre != null) {
        Player p = e.getPlayer();
        Location loc = pre.getLocationEntry().getLocation();

        p.sendMessage("");
        p.sendMessage(context.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO));
        p.sendMessage("");
        p.sendMessage(context.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_ID, pre.getId()));
        p.sendMessage(context.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_CREATED, pre.getCreated()));
        p.sendMessage(context.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_UPDATED, pre.getUpdated()));
        p.sendMessage(context.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_LOCATION, loc.getX(), loc.getY(),
                loc.getZ(), Objects.requireNonNull(loc.getWorld()).getName()));
        p.sendMessage(context.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_ID,
                pre.getLocationEntry().getPlayerId()));

        PlayerEntry owner = context.getPlayerService()
            .getPlayerEntryByInternalId(pre.getLocationEntry().getPlayerId());
        if (owner != null) {
          OfflinePlayer op = context.getPluginMetadataService().getPlugin().getServer()
              .getOfflinePlayer(UUID.fromString(owner.getUuid()));

          Calendar cal = Calendar.getInstance();

          p.sendMessage(context.getTranslationService()
              .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_UUID, owner.getUuid()));
          p.sendMessage(context.getTranslationService()
              .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_NAME, op.getName()));
          SimpleDateFormat df = new SimpleDateFormat(
              PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN_DATE_FORMAT);
          cal.setTimeInMillis(op.getLastPlayed());
          p.sendMessage(context.getTranslationService()
              .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN,
                  df.format(cal.getTime())));
        }
        p.sendMessage(context.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_MATERIAL, pre.getMaterialName()));
        p.sendMessage(context.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_FLAGS, pre.getFlags().toString()));
        p.sendMessage(context.getTranslationService()
            .get(MessageKey.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_RIGHTS, pre.getRights().toString()));
        p.sendMessage("");
        p.sendMessage("");
      }
      pe.setPlayerState(PlayerState.DEFAULT);
      pe.setPlayerStateParameter(null);
      e.setCancelled(true);
    } else if (pe.getPlayerState().equals(PlayerState.PROTECTION_FLAG_REMOVE)) {
      Block b = e.getClickedBlock();
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      ProtectionEntry pre = context.getProtectionService().getProtectionEntry(l);
      if (pre != null && context.getProtectionService().playerOwnsProtection(pre, e.getPlayer())) {
        e.getPlayer().sendMessage(context.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));
        boolean update = false;
        if (pre.getFlags().has(PLUGIN_EVENT_PROTECT_FLAGS)) {
          JSONArray flagJsonArray = pre.getFlags().getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS);

          String flag = ProtectionFlags.valueOf(
              ((String) pe.getPlayerStateParameter()).toUpperCase()).name();
          List<Object> list = flagJsonArray.toList();
          if (list.contains(flag)) {
            update = true;
            list.remove(flag);
            JSONObject flags = new JSONObject();
            flags.put(PLUGIN_EVENT_PROTECT_FLAGS, list);

            pre.setFlags(flags);
          }
        }

        if (update) {
          context.getProtectionService().updateProtectionFlags(pre);
          e.getPlayer().sendMessage(context.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE));
        } else {
          e.getPlayer().sendMessage(context.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE_FAILED));
        }

        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);

        e.setCancelled(true);
      } else {
        e.getPlayer().sendMessage(context.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
      }
    } else if (pe.getPlayerState().equals(PlayerState.PROTECTION_FLAG_ADD)) {
      Block b = e.getClickedBlock();
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      ProtectionEntry pre = context.getProtectionService().getProtectionEntry(l);
      if (pre != null && context.getProtectionService().playerOwnsProtection(pre, e.getPlayer())) {
        e.getPlayer().sendMessage(context.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));
        boolean update = false;
        if (pre.getFlags().has(PLUGIN_EVENT_PROTECT_FLAGS)) {
          JSONArray flagJsonArray = pre.getFlags().getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS);

          List<Object> list = flagJsonArray.toList();
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
          context.getProtectionService().updateProtectionFlags(pre);
          e.getPlayer().sendMessage(context.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD));
        } else {
          e.getPlayer().sendMessage(context.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD_FAILED));
        }

        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
        e.setCancelled(true);
      } else {
        e.getPlayer().sendMessage(context.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
      }
    } else if (pe.getPlayerState().equals(PlayerState.PROTECTION_RIGHT_ADD)) {
      Block b = e.getClickedBlock();
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      ProtectionEntry pre = context.getProtectionService().getProtectionEntry(l);
      if (pre != null && context.getProtectionService().playerOwnsProtection(pre, e.getPlayer())) {
        e.getPlayer().sendMessage(context.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));

        UUID uuid = UUID.fromString((String) pe.getPlayerStateParameter());
        int id = context.getPlayerService().getPlayerEntry(uuid).getId();

        context.getProtectionActionService().addRight(e.getPlayer(), pre, id, false);

        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
        e.setCancelled(true);
      } else {
        e.getPlayer().sendMessage(context.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
      }
    } else if (pe.getPlayerState().equals(PlayerState.PROTECTION_RIGHT_REMOVE)) {
      Block b = e.getClickedBlock();
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      ProtectionEntry pre = context.getProtectionService().getProtectionEntry(l);
      if (pre != null && context.getProtectionService().playerOwnsProtection(pre, e.getPlayer())) {
        e.getPlayer().sendMessage(context.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW));

        UUID uuid = UUID.fromString((String) pe.getPlayerStateParameter());
        int id = context.getPlayerService().getPlayerEntry(uuid).getId();

        context.getProtectionActionService().removeRight(e.getPlayer(), pre, id, false);

        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
        e.setCancelled(true);
      } else {
        e.getPlayer().sendMessage(context.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        pe.setPlayerState(PlayerState.DEFAULT);
        pe.setPlayerStateParameter(null);
      }
    }
  }
}
