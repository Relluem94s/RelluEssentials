package de.relluem94.minecraft.server.spigot.essentials.wrappers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.AFK;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import java.lang.reflect.Field;
import java.util.Optional;
import org.bukkit.command.PluginCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandWrapperTest {

    @Mock
    private RelluEssentials javaPlugin;

    @Mock
    private PluginCommand pluginCommand;

    private CommandWrapper commandWrapper;
    private CommandWrapper commandWrapperNoSubCommands;

    @Mock
    private CommandConstruct commandConstructWithRegistry;

    @BeforeEach
    void setUp() {
        commandWrapper = new CommandWrapper(new Admin());
        commandWrapperNoSubCommands = new CommandWrapper(new AFK());
    }

    @AfterEach
    void tearDown() {
        commandWrapper = null;
        commandWrapperNoSubCommands = null;
    }

    @Test
    void hasSubCommandsReturnsTrueWhenSubCommandsPresent() {
        assertTrue(commandWrapper.hasSubCommands());
    }

    @Test
    void hasSubCommandsReturnsFalseWhenNoSubCommandsPresent() {
        assertFalse(commandWrapperNoSubCommands.hasSubCommands());
    }

    @Test
    void getSubCommandsReturnsNonEmptyArrayWhenSubCommandsPresent() {
        assertNotEquals(0, commandWrapper.getSubCommands().length);
    }

    @Test
    void getSubCommandsReturnsEmptyArrayWhenNoSubCommandsPresent() {
        assertEquals(0, commandWrapperNoSubCommands.getSubCommands().length);
    }

    @Test
    void getCommandNameReturnsNonNullValue() {
        assertNotNull(commandWrapper.getCommandName());
    }

    @Test
    void initSetsExecutorAndTabCompleterAndMarksInitialised() throws Exception {
        Admin adminConstruct = new Admin();
        CommandWrapper wrapper = new CommandWrapper(adminConstruct);
        String commandName = wrapper.getCommandName();
        ServiceContext serviceContext = new ServiceContext(javaPlugin);
        when(javaPlugin.getCommand(commandName)).thenReturn(pluginCommand);

        wrapper.init(javaPlugin, serviceContext);

        Field initialisedField = CommandWrapper.class.getDeclaredField("initialised");
        initialisedField.setAccessible(true);

        assertAll(
                () -> assertTrue((boolean) initialisedField.get(wrapper)),
                () -> verify(pluginCommand).setExecutor(adminConstruct),
                () -> verify(pluginCommand).setTabCompleter(adminConstruct)
        );
    }

    @Test
    void initDoesNothingWhenAlreadyInitialised() {
        Admin adminConstruct = new Admin();
        CommandWrapper wrapper = new CommandWrapper(adminConstruct);
        String commandName = wrapper.getCommandName();

        ServiceContext serviceContext = new ServiceContext((RelluEssentials) javaPlugin);
        when(javaPlugin.getCommand(commandName)).thenReturn(pluginCommand);

        wrapper.init(javaPlugin, serviceContext);
        wrapper.init(javaPlugin, serviceContext);

        verify(pluginCommand, times(1)).setExecutor(adminConstruct);
        verify(pluginCommand, times(1)).setTabCompleter(adminConstruct);
    }

    @Test
    void initDoesNothingWhenPluginCommandIsNull() throws Exception {
        Admin adminConstruct = new Admin();
        CommandWrapper wrapper = new CommandWrapper(adminConstruct);
        String commandName = wrapper.getCommandName();

        ServiceContext serviceContext = new ServiceContext((RelluEssentials) javaPlugin);
        when(javaPlugin.getCommand(commandName)).thenReturn(null);

        wrapper.init(javaPlugin, serviceContext);

        Field initialisedField = CommandWrapper.class.getDeclaredField("initialised");
        initialisedField.setAccessible(true);

        assertFalse((boolean) initialisedField.get(wrapper));
    }

    @Test
    void hasSubCommandsReturnsTrueWhenSubCommandRegistryIsPresent() {
        when(commandConstructWithRegistry.getSubCommandRegistry()).thenReturn(Optional.of(mock(/* SubCommandRegistry class */)));
        CommandWrapper wrapper = new CommandWrapper(commandConstructWithRegistry);
        assertTrue(wrapper.hasSubCommands());
    }
}