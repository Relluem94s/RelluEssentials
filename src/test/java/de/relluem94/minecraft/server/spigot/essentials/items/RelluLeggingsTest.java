package de.relluem94.minecraft.server.spigot.essentials.items;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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
class RelluLeggingsTest {

    @Mock
    private LeatherArmorMeta mockLeatherArmorMeta;

    private RelluLeggings buildRelluLeggings() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockLeatherArmorMeta))) {
            return new RelluLeggings();
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
        assertEquals(Material.LEATHER_LEGGINGS, buildRelluLeggings().getMaterial());
    }

    @Test
    void constructorSetsCorrectAmount() {
        assertEquals(1, buildRelluLeggings().getAmount());
    }

    @Test
    void constructorSetsCorrectDisplayName() {
        assertEquals(ItemConstants.PLUGIN_ITEM_RELLU_LEGGINGS, buildRelluLeggings().getDisplayName());
    }

    @Test
    void constructorSetsCorrectItemType() {
        assertEquals(ItemHelper.Type.ARMOR, buildRelluLeggings().getItemType());
    }

    @Test
    void constructorInitializesAllFieldsCorrectly() {
        RelluLeggings relluLeggings = buildRelluLeggings();
        assertAll(
                () -> assertEquals(Material.LEATHER_LEGGINGS, relluLeggings.getMaterial()),
                () -> assertEquals(1, relluLeggings.getAmount()),
                () -> assertEquals(ItemConstants.PLUGIN_ITEM_RELLU_LEGGINGS, relluLeggings.getDisplayName()),
                () -> assertEquals(ItemHelper.Type.ARMOR, relluLeggings.getItemType())
        );
    }

    @Test
    void getItemMetaReturnsNonNullAfterConstruction() {
        assertNotNull(buildRelluLeggings().getItemMeta());
    }

    @Test
    void getItemMetaDisplayNameIsSetDuringConstruction() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockLeatherArmorMeta))) {
            new RelluLeggings();
            verify(mockLeatherArmorMeta).setDisplayName(ItemConstants.PLUGIN_ITEM_RELLU_LEGGINGS);
        }
    }

    @Test
    @Disabled("Enchantment registry is not available outside a Spigot server environment")
    void initDoesNotThrowWhenCalledDirectly() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockLeatherArmorMeta))) {
            RelluLeggings leggings = new RelluLeggings();
            assertDoesNotThrow(() -> leggings.init());
        }
    }
}