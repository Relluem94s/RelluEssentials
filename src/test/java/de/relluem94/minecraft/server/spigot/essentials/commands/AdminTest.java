package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.NpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TabCompleterService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private GroupService groupService;

  @Mock
  private ServerService serverService;

  @Mock
  private TabCompleterService tabCompleterService;

  @Mock
  private NpcService npcService;

  @Mock
  private Command command;

  @Mock
  private Player player;

  @Mock
  private World world;

  private Admin admin;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    admin = new Admin();
    admin.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(serviceContext.getNpcService()).thenReturn(npcService);
    lenient().when(serviceContext.getTabCompleterService()).thenReturn(tabCompleterService);
  }

  @Test
  void getCommandsReturnsAllSubCommands() {
    CommandsEnum[] result = admin.getCommands();

    assertNotNull(result);
    assertEquals(Admin.Commands.values().length, result.length);
  }

  @Test
  void commandsEnumAfkHasCorrectName() {
    assertEquals("afk", Admin.Commands.AFK.getName());
  }

  @Test
  void commandsEnumCleanProtectionsHasCorrectName() {
    assertEquals("cleanProtections", Admin.Commands.CLEAN_PROTECTIONS.getName());
  }

  @Test
  void commandsEnumCleanLocationsHasCorrectName() {
    assertEquals("cleanLocations", Admin.Commands.CLEAN_LOCATIONS.getName());
  }

  @Test
  void commandsEnumChatHasCorrectName() {
    assertEquals("chat", Admin.Commands.CHAT.getName());
  }

  @Test
  void commandsEnumInfoHasCorrectName() {
    assertEquals("info", Admin.Commands.INFO.getName());
  }

  @Test
  void commandsEnumLightHasCorrectName() {
    assertEquals("light", Admin.Commands.LIGHT.getName());
  }

  @Test
  void commandsEnumNpcHasCorrectNameAndSubCommands() {
    assertEquals("npc", Admin.Commands.NPC.getName());
    assertEquals(5, Admin.Commands.NPC.getSubCommands().length);
  }

  @Test
  void commandsEnumPingHasCorrectName() {
    assertEquals("ping", Admin.Commands.PING.getName());
  }

  @Test
  void commandsEnumTopHasCorrectName() {
    assertEquals("top", Admin.Commands.TOP.getName());
  }

  @Test
  void commandsEnumAdminToolsHasCorrectName() {
    assertEquals("adminTools", Admin.Commands.ADMIN_TOOLS.getName());
  }

  @Test
  void onCommandSendsNotaPlayerMessageWhenSenderIsNotPlayer() {
    CommandSender consoleSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = admin.onCommand(consoleSender, command, "admin", new String[]{});

    assertTrue(result);
    verify(consoleSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsNotMod() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = admin.onCommand(player, command, "admin", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsAdminInfoWhenNoArgsProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_INFO)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = admin.onCommand(player, command, "admin", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsWrongSubCommandWhenUnknownSubCommandProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = admin.onCommand(player, command, "admin", new String[]{"unknownSubCommand"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotAuthorized() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "mod")).thenReturn(false);

    List<String> result = admin.onTabComplete(unauthorizedSender, command, "admin", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    CommandSender consoleSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(consoleSender, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(consoleSender, command, "admin", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsAllCommandsWhenOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(tabCompleterService.getCommands(Admin.Commands.values()))
        .thenReturn(List.of("afk", "cleanProtections", "cleanLocations", "chat", "info", "light", "npc", "ping", "top", "adminTools"));

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"a"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayersWhenTwoArgsAndFirstArgIsPing() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(tabCompleterService.getOnlinePlayerNames(player)).thenReturn(List.of("OnlinePlayer"));

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"ping", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("OnlinePlayer"));
  }

  @Test
  void onTabCompleteReturnsNpcSubCommandsWhenTwoArgsAndFirstArgIsNpc() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("create"));
    assertTrue(result.contains("update"));
    assertTrue(result.contains("delete"));
    assertTrue(result.contains("dialogue"));
    assertTrue(result.contains("equip"));
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenTwoArgsAndFirstArgIsUnknown() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"unknown", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsProfileNamePlaceholderWhenThreeArgsAndNpcCreate() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "create", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("<profileName>"));
  }

  @Test
  void onTabCompleteReturnsDialogueActionsWhenThreeArgsAndNpcDialogue() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("add"));
    assertTrue(result.contains("update"));
    assertTrue(result.contains("delete"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"update", "equip", "delete"})
  void onTabCompleteReturnsNearestNpcIdWhenThreeArgsAndNpcUpdateEquipDelete(String subCommand) {
    UUID npcId = UUID.randomUUID();
    Npc npc = mock(Npc.class);
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(npcService.getNearestNpc(10.0, 64.0, 20.0, "world")).thenReturn(Optional.of(npc));
    when(npc.getId()).thenReturn(npcId);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", subCommand, "partial"});

    assertNotNull(result);
    assertTrue(result.contains(npcId.toString()));
  }

  @ParameterizedTest
  @ValueSource(strings = {"update", "equip", "delete"})
  void onTabCompleteReturnsNpcIdPlaceholderWhenThreeArgsAndNoNearestNpcFound(String subCommand) {
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(npcService.getNearestNpc(10.0, 64.0, 20.0, "world")).thenReturn(Optional.empty());

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", subCommand, "partial"});

    assertNotNull(result);
    assertTrue(result.contains("<NPC-ID>"));
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenThreeArgsAndUnknownNpcSubCommand() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "unknown", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsxCoordinateWhenFourArgsAndNpcCreate() {
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "create", "profileName", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("10"));
  }

  @Test
  void onTabCompleteReturnsUpdateOptionsWhenFourArgsAndNpcUpdate() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "update", "some-id", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("profile"));
    assertTrue(result.contains("position"));
  }

  @Test
  void onTabCompleteReturnsNearestNpcIdWhenFourArgsAndNpcDialogue() {
    UUID npcId = UUID.randomUUID();
    Npc npc = mock(Npc.class);
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(npcService.getNearestNpc(10.0, 64.0, 20.0, "world")).thenReturn(Optional.of(npc));
    when(npc.getId()).thenReturn(npcId);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "add", "partial"});

    assertNotNull(result);
    assertTrue(result.contains(npcId.toString()));
  }

  @Test
  void onTabCompleteReturnsNpcIdPlaceholderWhenFourArgsAndNpcDialogueAndNoNearestNpc() {
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(npcService.getNearestNpc(10.0, 64.0, 20.0, "world")).thenReturn(Optional.empty());

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "add", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("<NPC-ID>"));
  }

  @Test
  void onTabCompleteReturnsyCoordinateWhenFiveArgsAndNpcCreate() {
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "create", "profileName", "10", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("64"));
  }

  @Test
  void onTabCompleteReturnsProfileNamePlaceholderWhenFiveArgsAndNpcUpdateProfile() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "update", "some-id", "profile", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("<profileName>"));
  }

  @Test
  void onTabCompleteReturnsxCoordinateWhenFiveArgsAndNpcUpdatePosition() {
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "update", "some-id", "position", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("10"));
  }

  @Test
  void onTabCompleteReturnsAvailablePositionsWhenFiveArgsAndNpcDialogueAdd() {
    UUID npcId = UUID.randomUUID();
    Npc npc = mock(Npc.class);
    NpcDialogueEntry entry = mock(NpcDialogueEntry.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(npc.getDbid()).thenReturn(1);
    when(npcService.getNpcDialogues(1)).thenReturn(List.of(entry));
    when(entry.getListPosition()).thenReturn(1);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "add", npcId.toString(), "partial"});

    assertNotNull(result);
    assertTrue(result.contains("2"));
  }

  @Test
  void onTabCompleteReturnsPositionOneWhenFiveArgsAndNpcDialogueAddAndNoExistingEntries() {
    UUID npcId = UUID.randomUUID();
    Npc npc = mock(Npc.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(npc.getDbid()).thenReturn(1);
    when(npcService.getNpcDialogues(1)).thenReturn(List.of());

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "add", npcId.toString(), "partial"});

    assertNotNull(result);
    assertTrue(result.contains("1"));
  }

  @Test
  void onTabCompleteReturnsGapPositionsWhenFiveArgsAndNpcDialogueAddAndGapsExist() {
    UUID npcId = UUID.randomUUID();
    Npc npc = mock(Npc.class);
    NpcDialogueEntry entry1 = mock(NpcDialogueEntry.class);
    NpcDialogueEntry entry3 = mock(NpcDialogueEntry.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(npc.getDbid()).thenReturn(1);
    when(npcService.getNpcDialogues(1)).thenReturn(List.of(entry1, entry3));
    when(entry1.getListPosition()).thenReturn(1);
    when(entry3.getListPosition()).thenReturn(3);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "add", npcId.toString(), "partial"});

    assertNotNull(result);
    assertTrue(result.contains("2"));
    assertTrue(result.contains("4"));
  }

  @Test
  void onTabCompleteReturnsUsedPositionsWhenFiveArgsAndNpcDialogueUpdate() {
    UUID npcId = UUID.randomUUID();
    Npc npc = mock(Npc.class);
    NpcDialogueEntry entry = mock(NpcDialogueEntry.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(npc.getDbid()).thenReturn(1);
    when(npcService.getNpcDialogues(1)).thenReturn(List.of(entry));
    when(entry.getListPosition()).thenReturn(2);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "update", npcId.toString(), "partial"});

    assertNotNull(result);
    assertTrue(result.contains("2"));
  }

  @Test
  void onTabCompleteReturnsUsedPositionsWhenFiveArgsAndNpcDialogueDelete() {
    UUID npcId = UUID.randomUUID();
    Npc npc = mock(Npc.class);
    NpcDialogueEntry entry = mock(NpcDialogueEntry.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(npc.getDbid()).thenReturn(1);
    when(npcService.getNpcDialogues(1)).thenReturn(List.of(entry));
    when(entry.getListPosition()).thenReturn(1);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "delete", npcId.toString(), "partial"});

    assertNotNull(result);
    assertTrue(result.contains("1"));
  }

  @Test
  void onTabCompleteReturnsEmptyWhenFiveArgsAndNpcDialogueAndInvalidNpcId() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "add", "not-a-uuid", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenFiveArgsAndNpcDialogueAndNpcNotFound() {
    UUID npcId = UUID.randomUUID();
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.empty());

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "add", npcId.toString(), "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnszCoordinateWhenSixArgsAndNpcCreate() {
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "create", "profileName", "10", "64", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("20"));
  }

  @Test
  void onTabCompleteReturnsyCoordinateWhenSixArgsAndNpcUpdatePosition() {
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "update", "some-id", "position", "10", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("64"));
  }

  @Test
  void onTabCompleteReturnsTextPlaceholderWhenSixArgsAndNpcDialogueAdd() {
    UUID npcId = UUID.randomUUID();
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "add", npcId.toString(), "1", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("<text...>"));
  }

  @Test
  void onTabCompleteReturnsCurrentTextWhenSixArgsAndNpcDialogueUpdateAndTextFound() {
    UUID npcId = UUID.randomUUID();
    Npc npc = mock(Npc.class);
    NpcDialogueEntry entry = mock(NpcDialogueEntry.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(npc.getDbid()).thenReturn(1);
    when(npcService.getNpcDialogues(1)).thenReturn(List.of(entry));
    when(entry.getListPosition()).thenReturn(1);
    when(entry.getText()).thenReturn("Hello World");

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "update", npcId.toString(), "1", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("Hello World"));
  }

  @Test
  void onTabCompleteConvertsColorCodesWhenSixArgsAndNpcDialogueUpdateAndTextHasColorCodes() {
    UUID npcId = UUID.randomUUID();
    Npc npc = mock(Npc.class);
    NpcDialogueEntry entry = mock(NpcDialogueEntry.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(npc.getDbid()).thenReturn(1);
    when(npcService.getNpcDialogues(1)).thenReturn(List.of(entry));
    when(entry.getListPosition()).thenReturn(1);
    when(entry.getText()).thenReturn("§aGreen Text");

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "update", npcId.toString(), "1", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("&aGreen Text"));
  }

  @Test
  void onTabCompleteReturnsTextPlaceholderWhenSixArgsAndNpcDialogueUpdateAndTextNotFound() {
    UUID npcId = UUID.randomUUID();
    Npc npc = mock(Npc.class);
    NpcDialogueEntry entry = mock(NpcDialogueEntry.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    when(npc.getDbid()).thenReturn(1);
    when(npcService.getNpcDialogues(1)).thenReturn(List.of(entry));
    when(entry.getListPosition()).thenReturn(2);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "update", npcId.toString(), "1", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("<text...>"));
  }

  @Test
  void onTabCompleteReturnsTextPlaceholderWhenSixArgsAndNpcDialogueUpdateAndInvalidNpcId() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "update", "not-a-uuid", "1", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("<text...>"));
  }

  @Test
  void onTabCompleteReturnsTextPlaceholderWhenSixArgsAndNpcDialogueUpdateAndInvalidPosition() {
    UUID npcId = UUID.randomUUID();
    Npc npc = mock(Npc.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    lenient().when(npcService.getNpcById(npcId)).thenReturn(Optional.of(npc));
    lenient().when(npc.getDbid()).thenReturn(1);
    lenient().when(npcService.getNpcDialogues(1)).thenReturn(List.of());

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "dialogue", "update", npcId.toString(), "notAnInt", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("<text...>"));
  }

  @Test
  void onTabCompleteReturnsYawWhenSevenArgsAndNpcCreate() {
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "create", "profileName", "10", "64", "20", "partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnszCoordinateWhenSevenArgsAndNpcUpdatePosition() {
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "update", "some-id", "position", "10", "64", "partial"});

    assertNotNull(result);
    assertTrue(result.contains("20"));
  }

  @Test
  void onTabCompleteReturnsPitchWhenEightArgsAndNpcCreate() {
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "create", "profileName", "10", "64", "20", "0", "partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsYawWhenEightArgsAndNpcUpdatePosition() {
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "update", "some-id", "position", "10", "64", "20", "partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsPitchWhenNineArgsAndNpcUpdatePosition() {
    Location location = new Location(world, 10, 64, 20);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getLocation()).thenReturn(location);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "update", "some-id", "position", "10", "64", "20", "0", "partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenNineArgsAndNpcUpdatePositionConditionNotMet() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "update", "some-id", "profile", "10", "64", "20", "0", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenSevenArgsAndNpcUpdatePositionConditionNotMet() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "update", "some-id", "profile", "10", "64", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenEightArgsAndNpcUpdatePositionConditionNotMet() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "update", "some-id", "profile", "10", "64", "20", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenSixArgsAndNpcCreateConditionNotMet() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin", new String[]{"npc", "update", "some-id", "profile", "value", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenFiveArgsAndNpcUnknownSubCommand() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"npc", "unknown", "some-id", "profile", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenSixArgsAndNpcUnknownSubCommand() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"npc", "unknown", "some-id", "profile", "value", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenSixArgsAndNpcDialogueDeleteAction() {
    UUID npcId = UUID.randomUUID();
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"npc", "dialogue", "delete", npcId.toString(), "1", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenSevenArgsAndNonNpcCommand() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"ping", "arg1", "arg2", "arg3", "arg4", "arg5", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenEightArgsAndNonNpcCommand() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"ping", "arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenNineArgsAndNonNpcCommand() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"ping", "arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "arg7", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenSevenArgsAndNpcButNotCreateOrUpdatePosition() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"npc", "dialogue", "add", "some-id", "1", "text", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenSixArgsAndNpcDialogueUnknownAction() {
    UUID npcId = UUID.randomUUID();
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"npc", "dialogue", "unknown", npcId.toString(), "1", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenFiveArgsAndNpcUpdateUnknownUpdateType() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"npc", "update", "some-id", "unknown", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenEightArgsAndNpcDialogue() {
    UUID npcId = UUID.randomUUID();
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"npc", "dialogue", "add", npcId.toString(), "1", "text", "extra", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenFiveArgsAndNonNpcCommand() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"ping", "arg1", "arg2", "arg3", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenSixArgsAndNonNpcCommand() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"ping", "arg1", "arg2", "arg3", "arg4", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenNineArgsAndNpcButNotUpdateSubCommand() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"npc", "create", "profileName", "position", "10", "64", "20", "0", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenThreeArgsAndNonNpcCommand() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"ping", "somePlayer", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenFourArgsAndNonNpcCommand() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"ping", "arg1", "arg2", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyWhenFourArgsAndNpcButUnknownSubCommand() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = admin.onTabComplete(player, command, "admin",
        new String[]{"npc", "unknown", "some-id", "partial"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onCommandExecutesSubCommandWhenValidSubCommandProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    boolean result = admin.onCommand(player, command, "admin", new String[]{"ping"});

    assertTrue(result);
  }

  @Test
  void resolvePlayerCoordinateReturnsPlaceholderForUnknownAxis() {
    String result = admin.resolvePlayerCoordinate(player, "unknown");
    assertEquals("<unknown>", result);
  }
}