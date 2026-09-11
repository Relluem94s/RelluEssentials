package de.relluem94.minecraft.server.spigot.essentials.services;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ExperienceHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupInventoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupSettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.WorldGroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.WorldGroupRepository;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Service responsible for managing world groups, including their settings,
 * associated worlds, and player inventory persistence within those groups.
 */
public class WorldGroupService {

  private final WorldGroupRegistry worldGroupRegistry;
  private final WorldGroupRepository worldGroupRepository;
  @Getter
  private final Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
  private final ServiceContext serviceContext;

  /**
   * Constructs a new WorldGroupService.
   *
   * @param serviceContext       serviceContext to get PlayerService
   * @param worldGroupRegistry   the registry used to manage active settings for worlds
   * @param worldGroupRepository the repository used for data persistence
   */
  public WorldGroupService(ServiceContext serviceContext, WorldGroupRegistry worldGroupRegistry,
      WorldGroupRepository worldGroupRepository) {
    this.serviceContext = serviceContext;
    this.worldGroupRegistry = worldGroupRegistry;
    this.worldGroupRepository = worldGroupRepository;
  }

  /**
   * Loads all world groups and their associated worlds from the repository
   * and updates the registry with active settings.
   */
  public void loadAll() {
    worldsMap.clear();
    Map<WorldSetting, Set<String>> worldNamesByActiveSetting = buildEmptySettingMap();

    for (WorldGroupEntry worldGroupEntry : worldGroupRepository.findAllWorldGroups()) {
      for (WorldEntry worldEntry : worldGroupRepository.findWorldsByGroup(worldGroupEntry)) {
        worldsMap.put(worldGroupEntry, worldEntry);
        collectActiveSettingsForWorld(worldGroupEntry, worldEntry.getName(),
            worldNamesByActiveSetting);
      }
    }

    worldNamesByActiveSetting.forEach(worldGroupRegistry::loadWorldsForSetting);
  }

  /**
   * Checks if a specific world setting is active for a given world.
   *
   * @param worldSetting the setting to check
   * @param worldName the name of the world
   * @return true if the setting is active, false otherwise
   */
  public boolean isSettingActiveForWorld(WorldSetting worldSetting, String worldName) {
    return worldGroupRegistry.isSettingActiveForWorld(worldSetting, worldName);
  }

  /**
   * Finds the world group entry associated with the player's current world.
   *
   * @param player the player to check
   * @return the world group entry, or null if not found
   */
  @SuppressWarnings("unused")
  public @Nullable WorldGroupEntry findWorldGroupEntryForPlayer(Player player) {
    return findWorldGroupEntryByWorldName(player.getWorld().getName()).orElse(null);
  }

  /**
   * Finds the world entry associated with the player's current world.
   *
   * @param player the player to check
   * @return the world entry, or null if not found
   */
  @SuppressWarnings("unused")
  public @Nullable WorldEntry findWorldEntryForPlayer(Player player) {
    String worldName = player.getWorld().getName();
    return worldsMap.entries().stream()
        .filter(entry -> worldName.equals(entry.getValue().getName()))
        .map(Map.Entry::getValue)
        .findFirst()
        .orElse(null);
  }

  /**
   * Loads the saved inventory and player state for a player into their current world group.
   *
   * @param player the player whose inventory should be loaded
   */
  public void loadWorldGroupInventoryForPlayer(Player player) {
    PlayerEntry playerEntry = resolvePlayerEntry(player);
    findWorldGroupEntryByWorldName(player.getWorld().getName()).ifPresent(worldGroupEntry -> {
      WorldGroupInventoryEntry existingEntry = resolveExistingInventoryEntry(playerEntry, worldGroupEntry);
      if (existingEntry == null) {
        WorldGroupInventoryEntry newEntry = buildNewInventoryEntry(player, playerEntry, worldGroupEntry);
        worldGroupRepository.saveInventory(newEntry);
        return;
      }
      applyInventoryEntryToPlayer(player, existingEntry);
    });
  }

  /**
   * Saves the player's current inventory and state to the world group they are currently in.
   *
   * @param player the player whose inventory should be saved
   * @param clearAfterSave whether to clear the player's inventory and experience after saving
   * @return true if the save was successful, false otherwise
   */
  public boolean saveWorldGroupInventoryForPlayer(Player player, boolean clearAfterSave) {
    return saveWorldGroupInventoryForPlayerInWorld(player, player.getWorld(), clearAfterSave);
  }

  /**
   * Saves the player's current inventory and state to a specific world.
   *
   * @param player the player whose inventory should be saved
   * @param world the world where the inventory should be saved
   * @param clearAfterSave whether to clear the player's inventory and experience after saving
   * @return true if the save was successful, false otherwise
   */
  public boolean saveWorldGroupInventoryForPlayerInWorld(Player player, World world,
      boolean clearAfterSave) {
    PlayerEntry playerEntry = resolvePlayerEntry(player);
    return findWorldGroupEntryByWorldName(world.getName())
        .map(worldGroupEntry -> persistPlayerInventory(worldGroupEntry, playerEntry, player,
            clearAfterSave))
        .orElse(false);
  }

  /**
   * Checks if the player has a saved inventory for the specified world.
   *
   * @param player the player to check
   * @param world the world to check against
   * @return true if an inventory exists, false otherwise
   */
  @SuppressWarnings("unused")
  public boolean hasWorldGroupInventory(Player player, World world) {
    PlayerEntry playerEntry = resolvePlayerEntry(player);
    return findWorldGroupEntryByWorldName(world.getName())
        .map(worldGroupEntry -> resolveExistingInventoryEntry(playerEntry, worldGroupEntry) != null)
        .orElse(false);
  }

  private Optional<WorldGroupEntry> findWorldGroupEntryByWorldName(String worldName) {
    return worldsMap.entries().stream()
        .filter(entry -> worldName.equals(entry.getValue().getName()))
        .map(Map.Entry::getKey)
        .findFirst();
  }

  private void applyInventoryEntryToPlayer(Player player,
      WorldGroupInventoryEntry inventoryEntry) {
    if (inventoryEntry.getInventory() != null) {
      InventoryHelper.createInventory(inventoryEntry.getInventory().toString(), player);
    }
    player.setFoodLevel(inventoryEntry.getFoodLevel());
    player.setHealth(inventoryEntry.getHealth());
    ExperienceHelper.setTotalExperience(player, inventoryEntry.getTotalExperience());
  }

  private boolean persistPlayerInventory(WorldGroupEntry worldGroupEntry, PlayerEntry playerEntry,
      Player player, boolean clearAfterSave) {
    WorldGroupInventoryEntry existingEntry = resolveExistingInventoryEntry(playerEntry,
        worldGroupEntry);

    if (existingEntry == null) {
      WorldGroupInventoryEntry newEntry = buildNewInventoryEntry(player, playerEntry,
          worldGroupEntry);
      clearPlayerStateIfRequired(player, clearAfterSave);
      worldGroupRepository.saveInventory(newEntry);
      return false;
    }

    updateInventoryEntryFromPlayer(existingEntry, player, playerEntry);
    clearPlayerStateIfRequired(player, clearAfterSave);
    worldGroupRepository.updateInventory(existingEntry);
    return true;
  }

  private WorldGroupInventoryEntry buildNewInventoryEntry(Player player, PlayerEntry playerEntry,
      WorldGroupEntry worldGroupEntry) {
    WorldGroupInventoryEntry entry = new WorldGroupInventoryEntry();
    entry.setCreatedBy(playerEntry.getId());
    entry.setPlayerId(playerEntry.getId());
    entry.setWorldGroupEntry(worldGroupEntry);
    entry.setInventory(InventoryHelper.saveInventoryToJSON(player));
    entry.setFoodLevel(player.getFoodLevel());
    entry.setHealth(player.getHealth());
    entry.setTotalExperience(ExperienceHelper.getTotalExperience(player));
    return entry;
  }

  private void updateInventoryEntryFromPlayer(WorldGroupInventoryEntry entry, Player player,
      PlayerEntry playerEntry) {
    entry.setInventory(InventoryHelper.saveInventoryToJSON(player));
    entry.setFoodLevel(player.getFoodLevel());
    entry.setHealth(player.getHealth());
    entry.setUpdatedBy(playerEntry.getId());
    entry.setTotalExperience(ExperienceHelper.getTotalExperience(player));
  }

  private void clearPlayerStateIfRequired(Player player, boolean clearAfterSave) {
    if (!clearAfterSave) {
      return;
    }
    player.setTotalExperience(0);
    player.setLevel(0);
    player.setExp(0);
    player.getInventory().clear();
  }

  private @Nullable WorldGroupInventoryEntry resolveExistingInventoryEntry(PlayerEntry playerEntry,
      WorldGroupEntry worldGroupEntry) {
    return worldGroupRepository.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry);
  }

  private PlayerEntry resolvePlayerEntry(Player player) {
    return serviceContext.getPlayerService().getPlayerEntry(player);
  }

  private Map<WorldSetting, Set<String>> buildEmptySettingMap() {
    Map<WorldSetting, Set<String>> map = new EnumMap<>(WorldSetting.class);
    Arrays.stream(WorldSetting.values()).forEach(setting -> map.put(setting, new HashSet<>()));
    return map;
  }

  private void collectActiveSettingsForWorld(WorldGroupEntry worldGroupEntry, String worldName,
      Map<WorldSetting, Set<String>> worldNamesByActiveSetting) {
    for (WorldSetting worldSetting : WorldSetting.values()) {
      boolean isActive = worldGroupEntry.getSettings().stream()
          .filter(s -> worldSetting.name().equals(s.getSettingEntry().getName()))
          .findFirst()
          .map(WorldGroupSettingEntry::isValue)
          .orElse(false);
      if (isActive) {
        worldNamesByActiveSetting.get(worldSetting).add(worldName);
      }
    }
  }

  /**
   * Creates a new world group.
   *
   * @param player the player creating the group
   * @param name the name of the new group
   * @return the created world group entry
   */
  @SuppressWarnings("unused")
  public WorldGroupEntry createWorldGroup(Player player, String name) {
    PlayerEntry playerEntry = resolvePlayerEntry(player);
    WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
    worldGroupEntry.setCreatedBy(playerEntry.getId());
    worldGroupEntry.setName(name);
    worldGroupRepository.saveWorldGroup(worldGroupEntry);
    return worldGroupRepository.findWorldGroupByName(name);
  }

  /**
   * Adds a world to an existing world group.
   *
   * @param player the player performing the action
   * @param worldGroupEntry the group to add the world to
   * @param worldName the name of the world to add
   */
  @SuppressWarnings("unused")
  public void addWorldToGroup(Player player, WorldGroupEntry worldGroupEntry, String worldName) {
    PlayerEntry playerEntry = resolvePlayerEntry(player);
    WorldEntry worldEntry = new WorldEntry();
    worldEntry.setCreatedBy(playerEntry.getId());
    worldEntry.setName(worldName);
    worldEntry.setWorldGroupEntry(worldGroupEntry);
    worldGroupRepository.saveWorld(worldEntry);
    worldsMap.put(worldGroupEntry, worldEntry);
    collectActiveSettingsForWorld(worldGroupEntry, worldName, buildEmptySettingMap());
  }

  /**
   * Removes a world from a world group.
   *
   * @param worldGroupEntry the group to remove the world from
   * @param worldName the name of the world to remove
   */
  @SuppressWarnings("unused")
  public void removeWorldFromGroup(WorldGroupEntry worldGroupEntry, String worldName) {
    worldsMap.entries().removeIf(entry ->
        worldGroupEntry.equals(entry.getKey()) && worldName.equals(entry.getValue().getName())
    );
    worldGroupRegistry.removeWorldFromAllSettings(worldName);
  }

  /**
   * Finds a world group by its name.
   *
   * @param name the name of the group to find
   * @return an optional containing the world group entry if found
   */
  public Optional<WorldGroupEntry> findWorldGroupByName(String name) {
    return worldsMap.keySet().stream()
        .filter(entry -> name.equals(entry.getName()))
        .findFirst()
        .or(() -> Optional.ofNullable(worldGroupRepository.findWorldGroupByName(name)));
  }

  /**
   * Creates a new world and registers it within an existing world group.
   *
   * @param worldName the name of the new world
   * @param worldGroupName the name of the group to register the world in
   * @param groupEntry the group entry to associate with the world
   * @param createdBy the ID of the user who created the world
   */
  @SuppressWarnings("unused")
  public void createAndRegisterWorld(String worldName, String worldGroupName,
      GroupEntry groupEntry, int createdBy) {
    WorldGroupEntry persistedWorldGroupEntry =
        worldGroupRepository.findWorldGroupByName(worldGroupName);
    if (persistedWorldGroupEntry == null) {
      return;
    }

    WorldEntry worldEntry = new WorldEntry();
    worldEntry.setName(worldName);
    worldEntry.setCreatedBy(createdBy);
    worldEntry.setWorldGroupEntry(persistedWorldGroupEntry);
    worldEntry.setGroupEntry(groupEntry);

    worldGroupRepository.saveWorld(worldEntry);
    worldsMap.put(persistedWorldGroupEntry, worldEntry);
    collectActiveSettingsForWorld(persistedWorldGroupEntry, worldName, buildEmptySettingMap());
  }

  /**
   * Creates a new world group without any worlds.
   *
   * @param worldGroupName the name of the new group
   * @param createdBy the ID of the user who created the group
   */
  @SuppressWarnings("unused")
  public void createAndRegisterWorldGroup(String worldGroupName, int createdBy) {
    WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
    worldGroupEntry.setName(worldGroupName);
    worldGroupEntry.setCreatedBy(createdBy);
    worldGroupRepository.saveWorldGroup(worldGroupEntry);
  }

  private boolean isWorldGroupRegisteredInMemory(String worldGroupName) {
    return worldsMap.keySet().stream()
        .anyMatch(entry -> worldGroupName.equals(entry.getName()));
  }

  /**
   * Creates a new world group and immediately initializes it with a world.
   *
   * @param worldGroupName the name of the new group
   * @param worldName the name of the world to initialize the group with
   * @param groupEntry the group entry to associate with the world
   * @param createdBy the ID of the user who created the group and world
   */
  @SuppressWarnings("unused")
  public void initializeWorldGroupWithWorld(String worldGroupName, String worldName,
      GroupEntry groupEntry, int createdBy) {
    if (isWorldGroupRegisteredInMemory(worldGroupName)) {
      return;
    }

    WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
    worldGroupEntry.setCreatedBy(createdBy);
    worldGroupEntry.setName(worldGroupName);
    worldGroupRepository.saveWorldGroup(worldGroupEntry);

    WorldGroupEntry persistedWorldGroupEntry =
        worldGroupRepository.findWorldGroupByName(worldGroupName);

    WorldEntry worldEntry = new WorldEntry();
    worldEntry.setGroupEntry(groupEntry);
    worldEntry.setName(worldName);
    worldEntry.setCreatedBy(createdBy);
    worldEntry.setWorldGroupEntry(persistedWorldGroupEntry);
    worldGroupRepository.saveWorld(worldEntry);

    loadAll();
  }
}