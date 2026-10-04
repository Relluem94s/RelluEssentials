package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.util.List;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BagsTest {

  private static final String MESSAGE = "translated-message";

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private ServiceContext serviceContext;

  @Mock
  private Command command;

  @Mock
  private CommandSender nonPlayerSender;

  @Mock
  private Player player;

  @Mock
  private World world;

  @Mock
  private Inventory inventory;

  private Bags bags;

  @BeforeEach
  void setUp() {
    bags = new Bags();
    bags.injectContext(serviceContext);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] commands = bags.getCommands();

    assertNotNull(commands);
    assertEquals(0, commands.length);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsUnauthorized() {
    List<String> suggestions =
        bags.onTabComplete(player, command, "bags", new String[]{"a"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
    verify(serviceContext.getPlayerService(), never()).getPlayerEntry(player);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    when(serviceContext.getGroupService().isSenderAuthorized(nonPlayerSender, "user"))
        .thenReturn(true);

    List<String> suggestions =
        bags.onTabComplete(nonPlayerSender, command, "bags", new String[]{"a"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgumentIsProvided() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "user"))
        .thenReturn(true);

    List<String> suggestions =
        bags.onTabComplete(player, command, "bags", new String[]{"a", "b"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1})
  void onTabCompleteReturnsPlayerBagNamesForSupportedArgumentCounts(int argumentCount) {
    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(42);
    when(serviceContext.getGroupService().isSenderAuthorized(player, "user"))
        .thenReturn(true);
    when(serviceContext.getPlayerService().getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getBagService().getBagTypeNamesForPlayer(42))
        .thenReturn(List.of("Mining", "Building"));

    List<String> suggestions =
        bags.onTabComplete(player, command, "bags", new String[argumentCount]);

    assertEquals(List.of("Mining", "Building"), suggestions);
  }

  @Test
  void onCommandSendsNotAnPlayerMessageForNonPlayerSender() {
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(MESSAGE);

    boolean result = bags.onCommand(nonPlayerSender, command, "bags", new String[]{});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsUnauthorized() {
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(MESSAGE);

    boolean result = bags.onCommand(player, command, "bags", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(MESSAGE);
    verify(player, never()).getWorld();
  }

  @Test
  void onCommandSendsPermissionMissingWhenBagsAreDisabled() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "user"))
        .thenReturn(true);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(MESSAGE);

    boolean result = bags.onCommand(player, command, "bags", new String[]{});

    assertTrue(result);
    verify(serviceContext.getWorldGroupService())
        .isSettingActiveForWorld(WorldSetting.COLLECT_BAG, "world");
    verify(player).sendMessage(MESSAGE);
    verify(player, never()).openInventory(inventory);
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 2})
  void onCommandOpensBagSelectionWhenArgumentCountIsNotOne(int argumentCount) {
    PlayerEntry playerEntry = configureEnabledBags();
    when(serviceContext.getPlayerService().getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getBagService().getBagsInventory(playerEntry)).thenReturn(inventory);

    boolean result = bags.onCommand(player, command, "bags", new String[argumentCount]);

    assertTrue(result);
    verify(player).openInventory(inventory);
  }

  @Test
  void onCommandOpensOwnedBagFoundById() {
    PlayerEntry playerEntry = configureEnabledBags();
    BagTypeEntry bagType = bagTypeWithId();
    when(serviceContext.getBagService().findBagTypeById(7))
        .thenReturn(Optional.of(bagType));
    when(serviceContext.getPlayerService().getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getBagService().hasBag(42, 7)).thenReturn(true);
    when(serviceContext.getBagService().getBagInventory(7, playerEntry))
        .thenReturn(inventory);

    boolean result = bags.onCommand(player, command, "bags", new String[]{"7"});

    assertTrue(result);
    verify(player).openInventory(inventory);
  }

  @Test
  void onCommandOpensOwnedBagFoundByPartialName() {
    PlayerEntry playerEntry = configureEnabledBags();
    BagTypeEntry bagType = bagTypeWithId();
    when(serviceContext.getBagService().findBagTypeByPartialName("Mining"))
        .thenReturn(Optional.of(bagType));
    when(serviceContext.getPlayerService().getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getBagService().hasBag(42, 7)).thenReturn(true);
    when(serviceContext.getBagService().getBagInventory(7, playerEntry))
        .thenReturn(inventory);

    boolean result = bags.onCommand(player, command, "bags", new String[]{"Mining"});

    assertTrue(result);
    verify(player).openInventory(inventory);
  }

  @Test
  void onCommandSendsNotFoundWhenPlayerDoesNotOwnBag() {
    PlayerEntry playerEntry = configureEnabledBags();
    when(serviceContext.getBagService().findBagTypeById(7))
        .thenReturn(Optional.of(bagTypeWithId()));
    when(serviceContext.getPlayerService().getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_BAGS_NOT_FOUND, "7")).thenReturn(MESSAGE);

    boolean result = bags.onCommand(player, command, "bags", new String[]{"7"});

    assertTrue(result);
    verify(serviceContext.getBagService()).hasBag(42, 7);
    verify(player).sendMessage(MESSAGE);
    verify(player, never()).openInventory(inventory);
  }

  @ParameterizedTest
  @ValueSource(strings = {"7", "Missing"})
  void onCommandSendsNotFoundWhenBagTypeDoesNotExist(String argument) {
    configureEnabledBags();
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_BAGS_NOT_FOUND, argument)).thenReturn(MESSAGE);

    boolean result = bags.onCommand(player, command, "bags", new String[]{argument});

    assertTrue(result);
    verify(player).sendMessage(MESSAGE);
    verify(player, never()).openInventory(inventory);
  }

  private PlayerEntry configureEnabledBags() {
    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(42);
    when(serviceContext.getGroupService().isSenderAuthorized(player, "user"))
        .thenReturn(true);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(serviceContext.getWorldGroupService()
        .isSettingActiveForWorld(WorldSetting.COLLECT_BAG, "world")).thenReturn(true);
    return playerEntry;
  }

  private BagTypeEntry bagTypeWithId() {
    BagTypeEntry bagType = new BagTypeEntry();
    bagType.setId(7);
    return bagType;
  }
}