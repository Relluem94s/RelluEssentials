package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.BagService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.lang.reflect.Field;
import java.util.Optional;
import org.bukkit.command.ConsoleCommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AutoSaveManagerTest {

  @Mock
  private RelluEssentials plugin;

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private GroupService groupService;

  @Mock
  private BagService bagService;

  @Mock
  private PlayerService playerService;

  @Mock
  private SchedulerService schedulerService;

  @Mock
  private ServerService serverService;

  @Mock
  private TranslationService translationService;

  @Mock
  private ConsoleCommandSender consoleCommandSender;

  @Mock
  private GroupEntry adminGroup;

  private AutoSaveManager autoSaveManager;

  @BeforeEach
  void setUp() {
    autoSaveManager = new AutoSaveManager();

    lenient().when(plugin.getServiceContext()).thenReturn(serviceContext);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getBagService()).thenReturn(bagService);
    lenient().when(serviceContext.getPlayerService()).thenReturn(playerService);
    lenient().when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serverService.getConsoleSender()).thenReturn(consoleCommandSender);
    lenient().when(translationService.get(any(MessageKey.class))).thenReturn("message");
  }

  @Test
  void autoSaveMinutesHasExpectedValue() {
    assertEquals(2L, AutoSaveManager.AUTO_SAVE_MINUTES);
  }

  @Test
  void maxRetriesHasExpectedValue() {
    assertEquals(4, AutoSaveManager.MAX_RETRIES);
  }

  @Test
  void enableRegistersThreeTimersWhenAdminGroupIsPresent() {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.of(adminGroup));

    autoSaveManager.enable(plugin);

    verify(schedulerService, times(3)).runTaskTimer(any(Runnable.class), eq(0L),
        eq(20 * 60 * AutoSaveManager.AUTO_SAVE_MINUTES));
  }

  @Test
  void enableSendsRegisterMessageWhenAdminGroupIsPresent() {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.of(adminGroup));

    autoSaveManager.enable(plugin);

    verify(translationService).get(MessageKey.PLUGIN_MANAGER_REGISTER_AUTOSAVE);
    verify(consoleCommandSender, times(2)).sendMessage(eq(PLUGIN_NAME_CONSOLE), any(String.class));
  }

  @Test
  void enableSendsRegisteredMessageWhenAdminGroupIsPresent() {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.of(adminGroup));

    autoSaveManager.enable(plugin);

    verify(translationService).get(MessageKey.PLUGIN_MANAGER_AUTOSAVE_REGISTERED);
  }

  @Test
  void enableSchedulesRetryWhenAdminGroupIsAbsentAndCountBelowMaxRetries() {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());

    autoSaveManager.enable(plugin);

    verify(schedulerService).runTaskLater(any(Runnable.class), eq(100L));
  }

  @Test
  void enableDoesNotScheduleRetryWhenAdminGroupIsAbsentAndCountExceedsMaxRetries()
      throws Exception {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());

    Field countField = AutoSaveManager.class.getDeclaredField("count");
    countField.setAccessible(true);
    countField.set(autoSaveManager, AutoSaveManager.MAX_RETRIES + 1);

    autoSaveManager.enable(plugin);

    verify(schedulerService, never()).runTaskLater(any(Runnable.class), anyLong());
  }

  @Test
  void enableIncrementsCountOnRetryWhenAdminGroupIsAbsent() throws Exception {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());

    autoSaveManager.enable(plugin);

    Field countField = AutoSaveManager.class.getDeclaredField("count");
    countField.setAccessible(true);
    int count = (int) countField.get(autoSaveManager);

    assertEquals(1, count);
  }

  @Test
  void enableTimerExecutesBagServiceSaveWhenAdminGroupIsPresent() {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.of(adminGroup));

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    autoSaveManager.enable(plugin);

    verify(schedulerService, times(3)).runTaskTimer(runnableCaptor.capture(), eq(0L),
        eq(20 * 60 * AutoSaveManager.AUTO_SAVE_MINUTES));

    runnableCaptor.getAllValues().getFirst().run();

    verify(bagService).savePendingBagUpdates(adminGroup);
  }

  @Test
  void enableTimerExecutesPlayerServiceSavePlayersWhenAdminGroupIsPresent() {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.of(adminGroup));

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    autoSaveManager.enable(plugin);

    verify(schedulerService, times(3)).runTaskTimer(runnableCaptor.capture(), eq(0L),
        eq(20 * 60 * AutoSaveManager.AUTO_SAVE_MINUTES));

    runnableCaptor.getAllValues().get(1).run();

    verify(playerService).savePlayers(adminGroup);
  }

  @Test
  void enableTimerExecutesPlayerServiceSavePlayersInvWhenAdminGroupIsPresent() {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.of(adminGroup));

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    autoSaveManager.enable(plugin);

    verify(schedulerService, times(3)).runTaskTimer(runnableCaptor.capture(), eq(0L),
        eq(20 * 60 * AutoSaveManager.AUTO_SAVE_MINUTES));

    runnableCaptor.getAllValues().get(2).run();

    verify(playerService).savePlayersInv(adminGroup);
  }

  @Test
  void enableTimerExecutesBagServiceSaveWhenAdminGroupIsPresentAtEnableTime() {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.of(adminGroup));

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    autoSaveManager.enable(plugin);

    verify(schedulerService, times(3)).runTaskTimer(runnableCaptor.capture(), eq(0L),
        eq(20 * 60 * AutoSaveManager.AUTO_SAVE_MINUTES));

    runnableCaptor.getAllValues().getFirst().run();

    verify(bagService).savePendingBagUpdates(adminGroup);
  }

  @Test
  void disableSavesAllDataWhenAdminGroupIsPresent() throws Exception {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.of(adminGroup));

    Field contextField = AutoSaveManager.class.getDeclaredField("context");
    contextField.setAccessible(true);
    contextField.set(autoSaveManager, serviceContext);

    autoSaveManager.disable(plugin);

    verify(bagService).savePendingBagUpdates(adminGroup);
    verify(playerService).savePlayers(adminGroup);
    verify(playerService).savePlayersInv(adminGroup);
  }

  @Test
  void disableDoesNothingWhenAdminGroupIsAbsent() throws Exception {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());

    Field contextField = AutoSaveManager.class.getDeclaredField("context");
    contextField.setAccessible(true);
    contextField.set(autoSaveManager, serviceContext);

    autoSaveManager.disable(plugin);

    verify(bagService, never()).savePendingBagUpdates(any());
    verify(playerService, never()).savePlayers(any());
    verify(playerService, never()).savePlayersInv(any());
  }

  @Test
  void initialCountFieldIsZero() throws Exception {
    Field countField = AutoSaveManager.class.getDeclaredField("count");
    countField.setAccessible(true);
    int count = (int) countField.get(autoSaveManager);

    assertEquals(0, count);
  }

  @Test
  void initialContextFieldIsNull() throws Exception {
    Field contextField = AutoSaveManager.class.getDeclaredField("context");
    contextField.setAccessible(true);

    assertFalse(Optional.ofNullable(contextField.get(autoSaveManager)).isPresent());
  }

  @Test
  void enableRetryRunnableCallsEnableAgainWhenAdminGroupIsAbsent() {
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    autoSaveManager.enable(plugin);

    verify(schedulerService).runTaskLater(runnableCaptor.capture(), eq(100L));

    when(groupService.findGroupByName("admin")).thenReturn(Optional.of(adminGroup));
    runnableCaptor.getValue().run();

    verify(schedulerService, times(3)).runTaskTimer(any(Runnable.class), eq(0L),
        eq(20 * 60 * AutoSaveManager.AUTO_SAVE_MINUTES));
  }
}