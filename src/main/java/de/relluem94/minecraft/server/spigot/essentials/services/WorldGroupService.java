package de.relluem94.minecraft.server.spigot.essentials.services;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
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

public class WorldGroupService {

  private final WorldGroupRegistry worldGroupRegistry;
  private final WorldGroupRepository worldGroupRepository;
  @Getter
  private final Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();

  public WorldGroupService(WorldGroupRegistry worldGroupRegistry,
      WorldGroupRepository worldGroupRepository) {
    this.worldGroupRegistry = worldGroupRegistry;
    this.worldGroupRepository = worldGroupRepository;
  }

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

  public boolean isSettingActiveForWorld(WorldSetting worldSetting, String worldName) {
    return worldGroupRegistry.isSettingActiveForWorld(worldSetting, worldName);
  }

  public @Nullable WorldGroupEntry findWorldGroupEntryForPlayer(Player player) {
    return findWorldGroupEntryByWorldName(player.getWorld().getName()).orElse(null);
  }

  public @Nullable WorldEntry findWorldEntryForPlayer(Player player) {
    String worldName = player.getWorld().getName();
    return worldsMap.entries().stream()
        .filter(entry -> entry.getKey() != null && entry.getValue() != null)
        .filter(entry -> worldName.equals(entry.getValue().getName()))
        .map(Map.Entry::getValue)
        .findFirst()
        .orElse(null);
  }

  public void loadWorldGroupInventoryForPlayer(Player player) {
    PlayerEntry playerEntry = resolvePlayerEntry(player);
    findWorldGroupEntryByWorldName(player.getWorld().getName()).ifPresent(worldGroupEntry -> {
      WorldGroupInventoryEntry inventoryEntry = resolveOrCreateInventoryEntry(player, playerEntry,
          worldGroupEntry);
      applyInventoryEntryToPlayer(player, inventoryEntry);
    });
  }

  public boolean saveWorldGroupInventoryForPlayer(Player player, boolean clearAfterSave) {
    return saveWorldGroupInventoryForPlayerInWorld(player, player.getWorld(), clearAfterSave);
  }

  public boolean saveWorldGroupInventoryForPlayerInWorld(Player player, World world,
      boolean clearAfterSave) {
    PlayerEntry playerEntry = resolvePlayerEntry(player);
    return findWorldGroupEntryByWorldName(world.getName())
        .map(worldGroupEntry -> persistPlayerInventory(worldGroupEntry, playerEntry, player,
            clearAfterSave))
        .orElse(false);
  }

  public boolean hasWorldGroupInventory(Player player, World world) {
    PlayerEntry playerEntry = resolvePlayerEntry(player);
    return findWorldGroupEntryByWorldName(world.getName())
        .map(worldGroupEntry -> resolveExistingInventoryEntry(playerEntry, worldGroupEntry) != null)
        .orElse(false);
  }

  private Optional<WorldGroupEntry> findWorldGroupEntryByWorldName(String worldName) {
    return worldsMap.entries().stream()
        .filter(entry -> entry.getKey() != null && entry.getValue() != null)
        .filter(entry -> worldName.equals(entry.getValue().getName()))
        .map(Map.Entry::getKey)
        .findFirst();
  }

  private WorldGroupInventoryEntry resolveOrCreateInventoryEntry(Player player,
      PlayerEntry playerEntry, WorldGroupEntry worldGroupEntry) {
    WorldGroupInventoryEntry existingEntry = resolveExistingInventoryEntry(playerEntry,
        worldGroupEntry);
    if (existingEntry != null) {
      return existingEntry;
    }

    WorldGroupInventoryEntry newEntry = buildNewInventoryEntry(player, playerEntry, worldGroupEntry);
    worldGroupRepository.saveInventory(newEntry);
    return newEntry;
  }

  private void applyInventoryEntryToPlayer(Player player,
      WorldGroupInventoryEntry inventoryEntry) {
    InventoryHelper.createInventory(inventoryEntry.getInventory().toString(), player);
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
    return RelluEssentials.getInstance().getServiceContext().getPlayerService()
        .getPlayerEntry(player);
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


  public WorldGroupEntry createWorldGroup(Player player, String name) {
    PlayerEntry playerEntry = resolvePlayerEntry(player);
    WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
    worldGroupEntry.setCreatedBy(playerEntry.getId());
    worldGroupEntry.setName(name);
    worldGroupRepository.saveWorldGroup(worldGroupEntry);
    return worldGroupRepository.findWorldGroupByName(name);
  }

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

  public void removeWorldFromGroup(WorldGroupEntry worldGroupEntry, String worldName) {
    worldsMap.entries().removeIf(entry ->
        worldGroupEntry.equals(entry.getKey()) && worldName.equals(entry.getValue().getName())
    );
    worldGroupRegistry.removeWorldFromAllSettings(worldName);
  }

  public Optional<WorldGroupEntry> findWorldGroupByName(String name) {
    return worldsMap.keySet().stream()
        .filter(entry -> name.equals(entry.getName()))
        .findFirst()
        .or(() -> Optional.ofNullable(worldGroupRepository.findWorldGroupByName(name)));
  }


  public void createAndRegisterWorld(String worldName, String worldGroupName, GroupEntry groupEntry, int createdBy) {
    WorldGroupEntry persistedWorldGroupEntry = worldGroupRepository.findWorldGroupByName(worldGroupName);
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

  public void createAndRegisterWorldGroup(String worldGroupName, int createdBy) {
    WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
    worldGroupEntry.setName(worldGroupName);
    worldGroupEntry.setCreatedBy(createdBy);
    worldGroupRepository.saveWorldGroup(worldGroupEntry);
  }


  public void initializeWorldGroupWithWorld(String worldGroupName, String worldName, GroupEntry groupEntry, int createdBy) {
    if (findWorldGroupByName(worldGroupName).isPresent()) {
      return;
    }

    WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
    worldGroupEntry.setCreatedBy(createdBy);
    worldGroupEntry.setName(worldGroupName);
    worldGroupRepository.saveWorldGroup(worldGroupEntry);

    WorldGroupEntry persistedWorldGroupEntry = worldGroupRepository.findWorldGroupByName(worldGroupName);

    WorldEntry worldEntry = new WorldEntry();
    worldEntry.setGroupEntry(groupEntry);
    worldEntry.setName(worldName);
    worldEntry.setCreatedBy(createdBy);
    worldEntry.setWorldGroupEntry(persistedWorldGroupEntry);
    worldGroupRepository.saveWorld(worldEntry);

    loadAll();
  }
}