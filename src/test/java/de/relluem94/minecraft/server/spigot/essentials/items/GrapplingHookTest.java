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
class GrapplingHookTest {

    @Mock
    private ItemMeta mockItemMeta;

    private GrapplingHook buildGrapplingHook() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockItemMeta))) {
            return new GrapplingHook();
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
        assertEquals(Material.FISHING_ROD, buildGrapplingHook().getMaterial());
    }

    @Test
    void constructorSetsCorrectAmount() {
        assertEquals(1, buildGrapplingHook().getAmount());
    }

    @Test
    void constructorSetsCorrectDisplayName() {
        assertEquals(ItemConstants.PLUGIN_ITEM_GRAPPLINGHOCK, buildGrapplingHook().getDisplayName());
    }

    @Test
    void constructorSetsCorrectItemType() {
        assertEquals(ItemHelper.Type.GADGET, buildGrapplingHook().getItemType());
    }

    @Test
    void constructorSetsCorrectRarity() {
        assertEquals(ItemHelper.Rarity.UNCOMMON, buildGrapplingHook().getRarity());
    }

    @Test
    void constructorInitializesAllFieldsCorrectly() {
        GrapplingHook grapplingHook = buildGrapplingHook();
        assertAll(
                () -> assertEquals(Material.FISHING_ROD, grapplingHook.getMaterial()),
                () -> assertEquals(1, grapplingHook.getAmount()),
                () -> assertEquals(ItemConstants.PLUGIN_ITEM_GRAPPLINGHOCK, grapplingHook.getDisplayName()),
                () -> assertEquals(ItemHelper.Type.GADGET, grapplingHook.getItemType()),
                () -> assertEquals(ItemHelper.Rarity.UNCOMMON, grapplingHook.getRarity())
        );
    }

    @Test
    void getItemMetaReturnsNonNullAfterConstruction() {
        assertNotNull(buildGrapplingHook().getItemMeta());
    }

    @Test
    void getItemMetaDisplayNameIsSetDuringConstruction() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockItemMeta))) {
            new GrapplingHook();
            verify(mockItemMeta).setDisplayName(ItemConstants.PLUGIN_ITEM_GRAPPLINGHOCK);
        }
    }
}