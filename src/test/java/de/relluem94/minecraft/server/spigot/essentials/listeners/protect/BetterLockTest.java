package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerSetting;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.SettingPlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BetterLockTest {

  @Mock
  private ServiceContext serviceContext;
  @Mock
  private ProtectionService protectionService;
  @Mock
  private PlayerService playerService;
  @Mock
  private TranslationService translationService;
  @Mock
  private GroupService groupService;
  @Mock
  private SchedulerService schedulerService;
  @Mock
  private SettingPlayerService settingPlayerService;
  @Mock
  private PlayerInteractEvent event;
  @Mock
  private Block clickedBlock;
  @Mock
  private Player player;
  @Mock
  private ProtectionEntry protectionEntry;
  @Mock
  private PlayerEntry playerEntry;
  @Mock
  private Location location;

  private BetterLock betterLock;

  @BeforeEach
  void setUp() {
    betterLock = new BetterLock();
    betterLock.injectContext(serviceContext);

    lenient().when(serviceContext.getProtectionService()).thenReturn(protectionService);
    lenient().when(serviceContext.getPlayerService()).thenReturn(playerService);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    lenient().when(serviceContext.getSettingPlayerService()).thenReturn(settingPlayerService);
    lenient().doAnswer(invocation -> {
      Runnable task = invocation.getArgument(0);
      task.run();
      return null;
    }).when(schedulerService).runTaskLater(any(), anyLong());
  }

  @Test
  void onInteractNullClickedBlockDoesNothing() {
    when(event.getClickedBlock()).thenReturn(null);

    betterLock.onInteract(event);

    verifyNoInteractions(protectionService);
  }

  @Test
  void onInteractNullProtectionEntryDoesNothing() {
    when(event.getClickedBlock()).thenReturn(clickedBlock);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(null);

      betterLock.onInteract(event);

      verifyNoInteractions(playerService);
    }
  }

  @Test
  void onInteractNullPlayerEntryDoesNothing() {
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(null);

      betterLock.onInteract(event);

      verify(event, never()).setCancelled(anyBoolean());
    }
  }

  @Test
  void onInteractNonOpenableNonProtectableMaterialDoesNothing() {
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(false);
      when(protectionService.isProtectableMaterial(any())).thenReturn(false);

      betterLock.onInteract(event);

      verify(event, never()).setCancelled(anyBoolean());
    }
  }

  @Test
  void onInteractNonOpenableProtectableMaterialWithDefaultStateAndRightsSendsAllowMessage() {
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    String allowMessage = "allow";
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW)).thenReturn(allowMessage);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(false);
      when(protectionService.isProtectableMaterial(any())).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.isOwner(protectionEntry, 1)).thenReturn(false);

      betterLock.onInteract(event);

      verify(player).sendMessage(allowMessage);
    }
  }

  @Test
  void onInteractNonOpenableProtectableMaterialWithDefaultStateAndNoRightsModeratorAllowsAccess() {
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    String modMessage = "mod overwrite";
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW_ADMIN_OVERWRITE)).thenReturn(modMessage);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(false);
      when(protectionService.isProtectableMaterial(any())).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(false);

      betterLock.onInteract(event);

      verify(event).setCancelled(false);
      verify(player).sendMessage(modMessage);
    }
  }

  @Test
  void onInteractNonOpenableProtectableMaterialWithDefaultStateAndNoRightsAndAllowPublicFlagSendsAllowMessage() {
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    String allowMessage = "allow";
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW)).thenReturn(allowMessage);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(false);
      when(protectionService.isProtectableMaterial(any())).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(false);
      protectionHelperMock.when(() -> ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.ALLOW_PUBLIC)).thenReturn(true);

      betterLock.onInteract(event);

      verify(player).sendMessage(allowMessage);
      verify(event, never()).setCancelled(true);
    }
  }

  @Test
  void onInteractNonOpenableProtectableMaterialWithDefaultStateAndNoRightsAndNoPublicFlagCancelsEvent() {
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    String disallowMessage = "disallow";
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW)).thenReturn(disallowMessage);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(false);
      when(protectionService.isProtectableMaterial(any())).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(false);
      protectionHelperMock.when(() -> ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.ALLOW_PUBLIC)).thenReturn(false);

      betterLock.onInteract(event);

      verify(event).setCancelled(true);
      verify(player).sendMessage(disallowMessage);
    }
  }

  @ParameterizedTest
  @EnumSource(value = PlayerState.class, names = {
      "PROTECTION_INFO", "PROTECTION_ADD", "PROTECTION_REMOVE",
      "PROTECTION_FLAG_ADD", "PROTECTION_FLAG_REMOVE",
      "PROTECTION_RIGHT_ADD", "PROTECTION_RIGHT_REMOVE"
  })
  void onInteractOpenableWithProtectionPlayerStateSkipsAccessCheck(PlayerState protectionState) {
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(protectionState);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);

      betterLock.onInteract(event);

      verify(event, never()).setCancelled(anyBoolean());
      verify(player, never()).sendMessage(anyString());
    }
  }

  @Test
  void onInteractOpenableWithNoRightsAndModeratorAllowsAccess() {
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    String modMessage = "mod overwrite";
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW_ADMIN_OVERWRITE)).thenReturn(modMessage);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(false);

      betterLock.onInteract(event);

      verify(event).setCancelled(false);
      verify(player).sendMessage(modMessage);
    }
  }

  @Test
  void onInteractOpenableWithNoRightsAndAllowPublicFlagSendsAllowMessage() {
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    String allowMessage = "allow";
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW)).thenReturn(allowMessage);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(false);
      protectionHelperMock.when(() -> ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.ALLOW_PUBLIC)).thenReturn(true);

      betterLock.onInteract(event);

      verify(player).sendMessage(allowMessage);
    }
  }

  @Test
  void onInteractOpenableWithNoRightsAndNoPublicFlagCancelsEvent() {
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    String disallowMessage = "disallow";
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW)).thenReturn(disallowMessage);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(false);
      protectionHelperMock.when(() -> ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.ALLOW_PUBLIC)).thenReturn(false);

      betterLock.onInteract(event);

      verify(event).setCancelled(true);
      verify(player).sendMessage(disallowMessage);
    }
  }

  @Test
  void onInteractOpenableDoorWithRightsAsOwnerWithNotifySelfDisabledSendsAllowMessage() {
    Door door = mock(Door.class);
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(clickedBlock.getBlockData()).thenReturn(door);
    when(settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF)).thenReturn(false);
    String allowMessage = "allow";
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ALLOW)).thenReturn(allowMessage);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.isOwner(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.getOtherPart(door, clickedBlock)).thenReturn(null);
      protectionHelperMock.when(() -> ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.AUTO_CLOSE)).thenReturn(false);

      betterLock.onInteract(event);

      verify(player).sendMessage(allowMessage);
    }
  }

  @Test
  void onInteractOpenableDoorWithRightsAsOwnerWithNotifySelfEnabledDoesNotSendAllowMessage() {
    Door door = mock(Door.class);
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(clickedBlock.getBlockData()).thenReturn(door);
    when(settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF)).thenReturn(true);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.isOwner(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.getOtherPart(door, clickedBlock)).thenReturn(null);
      protectionHelperMock.when(() -> ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.AUTO_CLOSE)).thenReturn(false);

      betterLock.onInteract(event);

      verify(player, never()).sendMessage(anyString());
    }
  }

  @Test
  void onInteractOpenableDoorWithRightsAndAutoCloseFlagSchedulesAutoClose() {
    Door door = mock(Door.class);
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(clickedBlock.getBlockData()).thenReturn(door);
    when(settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF)).thenReturn(true);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.isOwner(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.getOtherPart(door, clickedBlock)).thenReturn(null);
      protectionHelperMock.when(() -> ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.AUTO_CLOSE)).thenReturn(true);

      betterLock.onInteract(event);

      verify(schedulerService).runTaskLater(any(), eq(50L));
      verify(door).setOpen(false);
      verify(clickedBlock).setBlockData(door);
      verify(player).sendMessage(
          translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_AUTOCLOSE));
    }
  }

  @Test
  void onInteractOpenableTrapDoorWithRightsAndAutoCloseFlagSchedulesAutoClose() {
    TrapDoor trapDoor = mock(TrapDoor.class);
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(clickedBlock.getBlockData()).thenReturn(trapDoor);
    when(settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF)).thenReturn(true);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.isOwner(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.AUTO_CLOSE)).thenReturn(true);

      betterLock.onInteract(event);

      verify(schedulerService).runTaskLater(any(), eq(50L));
    }
  }

  @Test
  void onInteractOpenableGateWithRightsAndAutoCloseFlagSchedulesAutoClose() {
    Gate gate = mock(Gate.class);
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(clickedBlock.getBlockData()).thenReturn(gate);
    when(settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF)).thenReturn(true);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.isOwner(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.AUTO_CLOSE)).thenReturn(true);

      betterLock.onInteract(event);

      verify(schedulerService).runTaskLater(any(), eq(50L));
    }
  }

  @Test
  void onInteractOpenableTrapDoorWithRightsAndNoAutoCloseFlagDoesNotSchedule() {
    TrapDoor trapDoor = mock(TrapDoor.class);
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(clickedBlock.getBlockData()).thenReturn(trapDoor);
    when(settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF)).thenReturn(true);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.isOwner(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.AUTO_CLOSE)).thenReturn(false);

      betterLock.onInteract(event);

      verifyNoInteractions(schedulerService);
    }
  }

  @Test
  void onInteractOpenableGateWithRightsAndNoAutoCloseFlagDoesNotSchedule() {
    Gate gate = mock(Gate.class);
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(clickedBlock.getBlockData()).thenReturn(gate);
    when(settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF)).thenReturn(true);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.isOwner(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.AUTO_CLOSE)).thenReturn(false);

      betterLock.onInteract(event);

      verifyNoInteractions(schedulerService);
    }
  }

  @Test
  void onInteractOpenableDoorWithRightsAndDoubleDoorWithDifferentHingeOpensAndSchedulesAutoClose() {
    Door door = mock(Door.class);
    Block secondDoorBlock = mock(Block.class);
    Door door2 = mock(Door.class);

    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(clickedBlock.getBlockData()).thenReturn(door);
    when(secondDoorBlock.getBlockData()).thenReturn(door2);
    when(door.getHinge()).thenReturn(Door.Hinge.LEFT);
    when(door2.getHinge()).thenReturn(Door.Hinge.RIGHT);
    when(door2.isOpen()).thenReturn(false);
    when(settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF)).thenReturn(true);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.isOwner(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.getOtherPart(door, clickedBlock)).thenReturn(secondDoorBlock);
      protectionHelperMock.when(() -> ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.AUTO_CLOSE)).thenReturn(true);

      betterLock.onInteract(event);

      verify(door2).setOpen(true);
      verify(schedulerService).runTaskLater(any(), eq(50L));
      verify(door).setOpen(false);
      verify(door2).setOpen(false);
      verify(clickedBlock).setBlockData(door);
      verify(secondDoorBlock, times(2)).setBlockData(door2);
      verify(player).sendMessage(
          translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_AUTOCLOSE));
    }
  }

  @Test
  void onInteractOpenableDoorWithRightsAndDoubleDoorAlreadyOpenClosesSecondDoor() {
    Door door = mock(Door.class);
    Block secondDoorBlock = mock(Block.class);
    Door door2 = mock(Door.class);

    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(clickedBlock.getBlockData()).thenReturn(door);
    when(secondDoorBlock.getBlockData()).thenReturn(door2);
    when(door.getHinge()).thenReturn(Door.Hinge.LEFT);
    when(door2.getHinge()).thenReturn(Door.Hinge.RIGHT);
    when(door2.isOpen()).thenReturn(true);
    when(settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF)).thenReturn(true);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.isOwner(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.getOtherPart(door, clickedBlock)).thenReturn(secondDoorBlock);

      betterLock.onInteract(event);

      verify(door2).setOpen(false);
      verifyNoInteractions(schedulerService);
    }
  }

  @Test
  void onInteractOpenableDoorWithRightsAndDoubleDoorWithSameHingeDoesNothing() {
    Door door = mock(Door.class);
    Block secondDoorBlock = mock(Block.class);
    Door door2 = mock(Door.class);

    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(clickedBlock.getBlockData()).thenReturn(door);
    when(secondDoorBlock.getBlockData()).thenReturn(door2);
    when(door.getHinge()).thenReturn(Door.Hinge.LEFT);
    when(door2.getHinge()).thenReturn(Door.Hinge.LEFT);
    when(settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF)).thenReturn(true);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.isOwner(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.getOtherPart(door, clickedBlock)).thenReturn(secondDoorBlock);

      betterLock.onInteract(event);

      verify(door2, never()).setOpen(anyBoolean());
      verifyNoInteractions(schedulerService);
    }
  }

  @Test
  void onInteractOpenableDoorWithRightsAndSecondBlockNotDoorDoesNothing() {
    Door door = mock(Door.class);
    Block secondBlock = mock(Block.class);
    BlockData nonDoorData = mock(BlockData.class);

    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(clickedBlock.getBlockData()).thenReturn(door);
    when(secondBlock.getBlockData()).thenReturn(nonDoorData);
    when(settingPlayerService.isSettingActiveForPlayer(player, PlayerSetting.PROTECTION_NOTIFY_SELF)).thenReturn(true);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.hasRights(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.isOwner(protectionEntry, 1)).thenReturn(true);
      protectionHelperMock.when(() -> ProtectionHelper.getOtherPart(door, clickedBlock)).thenReturn(secondBlock);

      betterLock.onInteract(event);

      verifyNoInteractions(schedulerService);
    }
  }

  @Test
  void onInteractNonOpenableProtectableMaterialWithNonDefaultStateDoesNothing() {
    when(event.getClickedBlock()).thenReturn(clickedBlock);
    when(event.getPlayer()).thenReturn(player);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.PROTECTION_ADD);

    try (MockedStatic<ProtectionHelper> protectionHelperMock = mockStatic(ProtectionHelper.class)) {
      protectionHelperMock.when(() -> ProtectionHelper.getLocationFromBlockAlternateForDoor(clickedBlock)).thenReturn(location);
      when(protectionService.getProtectionEntry(location)).thenReturn(protectionEntry);
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
      protectionHelperMock.when(() -> ProtectionHelper.isOpenAble(clickedBlock)).thenReturn(false);
      when(protectionService.isProtectableMaterial(any())).thenReturn(true);

      betterLock.onInteract(event);

      verify(event, never()).setCancelled(anyBoolean());
      verify(player, never()).sendMessage(anyString());
    }
  }
}