package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CraftingBenchTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private GroupService groupService;

  @Mock
  private TranslationService translationService;

  @Mock
  private ServerService serverService;

  @Mock
  private CommandSender commandSender;

  @Mock
  private Player player;

  @Mock
  private Command command;

  @Mock
  private Inventory inventory;

  private CraftingBench craftingBench;

  @BeforeEach
  void setUp() {
    craftingBench = new CraftingBench() {
      @Override
      protected InventoryType getWorkbenchInventoryType() {
        return null;
      }
    };
    craftingBench.injectContext(serviceContext);
    lenient().when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void onCommandNonPlayerSenderSendsNotAPlayerMessage() {
    when(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn("not a player");

    boolean result = craftingBench.onCommand(commandSender, command, "craft", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(commandSender).sendMessage("not a player"),
        () -> verify(groupService, never()).isSenderAuthorized(any(), anyString())
    );
  }

  @Test
  void onCommandUnauthorizedPlayerSendsPermissionMissingMessage() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(false);
    when(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn("no permission");

    boolean result = craftingBench.onCommand(player, command, "craft", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage("no permission"),
        () -> verify(serverService, never()).createInventory(any(), any(), anyString())
    );
  }

  @Test
  void onCommandAuthorizedPlayerOpensWorkbenchInventory() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(translationService.get(any())).thenReturn("Crafting");
    when(translationService.getWithPrefix(any(), any())).thenReturn("opened");
    when(serverService.createInventory(any(), any(), any())).thenReturn(inventory);

    boolean result = craftingBench.onCommand(player, command, "craft", new String[]{});

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).openInventory(inventory),
        () -> verify(player).sendMessage("opened")
    );
  }


  @Test
  void onTabCompleteReturnsEmptyList() {
    List<String> result = craftingBench.onTabComplete(commandSender, command, "craft", new String[]{});
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] commands = craftingBench.getCommands();

    assertAll(
        () -> assertNotNull(commands),
        () -> assertEquals(0, commands.length)
    );
  }
}