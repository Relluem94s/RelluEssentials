package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Registry that manages the association between {@link WorldSetting} configurations
 * and specific world names.
 */
public class WorldGroupRegistry {

  private final Map<WorldSetting, Set<String>> worldNamesByActiveSetting =
      new EnumMap<>(WorldSetting.class);

  /**
   * Overwrites the current set of worlds associated with a specific setting.
   *
   * @param worldSetting the setting to configure
   * @param worldNames   the set of world names to associate with this setting
   */
  public void loadWorldsForSetting(WorldSetting worldSetting, Set<String> worldNames) {
    worldNamesByActiveSetting.put(worldSetting, new HashSet<>(worldNames));
  }

  /**
   * Checks if a specific world is registered under a given setting.
   *
   * @param worldSetting the setting to check against
   * @param worldName   the name of the world
   * @return true if the world is part of the specified setting, false otherwise
   */
  public boolean isSettingActiveForWorld(WorldSetting worldSetting, String worldName) {
    return worldNamesByActiveSetting
        .getOrDefault(worldSetting, Collections.emptySet())
        .contains(worldName);
  }

  /**
   * Retrieves an unmodifiable set of all worlds associated with a specific setting.
   *
   * @param worldSetting the setting to query
   * @return an unmodifiable set of world names
   */
  public Set<String> getWorldsWithActiveSetting(WorldSetting worldSetting) {
    return Collections.unmodifiableSet(
        worldNamesByActiveSetting.getOrDefault(worldSetting, Collections.emptySet()));
  }

  /**
   * Adds a single world to an existing setting.
   *
   * @param worldSetting the setting to add the world to
   * @param worldName    the name of the world
   */
  public void addWorldToSetting(WorldSetting worldSetting, String worldName) {
    worldNamesByActiveSetting
        .computeIfAbsent(worldSetting, _ -> new HashSet<>())
        .add(worldName);
  }

  /**
   * Removes a specific world from a specific setting.
   *
   * @param worldSetting the setting to remove the world from
   * @param worldName    the name of the world
   */
  public void removeWorldFromSetting(WorldSetting worldSetting, String worldName) {
    worldNamesByActiveSetting
        .getOrDefault(worldSetting, Collections.emptySet())
        .remove(worldName);
  }

  /**
   * Removes a world from every registered setting in the registry.
   *
   * @param worldName the name of the world to be removed everywhere
   */
  public void removeWorldFromAllSettings(String worldName) {
    worldNamesByActiveSetting.values().forEach(worldNames -> worldNames.remove(worldName));
  }
}