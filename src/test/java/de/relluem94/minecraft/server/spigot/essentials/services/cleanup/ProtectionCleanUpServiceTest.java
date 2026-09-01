package de.relluem94.minecraft.server.spigot.essentials.services.cleanup;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.HashMap;
import java.util.function.Consumer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProtectionCleanUpServiceTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private ProtectionService protectionService;

  @Mock
  private SchedulerService schedulerService;

  @Mock
  private TranslationService translationService;

  @Mock
  private Player player;

  @Mock
  private World world;

  @Mock
  private Block block;

  @Mock
  private BukkitTask bukkitTask;

  private ProtectionCleanUpService protectionCleanUpService;

  @BeforeEach
  void setUp() {
    lenient().when(serviceContext.getProtectionService()).thenReturn(protectionService);
    lenient().when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().doReturn("message").when(translationService).getWithPrefix(any(MessageKey.class));
    lenient().doReturn("message").when(translationService).getWithPrefix(any(MessageKey.class), any(Object[].class));

    protectionCleanUpService = new ProtectionCleanUpService(serviceContext);
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void cleanUpProtectionsWithNullPlayerThrowsNullPointerException() {
    assertThrows(NullPointerException.class,
        () -> protectionCleanUpService.cleanUpProtections(null));
  }

  @Test
  void cleanUpProtectionsWithEmptyProtectionEntriesCancelsTaskWithNoneRemovedMessage() {
    when(protectionService.getAllProtectionEntries()).thenReturn(new HashMap<>());
    when(protectionService.removeOutdatedProtectionsFromDatabaseAndRegistry()).thenReturn(0);

    @SuppressWarnings("unchecked")
    ArgumentCaptor<Consumer<BukkitTask>> taskCaptor = ArgumentCaptor.forClass(Consumer.class);

    doAnswer(invocation -> {
      Consumer<BukkitTask> taskConsumer = invocation.getArgument(0);
      taskConsumer.accept(bukkitTask);
      return null;
    }).when(schedulerService).runTaskTimer(taskCaptor.capture(), eq(0L), eq(300L));

    protectionCleanUpService.cleanUpProtections(player);

    assertAll(
        () -> verify(schedulerService).runTaskTimer(ArgumentMatchers.<Consumer<BukkitTask>>any(),
            eq(0L), eq(300L)), () -> verify(bukkitTask).cancel(),
        () -> verify(translationService).getWithPrefix(
            MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_NONE),
        () -> verify(translationService).getWithPrefix(
            eq(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_START), eq(0)));
  }

  @Test
  void cleanUpProtectionsWithLocationHavingNullWorldSkipsBlockCheck() {
    Location locationWithNullWorld = mock(Location.class);
    when(locationWithNullWorld.getWorld()).thenReturn(null);

    ProtectionEntry protectionEntry = mock(ProtectionEntry.class);

    HashMap<Location, ProtectionEntry> entries = new HashMap<>();
    entries.put(locationWithNullWorld, protectionEntry);

    when(protectionService.getAllProtectionEntries()).thenReturn(entries);
    when(protectionService.removeOutdatedProtectionsFromDatabaseAndRegistry()).thenReturn(0);

    doAnswer(invocation -> {
      Consumer<BukkitTask> taskConsumer = invocation.getArgument(0);
      taskConsumer.accept(bukkitTask);
      return null;
    }).when(schedulerService)
        .runTaskTimer(ArgumentMatchers.<Consumer<BukkitTask>>any(), eq(0L), eq(300L));

    protectionCleanUpService.cleanUpProtections(player);

    assertAll(() -> verify(locationWithNullWorld, never()).getBlock(),
        () -> verify(protectionService, never()).deleteProtectionAndRemoveFromRegistry(any()),
        () -> verify(bukkitTask).cancel());
  }

  @Test
  void cleanUpProtectionsWithMatchingMaterialDoesNotRemoveProtectionEntry() {
    org.bukkit.Chunk chunk = mock(org.bukkit.Chunk.class);
    Location location = mock(Location.class);
    when(location.getWorld()).thenReturn(world);
    when(location.getChunk()).thenReturn(chunk);
    when(chunk.isLoaded()).thenReturn(true);
    when(location.getBlock()).thenReturn(block);
    when(block.getType()).thenReturn(Material.CHEST);

    ProtectionEntry protectionEntry = mock(ProtectionEntry.class);
    when(protectionEntry.getMaterialName()).thenReturn("CHEST");

    HashMap<Location, ProtectionEntry> entries = new HashMap<>();
    entries.put(location, protectionEntry);

    when(protectionService.getAllProtectionEntries()).thenReturn(entries);
    when(protectionService.removeOutdatedProtectionsFromDatabaseAndRegistry()).thenReturn(0);

    doAnswer(invocation -> {
      Consumer<BukkitTask> taskConsumer = invocation.getArgument(0);
      taskConsumer.accept(bukkitTask);
      return null;
    }).when(schedulerService)
        .runTaskTimer(ArgumentMatchers.<Consumer<BukkitTask>>any(), eq(0L), eq(300L));

    protectionCleanUpService.cleanUpProtections(player);

    assertAll(() -> verify(protectionService, never()).deleteProtectionAndRemoveFromRegistry(any()),
        () -> verify(translationService).getWithPrefix(
            MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_NONE), () -> verify(bukkitTask).cancel());
  }

  @Test
  void cleanUpProtectionsWithMismatchedMaterialRemovesProtectionEntry() {
    org.bukkit.Chunk chunk = mock(org.bukkit.Chunk.class);
    Location location = mock(Location.class);
    when(location.getWorld()).thenReturn(world);
    when(location.getChunk()).thenReturn(chunk);
    when(chunk.isLoaded()).thenReturn(true);
    when(location.getBlock()).thenReturn(block);
    when(block.getType()).thenReturn(Material.STONE);

    ProtectionEntry protectionEntry = mock(ProtectionEntry.class);
    when(protectionEntry.getMaterialName()).thenReturn("CHEST");
    when(protectionEntry.getId()).thenReturn(1);

    HashMap<Location, ProtectionEntry> entries = new HashMap<>();
    entries.put(location, protectionEntry);

    when(protectionService.getAllProtectionEntries()).thenReturn(entries);
    when(protectionService.removeOutdatedProtectionsFromDatabaseAndRegistry()).thenReturn(0);

    doAnswer(invocation -> {
      Consumer<BukkitTask> taskConsumer = invocation.getArgument(0);
      taskConsumer.accept(bukkitTask);
      return null;
    }).when(schedulerService)
        .runTaskTimer(ArgumentMatchers.<Consumer<BukkitTask>>any(), eq(0L), eq(300L));

    protectionCleanUpService.cleanUpProtections(player);

    assertAll(
        () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(protectionEntry),
        () -> verify(translationService).getWithPrefix(
            eq(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS), eq(1), eq("CHEST"), eq("STONE")),
        () -> verify(translationService).getWithPrefix(
            eq(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_CLEANING_UP), eq(1)),
        () -> verify(protectionService).removeProtectionEntry(location),
        () -> verify(bukkitTask).cancel());
  }

  @Test
  void cleanUpProtectionsWithUnloadedChunkLoadsChunkBeforeCheckingBlock() {
    org.bukkit.Chunk chunk = mock(org.bukkit.Chunk.class);
    Location location = mock(Location.class);
    when(location.getWorld()).thenReturn(world);
    when(location.getChunk()).thenReturn(chunk);
    when(chunk.isLoaded()).thenReturn(false);
    when(location.getBlock()).thenReturn(block);
    when(block.getType()).thenReturn(Material.CHEST);

    ProtectionEntry protectionEntry = mock(ProtectionEntry.class);
    when(protectionEntry.getMaterialName()).thenReturn("CHEST");

    HashMap<Location, ProtectionEntry> entries = new HashMap<>();
    entries.put(location, protectionEntry);

    when(protectionService.getAllProtectionEntries()).thenReturn(entries);
    when(protectionService.removeOutdatedProtectionsFromDatabaseAndRegistry()).thenReturn(0);

    doAnswer(invocation -> {
      Consumer<BukkitTask> taskConsumer = invocation.getArgument(0);
      taskConsumer.accept(bukkitTask);
      return null;
    }).when(schedulerService)
        .runTaskTimer(ArgumentMatchers.<Consumer<BukkitTask>>any(), eq(0L), eq(300L));

    protectionCleanUpService.cleanUpProtections(player);

    assertAll(() -> verify(chunk).load(), () -> verify(location).getBlock());
  }

  @Test
  void cleanUpProtectionsWithOutdatedProtectionsRemovedSendsOutdatedRemovedMessage() {
    when(protectionService.getAllProtectionEntries()).thenReturn(new HashMap<>());
    when(protectionService.removeOutdatedProtectionsFromDatabaseAndRegistry()).thenReturn(3);

    doAnswer(invocation -> {
      Consumer<BukkitTask> taskConsumer = invocation.getArgument(0);
      taskConsumer.accept(bukkitTask);
      return null;
    }).when(schedulerService)
        .runTaskTimer(ArgumentMatchers.<Consumer<BukkitTask>>any(), eq(0L), eq(300L));

    protectionCleanUpService.cleanUpProtections(player);

    assertAll(() -> verify(translationService).getWithPrefix(
            eq(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_OUTDATED_REMOVED), eq(3)),
        () -> verify(player, atLeastOnce()).sendMessage(anyString()));
  }

  @Test
  void cleanUpProtectionsWithZeroOutdatedProtectionsDoesNotSendOutdatedRemovedMessage() {
    when(protectionService.getAllProtectionEntries()).thenReturn(new HashMap<>());
    when(protectionService.removeOutdatedProtectionsFromDatabaseAndRegistry()).thenReturn(0);

    doAnswer(invocation -> {
      Consumer<BukkitTask> taskConsumer = invocation.getArgument(0);
      taskConsumer.accept(bukkitTask);
      return null;
    }).when(schedulerService)
        .runTaskTimer(ArgumentMatchers.<Consumer<BukkitTask>>any(), eq(0L), eq(300L));

    protectionCleanUpService.cleanUpProtections(player);

    verify(translationService, never()).getWithPrefix(
        eq(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_OUTDATED_REMOVED), anyInt());
  }

  @Test
  void cleanUpProtectionsWithMismatchedMaterialSendsEndMessages() {
    org.bukkit.Chunk chunk = mock(org.bukkit.Chunk.class);
    Location location = mock(Location.class);
    when(location.getWorld()).thenReturn(world);
    when(location.getChunk()).thenReturn(chunk);
    when(chunk.isLoaded()).thenReturn(true);
    when(location.getBlock()).thenReturn(block);
    when(block.getType()).thenReturn(Material.STONE);

    ProtectionEntry protectionEntry = mock(ProtectionEntry.class);
    when(protectionEntry.getMaterialName()).thenReturn("CHEST");
    when(protectionEntry.getId()).thenReturn(42);

    HashMap<Location, ProtectionEntry> entries = new HashMap<>();
    entries.put(location, protectionEntry);

    HashMap<Location, ProtectionEntry> remainingEntries = new HashMap<>();
    when(protectionService.getAllProtectionEntries()).thenReturn(entries)
        .thenReturn(remainingEntries);
    when(protectionService.removeOutdatedProtectionsFromDatabaseAndRegistry()).thenReturn(0);

    doAnswer(invocation -> {
      Consumer<BukkitTask> taskConsumer = invocation.getArgument(0);
      taskConsumer.accept(bukkitTask);
      return null;
    }).when(schedulerService)
        .runTaskTimer(ArgumentMatchers.<Consumer<BukkitTask>>any(), eq(0L), eq(300L));

    protectionCleanUpService.cleanUpProtections(player);

    assertAll(() -> verify(translationService).getWithPrefix(
            eq(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_END), eq(0)),
        () -> verify(translationService).getWithPrefix(
            eq(MessageKey.COMMAND_ADMIN_CLEAN_OLD_PROTECTIONS_END), eq(1)));
  }

  @Test
  void cleanUpProtectionsSendsPercentageProgressMessage() {
    when(protectionService.getAllProtectionEntries()).thenReturn(new HashMap<>());
    when(protectionService.removeOutdatedProtectionsFromDatabaseAndRegistry()).thenReturn(0);

    doAnswer(invocation -> {
      Consumer<BukkitTask> taskConsumer = invocation.getArgument(0);
      taskConsumer.accept(bukkitTask);
      return null;
    }).when(schedulerService)
        .runTaskTimer(ArgumentMatchers.<Consumer<BukkitTask>>any(), eq(0L), eq(300L));

    protectionCleanUpService.cleanUpProtections(player);

    verify(translationService).getWithPrefix(
        eq(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_PERCENTAGE), eq(0), eq(0), eq(100));
  }

  @Test
  void cleanUpProtectionsWithMoreLocationsThanBatchSizeSendsProgressMessageBeforeCompletion() {
    Chunk chunk = mock(Chunk.class);

    HashMap<Location, ProtectionEntry> entries = new HashMap<>();
    for (int i = 0; i < 6; i++) {
      Location location = mock(Location.class);
      Block locationBlock = mock(Block.class);
      when(location.getWorld()).thenReturn(world);
      when(location.getChunk()).thenReturn(chunk);
      when(chunk.isLoaded()).thenReturn(true);
      when(location.getBlock()).thenReturn(locationBlock);
      when(locationBlock.getType()).thenReturn(Material.CHEST);

      ProtectionEntry protectionEntry = mock(ProtectionEntry.class);
      when(protectionEntry.getMaterialName()).thenReturn("CHEST");

      entries.put(location, protectionEntry);
    }

    when(protectionService.getAllProtectionEntries()).thenReturn(entries);
    when(protectionService.removeOutdatedProtectionsFromDatabaseAndRegistry()).thenReturn(0);

    doAnswer(invocation -> {
      Consumer<BukkitTask> taskConsumer = invocation.getArgument(0);
      taskConsumer.accept(bukkitTask);
      taskConsumer.accept(bukkitTask);
      return null;
    }).when(schedulerService)
        .runTaskTimer(ArgumentMatchers.<Consumer<BukkitTask>>any(), eq(0L), eq(300L));

    protectionCleanUpService.cleanUpProtections(player);

    assertAll(
        () -> verify(player, atLeastOnce()).sendMessage(anyString()),
        () -> verify(bukkitTask).cancel()
    );
  }
}