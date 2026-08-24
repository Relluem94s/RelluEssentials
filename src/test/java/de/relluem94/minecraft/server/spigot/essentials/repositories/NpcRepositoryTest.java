package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.NpcDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.NpcMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcRepositoryTest {

  @Mock
  private NpcDao npcDao;

  @Mock
  private Npc npc;

  @Mock
  private NpcEntry npcEntry;

  @Mock
  private NpcDialogueEntry npcDialogueEntry;

  private NpcRepository npcRepository;

  private final UUID npcUuid = UUID.randomUUID();
  private final int actorPlayerId = 42;
  private final int npcDbId = 7;

  @BeforeEach
  void setUp() {
    npcRepository = new NpcRepository(npcDao);
  }

  @Test
  void loadAllReturnsMappedNpcs() {
    List<NpcEntry> entries = List.of(npcEntry);
    List<NpcDialogueEntry> dialogues = List.of(npcDialogueEntry);

    when(npcDao.findAll()).thenReturn(entries);
    when(npcEntry.getId()).thenReturn(npcDbId);
    when(npcDao.getNPCDialogues(npcDbId)).thenReturn(dialogues);

    try (MockedStatic<NpcMapper> mapperMock = mockStatic(NpcMapper.class)) {
      mapperMock.when(() -> NpcMapper.toDomain(npcEntry, dialogues)).thenReturn(npc);

      List<Npc> result = npcRepository.loadAll();

      assertAll(
          () -> assertNotNull(result),
          () -> assertEquals(1, result.size()),
          () -> assertEquals(npc, result.get(0))
      );

      mapperMock.verify(() -> NpcMapper.toDomain(npcEntry, dialogues));
    }
  }

  @Test
  void loadAllReturnsEmptyListWhenDaoReturnsEmpty() {
    when(npcDao.findAll()).thenReturn(List.of());

    List<Npc> result = npcRepository.loadAll();

    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.isEmpty())
    );
  }

  @Test
  void loadAllPropagatesDaoException() {
    when(npcDao.findAll()).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> npcRepository.loadAll());
  }

  @Test
  void loadByIdReturnsMappedNpcWhenFound() {
    List<NpcDialogueEntry> dialogues = List.of(npcDialogueEntry);

    when(npcDao.getNPC(npcUuid)).thenReturn(npcEntry);
    when(npcEntry.getId()).thenReturn(npcDbId);
    when(npcDao.getNPCDialogues(npcDbId)).thenReturn(dialogues);

    try (MockedStatic<NpcMapper> mapperMock = mockStatic(NpcMapper.class)) {
      mapperMock.when(() -> NpcMapper.toDomain(npcEntry, dialogues)).thenReturn(npc);

      Optional<Npc> result = npcRepository.loadById(npcUuid);

      assertAll(
          () -> assertTrue(result.isPresent()),
          () -> assertEquals(npc, result.get())
      );

      mapperMock.verify(() -> NpcMapper.toDomain(npcEntry, dialogues));
    }
  }

  @Test
  void loadByIdReturnsEmptyWhenNotFound() {
    when(npcDao.getNPC(npcUuid)).thenReturn(null);

    Optional<Npc> result = npcRepository.loadById(npcUuid);

    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.isEmpty())
    );
  }

  @Test
  void loadByIdPropagatesDaoException() {
    when(npcDao.getNPC(npcUuid)).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> npcRepository.loadById(npcUuid));
  }

  @Test
  void saveInsertsNewNpcWhenNotExisting() {
    when(npc.getId()).thenReturn(npcUuid);
    when(npcDao.getNPC(npcUuid)).thenReturn(null);
    when(npcDao.insertNPC(npcEntry)).thenReturn(npcDbId);

    try (MockedStatic<NpcMapper> mapperMock = mockStatic(NpcMapper.class)) {
      mapperMock.when(() -> NpcMapper.toEntry(npc, actorPlayerId)).thenReturn(npcEntry);

      npcRepository.save(npc, actorPlayerId);

      verify(npcDao).insertNPC(npcEntry);
      verify(npc).setDbid(npcDbId);
      verify(npcDao, never()).updateNPC(any());
    }
  }

  @Test
  void saveUpdatesExistingNpcWhenAlreadyExists() {
    when(npc.getId()).thenReturn(npcUuid);
    when(npcDao.getNPC(npcUuid)).thenReturn(npcEntry);
    when(npcEntry.getId()).thenReturn(npcDbId);

    try (MockedStatic<NpcMapper> mapperMock = mockStatic(NpcMapper.class)) {
      mapperMock.when(() -> NpcMapper.toEntry(npc, actorPlayerId)).thenReturn(npcEntry);

      npcRepository.save(npc, actorPlayerId);

      verify(npcEntry).setId(npcDbId);
      verify(npcDao).updateNPC(npcEntry);
      verify(npc).setDbid(npcDbId);
      verify(npcDao, never()).insertNPC(any());
    }
  }

  @Test
  void savePropagatesDaoException() {
    when(npc.getId()).thenReturn(npcUuid);
    when(npcDao.getNPC(npcUuid)).thenThrow(new RuntimeException("db error"));

    try (MockedStatic<NpcMapper> mapperMock = mockStatic(NpcMapper.class)) {
      mapperMock.when(() -> NpcMapper.toEntry(npc, actorPlayerId)).thenReturn(npcEntry);

      assertThrows(RuntimeException.class, () -> npcRepository.save(npc, actorPlayerId));
    }
  }

  @Test
  void deleteRemovesDialoguesAndNpc() {
    int deletedByPlayerId = 99;

    npcRepository.delete(npcUuid, deletedByPlayerId);

    verify(npcDao).deleteNPCDialogueByNpcId(npcUuid, deletedByPlayerId);
    verify(npcDao).deleteNPC(npcUuid, deletedByPlayerId);
  }

  @Test
  void deletePropagatesDaoException() {
    int deletedByPlayerId = 99;
    doThrow(new RuntimeException("db error")).when(npcDao).deleteNPCDialogueByNpcId(npcUuid, deletedByPlayerId);

    assertThrows(RuntimeException.class, () -> npcRepository.delete(npcUuid, deletedByPlayerId));
  }

  @Test
  void loadDialoguesByNpcDbIdReturnsDialogues() {
    List<NpcDialogueEntry> dialogues = List.of(npcDialogueEntry);
    when(npcDao.findDialoguesByNpcId(npcDbId)).thenReturn(dialogues);

    List<NpcDialogueEntry> result = npcRepository.loadDialoguesByNpcDbId(npcDbId);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(1, result.size()),
        () -> assertEquals(npcDialogueEntry, result.get(0))
    );
  }

  @Test
  void loadDialoguesByNpcDbIdPropagatesDaoException() {
    when(npcDao.findDialoguesByNpcId(npcDbId)).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> npcRepository.loadDialoguesByNpcDbId(npcDbId));
  }

  @Test
  void addDialogueDelegatesToDao() {
    npcRepository.addDialogue(npcDialogueEntry);

    verify(npcDao).insertNPCDialogue(npcDialogueEntry);
  }

  @Test
  void addDialoguePropagatesDaoException() {
    doThrow(new RuntimeException("db error")).when(npcDao).insertNPCDialogue(npcDialogueEntry);

    assertThrows(RuntimeException.class, () -> npcRepository.addDialogue(npcDialogueEntry));
  }

  @Test
  void updateDialogueReturnsTrueOnSuccess() {
    UUID dialogueUuid = UUID.randomUUID();
    when(npcDao.updateNPCDialogue(npcDialogueEntry, dialogueUuid)).thenReturn(true);

    boolean result = npcRepository.updateDialogue(npcDialogueEntry, dialogueUuid);

    assertTrue(result);
    verify(npcDao).updateNPCDialogue(npcDialogueEntry, dialogueUuid);
  }

  @Test
  void updateDialogueReturnsFalseWhenNotUpdated() {
    UUID dialogueUuid = UUID.randomUUID();
    when(npcDao.updateNPCDialogue(npcDialogueEntry, dialogueUuid)).thenReturn(false);

    boolean result = npcRepository.updateDialogue(npcDialogueEntry, dialogueUuid);

    assertFalse(result);
  }

  @Test
  void updateDialoguePropagatesDaoException() {
    UUID dialogueUuid = UUID.randomUUID();
    when(npcDao.updateNPCDialogue(npcDialogueEntry, dialogueUuid)).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> npcRepository.updateDialogue(npcDialogueEntry, dialogueUuid));
  }

  @Test
  void deleteDialogueByPositionDelegatesToDao() {
    int listPosition = 3;
    int deletedByPlayerId = 99;

    npcRepository.deleteDialogueByPosition(npcUuid, listPosition, deletedByPlayerId);

    verify(npcDao).deleteNPCDialogueById(npcUuid, listPosition, deletedByPlayerId);
  }

  @Test
  void deleteDialogueByPositionPropagatesDaoException() {
    int listPosition = 3;
    int deletedByPlayerId = 99;
    doThrow(new RuntimeException("db error")).when(npcDao).deleteNPCDialogueById(npcUuid, listPosition, deletedByPlayerId);

    assertThrows(RuntimeException.class, () -> npcRepository.deleteDialogueByPosition(npcUuid, listPosition, deletedByPlayerId));
  }

  @Test
  void deleteAllDialoguesByNpcUuidDelegatesToDao() {
    int deletedByPlayerId = 99;

    npcRepository.deleteAllDialoguesByNpcUuid(npcUuid, deletedByPlayerId);

    verify(npcDao).deleteNPCDialogueByNpcId(npcUuid, deletedByPlayerId);
  }

  @Test
  void deleteAllDialoguesByNpcUuidPropagatesDaoException() {
    int deletedByPlayerId = 99;
    doThrow(new RuntimeException("db error")).when(npcDao).deleteNPCDialogueByNpcId(npcUuid, deletedByPlayerId);

    assertThrows(RuntimeException.class, () -> npcRepository.deleteAllDialoguesByNpcUuid(npcUuid, deletedByPlayerId));
  }
}