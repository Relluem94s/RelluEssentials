package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignTest {

  private static final String MESSAGE = "translated-message";

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private ServiceContext serviceContext;

  @Mock
  private Command command;

  @Mock
  private CommandSender nonPlayerSender;

  @Mock
  private Player player;

  private Sign sign;

  @BeforeEach
  void setUp() {
    sign = new Sign();
    sign.injectContext(serviceContext);
  }

  @Test
  void getCommandsReturnsBothSubCommands() {
    CommandsEnum[] commands = sign.getCommands();

    assertNotNull(commands);
    assertEquals(2, commands.length);
  }

  @Test
  void commandsEnumEditHasCorrectName() {
    assertEquals("edit", Sign.Commands.EDIT.getName());
  }

  @Test
  void commandsEnumCopyHasCorrectName() {
    assertEquals("copy", Sign.Commands.COPY.getName());
  }

  @Test
  void commandsEnumEditHasEmptySubCommands() {
    assertNotNull(Sign.Commands.EDIT.getSubCommands());
    assertEquals(0, Sign.Commands.EDIT.getSubCommands().length);
  }

  @Test
  void commandsEnumCopyHasEmptySubCommands() {
    assertNotNull(Sign.Commands.COPY.getSubCommands());
    assertEquals(0, Sign.Commands.COPY.getSubCommands().length);
  }

  @Test
  void onCommandSendsNotAnPlayerMessageForNonPlayerSender() {
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(MESSAGE);

    boolean result = sign.onCommand(nonPlayerSender, command, "sign", new String[]{});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsUnauthorized() {
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(MESSAGE);

    boolean result = sign.onCommand(player, command, "sign", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(MESSAGE);
  }

  @Test
  void onCommandSendsSignInfoWhenNoArgumentsProvided() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SIGN_INFO, "sign", "copy", "edit"))
        .thenReturn(MESSAGE);

    boolean result = sign.onCommand(player, command, "sign", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(MESSAGE);
  }

  @Test
  void onCommandSendsTooManyArgumentsWhenMoreThanOneArgumentIsProvided() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS)).thenReturn(MESSAGE);

    boolean result = sign.onCommand(player, command, "sign", new String[]{"edit", "extra"});

    assertTrue(result);
    verify(player).sendMessage(MESSAGE);
  }

  @Test
  void onCommandSetsPlayerStateToSignEditWhenEditArgumentProvided() {
    PlayerEntry playerEntry = new PlayerEntry();
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getPlayerService().getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SIGN_EDIT)).thenReturn(MESSAGE);

    boolean result = sign.onCommand(player, command, "sign", new String[]{"edit"});

    assertTrue(result);
    assertEquals(PlayerState.SIGN_EDIT, playerEntry.getPlayerState());
    verify(player).sendMessage(MESSAGE);
  }

  @Test
  void onCommandSetsPlayerStateToSignEditWhenEditArgumentProvidedCaseInsensitive() {
    PlayerEntry playerEntry = new PlayerEntry();
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getPlayerService().getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SIGN_EDIT)).thenReturn(MESSAGE);

    boolean result = sign.onCommand(player, command, "sign", new String[]{"EDIT"});

    assertTrue(result);
    assertEquals(PlayerState.SIGN_EDIT, playerEntry.getPlayerState());
    verify(player).sendMessage(MESSAGE);
  }

  @Test
  void onCommandSetsPlayerStateToSignCopyWhenCopyArgumentProvided() {
    PlayerEntry playerEntry = new PlayerEntry();
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getPlayerService().getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SIGN_COPY)).thenReturn(MESSAGE);

    boolean result = sign.onCommand(player, command, "sign", new String[]{"copy"});

    assertTrue(result);
    assertEquals(PlayerState.SIGN_COPY, playerEntry.getPlayerState());
    verify(player).sendMessage(MESSAGE);
  }

  @Test
  void onCommandSetsPlayerStateToSignCopyWhenCopyArgumentProvidedCaseInsensitive() {
    PlayerEntry playerEntry = new PlayerEntry();
    when(serviceContext.getGroupService().isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getPlayerService().getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SIGN_COPY)).thenReturn(MESSAGE);

    boolean result = sign.onCommand(player, command, "sign", new String[]{"COPY"});

    assertTrue(result);
    assertEquals(PlayerState.SIGN_COPY, playerEntry.getPlayerState());
    verify(player).sendMessage(MESSAGE);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsUnauthorized() {
    List<String> suggestions = sign.onTabComplete(player, command, "sign", new String[]{"a"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenSenderIsNotPlayer() {
    when(serviceContext.getGroupService().isSenderAuthorized(nonPlayerSender, "user"))
        .thenReturn(true);

    List<String> suggestions =
        sign.onTabComplete(nonPlayerSender, command, "sign", new String[]{"a"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 3})
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgumentIsProvided(int argumentCount) {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "user"))
        .thenReturn(true);

    List<String> suggestions =
        sign.onTabComplete(player, command, "sign", new String[argumentCount]);

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
    verify(serviceContext.getTabCompleterService(), never()).getCommands(sign.getCommands());
  }

  @Test
  void onTabCompleteReturnsSubCommandsWhenOneArgumentIsProvided() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "user"))
        .thenReturn(true);
    when(serviceContext.getTabCompleterService().getCommands(sign.getCommands()))
        .thenReturn(List.of("edit", "copy"));

    List<String> suggestions =
        sign.onTabComplete(player, command, "sign", new String[]{"e"});

    assertEquals(List.of("edit", "copy"), suggestions);
  }
}