package de.relluem94.minecraft.server.spigot.essentials.services;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupSettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.WorldGroupSettingRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.WorldGroupSettingRepository;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;

public class WorldGroupService {

  private final WorldGroupSettingRegistry worldGroupSettingRegistry;
  private final WorldGroupSettingRepository worldGroupSettingRepository;
  @Getter
  private final Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();

  public WorldGroupService(WorldGroupSettingRegistry worldGroupSettingRegistry,
      WorldGroupSettingRepository worldGroupSettingRepository) {
    this.worldGroupSettingRegistry = worldGroupSettingRegistry;
    this.worldGroupSettingRepository = worldGroupSettingRepository;
  }

  public void loadAll() {
    worldsMap.clear();
    Map<WorldSetting, Set<String>> worldNamesByActiveSetting = buildEmptySettingMap();

    for (WorldGroupEntry worldGroupEntry : worldGroupSettingRepository.findAllWorldGroups()) {
      for (WorldEntry worldEntry : worldGroupSettingRepository.findWorldsByGroup(worldGroupEntry)) {
        worldsMap.put(worldGroupEntry, worldEntry);
        collectActiveSettingsForWorld(worldGroupEntry, worldEntry.getName(),
            worldNamesByActiveSetting);
      }
    }

    worldNamesByActiveSetting.forEach(worldGroupSettingRegistry::loadWorldsForSetting);
  }

  public boolean isSettingActiveForWorld(WorldSetting worldSetting, String worldName) {
    return worldGroupSettingRegistry.isSettingActiveForWorld(worldSetting, worldName);
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
}