package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry responsible for managing and providing access to {@link SettingEntry} objects. It allows
 * looking up settings by their unique identifier or their name.
 */
public class SettingRegistry {

  private final Map<Integer, SettingEntry> settingsById = new ConcurrentHashMap<>();
  private final Map<String, SettingEntry> settingsByName = new ConcurrentHashMap<>();

  /**
   * Populates the registry with the provided list of settings. This operation clears any existing
   * settings before loading the new ones.
   *
   * @param settings the list of {@link SettingEntry} to be registered
   */
  public void loadAll(List<SettingEntry> settings) {
    settingsById.clear();
    settingsByName.clear();
    settings.forEach(setting -> {
      settingsById.put(setting.getId(), setting);
      settingsByName.put(setting.getName(), setting);
    });
  }

  /**
   * Finds a setting by its unique integer ID.
   *
   * @param id the unique identifier of the setting
   * @return an {@link Optional} containing the found {@link SettingEntry}, or empty if not found
   */
  public Optional<SettingEntry> findById(int id) {
    return Optional.ofNullable(settingsById.get(id));
  }

  /**
   * Finds a setting by its string name.
   *
   * @param name the name of the setting
   * @return an {@link Optional} containing the found {@link SettingEntry}, or empty if not found
   */
  public Optional<SettingEntry> findByName(String name) {
    return Optional.ofNullable(settingsByName.get(name));
  }

  /**
   * Retrieves all registered settings.
   *
   * @return an unmodifiable list containing all {@link SettingEntry} objects in the registry
   */
  public List<SettingEntry> getAll() {
    return List.copyOf(settingsById.values());
  }
}