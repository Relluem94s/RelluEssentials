package de.relluem94.minecraft.server.spigot.essentials.wrapper;

import de.relluem94.minecraft.server.spigot.essentials.commands.AFK;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandWrapperTest {

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
    }

    @Test
    void hasSubCommands() {
        Assertions.assertTrue(commandWrapper.hasSubCommands());
        Assertions.assertFalse(commandWrapperNoSubCommands.hasSubCommands());
    }

    @Test
    void getSubCommands() {
        Assertions.assertNotEquals(0, commandWrapper.getSubCommands().length);
        Assertions.assertEquals(0, commandWrapperNoSubCommands.getSubCommands().length);
    }

    @Test
    void getCommandName() {
        Assertions.assertNotNull(commandWrapper.getCommandName());
    }
}