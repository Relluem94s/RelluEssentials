package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PingCommandTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private TranslationService translationService;

  @Mock
  private ServerService serverService;

  @Mock
  private Player player;

  private PingCommand pingCommand;

  private static final String TRANSLATED_MESSAGE = "translated-message";

  @BeforeEach
  void setUp() {
    pingCommand = new PingCommand(serviceContext);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
  }

  @Test
  void executeSendsOwnPingWhenNoTargetArgProvided() {
    when(player.getPing()).thenReturn(42);
    when(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_PING, 42)).thenReturn(TRANSLATED_MESSAGE);

    pingCommand.execute(player, new String[]{"ping"});

    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void executeSendsTargetNotFoundWhenTargetPlayerIsNull() {
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.getPlayer("UnknownPlayer")).thenReturn(null);
    when(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_PING_OTHER_NOT_FOUND, "UnknownPlayer")).thenReturn(TRANSLATED_MESSAGE);

    pingCommand.execute(player, new String[]{"ping", "UnknownPlayer"});

    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @Test
  void executeSendsTargetPingWhenTargetPlayerIsFound() {
    Player targetPlayer = mock(Player.class);

    when(serviceContext.getServerService()).thenReturn(serverService);
    when(serverService.getPlayer("TargetPlayer")).thenReturn(targetPlayer);
    when(targetPlayer.getCustomName()).thenReturn("TargetPlayer");
    when(targetPlayer.getPing()).thenReturn(99);
    when(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_PING_OTHER, "TargetPlayer", 99)).thenReturn(TRANSLATED_MESSAGE);

    pingCommand.execute(player, new String[]{"ping", "TargetPlayer"});

    verify(player).sendMessage(TRANSLATED_MESSAGE);
  }

  @ParameterizedTest
  @ValueSource(strings = {"ping", "PING", "Ping"})
  void matchesReturnsTrueWhenArgIsPingWithOneArg(String commandName) {
    boolean result = pingCommand.matches(new String[]{commandName});

    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"ping", "PING", "Ping"})
  void matchesReturnsTrueWhenArgIsPingWithTwoArgs(String commandName) {
    boolean result = pingCommand.matches(new String[]{commandName, "TargetPlayer"});

    assertTrue(result);
  }

  @Test
  void matchesReturnsFalseWhenArgIsNotPing() {
    boolean result = pingCommand.matches(new String[]{"notping"});

    assertFalse(result);
  }

  @Test
  void matchesReturnsFalseWhenNoArgsProvided() {
    boolean result = pingCommand.matches(new String[]{});

    assertFalse(result);
  }

  @Test
  void matchesReturnsFalseWhenMoreThanTwoArgsProvided() {
    boolean result = pingCommand.matches(new String[]{"ping", "TargetPlayer", "extraArg"});

    assertFalse(result);
  }
}