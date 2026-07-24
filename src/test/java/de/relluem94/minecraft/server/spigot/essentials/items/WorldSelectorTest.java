package de.relluem94.minecraft.server.spigot.essentials.items;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WorldSelectorTest {

    @Mock
    private ItemMeta mockItemMeta;

    @Mock
    private ItemStack mockPlayerHeadItemStack;

    private WorldSelector buildWorldSelector() {
        try (MockedStatic<PlayerHeadHelper> ignored = mockStatic(PlayerHeadHelper.class)) {
            ignored.when(() -> PlayerHeadHelper.getCustomSkull(any())).thenReturn(mockPlayerHeadItemStack);
            when(mockPlayerHeadItemStack.getItemMeta()).thenReturn(mockItemMeta);
            when(mockPlayerHeadItemStack.getType()).thenReturn(Material.PLAYER_HEAD);
            when(mockPlayerHeadItemStack.getAmount()).thenReturn(1);
            return new WorldSelector();
        }
    }

    @BeforeAll
    static void setUpServer() {
        Server mockServer = mock(Server.class);
        ItemFactory mockItemFactory = mock(ItemFactory.class);
        ItemMeta mockItemMeta = mock(ItemMeta.class);

        when(mockServer.getItemFactory()).thenReturn(mockItemFactory);
        when(mockServer.getLogger()).thenReturn(Logger.getLogger("test"));
        when(mockItemFactory.getItemMeta(any(Material.class))).thenReturn(mockItemMeta);
        when(mockItemFactory.equals(any(), any())).thenReturn(false);

        try {
            org.bukkit.Bukkit.setServer(mockServer);
        } catch (UnsupportedOperationException ignored) {
        }
    }

    @Test
    void constructorSetsCorrectDisplayName() {
        assertEquals(ItemConstants.PLUGIN_ITEM_WORLDSELECTOR, buildWorldSelector().getDisplayName());
    }

    @Test
    void constructorSetsCorrectItemType() {
        assertEquals(ItemHelper.Type.GADGET, buildWorldSelector().getItemType());
    }

    @Test
    void constructorInitializesAllFieldsCorrectly() {
        WorldSelector worldSelector = buildWorldSelector();
        assertAll(
                () -> assertEquals(ItemConstants.PLUGIN_ITEM_WORLDSELECTOR, worldSelector.getDisplayName()),
                () -> assertEquals(ItemHelper.Type.GADGET, worldSelector.getItemType())
        );
    }

    @Test
    void getItemMetaReturnsNonNullAfterConstruction() {
        assertNotNull(buildWorldSelector().getItemMeta());
    }

    @Test
    void getItemMetaDisplayNameIsSetDuringConstruction() {
        try (MockedStatic<PlayerHeadHelper> ignored = mockStatic(PlayerHeadHelper.class)) {
            ignored.when(() -> PlayerHeadHelper.getCustomSkull(any())).thenReturn(mockPlayerHeadItemStack);
            when(mockPlayerHeadItemStack.getItemMeta()).thenReturn(mockItemMeta);
            when(mockPlayerHeadItemStack.getType()).thenReturn(Material.PLAYER_HEAD);
            when(mockPlayerHeadItemStack.getAmount()).thenReturn(1);

            new WorldSelector();
            verify(mockItemMeta).setDisplayName(ItemConstants.PLUGIN_ITEM_WORLDSELECTOR);
        }
    }
}