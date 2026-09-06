package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerSetting;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.SettingPlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.SettingPlayerRepository;
import java.util.List;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingPlayerServiceTest {

  @Mock
  private SettingPlayerRegistry settingPlayerRegistry;

  @Mock
  private SettingPlayerRepository settingPlayerRepository;

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private PlayerService playerService;

  @Mock
  private Player player;

  private SettingPlayerService settingPlayerService;

  @BeforeEach
  void setUp() {
    settingPlayerService = new SettingPlayerService(
        settingPlayerRegistry,
        settingPlayerRepository,
        serviceContext
    );
  }

  @Test
  void loadAllForPlayerLoadsEntriesFromRepositoryIntoRegistry() {
    int playerId = 1;
    List<SettingPlayerEntry> entries = List.of(new SettingPlayerEntry());
    when(settingPlayerRepository.findAllByPlayerId(playerId)).thenReturn(entries);

    settingPlayerService.loadAllForPlayer(playerId);

    assertAll(
        () -> verify(settingPlayerRepository).findAllByPlayerId(playerId),
        () -> verify(settingPlayerRegistry).loadAllForPlayer(playerId, entries)
    );
  }

  @Test
  void findByIdReturnsCachedEntryWhenPresentInRegistry() {
    int id = 42;
    SettingPlayerEntry cachedEntry = new SettingPlayerEntry();
    cachedEntry.setId(id);
    when(settingPlayerRegistry.findById(id)).thenReturn(Optional.of(cachedEntry));

    Optional<SettingPlayerEntry> result = settingPlayerService.findById(id);

    assertTrue(result.isPresent());
    assertEquals(id, result.get().getId());
  }

  @Test
  void findByIdFallsBackToRepositoryWhenNotInRegistry() {
    int id = 42;
    SettingPlayerEntry repositoryEntry = new SettingPlayerEntry();
    repositoryEntry.setId(id);
    when(settingPlayerRegistry.findById(id)).thenReturn(Optional.empty());
    when(settingPlayerRepository.findById(id)).thenReturn(Optional.of(repositoryEntry));

    Optional<SettingPlayerEntry> result = settingPlayerService.findById(id);

    assertTrue(result.isPresent());
    assertAll(
        () -> assertEquals(id, result.get().getId()),
        () -> verify(settingPlayerRegistry).put(repositoryEntry)
    );
  }

  @Test
  void findByIdReturnsEmptyWhenNotFoundAnywhere() {
    int id = 99;
    when(settingPlayerRegistry.findById(id)).thenReturn(Optional.empty());
    when(settingPlayerRepository.findById(id)).thenReturn(Optional.empty());

    Optional<SettingPlayerEntry> result = settingPlayerService.findById(id);

    assertFalse(result.isPresent());
  }

  @Test
  void findByPlayerIdAndSettingIdReturnsCachedEntryWhenPresent() {
    int playerId = 1;
    int settingId = 2;
    SettingPlayerEntry cachedEntry = new SettingPlayerEntry();
    cachedEntry.setPlayerFk(playerId);
    cachedEntry.setSettingFk(settingId);
    when(settingPlayerRegistry.findByPlayerIdAndSettingId(playerId, settingId))
        .thenReturn(Optional.of(cachedEntry));

    Optional<SettingPlayerEntry> result = settingPlayerService.findByPlayerIdAndSettingId(playerId, settingId);

    assertTrue(result.isPresent());
    assertAll(
        () -> assertEquals(playerId, result.get().getPlayerFk()),
        () -> assertEquals(settingId, result.get().getSettingFk())
    );
  }

  @Test
  void findByPlayerIdAndSettingIdFallsBackToRepositoryWhenNotInRegistry() {
    int playerId = 1;
    int settingId = 2;
    SettingPlayerEntry repositoryEntry = new SettingPlayerEntry();
    repositoryEntry.setPlayerFk(playerId);
    repositoryEntry.setSettingFk(settingId);
    when(settingPlayerRegistry.findByPlayerIdAndSettingId(playerId, settingId))
        .thenReturn(Optional.empty());
    when(settingPlayerRepository.findAllByPlayerId(playerId))
        .thenReturn(List.of(repositoryEntry));

    Optional<SettingPlayerEntry> result = settingPlayerService.findByPlayerIdAndSettingId(playerId, settingId);

    assertTrue(result.isPresent());
    assertAll(
        () -> assertEquals(settingId, result.get().getSettingFk()),
        () -> verify(settingPlayerRegistry).put(repositoryEntry)
    );
  }

  @Test
  void findByPlayerIdAndSettingIdReturnsEmptyWhenNotFoundAnywhere() {
    int playerId = 1;
    int settingId = 2;
    when(settingPlayerRegistry.findByPlayerIdAndSettingId(playerId, settingId))
        .thenReturn(Optional.empty());
    when(settingPlayerRepository.findAllByPlayerId(playerId)).thenReturn(List.of());

    Optional<SettingPlayerEntry> result = settingPlayerService.findByPlayerIdAndSettingId(playerId, settingId);

    assertFalse(result.isPresent());
  }

  @Test
  void findAllByPlayerIdReturnsCachedEntriesWhenPresent() {
    int playerId = 1;
    SettingPlayerEntry cachedEntry = new SettingPlayerEntry();
    cachedEntry.setPlayerFk(playerId);
    when(settingPlayerRegistry.findAllByPlayerId(playerId)).thenReturn(List.of(cachedEntry));

    List<SettingPlayerEntry> result = settingPlayerService.findAllByPlayerId(playerId);

    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertEquals(playerId, result.getFirst().getPlayerFk())
    );
  }

  @Test
  void findAllByPlayerIdLoadsFromRepositoryWhenCacheIsEmpty() {
    int playerId = 1;
    SettingPlayerEntry repositoryEntry = new SettingPlayerEntry();
    repositoryEntry.setPlayerFk(playerId);
    when(settingPlayerRegistry.findAllByPlayerId(playerId)).thenReturn(List.of());
    when(settingPlayerRepository.findAllByPlayerId(playerId)).thenReturn(List.of(repositoryEntry));

    List<SettingPlayerEntry> result = settingPlayerService.findAllByPlayerId(playerId);

    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertEquals(playerId, result.getFirst().getPlayerFk()),
        () -> verify(settingPlayerRegistry).loadAllForPlayer(playerId, List.of(repositoryEntry))
    );
  }

  @Test
  void insertPersistsEntryInRepositoryAndRegistry() {
    SettingPlayerEntry entry = new SettingPlayerEntry();
    entry.setId(1);
    entry.setPlayerFk(10);
    entry.setSettingFk(20);

    settingPlayerService.insert(entry);

    assertAll(
        () -> verify(settingPlayerRepository).insert(entry),
        () -> verify(settingPlayerRegistry).put(entry)
    );
  }

  @Test
  void updatePersistsEntryInRepositoryAndRegistry() {
    SettingPlayerEntry entry = new SettingPlayerEntry();
    entry.setId(1);
    entry.setPlayerFk(10);
    entry.setSettingFk(20);

    settingPlayerService.update(entry);

    assertAll(
        () -> verify(settingPlayerRepository).update(entry),
        () -> verify(settingPlayerRegistry).put(entry)
    );
  }

  @Test
  void deleteSoftDeletesInRepositoryAndRemovesFromRegistry() {
    int id = 5;
    int deletedBy = 99;

    settingPlayerService.delete(id, deletedBy);

    assertAll(
        () -> verify(settingPlayerRepository).softDelete(id, deletedBy),
        () -> verify(settingPlayerRegistry).remove(id)
    );
  }

  @Test
  void isSettingActiveForPlayerReturnsTrueWhenSettingIsActiveAndValueIsTrue() {
    int playerId = 1;
    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(playerId);

    SettingEntry settingEntry = new SettingEntry();
    settingEntry.setName(PlayerSetting.PROTECTION_NOTIFY_SELF.name());

    SettingPlayerEntry settingPlayerEntry = new SettingPlayerEntry();
    settingPlayerEntry.setPlayerFk(playerId);
    settingPlayerEntry.setSettingEntry(settingEntry);
    settingPlayerEntry.setValue(true);

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(settingPlayerRegistry.findAllByPlayerId(playerId)).thenReturn(List.of(settingPlayerEntry));

    boolean result = settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF);

    assertTrue(result);
  }

  @Test
  void isSettingActiveForPlayerReturnsFalseWhenSettingValueIsFalse() {
    int playerId = 1;
    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(playerId);

    SettingEntry settingEntry = new SettingEntry();
    settingEntry.setName(PlayerSetting.PROTECTION_NOTIFY_SELF.name());

    SettingPlayerEntry settingPlayerEntry = new SettingPlayerEntry();
    settingPlayerEntry.setPlayerFk(playerId);
    settingPlayerEntry.setSettingEntry(settingEntry);
    settingPlayerEntry.setValue(false);

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(settingPlayerRegistry.findAllByPlayerId(playerId)).thenReturn(List.of(settingPlayerEntry));

    boolean result = settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF);

    assertFalse(result);
  }

  @Test
  void isSettingActiveForPlayerReturnsFalseWhenSettingNotFound() {
    int playerId = 1;
    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(playerId);

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(settingPlayerRegistry.findAllByPlayerId(playerId)).thenReturn(List.of());

    boolean result = settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF);

    assertFalse(result);
  }

  @Test
  void findByPlayerIdAndSettingIdReturnsEmptyWhenSettingFkDoesNotMatchSettingId() {
    int playerId = 1;
    int settingId = 2;
    int differentSettingId = 99;
    SettingPlayerEntry repositoryEntry = new SettingPlayerEntry();
    repositoryEntry.setPlayerFk(playerId);
    repositoryEntry.setSettingFk(differentSettingId);
    when(settingPlayerRegistry.findByPlayerIdAndSettingId(playerId, settingId))
        .thenReturn(Optional.empty());
    when(settingPlayerRepository.findAllByPlayerId(playerId))
        .thenReturn(List.of(repositoryEntry));

    Optional<SettingPlayerEntry> result = settingPlayerService.findByPlayerIdAndSettingId(playerId, settingId);

    assertFalse(result.isPresent());
  }
}