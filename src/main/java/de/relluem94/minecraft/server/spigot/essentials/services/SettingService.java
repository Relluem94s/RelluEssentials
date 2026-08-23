package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.SettingRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.SettingRepository;
import java.util.List;
import java.util.Optional;

/**
 * Service responsible for managing settings by coordinating between
 * the repository and the registry.
 */
public class SettingService {

  private final SettingRegistry settingRegistry;
  private final SettingRepository settingRepository;

  /**
   * Constructs a new SettingService.
   *
   * @param settingRegistry the registry used for in-memory access to settings
   * @param settingRepository the repository used for persistent storage access
   */
  public SettingService(SettingRegistry settingRegistry, SettingRepository settingRepository) {
    this.settingRegistry = settingRegistry;
    this.settingRepository = settingRepository;
  }

  /**
   * Loads all settings from the repository into the registry.
   */
  public void loadAll() {
    List<SettingEntry> settings = settingRepository.findAll();
    settingRegistry.loadAll(settings);
  }

  /**
   * Finds a setting by its unique identifier.
   *
   * @param id the unique identifier of the setting
   * @return an Optional containing the found setting, or empty if not found
   */
  public Optional<SettingEntry> findById(int id) {
    return settingRegistry.findById(id);
  }

  /**
   * Finds a setting by its name.
   *
   * @param name the name of the setting
   * @return an Optional containing the found setting, or empty if not found
   */
  public Optional<SettingEntry> findByName(String name) {
    return settingRegistry.findByName(name);
  }

  /**
   * Retrieves all currently loaded settings.
   *
   * @return a list of all settings
   */
  public List<SettingEntry> getAll() {
    return settingRegistry.getAll();
  }
}