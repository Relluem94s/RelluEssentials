package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SettingRegistry {

  private final Map<Integer, SettingEntry> settingsById = new ConcurrentHashMap<>();
  private final Map<String, SettingEntry> settingsByName = new ConcurrentHashMap<>();

  public void loadAll(List<SettingEntry> settings) {
    settingsById.clear();
    settingsByName.clear();
    settings.forEach(setting -> {
      settingsById.put(setting.getId(), setting);
      settingsByName.put(setting.getName(), setting);
    });
  }

  public Optional<SettingEntry> findById(int id) {
    return Optional.ofNullable(settingsById.get(id));
  }

  public Optional<SettingEntry> findByName(String name) {
    return Optional.ofNullable(settingsByName.get(name));
  }

  public List<SettingEntry> getAll() {
    return Collections.unmodifiableList(List.copyOf(settingsById.values()));
  }
}