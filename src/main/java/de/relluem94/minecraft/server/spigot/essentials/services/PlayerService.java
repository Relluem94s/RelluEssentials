package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CHAT_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessageInChannel;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.PlayerRegistry;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerService {

  private final PlayerRegistry playerRegistry;
  private final DatabaseHelper databaseHelper;
  private final GroupService groupService;
  private final TranslationService translationService;

  public PlayerService(PlayerRegistry playerRegistry, DatabaseHelper databaseHelper,
      GroupService groupService, TranslationService translationService) {
    this.playerRegistry = playerRegistry;
    this.databaseHelper = databaseHelper;
    this.groupService = groupService;
    this.translationService = translationService;
  }

  public @Nullable PlayerEntry getPlayer(String name) {
    for (PlayerEntry pe : playerRegistry.getPlayerEntryMap()
        .values()) {
      if (pe.getName().equals(name)) {
        return pe;
      }
    }

    return null;
  }

  public String getCustomName(Player p) {
    String name;
    PlayerEntry pe = playerRegistry.getPlayerEntry(p.getUniqueId());
    if (pe.getCustomName() != null && !pe.getCustomName().equals("null")) {
      name = pe.getCustomName();
    } else {
      name = p.getName();
    }

    return name;
  }

  public void setGroup(Player p, GroupEntry g) {
    p.setCustomName(g.getPrefix() + getCustomName(p));
    p.setPlayerListName(p.getCustomName());
  }

  public void updateGroup(OfflinePlayer p, GroupEntry g) {
    PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry()
        .getPlayerEntry(p.getUniqueId());

    if (p.isOnline()) {
      Player player = Bukkit.getPlayer(p.getUniqueId());
      if (player != null) {
        player.setCustomName(g.getPrefix() + getCustomName(player));
        player.setPlayerListName(g.getPrefix() + getCustomName(player));
      }
    }

    pe.setGroup(g);
    pe.setUpdatedBy(pe.getId());
    pe.setHasToBeUpdated(true);
  }

  /**
   *
   * @param p    Player
   * @param join Boolean
   *
   */
  public void setAFK(Player p, boolean join) {
    PlayerEntry pe = playerRegistry.getPlayerEntry(p.getUniqueId());
    boolean isAFK = pe.isAfk();

    if (pe.getPlayerState().equals(PlayerState.FAKE_AFK_ACTIVE)) {
      isAFK = true;
    } else if (pe.getPlayerState().equals(PlayerState.FAKE_AFK_ON)) {
      isAFK = false;
    }

    if (!join) {
      Bukkit.broadcastMessage(
          translationService.getWithPrefix(
              !isAFK ? MessageKey.COMMAND_AFK_ACTIVATED : MessageKey.COMMAND_AFK_DEACTIVATED,
              p.getLocale(),
              p.getCustomName() + "§f",
              !isAFK ? "§c" : "§a"
          )
      );
      isAFK = !isAFK; // Invert for single invertion ^_^
    }

    if (pe.getPlayerState().equals(PlayerState.DEFAULT)) {
      if (!join) {
        pe.setUpdatedBy(pe.getId());
        pe.setAfk(isAFK);
        pe.setUpdatedBy(pe.getId());
        pe.setHasToBeUpdated(true);
      }
      p.setInvulnerable(isAFK);
    }

    p.setPlayerListName((isAFK ? "§c[AFK] " : "") + p.getCustomName());
  }

  public PlayerPartnerEntry getPartner(PlayerEntry playerEntry) {
    if (playerEntry.getPartner() == null) {
      return databaseHelper.getPlayerPartner(playerEntry.getId());
    }
    return playerEntry.getPartner();
  }

  public void reloadPlayerHomes() {
    List<PlayerEntry> playerEntries = databaseHelper.getPlayers();

    playerRegistry.clearPlayerEntries();

    playerEntries.forEach(playerEntry ->
        playerRegistry.putPlayerEntry(UUID.fromString(playerEntry.getUuid()), playerEntry));
  }

  /**
   *
   * @param p Player to set Flying
   */
  public void setFlying(Player p) {
    if (groupService.isSenderAuthorized(p, "vip")) {
      PlayerEntry pe = playerRegistry.getPlayerEntry(p.getUniqueId());
      if (pe.isFlying()) {
        p.setAllowFlight(true);
        p.setFlying(true);
      }
    }
  }

  public void savePlayers(GroupEntry adminGroup) {
    int updatedPlayers = 0;

    for (PlayerEntry pe : playerRegistry.getPlayerEntryMap()
        .values()) {
      updatedPlayers += savePlayer(pe);
    }

    if (updatedPlayers != 0) {
      sendMessageInChannel(
          translationService.get(MessageKey.PLUGIN_PLAYERS_SAVED, updatedPlayers),
          PLUGIN_NAME_CHAT_CONSOLE,
          BetterChatFormat.ADMIN_CHANNEL,
          adminGroup
      );
    }
  }

  public void savePlayersInv(GroupEntry adminGroup) {
    int updatedPlayers = 0;

    for (Player p : Bukkit.getOnlinePlayers()) {
      updatedPlayers += WorldHelper.saveWorldGroupInventory(p, false) ? 1 : 0;
    }

    if (updatedPlayers != 0) {
      sendMessageInChannel(
          translationService.get(MessageKey.PLUGIN_PLAYERS_INVENTORY_SAVED, updatedPlayers),
          PLUGIN_NAME_CHAT_CONSOLE,
          BetterChatFormat.ADMIN_CHANNEL,
          adminGroup
      );
    }
  }


  public void savePlayer(Player p) {
    PlayerEntry pe = playerRegistry.getPlayerEntry(p);
    savePlayer(pe);
  }

  public int savePlayer(@NotNull PlayerEntry pe) {
    if (pe.isHasToBeUpdated()) {
      databaseHelper.updatePlayer(pe);
      pe.setHasToBeUpdated(false);
      return 1;
    }

    return 0;
  }
}