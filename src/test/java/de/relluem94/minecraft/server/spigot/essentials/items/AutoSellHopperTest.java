package de.relluem94.minecraft.server.spigot.essentials.items;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AutoSellHopperTest {

    @Mock
    private ItemMeta mockItemMeta;

    private AutoSellHopper buildAutoSellHopper() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockItemMeta))) {
            return new AutoSellHopper();
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
    void constructorSetsCorrectMaterial() {
        assertEquals(Material.HOPPER, buildAutoSellHopper().getMaterial());
    }

    @Test
    void constructorSetsCorrectAmount() {
        assertEquals(1, buildAutoSellHopper().getAmount());
    }

    @Test
    void constructorSetsCorrectDisplayName() {
        assertEquals(ItemConstants.PLUGIN_ITEM_AUTOSELLHOPER, buildAutoSellHopper().getDisplayName());
    }

    @Test
    void constructorSetsCorrectItemType() {
        assertEquals(ItemHelper.Type.TOOL, buildAutoSellHopper().getItemType());
    }

    @Test
    void constructorSetsCorrectRarity() {
        assertEquals(ItemHelper.Rarity.LEGENDARY, buildAutoSellHopper().getRarity());
    }

    @Test
    void constructorInitializesAllFieldsCorrectly() {
        AutoSellHopper autoSellHopper = buildAutoSellHopper();
        assertAll(
                () -> assertEquals(Material.HOPPER, autoSellHopper.getMaterial()),
                () -> assertEquals(1, autoSellHopper.getAmount()),
                () -> assertEquals(ItemConstants.PLUGIN_ITEM_AUTOSELLHOPER, autoSellHopper.getDisplayName()),
                () -> assertEquals(ItemHelper.Type.TOOL, autoSellHopper.getItemType()),
                () -> assertEquals(ItemHelper.Rarity.LEGENDARY, autoSellHopper.getRarity())
        );
    }

    @Test
    void getItemMetaReturnsNonNullAfterConstruction() {
        assertNotNull(buildAutoSellHopper().getItemMeta());
    }

    @Test
    void getItemMetaDisplayNameIsSetDuringConstruction() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockItemMeta))) {
            new AutoSellHopper();
            verify(mockItemMeta).setDisplayName(ItemConstants.PLUGIN_ITEM_AUTOSELLHOPER);
        }
    }
}