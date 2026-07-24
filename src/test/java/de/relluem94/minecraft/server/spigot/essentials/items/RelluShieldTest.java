package de.relluem94.minecraft.server.spigot.essentials.items;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Banner;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
class RelluShieldTest {

    @Mock
    private ItemFactory mockItemFactory;

    @Mock
    private BlockStateMeta mockBlockStateMeta;

    @Mock
    private Banner mockBanner;

    private RelluShield buildRelluShield() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockBlockStateMeta))) {
            return new RelluShield();
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
        assertEquals(Material.SHIELD, buildRelluShield().getMaterial());
    }

    @Test
    void constructorSetsCorrectAmount() {
        assertEquals(1, buildRelluShield().getAmount());
    }

    @Test
    void constructorSetsCorrectDisplayName() {
        assertEquals(ItemConstants.PLUGIN_ITEM_RELLU_SHIELD, buildRelluShield().getDisplayName());
    }

    @Test
    void constructorSetsCorrectItemType() {
        assertEquals(ItemHelper.Type.ARMOR, buildRelluShield().getItemType());
    }

    @Test
    void constructorInitializesAllFieldsCorrectly() {
        RelluShield relluShield = buildRelluShield();
        assertAll(
                () -> assertEquals(Material.SHIELD, relluShield.getMaterial()),
                () -> assertEquals(1, relluShield.getAmount()),
                () -> assertEquals(ItemConstants.PLUGIN_ITEM_RELLU_SHIELD, relluShield.getDisplayName()),
                () -> assertEquals(ItemHelper.Type.ARMOR, relluShield.getItemType())
        );
    }

    @Test
    void getItemMetaReturnsNonNullAfterConstruction() {
        assertNotNull(buildRelluShield().getItemMeta());
    }

    @Test
    void getItemMetaDisplayNameIsSetDuringConstruction() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockBlockStateMeta))) {
            new RelluShield();
            verify(mockBlockStateMeta).setDisplayName(ItemConstants.PLUGIN_ITEM_RELLU_SHIELD);
        }
    }

    @Test
    @Disabled("Enchantment registry is not available outside a Spigot server environment")
    void initDoesNotThrowWhenCalledDirectly() {
        when(mockBlockStateMeta.getBlockState()).thenReturn(mockBanner);
        when(mockBlockStateMeta.addEnchant(any(), anyInt(), anyBoolean())).thenReturn(false);

        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockBlockStateMeta))) {
            RelluShield shield = new RelluShield();
            assertDoesNotThrow(() -> shield.init());
        }
    }
}