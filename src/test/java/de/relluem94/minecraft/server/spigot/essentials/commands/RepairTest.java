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
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RepairTest {

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
  private PlayerInventory playerInventory;

  private Repair repair;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    repair = new Repair();
    repair.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = repair.getCommands();

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  void onCommandSendsNotAPlayerMessageWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = repair.onCommand(nonPlayerSender, command, "repair", new String[]{});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsNotMod() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = repair.onCommand(player, command, "repair", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandRepairsSelfItemInMainHandWhenDamageable() {
    ItemStack item = mock(ItemStack.class);
    Damageable damageable = mock(Damageable.class);

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItemInMainHand()).thenReturn(item);
    when(item.getItemMeta()).thenReturn(damageable);
    when(damageable.hasDamage()).thenReturn(true);
    when(item.getType()).thenReturn(Material.DIAMOND_SWORD);
    when(translationService.getWithPrefix(MessageKey.COMMAND_REPAIR, Material.DIAMOND_SWORD.name()))
        .thenReturn(TRANSLATED_MESSAGE);

    boolean result = repair.onCommand(player, command, "repair", new String[]{});

    assertTrue(result);
    verify(damageable).setDamage(0);
    verify(item).setItemMeta(damageable);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsCannotRepairMessageWhenSelfItemIsNotDamageable() {
    ItemStack item = mock(ItemStack.class);
    ItemMeta nonDamageableMeta = mock(ItemMeta.class);

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItemInMainHand()).thenReturn(item);
    when(item.getItemMeta()).thenReturn(nonDamageableMeta);
    when(item.getType()).thenReturn(Material.DIRT);
    when(translationService.getWithPrefix(MessageKey.COMMAND_CANNOT_REPAIR, Material.DIRT.name()))
        .thenReturn(TRANSLATED_MESSAGE);

    boolean result = repair.onCommand(player, command, "repair", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsCannotRepairMessageWhenSelfItemHasNoDamage() {
    ItemStack item = mock(ItemStack.class);
    Damageable damageable = mock(Damageable.class);

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItemInMainHand()).thenReturn(item);
    when(item.getItemMeta()).thenReturn(damageable);
    when(damageable.hasDamage()).thenReturn(false);
    when(item.getType()).thenReturn(Material.DIAMOND_SWORD);
    when(translationService.getWithPrefix(MessageKey.COMMAND_CANNOT_REPAIR, Material.DIAMOND_SWORD.name()))
        .thenReturn(TRANSLATED_MESSAGE);

    boolean result = repair.onCommand(player, command, "repair", new String[]{});

    assertTrue(result);
    verify(damageable, never()).setDamage(0);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsTargetNotAPlayerMessageWhenTargetNotFound() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer("UnknownPlayer")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "UnknownPlayer"))
        .thenReturn(TRANSLATED_MESSAGE);

    boolean result = repair.onCommand(player, command, "repair", new String[]{"UnknownPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandRepairsTargetItemAndNotifiesBothPlayersWhenDamageable() {
    Player targetPlayer = mock(Player.class);
    PlayerInventory targetInventory = mock(PlayerInventory.class);
    ItemStack item = mock(ItemStack.class);
    Damageable damageable = mock(Damageable.class);

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);
    when(targetPlayer.getInventory()).thenReturn(targetInventory);
    when(targetInventory.getItemInMainHand()).thenReturn(item);
    when(item.getItemMeta()).thenReturn(damageable);
    when(damageable.hasDamage()).thenReturn(true);
    when(item.getType()).thenReturn(Material.DIAMOND_SWORD);
    when(translationService.getWithPrefix(MessageKey.COMMAND_REPAIR, Material.DIAMOND_SWORD.name()))
        .thenReturn("repair-sender-message");
    when(translationService.getWithPrefix(MessageKey.COMMAND_REPAIR_PLAYER, Material.DIAMOND_SWORD.name()))
        .thenReturn("repair-target-message");

    boolean result = repair.onCommand(player, command, "repair", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(damageable).setDamage(0);
    verify(item).setItemMeta(damageable);
    verify(player).sendMessage("repair-sender-message");
    verify(targetPlayer).sendMessage("repair-target-message");
  }

  @Test
  void onCommandSendsCannotRepairMessageToSenderWhenTargetItemIsNotDamageable() {
    Player targetPlayer = mock(Player.class);
    PlayerInventory targetInventory = mock(PlayerInventory.class);
    ItemStack item = mock(ItemStack.class);
    ItemMeta nonDamageableMeta = mock(ItemMeta.class);

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);
    when(targetPlayer.getInventory()).thenReturn(targetInventory);
    when(targetInventory.getItemInMainHand()).thenReturn(item);
    when(item.getItemMeta()).thenReturn(nonDamageableMeta);
    when(item.getType()).thenReturn(Material.DIRT);
    when(translationService.getWithPrefix(MessageKey.COMMAND_CANNOT_REPAIR, Material.DIRT.name()))
        .thenReturn(TRANSLATED_MESSAGE);

    boolean result = repair.onCommand(player, command, "repair", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(targetPlayer, never()).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onCommandSendsCannotRepairMessageToSenderWhenTargetItemHasNoDamage() {
    Player targetPlayer = mock(Player.class);
    PlayerInventory targetInventory = mock(PlayerInventory.class);
    ItemStack item = mock(ItemStack.class);
    Damageable damageable = mock(Damageable.class);

    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);
    when(targetPlayer.getInventory()).thenReturn(targetInventory);
    when(targetInventory.getItemInMainHand()).thenReturn(item);
    when(item.getItemMeta()).thenReturn(damageable);
    when(damageable.hasDamage()).thenReturn(false);
    when(item.getType()).thenReturn(Material.DIAMOND_SWORD);
    when(translationService.getWithPrefix(MessageKey.COMMAND_CANNOT_REPAIR, Material.DIAMOND_SWORD.name()))
        .thenReturn(TRANSLATED_MESSAGE);

    boolean result = repair.onCommand(player, command, "repair", new String[]{"TargetPlayer"});

    assertTrue(result);
    verify(damageable, never()).setDamage(0);
    verify(player).sendMessage(TRANSLATED_MESSAGE);
    verify(targetPlayer, never()).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotMod() {
    CommandSender unauthorizedSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(unauthorizedSender, "mod")).thenReturn(false);

    List<String> result = repair.onTabComplete(unauthorizedSender, command, "repair", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    CommandSender consoleSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(consoleSender, "mod")).thenReturn(true);

    List<String> result = repair.onTabComplete(consoleSender, command, "repair", new String[]{""});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);

    List<String> result = repair.onTabComplete(player, command, "repair", new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayerNamesWhenModWithOneArg() {
    Player onlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(onlinePlayer.getName()).thenReturn("OnlinePlayer");

    List<String> result = repair.onTabComplete(player, command, "repair", new String[]{"partial"});

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains("OnlinePlayer"));
  }

  @Test
  void onTabCompleteReturnsAllOnlinePlayersWhenModWithEmptyArg() {
    Player firstOnlinePlayer = mock(Player.class);
    Player secondOnlinePlayer = mock(Player.class);
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    doReturn(List.of(firstOnlinePlayer, secondOnlinePlayer)).when(serverService).getOnlinePlayers();
    when(firstOnlinePlayer.getName()).thenReturn("FirstPlayer");
    when(secondOnlinePlayer.getName()).thenReturn("SecondPlayer");

    List<String> result = repair.onTabComplete(player, command, "repair", new String[]{""});

    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains("FirstPlayer"));
    assertTrue(result.contains("SecondPlayer"));
  }
}