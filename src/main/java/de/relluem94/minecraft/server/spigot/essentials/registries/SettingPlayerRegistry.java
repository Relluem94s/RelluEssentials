package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SettingPlayerRegistry {

  private final Map<Integer, SettingPlayerEntry> entriesById = new ConcurrentHashMap<>();

  public void loadAllForPlayer(int playerId, List<SettingPlayerEntry> entries) {
    entriesById.entrySet().removeIf(e -> e.getValue().getPlayerFk() == playerId);
    entries.forEach(entry -> entriesById.put(entry.getId(), entry));
  }

  public Optional<SettingPlayerEntry> findById(int id) {
    return Optional.ofNullable(entriesById.get(id));
  }

  public Optional<SettingPlayerEntry> findByPlayerIdAndSettingId(int playerId, int settingId) {
    return entriesById.values().stream()
        .filter(entry -> entry.getPlayerFk() == playerId && entry.getSettingFk() == settingId)
        .findFirst();
  }

  public List<SettingPlayerEntry> findAllByPlayerId(int playerId) {
    return Collections.unmodifiableList(
        entriesById.values().stream()
            .filter(entry -> entry.getPlayerFk() == playerId)
            .collect(Collectors.toList())
    );
  }

  public void put(SettingPlayerEntry entry) {
    entriesById.put(entry.getId(), entry);
  }

  public void remove(int id) {
    entriesById.remove(id);
  }
}