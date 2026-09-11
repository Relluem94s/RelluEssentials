package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnderchestTest {

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

  @Mock
  private Inventory enderChestInventory;

  private Enderchest enderchest;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    enderchest = new Enderchest();
    enderchest.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = enderchest.getCommands();

    assertAll(
        () -> assertEquals(0, result.length)
    );
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = enderchest.onCommand(nonPlayerSender, command, "enderchest", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(nonPlayerSender).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsNotVip() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = enderchest.onCommand(player, command, "enderchest", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE),
        () -> verify(player, never()).openInventory(enderChestInventory)
    );
  }

  @Test
  void onCommandOpensSelfEnderChestWhenNoArgsAndPlayerIsVip() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(player.getEnderChest()).thenReturn(enderChestInventory);
    when(translationService.getWithPrefix(MessageKey.COMMAND_ENDERCHEST)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = enderchest.onCommand(player, command, "enderchest", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).openInventory(enderChestInventory),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onCommandSendsTargetNotAPlayerWhenTargetNotFound() {
    String targetName = "UnknownPlayer";
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer(targetName)).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, targetName)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = enderchest.onCommand(player, command, "enderchest", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE),
        () -> verify(player, never()).openInventory(enderChestInventory)
    );
  }

  @Test
  void onCommandSendsTargetNotAPlayerWhenTargetPlayerIsNull() {
    String targetName = "OfflinePlayer";
    Player offlinePlayerEntity = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer(targetName)).thenReturn(offlinePlayerEntity);
    when(offlinePlayerEntity.getPlayer()).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, targetName)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = enderchest.onCommand(player, command, "enderchest", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE),
        () -> verify(player, never()).openInventory(enderChestInventory)
    );
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsNotModAndTargetExists() {
    String targetName = "TargetPlayer";
    Player targetPlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(serverService.getPlayer(targetName)).thenReturn(targetPlayer);
    when(targetPlayer.getPlayer()).thenReturn(targetPlayer);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = enderchest.onCommand(player, command, "enderchest", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE),
        () -> verify(player, never()).openInventory(enderChestInventory)
    );
  }

  @Test
  void onCommandOpensTargetEnderChestWhenPlayerIsModAndTargetExists() {
    String targetName = "TargetPlayer";
    String targetCustomName = "TargetCustomName";
    Player targetPlayer = mock(Player.class);
    Inventory targetEnderChest = mock(Inventory.class);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer(targetName)).thenReturn(targetPlayer);
    when(targetPlayer.getPlayer()).thenReturn(targetPlayer);
    when(targetPlayer.getEnderChest()).thenReturn(targetEnderChest);
    when(targetPlayer.getCustomName()).thenReturn(targetCustomName);
    when(translationService.getWithPrefix(MessageKey.COMMAND_ENDERCHEST_PLAYER, targetCustomName)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = enderchest.onCommand(player, command, "enderchest", new String[]{targetName});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).openInventory(targetEnderChest),
        () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
    );
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotMod() {
    CommandSender nonModSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(nonModSender, "mod")).thenReturn(false);

    List<String> result = enderchest.onTabComplete(nonModSender, command, "enderchest", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    CommandSender modSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(modSender, "mod")).thenReturn(true);

    List<String> result = enderchest.onTabComplete(modSender, command, "enderchest", new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayersWhenSenderIsModAndOneArg() {
    CommandSender modSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(modSender, "mod")).thenReturn(true);

    List<String> result = enderchest.onTabComplete(modSender, command, "enderchest", new String[]{"partial"});

    assertNotNull(result);
  }
}