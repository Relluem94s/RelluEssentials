package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerSetting;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.SettingPlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.SettingPlayerRepository;
import java.util.List;
import java.util.Optional;
import org.bukkit.entity.Player;

public class SettingPlayerService {

  private final SettingPlayerRegistry settingPlayerRegistry;
  private final SettingPlayerRepository settingPlayerRepository;
  private final ServiceContext serviceContext;

  public SettingPlayerService(
      SettingPlayerRegistry settingPlayerRegistry,
      SettingPlayerRepository settingPlayerRepository,
      ServiceContext serviceContext
  ) {
    this.settingPlayerRegistry = settingPlayerRegistry;
    this.settingPlayerRepository = settingPlayerRepository;
    this.serviceContext = serviceContext;
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
    settingPlayerRepository.insert(entry);
    settingPlayerRegistry.put(entry);
  }

  public void update(SettingPlayerEntry entry) {
    settingPlayerRepository.update(entry);
    settingPlayerRegistry.put(entry);
  }

  public void delete(int id, int deletedBy) {
    settingPlayerRepository.softDelete(id, deletedBy);
    settingPlayerRegistry.remove(id);
  }

  public boolean isSettingActiveForPlayer(Player player, PlayerSetting playerSetting) {
    PlayerEntry playerEntry = serviceContext.getPlayerService().getPlayerEntry(player);
    return findAllByPlayerId(playerEntry.getId()).stream()
        .filter(entry -> playerSetting.name().equals(entry.getSettingEntry().getName()))
        .findFirst()
        .map(SettingPlayerEntry::isValue)
        .orElse(false);
  }
}