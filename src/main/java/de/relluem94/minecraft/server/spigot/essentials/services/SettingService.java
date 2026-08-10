package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.SettingRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.SettingRepository;
import java.util.List;
import java.util.Optional;

public class SettingService {

  private final SettingRegistry settingRegistry;
  private final SettingRepository settingRepository;

  public SettingService(SettingRegistry settingRegistry, SettingRepository settingRepository) {
    this.settingRegistry = settingRegistry;
    this.settingRepository = settingRepository;
  }

  public void loadAll() {
    List<SettingEntry> settings = settingRepository.findAll();
    settingRegistry.loadAll(settings);
  }

  public Optional<SettingEntry> findById(int id) {
    return settingRegistry.findById(id);
  }

  public Optional<SettingEntry> findByName(String name) {
    return settingRegistry.findByName(name);
  }

  public List<SettingEntry> getAll() {
    return settingRegistry.getAll();
  }
}