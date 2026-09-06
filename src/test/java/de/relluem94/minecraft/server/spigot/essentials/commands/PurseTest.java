package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.CoinItemService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PurseTest {

  @Mock
  private ServiceContext serviceContext;
  @Mock
  private TranslationService translationService;
  @Mock
  private GroupService groupService;
  @Mock
  private PlayerService playerService;
  @Mock
  private CoinItemService coinItemService;
  @Mock
  private PluginMetadataService pluginMetadataService;
  @Mock
  private Plugin plugin;
  @Mock
  private Server server;
  @Mock
  private Player player;
  @Mock
  private Player targetPlayer;
  @Mock
  private CommandSender commandSender;
  @Mock
  private Command command;
  @Mock
  private PlayerEntry playerEntry;
  @Mock
  private PlayerInventory inventory;
  @Mock
  private ItemStack coinItemStack;

  private Purse purse;

  @BeforeEach
  void setUp() {
    purse = new Purse();
    purse.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
  }

  @Test
  void onCommandReturnsTrueWhenSenderIsNotPlayer() {
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(
        "not a player");

    boolean result = purse.onCommand(commandSender, command, "purse", new String[]{});

    assertAll(() -> assertTrue(result), () -> verify(commandSender).sendMessage("not a player"));
  }

  @Test
  void onCommandReturnsTrueWhenPlayerLacksUserPermission() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(
        "no permission");

    boolean result = purse.onCommand(player, command, "purse", new String[]{});

    assertAll(() -> assertTrue(result), () -> verify(player).sendMessage("no permission"));
  }

  @Test
  void onCommandDisplaysOwnBalanceWhenNoArgsProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getPurse()).thenReturn(100.0);
    String formatted = StringHelper.formatDouble(100.0);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PURSE_TOTAL, formatted)).thenReturn(
        "balance: 100");

    boolean result = purse.onCommand(player, command, "purse", new String[]{});

    assertAll(() -> assertTrue(result), () -> verify(player).sendMessage("balance: 100"));
  }

  @Test
  void onCommandDisplaysTargetBalanceWhenModViewsOtherPlayer() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getPlayer("targetName")).thenReturn(targetPlayer);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(targetPlayer)).thenReturn(playerEntry);
    when(playerEntry.getPurse()).thenReturn(200.0);
    when(targetPlayer.getCustomName()).thenReturn("targetName");
    String formatted = StringHelper.formatDouble(200.0);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PURSE_TOTAL_OTHER, "targetName",
        formatted)).thenReturn("targetName balance: 200");

    boolean result = purse.onCommand(player, command, "purse", new String[]{"targetName"});

    assertAll(() -> assertTrue(result),
        () -> verify(player).sendMessage("targetName balance: 200"));
  }

  @Test
  void onCommandDeniesTargetBalanceViewWhenNotMod() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getPlayer("targetName")).thenReturn(targetPlayer);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(
        "no permission");

    boolean result = purse.onCommand(player, command, "purse", new String[]{"targetName"});

    assertAll(() -> assertTrue(result), () -> verify(player).sendMessage("no permission"));
  }

  @Test
  void onCommandRejectsInvalidIntegerArgument() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getPlayer("notAnInt")).thenReturn(null);
    when(translationService.getWithPrefix(
        MessageKey.COMMAND_PURSE_TO_ITEM_VALUE_INVALID)).thenReturn("invalid value");

    boolean result = purse.onCommand(player, command, "purse", new String[]{"notAnInt"});

    assertAll(() -> assertTrue(result), () -> verify(player).sendMessage("invalid value"));
  }

  @Test
  void onCommandWithdrawsCoinsWhenSufficientBalance() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getPlayer("50")).thenReturn(null);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getPurse()).thenReturn(100.0);
    when(playerEntry.getId()).thenReturn(1);
    when(serviceContext.getCoinItemService()).thenReturn(coinItemService);
    when(coinItemService.getCoin(50)).thenReturn(coinItemStack);
    when(player.getInventory()).thenReturn(inventory);
    String formatted = StringHelper.formatInt(50);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PURSE_TO_ITEM, formatted)).thenReturn(
        "withdrew 50");

    boolean result = purse.onCommand(player, command, "purse", new String[]{"50"});

    assertAll(() -> assertTrue(result), () -> verify(playerEntry).setPurse(50.0),
        () -> verify(playerEntry).setHasToBeUpdated(true),
        () -> verify(playerEntry).setUpdatedBy(1), () -> verify(inventory).addItem(coinItemStack),
        () -> verify(player).sendMessage("withdrew 50"));
  }

  @Test
  void onCommandRejectsWithdrawalWhenInsufficientBalance() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getPlayer("200")).thenReturn(null);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getPurse()).thenReturn(100.0);
    when(translationService.getWithPrefix(
        MessageKey.COMMAND_PURSE_TO_ITEM_NOT_ENOUGH_MONEY)).thenReturn("not enough money");

    boolean result = purse.onCommand(player, command, "purse", new String[]{"200"});

    assertAll(() -> assertTrue(result), () -> verify(player).sendMessage("not enough money"),
        () -> verify(playerEntry, never()).setPurse(any(Double.class)));
  }

  @Test
  void onCommandWithdrawsAbsoluteValueWhenNegativeIntegerProvided() {
    when(groupService.isSenderAuthorized(player, "user")).thenReturn(true);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(server.getPlayer("-30")).thenReturn(null);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getPurse()).thenReturn(100.0);
    when(playerEntry.getId()).thenReturn(1);
    when(serviceContext.getCoinItemService()).thenReturn(coinItemService);
    when(coinItemService.getCoin(30)).thenReturn(coinItemStack);
    when(player.getInventory()).thenReturn(inventory);
    String formatted = StringHelper.formatInt(30);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PURSE_TO_ITEM, formatted)).thenReturn(
        "withdrew 30");

    boolean result = purse.onCommand(player, command, "purse", new String[]{"-30"});

    assertAll(() -> assertTrue(result), () -> verify(playerEntry).setPurse(70.0),
        () -> verify(inventory).addItem(coinItemStack));
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] commands = purse.getCommands();

    assertAll(() -> assertNotNull(commands), () -> assertEquals(0, commands.length));
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenNotMod() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(false);

    List<String> result = purse.onTabComplete(commandSender, command, "purse", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgument() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(true);

    List<String> result = purse.onTabComplete(commandSender, command, "purse",
        new String[]{"a", "b"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayerNamesWhenModAndOneArg() {
    when(groupService.isSenderAuthorized(commandSender, "mod")).thenReturn(true);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(targetPlayer.getName()).thenReturn("targetName");
    doReturn(List.of(targetPlayer)).when(server.getOnlinePlayers());

    List<String> result = purse.onTabComplete(commandSender, command, "purse", new String[]{"t"});

    assertNotNull(result);
    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertEquals("targetName", result.getFirst())
    );
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  void onCommandThrowsNullPointerExceptionWhenSenderIsNull() {
    assertThrows(NullPointerException.class,
        () -> purse.onCommand(null, command, "purse", new String[]{}));
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  void onCommandThrowsNullPointerExceptionWhenLabelIsNull() {
    assertThrows(NullPointerException.class,
        () -> purse.onCommand(commandSender, command, null, new String[]{}));
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  void onCommandReturnsTrueWhenCommandIsNull() {
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(
        "not a player");

    boolean result = purse.onCommand(commandSender, null, "purse", new String[]{});

    assertAll(() -> assertTrue(result), () -> verify(commandSender).sendMessage("not a player"));
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  void constructorThrowsNullPointerExceptionWhenServiceContextIsNull() {
    assertThrows(NullPointerException.class, () -> new CoinItemService(null));
  }
}