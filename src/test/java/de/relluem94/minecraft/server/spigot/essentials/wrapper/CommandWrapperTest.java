package de.relluem94.minecraft.server.spigot.essentials.wrapper;

import de.relluem94.minecraft.server.spigot.essentials.commands.AFK;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandWrapperTest {

    @Mock
    private JavaPlugin javaPlugin;

    @Mock
    private PluginCommand pluginCommand;

    private CommandWrapper commandWrapper;
    private CommandWrapper commandWrapperNoSubCommands;

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

        when(javaPlugin.getCommand(commandName)).thenReturn(pluginCommand);

        wrapper.init(javaPlugin);

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

        when(javaPlugin.getCommand(commandName)).thenReturn(pluginCommand);

        wrapper.init(javaPlugin);
        wrapper.init(javaPlugin);

        verify(pluginCommand, times(1)).setExecutor(adminConstruct);
        verify(pluginCommand, times(1)).setTabCompleter(adminConstruct);
    }

    @Test
    void initDoesNothingWhenPluginCommandIsNull() throws Exception {
        Admin adminConstruct = new Admin();
        CommandWrapper wrapper = new CommandWrapper(adminConstruct);
        String commandName = wrapper.getCommandName();

        when(javaPlugin.getCommand(commandName)).thenReturn(null);

        wrapper.init(javaPlugin);

        Field initialisedField = CommandWrapper.class.getDeclaredField("initialised");
        initialisedField.setAccessible(true);

        assertFalse((boolean) initialisedField.get(wrapper));
    }
}