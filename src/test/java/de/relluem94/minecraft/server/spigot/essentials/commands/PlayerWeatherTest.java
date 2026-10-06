package de.relluem94.minecraft.server.spigot.essentials.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.List;
import org.bukkit.WeatherType;
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
class PlayerWeatherTest {

  private static final String MESSAGE = "translated-message";

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private ServiceContext serviceContext;

  @Mock
  private Command command;

  @Mock
  private CommandSender nonPlayerSender;

  @Mock
  private Player player;

  private PlayerWeather playerWeather;

  @BeforeEach
  void setUp() {
    playerWeather = new PlayerWeather();
    playerWeather.injectContext(serviceContext);
  }

  @Test
  void getCommandsReturnsEmptyArray() {
    CommandsEnum[] commands = playerWeather.getCommands();

    assertNotNull(commands);
    assertEquals(0, commands.length);
  }

  @Test
  void onCommandSendsNotAnPlayerMessageForNonPlayerSender() {
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER)).thenReturn(MESSAGE);

    boolean result = playerWeather.onCommand(nonPlayerSender, command, "playerweather", new String[]{});

    assertTrue(result);
    verify(nonPlayerSender).sendMessage(MESSAGE);
  }

  @Test
  void onCommandSendsPermissionMissingWhenPlayerIsUnauthorized() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "vip"))
        .thenReturn(false);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING)).thenReturn(MESSAGE);

    boolean result = playerWeather.onCommand(player, command, "playerweather", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(MESSAGE);
    verify(player, never()).setPlayerWeather(WeatherType.CLEAR);
  }

  @Test
  void onCommandSendsTooFewArgumentsWhenNoArgumentsProvided() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "vip"))
        .thenReturn(true);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS)).thenReturn(MESSAGE);

    boolean result = playerWeather.onCommand(player, command, "playerweather", new String[]{});

    assertTrue(result);
    verify(player).sendMessage(MESSAGE);
    verify(player, never()).setPlayerWeather(WeatherType.CLEAR);
  }

  @Test
  void onCommandSendsTooManyArgumentsWhenMoreThanOneArgumentProvided() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "vip"))
        .thenReturn(true);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS)).thenReturn(MESSAGE);

    boolean result = playerWeather.onCommand(player, command, "playerweather", new String[]{"CLEAR", "DOWNFALL"});

    assertTrue(result);
    verify(player).sendMessage(MESSAGE);
    verify(player, never()).setPlayerWeather(WeatherType.CLEAR);
  }

  @ParameterizedTest
  @ValueSource(strings = {"CLEAR", "clear", "Clear", "DOWNFALL", "downfall", "Downfall"})
  void onCommandSetsPlayerWeatherForValidWeatherType(String weatherInput) {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "vip"))
        .thenReturn(true);

    boolean result = playerWeather.onCommand(player, command, "playerweather", new String[]{weatherInput});

    assertTrue(result);
    verify(player).setPlayerWeather(WeatherType.valueOf(weatherInput.toUpperCase()));
  }

  @Test
  void onCommandSendsWrongSubCommandForInvalidWeatherType() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "vip"))
        .thenReturn(true);
    when(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND)).thenReturn(MESSAGE);

    boolean result = playerWeather.onCommand(player, command, "playerweather", new String[]{"INVALID"});

    assertTrue(result);
    verify(player).sendMessage(MESSAGE);
    verify(player, never()).setPlayerWeather(WeatherType.CLEAR);
  }

  @Test
  void onTabCompleteReturnsEmptyListForNonPlayerSender() {
    List<String> suggestions = playerWeather.onTabComplete(nonPlayerSender, command, "playerweather", new String[]{"C"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenPlayerIsUnauthorized() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "vip"))
        .thenReturn(false);

    List<String> suggestions = playerWeather.onTabComplete(player, command, "playerweather", new String[]{"C"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }

  @Test
  void onTabCompleteReturnsWeatherTypesWhenFirstArgumentIsBeingTyped() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "vip"))
        .thenReturn(true);
    when(serviceContext.getTabCompleterService().getWeatherTypes())
        .thenReturn(List.of("CLEAR", "DOWNFALL"));

    List<String> suggestions = playerWeather.onTabComplete(player, command, "playerweather", new String[]{"C"});

    assertNotNull(suggestions);
    assertEquals(List.of("CLEAR", "DOWNFALL"), suggestions);
  }

  @Test
  void onTabCompleteReturnsEmptyListWhenMoreThanOneArgumentIsProvided() {
    when(serviceContext.getGroupService().isSenderAuthorized(player, "vip"))
        .thenReturn(true);

    List<String> suggestions = playerWeather.onTabComplete(player, command, "playerweather", new String[]{"CLEAR", "extra"});

    assertNotNull(suggestions);
    assertTrue(suggestions.isEmpty());
  }
}