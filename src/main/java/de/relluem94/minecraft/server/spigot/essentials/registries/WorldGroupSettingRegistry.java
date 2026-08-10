package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorldGroupSettingRegistry {

  private final Map<WorldSetting, Set<String>> worldNamesByActiveSetting =
      new EnumMap<>(WorldSetting.class);

  public void loadWorldsForSetting(WorldSetting worldSetting, Set<String> worldNames) {
    worldNamesByActiveSetting.put(worldSetting, new HashSet<>(worldNames));
  }

  public boolean isSettingActiveForWorld(WorldSetting worldSetting, String worldName) {
    return worldNamesByActiveSetting
        .getOrDefault(worldSetting, Collections.emptySet())
        .contains(worldName);
  }

  public Set<String> getWorldsWithActiveSetting(WorldSetting worldSetting) {
    return Collections.unmodifiableSet(
        worldNamesByActiveSetting.getOrDefault(worldSetting, Collections.emptySet()));
  }
}