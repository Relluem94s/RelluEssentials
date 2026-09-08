package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionActionService;
import de.relluem94.minecraft.server.spigot.essentials.services.ProtectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarryTest {

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
  private SchedulerService schedulerService;

  @Mock
  private ProtectionService protectionService;

  @Mock
  private ProtectionActionService protectionActionService;

  @Mock
  private Command command;

  @Mock
  private Player player;

  private Marry marry;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    marry = new Marry();
    marry.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(serviceContext.getPlayerService()).thenReturn(playerService);
    lenient().when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    lenient().when(serviceContext.getProtectionService()).thenReturn(protectionService);
    lenient().when(serviceContext.getProtectionActionService()).thenReturn(protectionActionService);
  }

  @Test
  void getCommandsReturnsAllEnumValues() {
    CommandsEnum[] result = marry.getCommands();

    assertAll(
        () -> assertEquals(2, result.length),
        () -> assertEquals("accept", result[0].getName()),
        () -> assertEquals("divorce", result[1].getName())
    );
  }

  @Test
  void commandsEnumAcceptHasCorrectName() {
    assertAll(
        () -> assertEquals("accept", Marry.Commands.ACCEPT.getName()),
        () -> assertNotNull(Marry.Commands.ACCEPT.getSubCommands())
    );
  }

  @Test
  void commandsEnumDivorceHasCorrectName() {
    assertAll(
        () -> assertEquals("divorce", Marry.Commands.DIVORCE.getName()),
        () -> assertNotNull(Marry.Commands.DIVORCE.getSubCommands())
    );
  }

  @Test
  void onCommandReturnsFalseWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);

    boolean result = marry.onCommand(nonPlayerSender, command, "marry", new String[]{});

    assertAll(
        () -> assertFalse(result)
    );
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsNotVip() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = marry.onCommand(player, command, "marry", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsInfoMessageWhenNoArgsProvided() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(command.getName()).thenReturn("marry");
    when(translationService.getWithPrefix(
        MessageKey.COMMAND_MARRY_INFO,
        "marry", "marry", "accept", "marry", "divorce"
    )).thenReturn(TRANSLATED_MESSAGE);

    boolean result = marry.onCommand(player, command, "marry", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsNoRequestMessageWhenAcceptWithNoPendingRequest() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_MARRY_ACCEPT_NO_REQUEST)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = marry.onCommand(player, command, "marry", new String[]{"accept"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsNotMarriedMessageWhenDivorceWithNoPartner() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getPartner()).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_MARRY_DIVORCE_NOT_MARRIED)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = marry.onCommand(player, command, "marry", new String[]{"divorce"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsTargetNotAPlayerWhenTargetIsNull() {
    String targetName = "UnknownPlayer";
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serviceContext.getPluginMetadataService()).thenReturn(mock(de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService.class));
    when(serviceContext.getPluginMetadataService().getPlugin()).thenReturn(mock(org.bukkit.plugin.java.JavaPlugin.class));
    when(serviceContext.getPluginMetadataService().getPlugin().getServer()).thenReturn(mock(org.bukkit.Server.class));
    when(serviceContext.getPluginMetadataService().getPlugin().getServer().getPlayer(targetName)).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, targetName)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = marry.onCommand(player, command, "marry", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsSelfMarriageMessageWhenTargetIsSelf() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(player.getName()).thenReturn("SomePlayer");

    Player targetPlayer = mock(Player.class);
    when(targetPlayer.getName()).thenReturn("SomePlayer");

    when(serviceContext.getPluginMetadataService()).thenReturn(mock(de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService.class));
    when(serviceContext.getPluginMetadataService().getPlugin()).thenReturn(mock(org.bukkit.plugin.java.JavaPlugin.class));
    when(serviceContext.getPluginMetadataService().getPlugin().getServer()).thenReturn(mock(org.bukkit.Server.class));
    when(serviceContext.getPluginMetadataService().getPlugin().getServer().getPlayer("SomePlayer")).thenReturn(targetPlayer);
    when(translationService.getWithPrefix(MessageKey.COMMAND_MARRY_SELF_MARRIAGE)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = marry.onCommand(player, command, "marry", new String[]{"SomePlayer"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsRequestWhenTargetIsValidAndNeitherIsMarried() {
    String targetName = "TargetPlayer";
    Player targetPlayer = mock(Player.class);
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    PlayerEntry targetEntry = mock(PlayerEntry.class);

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(player.getName()).thenReturn("SenderPlayer");
    when(targetPlayer.getName()).thenReturn(targetName);

    when(serviceContext.getPluginMetadataService()).thenReturn(mock(de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService.class));
    when(serviceContext.getPluginMetadataService().getPlugin()).thenReturn(mock(org.bukkit.plugin.java.JavaPlugin.class));
    when(serviceContext.getPluginMetadataService().getPlugin().getServer()).thenReturn(mock(org.bukkit.Server.class));
    when(serviceContext.getPluginMetadataService().getPlugin().getServer().getPlayer(targetName)).thenReturn(targetPlayer);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerService.getPlayerEntry(targetPlayer)).thenReturn(targetEntry);
    when(playerEntry.getPartner()).thenReturn(null);
    when(targetEntry.getPartner()).thenReturn(null);

    when(player.getCustomName()).thenReturn("SenderPlayer");
    when(targetPlayer.getCustomName()).thenReturn(targetName);

    when(translationService.getWithPrefix(MessageKey.COMMAND_MARRY_SEND_REQUEST, targetName)).thenReturn("send-request");
    when(translationService.getWithPrefix(MessageKey.COMMAND_MARRY_RECEIVE_REQUEST, "SenderPlayer")).thenReturn("receive-request");

    boolean result = marry.onCommand(player, command, "marry", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage("send-request"),
        () -> verify(targetPlayer).sendMessage("receive-request"),
        () -> verify(schedulerService).runTaskLater(any(Runnable.class), anyLong())
    );
  }

  @Test
  void onCommandSendsAlreadyMarriedMessageWhenSenderHasPartner() {
    String targetName = "TargetPlayer";
    Player targetPlayer = mock(Player.class);
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    PlayerPartnerEntry existingPartner = mock(PlayerPartnerEntry.class);

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(player.getName()).thenReturn("SenderPlayer");
    when(targetPlayer.getName()).thenReturn(targetName);

    when(serviceContext.getPluginMetadataService()).thenReturn(mock(de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService.class));
    when(serviceContext.getPluginMetadataService().getPlugin()).thenReturn(mock(org.bukkit.plugin.java.JavaPlugin.class));
    when(serviceContext.getPluginMetadataService().getPlugin().getServer()).thenReturn(mock(org.bukkit.Server.class));
    when(serviceContext.getPluginMetadataService().getPlugin().getServer().getPlayer(targetName)).thenReturn(targetPlayer);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getPartner()).thenReturn(existingPartner);

    when(translationService.getWithPrefix(MessageKey.COMMAND_MARRY_REQUEST_IS_MARRIED)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = marry.onCommand(player, command, "marry", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE),
        () -> verify(schedulerService, never()).runTaskLater(any(Runnable.class), anyLong())
    );
  }

  @Test
  void onCommandSendsTooManyArgumentsMessageWhenMoreThanOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = marry.onCommand(player, command, "marry", new String[]{"arg1", "arg2"});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotAuthorized() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "user")).thenReturn(false);

    List<String> result = marry.onTabComplete(unauthorizedSender, command, "marry", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(nonPlayerSender, "user")).thenReturn(true);

    List<String> result = marry.onTabComplete(nonPlayerSender, command, "marry", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);

    List<String> result = marry.onTabComplete(player, command, "marry", new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsSubCommandsAndOnlinePlayersWhenAuthorizedPlayerWithOneArg() {
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = marry.onTabComplete(player, command, "marry", new String[]{"partial"});

    assertNotNull(result);
    assertAll(
        () -> assertTrue(result.contains("accept")),
        () -> assertTrue(result.contains("divorce")),
        () -> assertTrue(result.contains("OnlinePlayer"))
    );
  }

  @Test
  void onCommandSendsAlreadyMarriedMessageWhenTargetHasPartner() {
    String targetName = "TargetPlayer";
    Player targetPlayer = mock(Player.class);
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    PlayerEntry targetEntry = mock(PlayerEntry.class);
    PlayerPartnerEntry existingPartner = mock(PlayerPartnerEntry.class);

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(player.getName()).thenReturn("SenderPlayer");
    when(targetPlayer.getName()).thenReturn(targetName);

    when(serviceContext.getPluginMetadataService()).thenReturn(mock(de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService.class));
    when(serviceContext.getPluginMetadataService().getPlugin()).thenReturn(mock(org.bukkit.plugin.java.JavaPlugin.class));
    when(serviceContext.getPluginMetadataService().getPlugin().getServer()).thenReturn(mock(org.bukkit.Server.class));
    when(serviceContext.getPluginMetadataService().getPlugin().getServer().getPlayer(targetName)).thenReturn(targetPlayer);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerService.getPlayerEntry(targetPlayer)).thenReturn(targetEntry);
    when(playerEntry.getPartner()).thenReturn(null);
    when(targetEntry.getPartner()).thenReturn(existingPartner);

    when(translationService.getWithPrefix(MessageKey.COMMAND_MARRY_REQUEST_IS_MARRIED)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = marry.onCommand(player, command, "marry", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE),
        () -> verify(schedulerService, never()).runTaskLater(any(Runnable.class), anyLong())
    );
  }

  @Test
  void onCommandSendsExpiredMessageWhenMarryRequestTimesOut() {
    String targetName = "TargetPlayer";
    Player targetPlayer = mock(Player.class);
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    PlayerEntry targetEntry = mock(PlayerEntry.class);

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(player.getName()).thenReturn("SenderPlayer");
    when(targetPlayer.getName()).thenReturn(targetName);

    when(serviceContext.getPluginMetadataService()).thenReturn(mock(de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService.class));
    when(serviceContext.getPluginMetadataService().getPlugin()).thenReturn(mock(org.bukkit.plugin.java.JavaPlugin.class));
    when(serviceContext.getPluginMetadataService().getPlugin().getServer()).thenReturn(mock(org.bukkit.Server.class));
    when(serviceContext.getPluginMetadataService().getPlugin().getServer().getPlayer(targetName)).thenReturn(targetPlayer);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerService.getPlayerEntry(targetPlayer)).thenReturn(targetEntry);
    when(playerEntry.getPartner()).thenReturn(null);
    when(targetEntry.getPartner()).thenReturn(null);

    when(player.getCustomName()).thenReturn("SenderPlayer");
    when(targetPlayer.getCustomName()).thenReturn(targetName);

    when(translationService.getWithPrefix(MessageKey.COMMAND_MARRY_SEND_REQUEST, targetName)).thenReturn("send-request");
    when(translationService.getWithPrefix(MessageKey.COMMAND_MARRY_RECEIVE_REQUEST, "SenderPlayer")).thenReturn("receive-request");
    when(translationService.getWithPrefix(MessageKey.COMMAND_MARRY_REQUEST_EXPIRED)).thenReturn("request-expired");

    Runnable[] capturedTask = new Runnable[1];
    doAnswer(invocation -> {
      capturedTask[0] = invocation.getArgument(0);
      return null;
    }).when(schedulerService).runTaskLater(any(Runnable.class), anyLong());

    marry.onCommand(player, command, "marry", new String[]{targetName});

    capturedTask[0].run();

    assertAll(
        () -> verify(player).sendMessage("request-expired"),
        () -> verify(targetPlayer).sendMessage("request-expired")
    );
  }
}