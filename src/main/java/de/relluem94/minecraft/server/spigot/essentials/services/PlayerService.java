package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CHAT_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_PLAYERSERVICE_ALREADY_INITIALIZED;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.PlayerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Service responsible for managing player data, including registration, retrieval, and persistence
 * via the player registry and repository.
 */
public class PlayerService {

  private final PlayerRegistry playerRegistry;
  private final ServiceContext serviceContext;
  private final PlayerRepository playerRepository;
  private boolean initialized = false;

  /**
   * Constructs a new PlayerService.
   *
   * @param serviceContext   The global service context.
   * @param playerRegistry   The registry for managing in-memory player entries.
   * @param playerRepository The repository for persisting player data.
   */
  public PlayerService(ServiceContext serviceContext, PlayerRegistry playerRegistry,
      PlayerRepository playerRepository) {
    this.serviceContext = serviceContext;
    this.playerRegistry = playerRegistry;
    this.playerRepository = playerRepository;
  }

  /**
   * Initializes the service by loading all player entries from the repository into the registry and
   * applying group prefixes to currently online players.
   *
   * @throws IllegalStateException if the service has already been initialized.
   */
  public void initialize() {
    if (initialized) {
      throw new IllegalStateException(PLUGIN_EXCEPTION_PLAYERSERVICE_ALREADY_INITIALIZED);
    }
    initialized = true;

    List<PlayerEntry> playerEntries = playerRepository.findAll();
    playerEntries.forEach(
        playerEntry -> playerRegistry.putPlayerEntry(UUID.fromString(playerEntry.getUuid()),
            playerEntry));

    serviceContext.getPluginMetadataService().getPlugin().getServer().getOnlinePlayers()
        .forEach(player -> {
          PlayerEntry playerEntry = playerRegistry.getPlayerEntry(player.getUniqueId());
          setGroup(player, playerEntry.getGroup());
        });
  }

  /**
   * Retrieves a player entry by their username.
   *
   * @param name The username to search for.
   * @return The {@link PlayerEntry} if found, otherwise {@code null}.
   */
  @SuppressWarnings("unused")
  public @Nullable PlayerEntry getPlayerByName(String name) {
    for (PlayerEntry pe : playerRegistry.getPlayerEntryMap().values()) {
      if (pe.getName().equals(name)) {
        return pe;
      }
    }

    return null;
  }

  /**
   * Retrieves a player entry by their UUID string.
   *
   * @param uuid The UUID string to search for.
   * @return The {@link PlayerEntry} if found, otherwise {@code null}.
   */
  public @Nullable PlayerEntry getPlayerByUuid(String uuid) {
    for (PlayerEntry pe : playerRegistry.getPlayerEntryMap().values()) {
      if (pe.getUuid().equals(uuid)) {
        return pe;
      }
    }

    return null;
  }

  /**
   * Gets the custom name of a player. Falling back to their Minecraft name if no custom name is
   * set.
   *
   * @param p The player.
   * @return The custom name or the default Minecraft name.
   */
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

  /**
   * Sets the player's display name and tab list name based on their group prefix.
   *
   * @param p The player.
   * @param g The group entry containing the prefix.
   */
  public void setGroup(Player p, GroupEntry g) {
    p.setCustomName(g.getPrefix() + getCustomName(p));
    p.setPlayerListName(p.getCustomName());
  }

  /**
   * Updates a player's group, applying the prefix to their display name if they are online.
   *
   * @param p The offline player.
   * @param g The new group entry.
   */
  public void updateGroup(OfflinePlayer p, GroupEntry g) {
    PlayerEntry pe = playerRegistry.getPlayerEntry(p.getUniqueId());

    if (p.isOnline()) {
      Player player = serviceContext.getPluginMetadataService().getPlugin().getServer()
          .getPlayer(p.getUniqueId());
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
   * Retrieves the player entry for a specific player.
   *
   * @param player The player.
   * @return The associated {@link PlayerEntry}.
   */
  public PlayerEntry getPlayerEntry(Player player) {
    return playerRegistry.getPlayerEntry(player);
  }

  /**
   * Retrieves a player entry by UUID.
   *
   * @param uuid The player's UUID.
   * @return The {@link PlayerEntry}.
   */
  public PlayerEntry getPlayerEntry(UUID uuid) {
    return playerRegistry.getPlayerEntry(uuid);
  }

  /**
   * Retrieves a player entry by their internal integer ID.
   *
   * @param id The internal ID.
   * @return The {@link PlayerEntry} if found, otherwise {@code null}.
   */
  public @Nullable PlayerEntry getPlayerEntryByInternalId(int id) {
    return playerRegistry.getPlayerEntryMap().values().stream().filter(pe -> pe.getId() == id)
        .findFirst().orElse(null);
  }

  /**
   * Sets the AFK status of a player.
   *
   * @param p    The player.
   * @param join Whether this is a join event (true) or a command toggle (false).
   */
  public void setAfk(Player p, boolean join) {
    PlayerEntry pe = playerRegistry.getPlayerEntry(p.getUniqueId());
    boolean isAfk = pe.isAfk();

    if (pe.getPlayerState().equals(PlayerState.FAKE_AFK_ACTIVE)) {
      isAfk = true;
    } else if (pe.getPlayerState().equals(PlayerState.FAKE_AFK_ON)) {
      isAfk = false;
    }

    if (!join) {
      serviceContext.getPluginMetadataService().getPlugin().getServer().broadcastMessage(
          serviceContext.getTranslationService().getWithPrefix(
              !isAfk ? MessageKey.COMMAND_AFK_ACTIVATED : MessageKey.COMMAND_AFK_DEACTIVATED,
              p.getLocale(), p.getCustomName() + "§f", !isAfk ? "§c" : "§a"));
      isAfk = !isAfk; // Invert for single invertion ^_^
    }

    if (pe.getPlayerState().equals(PlayerState.DEFAULT)) {
      if (!join) {
        pe.setUpdatedBy(pe.getId());
        pe.setAfk(isAfk);
        pe.setUpdatedBy(pe.getId());
        pe.setHasToBeUpdated(true);
      }
      p.setInvulnerable(isAfk);
    }

    p.setPlayerListName((isAfk ? "§c[AFK] " : "") + p.getCustomName());
  }

  /**
   * Retrieves the partner entry for a given player entry.
   *
   * @param playerEntry The player entry to check.
   * @return The {@link PlayerPartnerEntry} associated with the player.
   */
  public PlayerPartnerEntry getPartner(PlayerEntry playerEntry) {
    if (playerEntry.getPartner() == null) {
      return playerRepository.findPartnerByPlayerId(playerEntry.getId());
    }
    return playerEntry.getPartner();
  }

  /**
   * Enables flying for a player if they are authorized.
   *
   * @param p The player.
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

  /**
   * Saves all player entries currently in the registry to the repository.
   *
   * @param adminGroup The group of the administrator performing the save.
   */
  public void savePlayers(GroupEntry adminGroup) {
    int updatedPlayers = 0;

    for (PlayerEntry pe : playerRegistry.getPlayerEntryMap().values()) {
      updatedPlayers += savePlayer(pe);
    }

    if (updatedPlayers != 0) {
      serviceContext.getChatService().sendMessageInChannel(serviceContext.getTranslationService()
              .get(MessageKey.PLUGIN_PLAYERS_SAVED, updatedPlayers), PLUGIN_NAME_CHAT_CONSOLE,
          BetterChatFormat.ADMIN_CHANNEL, adminGroup);
    }
  }

  /**
   * Saves the inventories of all online players.
   *
   * @param adminGroup The group of the administrator performing the save.
   */
  public void savePlayersInv(GroupEntry adminGroup) {
    int updatedPlayers = 0;

    for (Player p : serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getOnlinePlayers()) {
      updatedPlayers +=
          serviceContext.getWorldGroupService().saveWorldGroupInventoryForPlayer(p, false) ? 1 : 0;
    }

    if (updatedPlayers != 0) {
      serviceContext.getChatService().sendMessageInChannel(serviceContext.getTranslationService()
              .get(MessageKey.PLUGIN_PLAYERS_INVENTORY_SAVED, updatedPlayers),
          PLUGIN_NAME_CHAT_CONSOLE, BetterChatFormat.ADMIN_CHANNEL, adminGroup);
    }
  }

  /**
   * Saves the data for a specific player.
   *
   * @param p The player.
   */
  public void savePlayer(Player p) {
    PlayerEntry pe = playerRegistry.getPlayerEntry(p);
    savePlayer(pe);
  }

  /**
   * Saves the specific player entry if it has pending updates.
   *
   * @param playerEntry The player entry to save.
   * @return The number of players updated (1 if updated, 0 otherwise).
   */
  public int savePlayer(@NotNull PlayerEntry playerEntry) {
    if (playerEntry.isHasToBeUpdated()) {
      playerRepository.update(playerEntry);
      playerEntry.setHasToBeUpdated(false);
      return 1;
    }
    return 0;
  }

  /**
   * Manually adds a player entry to the registry.
   *
   * @param uuid        The player's UUID.
   * @param playerEntry The player entry to add.
   */
  public void putPlayerEntry(UUID uuid, PlayerEntry playerEntry) {
    playerRegistry.putPlayerEntry(uuid, playerEntry);
  }

  /**
   * Retrieves all player entries currently in the registry.
   *
   * @return A list of all {@link PlayerEntry} objects.
   */
  public List<PlayerEntry> getAllPlayerEntries() {
    return playerRegistry.getAllPlayerEntries();
  }

  /**
   * Clears all player entries from the registry.
   */
  @SuppressWarnings("unused")
  public void clearPlayerEntries() {
    playerRegistry.clearPlayerEntries();
  }

  /**
   * Saves a partner entry to the repository.
   *
   * @param partnerEntry The partner entry to save.
   */
  public void savePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    playerRepository.savePartner(partnerEntry);
  }

  /**
   * Deletes a partner entry from the repository.
   *
   * @param partnerEntry The partner entry to delete.
   */
  public void deletePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    playerRepository.deletePartner(partnerEntry);
  }

  /**
   * Updates an existing partner entry in the repository.
   *
   * @param partnerEntry The partner entry to update.
   */
  @SuppressWarnings("unused")
  public void updatePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    playerRepository.updatePartner(partnerEntry);
  }

  /**
   * Finds a player entry in the repository by their UUID.
   *
   * @param uuid The UUID string.
   * @return The {@link PlayerEntry} if found.
   */
  @SuppressWarnings("unused")
  public PlayerEntry findByUuid(@NotNull String uuid) {
    return playerRepository.findByUuid(uuid);
  }

  /**
   * Registers a new player by saving them to the repository and adding them to the registry.
   *
   * @param playerEntry The new player entry.
   */
  public void registerNewPlayer(@NotNull PlayerEntry playerEntry) {
    playerRepository.save(playerEntry);
    playerRegistry.putPlayerEntry(UUID.fromString(playerEntry.getUuid()), playerEntry);
  }

  /**
   * Retrieves the names of all home and death locations for a player.
   *
   * @param player The player.
   * @return A list of location name strings.
   */
  public List<String> getHomeAndDeathLocationNames(Player player) {
    PlayerEntry playerEntry = playerRegistry.getPlayerEntry(player.getUniqueId());
    List<String> locationNames = new ArrayList<>();

    for (LocationEntry locationEntry : playerEntry.getHomes()) {
      locationNames.add(locationEntry.getLocationName());
    }

    for (LocationEntry locationEntry : playerEntry.getDeaths()) {
      locationNames.add(locationEntry.getLocationName());
    }

    return locationNames;
  }
}