package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DayTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private GroupService groupService;

  @Mock
  private ServerService serverService;

  @Mock
  private Command command;

  @Mock
  private Player player;

  private Day day;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    day = new Day();
    day.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = day.getCommands();

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = day.onCommand(nonPlayerSender, command, "day", new String[]{});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsNotMod() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = day.onCommand(player, command, "day", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSetsTimeToZeroInPlayerWorldWhenNoArgsProvided() {
    World world = mock(World.class);

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TIME_DAY, "world")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = day.onCommand(player, command, "day", new String[]{});

    assertTrue(result);
    verify(world).setTime(0L);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsWorldNotLoadedWhenWorldArgIsUnknown() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getWorld("unknown_world")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_WORLD_NOT_LOADED, "unknown_world")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = day.onCommand(player, command, "day", new String[]{"unknown_world"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(serverService, never()).getWorlds();
  }

  @Test
  void onCommandSetsTimeToZeroInSpecifiedWorldWhenValidWorldArgProvided() {
    World world = mock(World.class);

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getWorld("nether")).thenReturn(world);
    when(world.getName()).thenReturn("nether");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TIME_DAY, "nether")).thenReturn(TRANSLATED_MESSAGE);

    boolean result = day.onCommand(player, command, "day", new String[]{"nether"});

    assertTrue(result);
    verify(world).setTime(0L);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotMod() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "mod")).thenReturn(false);

    List<String> result = day.onTabComplete(unauthorizedSender, command, "day", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = day.onTabComplete(player, command, "day", new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsWorldNamesWhenModWithOneArg() {
    World world = mock(World.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    doReturn(List.of(world)).when(serverService).getWorlds();
    when(world.getName()).thenReturn("world");

    List<String> result = day.onTabComplete(player, command, "day", new String[]{"wo"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains("world"));
  }

  @Test
  void onTabCompleteReturnsAllWorldNamesWhenModWithEmptyArg() {
    World firstWorld = mock(World.class);
    World secondWorld = mock(World.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    doReturn(List.of(firstWorld, secondWorld)).when(serverService).getWorlds();
    when(firstWorld.getName()).thenReturn("world");
    when(secondWorld.getName()).thenReturn("nether");

    List<String> result = day.onTabComplete(player, command, "day", new String[]{""});

    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains("world"));
    assertTrue(result.contains("nether"));
  }
}