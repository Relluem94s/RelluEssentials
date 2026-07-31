package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.PlayerRegistry;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PermissionHelperTest {

    private Player mockPlayer;
    private PlayerEntry mockPlayerEntry;

    @BeforeEach
    void setUp() {
        PlayerRegistry mockPlayerRegistry = mock(PlayerRegistry.class);
        mockPlayer = mock(Player.class);
        mockPlayerEntry = mock(PlayerEntry.class);

        when(mockPlayerRegistry.getPlayerEntry(mockPlayer)).thenReturn(mockPlayerEntry);

        PermissionHelper.injectPlayerAPI(mockPlayerRegistry);
    }

    @AfterEach
    void tearDown() {
        PermissionHelper.injectPlayerAPI(null);
    }

    private void givenPlayerHasGroupId(long groupId) {
        GroupEntry groupEntry = new GroupEntry((int) groupId, "testgroup", "§f");
        when(mockPlayerEntry.getGroup()).thenReturn(groupEntry);
    }

    @Test
    void playerWithEqualGroupIdIsAuthorized() {
        givenPlayerHasGroupId(2);
        assertTrue(PermissionHelper.isAuthorized(mockPlayer, 2));
    }

    @Test
    void playerWithHigherGroupIdIsAuthorized() {
        givenPlayerHasGroupId(3);
        assertTrue(PermissionHelper.isAuthorized(mockPlayer, 2));
    }

    @Test
    void playerWithLowerGroupIdIsNotAuthorized() {
        givenPlayerHasGroupId(1);
        assertFalse(PermissionHelper.isAuthorized(mockPlayer, 2));
    }

    @Test
    void consoleIsAlwaysAuthorized() {
        ConsoleCommandSender consoleSender = mock(ConsoleCommandSender.class);
        assertTrue(PermissionHelper.isAuthorized(consoleSender, 999));
    }

    @Test
    void commandBlockIsAlwaysAuthorized() {
        BlockCommandSender cmdBlockSender = mock(BlockCommandSender.class);
        assertTrue(PermissionHelper.isAuthorized(cmdBlockSender, 999));
    }

    @Test
    void playerSenderDelegatesToPlayerCheck() {
        givenPlayerHasGroupId(2);
        CommandSender playerAsSender = mockPlayer;
        assertTrue(PermissionHelper.isAuthorized(playerAsSender, 2));
    }

    @Test
    void checkForUtilityClass(){
        Assertions.assertThrows(IllegalStateException.class, PermissionHelper::new);
    }
}