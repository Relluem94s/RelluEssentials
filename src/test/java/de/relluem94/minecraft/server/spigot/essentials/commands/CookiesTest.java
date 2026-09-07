package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CookiesTest {

  private static final String TRANSLATED_MESSAGE = "translated-message";
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
  private Cookies cookies;

  @BeforeEach
  void setUp() {
    cookies = new Cookies();
    cookies.injectContext(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] result = cookies.getCommands();

    assertAll(() -> assertNotNull(result), () -> assertEquals(0, result.length));
  }

  @Test
  void onCommandSendsPermissionMissingWhenSenderIsPlayerAndNotVip() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(
        TRANSLATED_MESSAGE);

    boolean result = cookies.onCommand(player, command, "cookie", new String[]{});

    assertAll(() -> assertTrue(result), () -> verify(player).sendMessage(TRANSLATED_MESSAGE));
  }

  @Test
  void onCommandSendsNotAPlayerWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(
        TRANSLATED_MESSAGE);

    boolean result = cookies.onCommand(nonPlayerSender, command, "cookie", new String[]{});

    assertAll(() -> assertTrue(result),
        () -> verify(nonPlayerSender).sendMessage(TRANSLATED_MESSAGE));
  }

  @Test
  void onCommandSendsTargetNotAPlayerWhenOneArgAndTargetNotFound() {
    String targetName = "UnknownPlayer";
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer(targetName)).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER,
        targetName)).thenReturn(TRANSLATED_MESSAGE);

    boolean result = cookies.onCommand(player, command, "cookie", new String[]{targetName});

    assertAll(() -> assertTrue(result), () -> verify(player).sendMessage(TRANSLATED_MESSAGE));
  }

  @Test
  void onCommandSendsGiftMessageAndDropsCookieWhenOneArgAndTargetExists() {
    String targetName = "TargetPlayer";
    String targetCustomName = "TargetCustomName";
    Player targetPlayer = mock(Player.class);
    ItemFactory itemFactory = mock(ItemFactory.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    World world = mock(World.class);
    Location location = mock(Location.class);

    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serverService.getPlayer(targetName)).thenReturn(targetPlayer);
    when(targetPlayer.getCustomName()).thenReturn(targetCustomName);
    when(targetPlayer.getWorld()).thenReturn(world);
    when(targetPlayer.getLocation()).thenReturn(location);
    when(translationService.get(MessageKey.COMMAND_COOKIES_PLAYER, targetCustomName)).thenReturn(TRANSLATED_MESSAGE);
    when(itemFactory.getItemMeta(any())).thenReturn(itemMeta);

    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getItemFactory).thenReturn(itemFactory);

      boolean result = cookies.onCommand(player, command, "cookie", new String[]{targetName});

      assertAll(
          () -> assertTrue(result),
          () -> verify(player).sendMessage(TRANSLATED_MESSAGE)
      );
    }
  }


  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotVip() {
    CommandSender nonVipSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(nonVipSender, "vip")).thenReturn(false);

    List<String> result = cookies.onTabComplete(nonVipSender, command, "cookie", new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    CommandSender nonPlayerSender = mock(CommandSender.class);
    when(groupService.isSenderAuthorized(nonPlayerSender, "vip")).thenReturn(true);

    List<String> result = cookies.onTabComplete(nonPlayerSender, command, "cookie",
        new String[]{"a"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgProvided() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);

    List<String> result = cookies.onTabComplete(player, command, "cookie",
        new String[]{"arg1", "arg2"});

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void onTabCompleteReturnsOnlinePlayersWhenSenderIsVipPlayerAndOneArg() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);

    List<String> result = cookies.onTabComplete(player, command, "cookie", new String[]{"partial"});

    assertNotNull(result);
  }

  @Test
  void onCommandDropsCookieToNearestPlayerWhenSenderIsCommandBlockWithAtP() {
    BlockCommandSender blockCommandSender = mock(BlockCommandSender.class);
    Block block = mock(Block.class);
    CommandBlock commandBlock = mock(CommandBlock.class);
    ItemFactory itemFactory = mock(ItemFactory.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    World world = mock(World.class);
    Location location = mock(Location.class);

    when(blockCommandSender.getBlock()).thenReturn(block);
    when(block.getState()).thenReturn(commandBlock);
    when(commandBlock.getBlock()).thenReturn(block);
    when(block.getLocation()).thenReturn(location);
    when(player.getWorld()).thenReturn(world);
    when(player.getLocation()).thenReturn(location);
    when(itemFactory.getItemMeta(any())).thenReturn(itemMeta);

    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class);
        MockedStatic<PlayerHelper> playerHelper = mockStatic(PlayerHelper.class)) {
      bukkit.when(Bukkit::getItemFactory).thenReturn(itemFactory);
      playerHelper.when(() -> PlayerHelper.getTargetedPlayer(location)).thenReturn(player);

      boolean result = cookies.onCommand(blockCommandSender, command, "cookie", new String[]{"@p"});

      assertAll(
          () -> assertTrue(result),
          () -> verify(world).dropItem(any(), any())
      );
    }
  }

  @Test
  void onCommandSendsNoPlayerInReachWhenSenderIsCommandBlockWithAtPAndNoPlayerNearby() {
    BlockCommandSender blockCommandSender = mock(BlockCommandSender.class);
    Block block = mock(Block.class);
    CommandBlock commandBlock = mock(CommandBlock.class);
    Location location = mock(Location.class);

    when(blockCommandSender.getBlock()).thenReturn(block);
    when(block.getState()).thenReturn(commandBlock);
    when(commandBlock.getBlock()).thenReturn(block);
    when(block.getLocation()).thenReturn(location);
    when(translationService.get(MessageKey.COMMAND_NO_PLAYER_IN_REACH)).thenReturn("no-player-in-reach");
    when(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, "no-player-in-reach")).thenReturn(TRANSLATED_MESSAGE);

    try (MockedStatic<PlayerHelper> playerHelper = mockStatic(PlayerHelper.class)) {
      playerHelper.when(() -> PlayerHelper.getTargetedPlayer(location)).thenReturn(null);

      boolean result = cookies.onCommand(blockCommandSender, command, "cookie", new String[]{"@p"});

      assertAll(
          () -> assertTrue(result),
          () -> verify(blockCommandSender).sendMessage(TRANSLATED_MESSAGE)
      );
    }
  }
}