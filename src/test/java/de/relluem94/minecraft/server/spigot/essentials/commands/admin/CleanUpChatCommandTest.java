package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CleanUpChatCommandTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private ServerService serverService;

  @Mock
  private Player executingPlayer;

  private CleanUpChatCommand cleanUpChatCommand;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    cleanUpChatCommand = new CleanUpChatCommand(serviceContext);

    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void executeSends100EmptyMessagesToEachOnlinePlayer() {
    Player firstOnlinePlayer = mock(Player.class);
    Player secondOnlinePlayer = mock(Player.class);

    doReturn(List.of(firstOnlinePlayer, secondOnlinePlayer)).when(serverService).getOnlinePlayers();
    when(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CHAT_CLEARED)).thenReturn(TRANSLATED_MESSAGE);
    cleanUpChatCommand.execute(executingPlayer, new String[]{"chat"});

    verify(firstOnlinePlayer, times(100)).sendMessage("");
    verify(secondOnlinePlayer, times(100)).sendMessage("");
  }

  @Test
  void executeSendsChatClearedMessageToExecutingPlayer() {
    when(serverService.getOnlinePlayers()).thenReturn(List.of());
    when(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CHAT_CLEARED)).thenReturn(TRANSLATED_MESSAGE);

    cleanUpChatCommand.execute(executingPlayer, new String[]{"chat"});

    verify(executingPlayer).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void executeDoesNotSendEmptyMessagesWhenNoPlayersAreOnline() {
    when(serverService.getOnlinePlayers()).thenReturn(List.of());
    when(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CHAT_CLEARED)).thenReturn(TRANSLATED_MESSAGE);

    cleanUpChatCommand.execute(executingPlayer, new String[]{"chat"});

    verify(executingPlayer, never()).sendMessage("");
  }

  @Test
  void executeSends100EmptyMessagesToSingleOnlinePlayer() {
    Player onlinePlayer = mock(Player.class);

    doReturn(List.of(onlinePlayer)).when(serverService).getOnlinePlayers();
    when(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CHAT_CLEARED)).thenReturn(TRANSLATED_MESSAGE);

    cleanUpChatCommand.execute(executingPlayer, new String[]{"chat"});

    verify(onlinePlayer, times(100)).sendMessage("");
  }

  @Test
  void matchesReturnsTrueWhenArgIsChatCommandName() {
    String chatCommandName = Admin.Commands.CHAT.getName();

    boolean result = cleanUpChatCommand.matches(new String[]{chatCommandName});

    assertTrue(result);
  }

  @Test
  void matchesReturnsTrueWhenArgIsChatCommandNameUpperCase() {
    String chatCommandNameUpperCase = Admin.Commands.CHAT.getName().toUpperCase();

    boolean result = cleanUpChatCommand.matches(new String[]{chatCommandNameUpperCase});

    assertTrue(result);
  }

  @Test
  void matchesReturnsFalseWhenNoArgsProvided() {
    boolean result = cleanUpChatCommand.matches(new String[]{});

    assertFalse(result);
  }

  @Test
  void matchesReturnsFalseWhenMoreThanOneArgProvided() {
    boolean result = cleanUpChatCommand.matches(new String[]{Admin.Commands.CHAT.getName(), "extra"});

    assertFalse(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"unknown", "fly", "heal", "", " "})
  void matchesReturnsFalseWhenArgDoesNotMatchChatCommandName(String invalidArg) {
    boolean result = cleanUpChatCommand.matches(new String[]{invalidArg});

    assertFalse(result);
  }
}