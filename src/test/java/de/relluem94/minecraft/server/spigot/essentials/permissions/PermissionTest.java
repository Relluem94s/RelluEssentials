package de.relluem94.minecraft.server.spigot.essentials.permissions;

import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PermissionTest {

    private Player mockPlayer;
    private PlayerEntry mockPlayerEntry;

    @BeforeEach
    void setUp() {
        PlayerAPI mockPlayerAPI = mock(PlayerAPI.class);
        mockPlayer = mock(Player.class);
        mockPlayerEntry = mock(PlayerEntry.class);

        when(mockPlayerAPI.getPlayerEntry(mockPlayer)).thenReturn(mockPlayerEntry);

        Permission.injectPlayerAPI(mockPlayerAPI);
    }

    @AfterEach
    void tearDown() {
        Permission.injectPlayerAPI(null);
    }

    private void givenPlayerHasGroupId(long groupId) {
        GroupEntry groupEntry = new GroupEntry((int) groupId, "testgroup", "§f");
        when(mockPlayerEntry.getGroup()).thenReturn(groupEntry);
    }

    @Test
    void playerWithEqualGroupIdIsAuthorized() {
        givenPlayerHasGroupId(2);
        assertTrue(Permission.isAuthorized(mockPlayer, 2));
    }

    @Test
    void playerWithHigherGroupIdIsAuthorized() {
        givenPlayerHasGroupId(3);
        assertTrue(Permission.isAuthorized(mockPlayer, 2));
    }

    @Test
    void playerWithLowerGroupIdIsNotAuthorized() {
        givenPlayerHasGroupId(1);
        assertFalse(Permission.isAuthorized(mockPlayer, 2));
    }

    @Test
    void consoleIsAlwaysAuthorized() {
        ConsoleCommandSender consoleSender = mock(ConsoleCommandSender.class);
        assertTrue(Permission.isAuthorized(consoleSender, 999));
    }

    @Test
    void commandBlockIsAlwaysAuthorized() {
        BlockCommandSender cmdBlockSender = mock(BlockCommandSender.class);
        assertTrue(Permission.isAuthorized(cmdBlockSender, 999));
    }

    @Test
    void playerSenderDelegatesToPlayerCheck() {
        givenPlayerHasGroupId(2);
        CommandSender playerAsSender = mockPlayer;
        assertTrue(Permission.isAuthorized(playerAsSender, 2));
    }

    @Test
    void checkForUtilityClass(){
        Assertions.assertThrows(IllegalStateException.class, Permission::new);
    }
}