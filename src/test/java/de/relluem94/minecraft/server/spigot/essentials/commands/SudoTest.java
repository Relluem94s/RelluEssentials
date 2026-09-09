package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.OfflinePlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.CommandService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.WorldGroupService;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SudoTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private GroupService groupService;

  @Mock
  private ServerService serverService;

  @Mock
  private PlayerService playerService;

  @Mock
  private CommandService commandService;

  @Mock
  private WorldGroupService worldGroupService;

  @Mock
  private Command command;

  @Mock
  private Player player;

  private Sudo sudo;

  private static final String TRANSLATED_MESSAGE = "translated-message";
  private static final UUID PLAYER_UUID = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    sudo = new Sudo();
    sudo.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(serviceContext.getPlayerService()).thenReturn(playerService);
    lenient().when(serviceContext.getCommandService()).thenReturn(commandService);
    lenient().when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    lenient().when(player.getUniqueId()).thenReturn(PLAYER_UUID);
  }

  @AfterEach
  void tearDown() {
    SudoManager.sudoers.remove(PLAYER_UUID);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = sudo.getCommands();

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  void onCommandSendsPermissionMissingWhenSenderIsNotAuthorized() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "admin")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = sudo.onCommand(unauthorizedSender, command, "sudo", new String[]{});

    assertTrue(result);
    verify(unauthorizedSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNotPlayer() {
    CommandSender consoleSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(consoleSender, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = sudo.onCommand(consoleSender, command, "sudo", new String[]{});

    assertTrue(result);
    verify(consoleSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTooLessArgumentsWhenNoArgsProvided() {
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = sudo.onCommand(player, command, "sudo", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandDispatchesCommandWhenFirstArgMatchesKnownCommand() {
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(commandService.getAllCommandNames()).thenReturn(List.of("fly"));
    ConsoleCommandSender consoleSender = mock(ConsoleCommandSender.class);
    when(serverService.getConsoleSender()).thenReturn(consoleSender);

    boolean result = sudo.onCommand(player, command, "sudo", new String[]{"fly"});

    assertTrue(result);
    verify(serverService).dispatchCommand(consoleSender, "fly");
  }

  @Test
  void onCommandSendsWrongSubCommandWhenMoreThanOneArgAndNotACommand() {
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(commandService.getAllCommandNames()).thenReturn(List.of());
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = sudo.onCommand(player, command, "sudo", new String[]{"somePlayer", "extraArg"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandExitsSudoModeWhenPlayerIsAlreadyInSudoMode() {
    GroupEntry originalGroup = new GroupEntry(1, "user", "§8");
    GroupEntry sudoGroup = new GroupEntry(2, "admin", "§c");

    PlayerEntry originalEntry = mock(PlayerEntry.class);
    when(originalEntry.getGroup()).thenReturn(originalGroup);
    when(originalEntry.getCustomName()).thenReturn(null);

    PlayerEntry currentEntry = mock(PlayerEntry.class);
    when(currentEntry.getGroup()).thenReturn(sudoGroup);

    SudoManager.sudoers.put(PLAYER_UUID, originalEntry);

    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(commandService.getAllCommandNames()).thenReturn(List.of());
    when(playerService.getPlayerEntry(player)).thenReturn(currentEntry);
    when(translationService.getWithPrefix(MessageKey.COMMAND_SUDO_DEACTIVATED)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = sudo.onCommand(player, command, "sudo", new String[]{"somePlayer"});

    assertTrue(result);
    verify(worldGroupService).saveWorldGroupInventoryForPlayer(player, true);
    verify(currentEntry).setGroup(originalGroup);
    verify(worldGroupService).loadWorldGroupInventoryForPlayer(player);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    assertTrue(SudoManager.sudoers.isEmpty());
  }

  @Test
  void onCommandSendsPlayerNotFoundWhenOfflinePlayerEntryIsNull() {
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(commandService.getAllCommandNames()).thenReturn(List.of());
    when(translationService.getWithPrefix(MessageKey.COMMAND_SUDO_PLAYER_NOT_FOUND, "UnknownPlayer")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = sudo.onCommand(player, command, "sudo", new String[]{"UnknownPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPlayerNotFoundWhenPlayerEntryForTargetIdIsNull() {
    OfflinePlayerEntry offlinePlayerEntry = mock(OfflinePlayerEntry.class);
    UUID targetUuid = UUID.randomUUID();
    when(offlinePlayerEntry.getId()).thenReturn(targetUuid);

    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(commandService.getAllCommandNames()).thenReturn(List.of());
    when(playerService.getPlayerEntry(targetUuid)).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_SUDO_PLAYER_NOT_FOUND, "KnownPlayer")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = sudo.onCommand(player, command, "sudo", new String[]{"KnownPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandActivatesSudoModeWhenTargetPlayerIsFound() {
    UUID targetUuid = UUID.randomUUID();
    GroupEntry targetGroup = new GroupEntry(2, "vip", "§e");

    OfflinePlayerEntry offlinePlayerEntry = mock(OfflinePlayerEntry.class);
    when(offlinePlayerEntry.getId()).thenReturn(targetUuid);
    when(offlinePlayerEntry.getName()).thenReturn("TargetPlayer");

    PlayerEntry targetEntry = mock(PlayerEntry.class);
    when(targetEntry.getGroup()).thenReturn(targetGroup);
    when(targetEntry.getCustomName()).thenReturn(null);

    PlayerEntry senderEntry = mock(PlayerEntry.class);
    when(senderEntry.getGroup()).thenReturn(new GroupEntry(1, "user", "§8"));

    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(commandService.getAllCommandNames()).thenReturn(List.of());
    when(playerService.getPlayerEntry(player)).thenReturn(senderEntry);
    when(playerService.getPlayerEntry(targetUuid)).thenReturn(targetEntry);
    when(translationService.getWithPrefix(MessageKey.COMMAND_SUDO_ACTIVATED, "§eTargetPlayer")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = sudo.onCommand(player, command, "sudo", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(worldGroupService).saveWorldGroupInventoryForPlayer(player, true);
    verify(senderEntry).setGroup(targetGroup);
    verify(worldGroupService).loadWorldGroupInventoryForPlayer(player);
    verify(player).setCustomName("§eTargetPlayer");
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    assertTrue(SudoManager.sudoers.containsKey(PLAYER_UUID));
  }

  @Test
  void onCommandActivatesSudoModeAndSetsCustomNameWhenTargetHasCustomName() {
    UUID targetUuid = UUID.randomUUID();
    GroupEntry targetGroup = new GroupEntry(2, "vip", "§e");

    OfflinePlayerEntry offlinePlayerEntry = mock(OfflinePlayerEntry.class);
    when(offlinePlayerEntry.getId()).thenReturn(targetUuid);
    when(offlinePlayerEntry.getName()).thenReturn("TargetPlayer");

    PlayerEntry targetEntry = mock(PlayerEntry.class);
    when(targetEntry.getGroup()).thenReturn(targetGroup);
    when(targetEntry.getCustomName()).thenReturn("CustomName");

    PlayerEntry senderEntry = mock(PlayerEntry.class);
    when(senderEntry.getGroup()).thenReturn(new GroupEntry(1, "user", "§8"));

    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(commandService.getAllCommandNames()).thenReturn(List.of());
    when(playerService.getPlayerEntry(player)).thenReturn(senderEntry);
    when(playerService.getPlayerEntry(targetUuid)).thenReturn(targetEntry);
    when(translationService.getWithPrefix(MessageKey.COMMAND_SUDO_ACTIVATED, "§eTargetPlayer")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = sudo.onCommand(player, command, "sudo", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(player).setCustomName("§eTargetPlayer");
    verify(player).setCustomName("§eCustomName");
  }

  @Test
  void exitSudoRestoresOriginalPlayerEntryAndRemovesSudoer() {
    GroupEntry originalGroup = new GroupEntry(1, "user", "§8");
    int originalId = 1;
    UUID originalUuid = UUID.randomUUID();

    PlayerEntry originalEntry = mock(PlayerEntry.class);
    when(originalEntry.getId()).thenReturn(originalId);
    when(originalEntry.getGroup()).thenReturn(originalGroup);
    when(originalEntry.getCustomName()).thenReturn(null);

    PlayerEntry currentEntry = mock(PlayerEntry.class);

    SudoManager.sudoers.put(PLAYER_UUID, originalEntry);

    when(playerService.getPlayerEntry(player)).thenReturn(currentEntry);
    when(translationService.getWithPrefix(MessageKey.COMMAND_SUDO_DEACTIVATED)).thenReturn(TRANSLATED_MESSAGE);

    Sudo.exitSudo(player, serviceContext);

    verify(worldGroupService).saveWorldGroupInventoryForPlayer(player, true);
    verify(currentEntry).setId(originalId);
    verify(currentEntry).setUuid(originalUuid.toString());
    verify(currentEntry).setGroup(originalGroup);
    verify(currentEntry).setCustomName(originalEntry.getCustomName());
    verify(currentEntry).setHomes(originalEntry.getHomes());
    verify(currentEntry).setPurse(originalEntry.getPurse());
    verify(player).setCustomName("§8" + player.getName());
    verify(worldGroupService).loadWorldGroupInventoryForPlayer(player);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    assertTrue(SudoManager.sudoers.isEmpty());
  }

  @Test
  void exitSudoSetsCustomNameWithPrefixAndCustomNameWhenOriginalHasCustomName() {
    GroupEntry originalGroup = new GroupEntry(1, "user", "§8");

    PlayerEntry originalEntry = mock(PlayerEntry.class);
    when(originalEntry.getGroup()).thenReturn(originalGroup);
    when(originalEntry.getCustomName()).thenReturn("MyCustomName");

    PlayerEntry currentEntry = mock(PlayerEntry.class);

    SudoManager.sudoers.put(PLAYER_UUID, originalEntry);

    when(playerService.getPlayerEntry(player)).thenReturn(currentEntry);
    when(translationService.getWithPrefix(MessageKey.COMMAND_SUDO_DEACTIVATED)).thenReturn(TRANSLATED_MESSAGE);

    Sudo.exitSudo(player, serviceContext);

    verify(player).setCustomName("§8" + player.getName());
    verify(player).setCustomName("§8MyCustomName");
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotAuthorized() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "admin")).thenReturn(false);

    List<String> result = sudo.onTabComplete(unauthorizedSender, command, "sudo", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    CommandSender consoleSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(consoleSender, "admin")).thenReturn(true);

    List<String> result = sudo.onTabComplete(consoleSender, command, "sudo", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsCommandNamesAndOnlinePlayersForFirstArgument() {
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(commandService.getAllCommandNames()).thenReturn(List.of("fly", "tp"));
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = sudo.onTabComplete(player, command, "sudo", new String[]{"partial"});

    assertNotNull(result);
    assertTrue(result.contains("fly"));
    assertTrue(result.contains("tp"));
    assertTrue(result.contains("OnlinePlayer"));
  }

  @Test
  void onTabCompleteReturnsOnlinePlayerNamesForSecondArgument() {
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = sudo.onTabComplete(player, command, "sudo", new String[]{"firstArg", "secondArg"});

    assertNotNull(result);
    assertTrue(result.contains("OnlinePlayer"));
    verify(commandService, never()).getAllCommandNames();
  }

  @Test
  void onTabCompleteReturnsEmptyListForMoreThanTwoArguments() {
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);

    List<String> result = sudo.onTabComplete(player, command, "sudo", new String[]{"arg1", "arg2", "arg3"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}