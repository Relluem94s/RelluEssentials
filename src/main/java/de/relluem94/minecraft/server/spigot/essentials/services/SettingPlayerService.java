package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.SettingPlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.SettingPlayerRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SettingPlayerService {

  private final SettingPlayerRegistry settingPlayerRegistry;
  private final SettingPlayerRepository settingPlayerRepository;

  public SettingPlayerService(SettingPlayerRegistry settingPlayerRegistry, SettingPlayerRepository settingPlayerRepository) {
    this.settingPlayerRegistry = settingPlayerRegistry;
    this.settingPlayerRepository = settingPlayerRepository;
  }

  public void loadAllForPlayer(int playerId) {
    List<SettingPlayerEntry> entries = settingPlayerRepository.findAllByPlayerId(playerId);
    settingPlayerRegistry.loadAllForPlayer(playerId, entries);
  }

  public Optional<SettingPlayerEntry> findById(int id) {
    return settingPlayerRegistry.findById(id)
        .or(() -> settingPlayerRepository.findById(id)
            .map(entry -> {
              settingPlayerRegistry.put(entry);
              return entry;
            }));
  }

  public Optional<SettingPlayerEntry> findByPlayerIdAndSettingId(int playerId, int settingId) {
    return settingPlayerRegistry.findByPlayerIdAndSettingId(playerId, settingId)
        .or(() -> settingPlayerRepository.findAllByPlayerId(playerId).stream()
            .filter(entry -> entry.getSettingFk() == settingId)
            .findFirst()
            .map(entry -> {
              settingPlayerRegistry.put(entry);
              return entry;
            }));
  }

  public List<SettingPlayerEntry> findAllByPlayerId(int playerId) {
    List<SettingPlayerEntry> cached = settingPlayerRegistry.findAllByPlayerId(playerId);
    if (!cached.isEmpty()) {
      return cached;
    }
    List<SettingPlayerEntry> entries = settingPlayerRepository.findAllByPlayerId(playerId);
    settingPlayerRegistry.loadAllForPlayer(playerId, entries);
    return entries;
  }

  public void insert(SettingPlayerEntry entry) {
    entry.setCreated(LocalDateTime.now());
    settingPlayerRepository.insert(entry);
    settingPlayerRegistry.put(entry);
  }

  public void update(SettingPlayerEntry entry) {
    entry.setUpdated(LocalDateTime.now());
    settingPlayerRepository.update(entry);
    settingPlayerRegistry.put(entry);
  }

  public void delete(int id, int deletedBy) {
    settingPlayerRepository.softDelete(id, deletedBy);
    settingPlayerRegistry.remove(id);
  }
}