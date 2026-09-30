package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerInfoTest {

  @Mock
  private ServiceContext serviceContext;
  @Mock
  private TranslationService translationService;
  @Mock
  private GroupService groupService;
  @Mock
  private PlayerService playerService;
  @Mock
  private ServerService serverService;
  @Mock
  private CommandSender sender;
  @Mock
  private Command command;
  @Mock
  private Player onlinePlayer;
  @Mock
  private OfflinePlayer offlinePlayer;

  private PlayerInfo playerInfoCommand;

  @BeforeEach
  void setUp() {
    playerInfoCommand = new PlayerInfo();
    playerInfoCommand.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getPlayerService()).thenReturn(playerService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void onCommandNoArgsSendsTooLessArgumentsMessage() {
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn("too less args");

    boolean result = playerInfoCommand.onCommand(sender, command, "playerinfo", new String[]{});

    verify(sender).sendMessage("too less args");
    assertTrue(result);
  }

  @Test
  void onCommandMoreThanOneArgSendsTooManyArgumentsMessage() {
    when(translationService.getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS)).thenReturn("too many args");

    boolean result = playerInfoCommand.onCommand(sender, command, "playerinfo", new String[]{"player1", "player2"});

    verify(sender).sendMessage("too many args");
    assertTrue(result);
  }

  @Test
  void onCommandSenderNotAuthorizedSendsPermissionMissingMessage() {
    when(groupService.isSenderAuthorized(sender, "vip")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn("no permission");

    boolean result = playerInfoCommand.onCommand(sender, command, "playerinfo", new String[]{"somePlayer"});

    verify(sender).sendMessage("no permission");
    assertTrue(result);
  }

  @Test
  void onCommandTargetPlayerNotFoundSendsNotaPlayerMessage() {
    when(groupService.isSenderAuthorized(sender, "vip")).thenReturn(true);
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_TARGET_NOT_A_PLAYER), anyString())).thenReturn("not a player");

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> helperMock =
        Mockito.mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {
      helperMock.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getOfflinePlayer("unknownPlayer"))
          .thenReturn(null);

      boolean result = playerInfoCommand.onCommand(sender, command, "playerinfo", new String[]{"unknownPlayer"});

      verify(sender).sendMessage("not a player");
      assertTrue(result);
    }
  }

  @Test
  void onCommandPlayerEntryNullSendsNotaPlayerMessage() {
    when(groupService.isSenderAuthorized(sender, "vip")).thenReturn(true);
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_TARGET_NOT_A_PLAYER), anyString())).thenReturn("not a player");

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> helperMock =
        Mockito.mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {
      helperMock.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getOfflinePlayer("somePlayer"))
          .thenReturn(offlinePlayer);
      when(offlinePlayer.getPlayer()).thenReturn(onlinePlayer);
      when(playerService.getPlayerEntry(onlinePlayer)).thenReturn(null);

      boolean result = playerInfoCommand.onCommand(sender, command, "playerinfo", new String[]{"somePlayer"});

      verify(sender).sendMessage("not a player");
      assertTrue(result);
    }
  }

  @Test
  void onCommandValidPlayerWithoutPartnerSendsAllInfoMessages() {
    when(groupService.isSenderAuthorized(sender, "vip")).thenReturn(true);

    GroupEntry groupEntry = new GroupEntry();
    groupEntry.setPrefix("§a");
    groupEntry.setName("Admin");

    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(1);
    playerEntry.setName("somePlayer");
    playerEntry.setHomes(new ArrayList<>());
    playerEntry.setDeaths(new ArrayList<>());
    playerEntry.setGroup(groupEntry);
    playerEntry.setPartner(null);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> helperMock =
        Mockito.mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {
      helperMock.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getOfflinePlayer("somePlayer"))
          .thenReturn(offlinePlayer);
      when(offlinePlayer.getPlayer()).thenReturn(onlinePlayer);
      when(offlinePlayer.getName()).thenReturn("somePlayer");
      when(offlinePlayer.getLastPlayed()).thenReturn(1000L);
      when(offlinePlayer.getFirstPlayed()).thenReturn(500L);
      when(offlinePlayer.getStatistic(eq(Statistic.MINE_BLOCK), any(Material.class))).thenReturn(0);
      when(offlinePlayer.getStatistic(Statistic.DEATHS)).thenReturn(0);
      when(offlinePlayer.getStatistic(Statistic.JUMP)).thenReturn(0);
      when(offlinePlayer.getStatistic(Statistic.LEAVE_GAME)).thenReturn(0);
      when(playerService.getPlayerEntry(onlinePlayer)).thenReturn(playerEntry);

      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO), anyString())).thenReturn("info header");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_HOMES), any())).thenReturn("homes");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_DEATHPOINTS), any())).thenReturn("deaths");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_GROUP), anyString())).thenReturn("group");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_LAST_ONLINE), any())).thenReturn("last online");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_FIRST_ONLINE), any())).thenReturn("first online");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_MINED), anyString(), any())).thenReturn("mined");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_DEATHS), any())).thenReturn("death count");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_JUMPED), any())).thenReturn("jumped");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_LEFT_GAME), any())).thenReturn("left game");

      boolean result = playerInfoCommand.onCommand(sender, command, "playerinfo", new String[]{"somePlayer"});

      verify(sender).sendMessage("info header");
      verify(sender).sendMessage("homes");
      verify(sender).sendMessage("deaths");
      verify(sender).sendMessage("group");
      verify(sender).sendMessage("last online");
      verify(sender).sendMessage("first online");
      verify(sender).sendMessage("left game");
      verify(sender).sendMessage("jumped");
      verify(sender).sendMessage("death count");
      assertTrue(result);
    }
  }

  @Test
  void onCommandValidPlayerWithPartnerSendsMarriedMessages() {
    when(groupService.isSenderAuthorized(sender, "vip")).thenReturn(true);

    PlayerEntry playerEntry = getPlayerEntry();

    PlayerEntry firstPartnerEntry = new PlayerEntry();
    firstPartnerEntry.setId(1);
    firstPartnerEntry.setName("Alice");

    PlayerEntry secondPartnerEntry = new PlayerEntry();
    secondPartnerEntry.setId(2);
    secondPartnerEntry.setName("Bob");

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> helperMock =
        Mockito.mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {
      helperMock.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getOfflinePlayer("somePlayer"))
          .thenReturn(offlinePlayer);
      when(offlinePlayer.getPlayer()).thenReturn(onlinePlayer);
      when(offlinePlayer.getName()).thenReturn("somePlayer");
      when(offlinePlayer.getLastPlayed()).thenReturn(1000L);
      when(offlinePlayer.getFirstPlayed()).thenReturn(500L);
      when(offlinePlayer.getStatistic(eq(Statistic.MINE_BLOCK), any(Material.class))).thenReturn(0);
      when(offlinePlayer.getStatistic(Statistic.DEATHS)).thenReturn(0);
      when(offlinePlayer.getStatistic(Statistic.JUMP)).thenReturn(0);
      when(offlinePlayer.getStatistic(Statistic.LEAVE_GAME)).thenReturn(0);
      when(playerService.getPlayerEntry(onlinePlayer)).thenReturn(playerEntry);
      when(playerService.getPlayerEntryByInternalId(1)).thenReturn(firstPartnerEntry);
      when(playerService.getPlayerEntryByInternalId(2)).thenReturn(secondPartnerEntry);

      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO), anyString())).thenReturn("info header");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_HOMES), any())).thenReturn("homes");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_DEATHPOINTS), any())).thenReturn("deaths");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_GROUP), anyString())).thenReturn("group");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_MARRIED_TO), anyString(), anyString())).thenReturn("married to");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_MARRIED_SINCE), any())).thenReturn("married since");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_LAST_ONLINE), any())).thenReturn("last online");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_FIRST_ONLINE), any())).thenReturn("first online");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_MINED), anyString(), any())).thenReturn("mined");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_DEATHS), any())).thenReturn("death count");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_JUMPED), any())).thenReturn("jumped");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_LEFT_GAME), any())).thenReturn("left game");

      boolean result = playerInfoCommand.onCommand(sender, command, "playerinfo", new String[]{"somePlayer"});

      verify(sender).sendMessage("married to");
      verify(sender).sendMessage("married since");
      assertTrue(result);
    }
  }

  @Test
  void onCommandValidPlayerWithPartnerFirstPartnerNullStopsEarly() {
    when(groupService.isSenderAuthorized(sender, "vip")).thenReturn(true);

    PlayerEntry playerEntry = getPlayerEntry();

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> helperMock =
        Mockito.mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {
      helperMock.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getOfflinePlayer("somePlayer"))
          .thenReturn(offlinePlayer);
      when(offlinePlayer.getPlayer()).thenReturn(onlinePlayer);
      when(offlinePlayer.getName()).thenReturn("somePlayer");
      when(playerService.getPlayerEntry(onlinePlayer)).thenReturn(playerEntry);
      when(playerService.getPlayerEntryByInternalId(1)).thenReturn(null);
      when(playerService.getPlayerEntryByInternalId(2)).thenReturn(null);

      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO), anyString())).thenReturn("info header");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_HOMES), any())).thenReturn("homes");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_DEATHPOINTS), any())).thenReturn("deaths");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_GROUP), anyString())).thenReturn("group");

      boolean result = playerInfoCommand.onCommand(sender, command, "playerinfo", new String[]{"somePlayer"});

      verify(sender).sendMessage("info header");
      verify(sender).sendMessage("homes");
      verify(sender).sendMessage("deaths");
      verify(sender).sendMessage("group");
      Mockito.verify(translationService, Mockito.never())
          .getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_LAST_ONLINE), any());
      assertTrue(result);
    }
  }

  private static @NonNull PlayerEntry getPlayerEntry() {
    GroupEntry groupEntry = new GroupEntry();
    groupEntry.setPrefix("§a");
    groupEntry.setName("Admin");

    PlayerPartnerEntry partnerEntry = new PlayerPartnerEntry();
    partnerEntry.setFirstPartnerId(1);
    partnerEntry.setSecondPartnerId(2);

    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(1);
    playerEntry.setName("somePlayer");
    playerEntry.setHomes(new ArrayList<>());
    playerEntry.setDeaths(new ArrayList<>());
    playerEntry.setGroup(groupEntry);
    playerEntry.setPartner(partnerEntry);
    return playerEntry;
  }

  @Test
  void onCommandValidPlayerStatisticsAreSentForAllTrackedMaterials() {
    when(groupService.isSenderAuthorized(sender, "vip")).thenReturn(true);

    GroupEntry groupEntry = new GroupEntry();
    groupEntry.setPrefix("§b");
    groupEntry.setName("User");

    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(1);
    playerEntry.setName("miner");
    playerEntry.setHomes(new ArrayList<>());
    playerEntry.setDeaths(new ArrayList<>());
    playerEntry.setGroup(groupEntry);
    playerEntry.setPartner(null);

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> helperMock =
        Mockito.mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {
      helperMock.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getOfflinePlayer("miner"))
          .thenReturn(offlinePlayer);
      when(offlinePlayer.getPlayer()).thenReturn(onlinePlayer);
      when(offlinePlayer.getName()).thenReturn("miner");
      when(offlinePlayer.getLastPlayed()).thenReturn(2000L);
      when(offlinePlayer.getFirstPlayed()).thenReturn(1000L);
      when(offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.STONE)).thenReturn(100);
      when(offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.DIRT)).thenReturn(200);
      when(offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.SAND)).thenReturn(300);
      when(offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.COBBLESTONE)).thenReturn(400);
      when(offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.DEEPSLATE)).thenReturn(500);
      when(offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE)).thenReturn(10);
      when(offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.DEEPSLATE_DIAMOND_ORE)).thenReturn(5);
      when(offlinePlayer.getStatistic(Statistic.DEATHS)).thenReturn(3);
      when(offlinePlayer.getStatistic(Statistic.JUMP)).thenReturn(50);
      when(offlinePlayer.getStatistic(Statistic.LEAVE_GAME)).thenReturn(7);
      when(playerService.getPlayerEntry(onlinePlayer)).thenReturn(playerEntry);

      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO), anyString())).thenReturn("info header");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_HOMES), any())).thenReturn("homes");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_DEATHPOINTS), any())).thenReturn("deaths");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_GROUP), anyString())).thenReturn("group");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_LAST_ONLINE), any())).thenReturn("last online");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_FIRST_ONLINE), any())).thenReturn("first online");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_MINED), anyString(), any())).thenReturn("mined");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_DEATHS), any())).thenReturn("death count");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_JUMPED), any())).thenReturn("jumped");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_LEFT_GAME), any())).thenReturn("left game");

      playerInfoCommand.onCommand(sender, command, "playerinfo", new String[]{"miner"});

      verify(offlinePlayer).getStatistic(Statistic.MINE_BLOCK, Material.STONE);
      verify(offlinePlayer).getStatistic(Statistic.MINE_BLOCK, Material.DIRT);
      verify(offlinePlayer).getStatistic(Statistic.MINE_BLOCK, Material.SAND);
      verify(offlinePlayer).getStatistic(Statistic.MINE_BLOCK, Material.COBBLESTONE);
      verify(offlinePlayer).getStatistic(Statistic.MINE_BLOCK, Material.DEEPSLATE);
      verify(offlinePlayer).getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE);
      verify(offlinePlayer).getStatistic(Statistic.MINE_BLOCK, Material.DEEPSLATE_DIAMOND_ORE);
      verify(offlinePlayer).getStatistic(Statistic.DEATHS);
      verify(offlinePlayer).getStatistic(Statistic.JUMP);
      verify(offlinePlayer).getStatistic(Statistic.LEAVE_GAME);
    }
  }

  @Test
  void onTabCompleteUnauthorizedReturnsEmptyList() {
    when(groupService.isSenderAuthorized(sender, "vip")).thenReturn(false);

    List<String> result = playerInfoCommand.onTabComplete(sender, command, "playerinfo", new String[]{"p"});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteNonPlayerSenderReturnsEmptyList() {
    when(groupService.isSenderAuthorized(sender, "vip")).thenReturn(true);

    List<String> result = playerInfoCommand.onTabComplete(sender, command, "playerinfo", new String[]{"p"});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteMoreThanOneArgReturnsEmptyList() {
    when(groupService.isSenderAuthorized(onlinePlayer, "vip")).thenReturn(true);

    List<String> result = playerInfoCommand.onTabComplete(onlinePlayer, command, "playerinfo", new String[]{"player1", "extra"});

    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteFirstArgAuthorizedPlayerReturnsOnlinePlayerNames() {
    when(groupService.isSenderAuthorized(onlinePlayer, "vip")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("somePlayer");

    List<String> result = playerInfoCommand.onTabComplete(onlinePlayer, command, "playerinfo", new String[]{"s"});

    assertTrue(result.contains("somePlayer"));
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    assertEquals(0, playerInfoCommand.getCommands().length);
  }

  @Test
  void onCommandPlayerHelperReturnsNullOnSecondCallInsideShowPlayerInfoSendsNotaPlayerMessage() {
    when(groupService.isSenderAuthorized(sender, "vip")).thenReturn(true);
    when(translationService.getWithPrefix(eq(MessageKey.COMMAND_TARGET_NOT_A_PLAYER), anyString()))
        .thenReturn("not a player");

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> helperMock =
        Mockito.mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {

      helperMock.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper
              .getOfflinePlayer("somePlayer"))
          .thenReturn(offlinePlayer)
          .thenReturn(null);

      boolean result = playerInfoCommand.onCommand(sender, command, "playerinfo", new String[]{"somePlayer"});

      verify(sender).sendMessage("not a player");
      assertTrue(result);
    }
  }

  @Test
  void onCommandValidPlayerWithPartnerSecondPartnerNullStopsEarly() {
    when(groupService.isSenderAuthorized(sender, "vip")).thenReturn(true);

    PlayerEntry playerEntry = getPlayerEntry();

    PlayerEntry firstPartnerEntry = new PlayerEntry();
    firstPartnerEntry.setId(1);
    firstPartnerEntry.setName("Alice");

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper> helperMock =
        Mockito.mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.class)) {
      helperMock.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getOfflinePlayer("somePlayer"))
          .thenReturn(offlinePlayer);
      when(offlinePlayer.getPlayer()).thenReturn(onlinePlayer);
      when(offlinePlayer.getName()).thenReturn("somePlayer");
      when(playerService.getPlayerEntry(onlinePlayer)).thenReturn(playerEntry);
      when(playerService.getPlayerEntryByInternalId(1)).thenReturn(firstPartnerEntry);
      when(playerService.getPlayerEntryByInternalId(2)).thenReturn(null);

      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO), anyString())).thenReturn("info header");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_HOMES), any())).thenReturn("homes");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_DEATHPOINTS), any())).thenReturn("deaths");
      when(translationService.getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_GROUP), anyString())).thenReturn("group");

      boolean result = playerInfoCommand.onCommand(sender, command, "playerinfo", new String[]{"somePlayer"});

      verify(sender).sendMessage("info header");
      verify(sender).sendMessage("homes");
      verify(sender).sendMessage("deaths");
      verify(sender).sendMessage("group");
      Mockito.verify(translationService, Mockito.never())
          .getWithPrefix(eq(MessageKey.COMMAND_PLAYERINFO_MARRIED_TO), anyString(), anyString());
      assertTrue(result);
    }
  }
}