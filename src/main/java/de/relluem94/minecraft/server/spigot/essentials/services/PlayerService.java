package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CHAT_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_PLAYERSERVICE_ALREADY_INITIALIZED;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.PlayerRepository;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerService {

  private final PlayerRegistry playerRegistry;
  private final ServiceContext serviceContext;
  private final PlayerRepository playerRepository;

  public PlayerService(ServiceContext serviceContext, PlayerRegistry playerRegistry, PlayerRepository playerRepository) {
    this.serviceContext = serviceContext;
    this.playerRegistry = playerRegistry;
    this.playerRepository = playerRepository;
  }

  private boolean initialized = false;

  public void initialize() {
    if (initialized) {
      throw new IllegalStateException(PLUGIN_EXCEPTION_PLAYERSERVICE_ALREADY_INITIALIZED);
    }
    initialized = true;

    List<PlayerEntry> playerEntries = playerRepository.findAll();
    playerEntries.forEach(playerEntry ->
        playerRegistry.putPlayerEntry(UUID.fromString(playerEntry.getUuid()), playerEntry)
    );

    Bukkit.getOnlinePlayers().forEach(player -> {
      PlayerEntry playerEntry = playerRegistry.getPlayerEntry(player.getUniqueId());
      setGroup(player, playerEntry.getGroup());
    });
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
    PlayerEntry pe = playerRegistry.getPlayerEntry(p.getUniqueId());

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

  public PlayerEntry getPlayerEntry(Player player) {
    return playerRegistry.getPlayerEntry(player);
  }

  public @Nullable PlayerEntry getPlayerEntryByInternalId(int id) {
    return playerRegistry.getPlayerEntryMap()
        .values()
        .stream()
        .filter(pe -> pe.getId() == id)
        .findFirst()
        .orElse(null);
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
          serviceContext.getTranslationService().getWithPrefix(
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
      return playerRepository.findPartnerByPlayerId(playerEntry.getId());
    }
    return playerEntry.getPartner();
  }

  /**
   *
   * @param p Player to set Flying
   */
  public void setFlying(Player p) {
    if (serviceContext.getGroupService().isSenderAuthorized(p, "vip")) {
      PlayerEntry pe = playerRegistry.getPlayerEntry(p.getUniqueId());
      if (pe.isFlying()) {
        p.setAllowFlight(true);
        p.setFlying(true);
      }
    }
  }

  public void savePlayers(GroupEntry adminGroup) {
    int updatedPlayers = 0;

    for (PlayerEntry pe : playerRegistry.getPlayerEntryMap().values()) {
      updatedPlayers += savePlayer(pe);
    }

    if (updatedPlayers != 0) {
      serviceContext.getChatService().sendMessageInChannel(
          serviceContext.getTranslationService()
              .get(MessageKey.PLUGIN_PLAYERS_SAVED, updatedPlayers),
          PLUGIN_NAME_CHAT_CONSOLE,
          BetterChatFormat.ADMIN_CHANNEL,
          adminGroup
      );
    }
  }

  public void savePlayersInv(GroupEntry adminGroup) {
    int updatedPlayers = 0;

    for (Player p : Bukkit.getOnlinePlayers()) {
      updatedPlayers += serviceContext.getWorldGroupService().saveWorldGroupInventoryForPlayer(p, false) ? 1 : 0;
    }

    if (updatedPlayers != 0) {
      serviceContext.getChatService().sendMessageInChannel(
          serviceContext.getTranslationService()
              .get(MessageKey.PLUGIN_PLAYERS_INVENTORY_SAVED, updatedPlayers),
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

  public int savePlayer(@NotNull PlayerEntry playerEntry) {
    if (playerEntry.isHasToBeUpdated()) {
      playerRepository.update(playerEntry);
      playerEntry.setHasToBeUpdated(false);
      return 1;
    }
    return 0;
  }

  public void putPlayerEntry(UUID uuid, PlayerEntry playerEntry) {
    playerRegistry.putPlayerEntry(uuid, playerEntry);
  }

  public PlayerEntry getPlayerEntry(UUID uuid) {
    return playerRegistry.getPlayerEntry(uuid);
  }

  public List<PlayerEntry> getAllPlayerEntries() {
    return playerRegistry.getAllPlayerEntries();
  }

  public void clearPlayerEntries() {
    playerRegistry.clearPlayerEntries();
  }

  public void savePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    playerRepository.savePartner(partnerEntry);
  }

  public void deletePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    playerRepository.deletePartner(partnerEntry);
  }

  public void updatePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    playerRepository.updatePartner(partnerEntry);
  }

  public PlayerEntry findByUuid(@NotNull String uuid) {
    return playerRepository.findByUuid(uuid);
  }

  public void registerNewPlayer(@NotNull PlayerEntry playerEntry) {
    playerRepository.save(playerEntry);
    playerRegistry.putPlayerEntry(UUID.fromString(playerEntry.getUuid()), playerEntry);
  }
}