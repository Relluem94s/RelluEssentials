package de.relluem94.minecraft.server.spigot.essentials.items;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
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
class RelluChestplateTest {

    @Mock
    private LeatherArmorMeta mockLeatherArmorMeta;

    private RelluChestplate buildRelluChestplate() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockLeatherArmorMeta))) {
            return new RelluChestplate();
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
        assertEquals(Material.LEATHER_CHESTPLATE, buildRelluChestplate().getMaterial());
    }

    @Test
    void constructorSetsCorrectAmount() {
        assertEquals(1, buildRelluChestplate().getAmount());
    }

    @Test
    void constructorSetsCorrectDisplayName() {
        assertEquals(ItemConstants.PLUGIN_ITEM_RELLU_CHESTPLATE, buildRelluChestplate().getDisplayName());
    }

    @Test
    void constructorSetsCorrectItemType() {
        assertEquals(ItemHelper.Type.ARMOR, buildRelluChestplate().getItemType());
    }

    @Test
    void constructorSetsCorrectRarity() {
        assertEquals(ItemHelper.Rarity.LEGENDARY, buildRelluChestplate().getRarity());
    }

    @Test
    void constructorInitializesAllFieldsCorrectly() {
        RelluChestplate relluChestplate = buildRelluChestplate();
        assertAll(
                () -> assertEquals(Material.LEATHER_CHESTPLATE, relluChestplate.getMaterial()),
                () -> assertEquals(1, relluChestplate.getAmount()),
                () -> assertEquals(ItemConstants.PLUGIN_ITEM_RELLU_CHESTPLATE, relluChestplate.getDisplayName()),
                () -> assertEquals(ItemHelper.Type.ARMOR, relluChestplate.getItemType()),
                () -> assertEquals(ItemHelper.Rarity.LEGENDARY, relluChestplate.getRarity())
        );
    }

    @Test
    void getItemMetaReturnsNonNullAfterConstruction() {
        assertNotNull(buildRelluChestplate().getItemMeta());
    }

    @Test
    void getItemMetaDisplayNameIsSetDuringConstruction() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockLeatherArmorMeta))) {
            new RelluChestplate();
            verify(mockLeatherArmorMeta).setDisplayName(ItemConstants.PLUGIN_ITEM_RELLU_CHESTPLATE);
        }
    }

    @Test
    @Disabled("Enchantment registry is not available outside a Spigot server environment")
    void initSetsCorrectArmorColorAndEnchantments() {
        when(mockLeatherArmorMeta.addEnchant(any(Enchantment.class), anyInt(), anyBoolean())).thenReturn(false);

        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockLeatherArmorMeta))) {
            RelluChestplate relluChestplate = new RelluChestplate();
            assertDoesNotThrow(() -> relluChestplate.init());
            verify(mockLeatherArmorMeta).setColor(Color.fromRGB(72, 179, 177));
            verify(mockLeatherArmorMeta).setUnbreakable(true);
        }
    }
}