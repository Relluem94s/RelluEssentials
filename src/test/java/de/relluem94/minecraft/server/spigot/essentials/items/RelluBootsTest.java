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
class RelluBootsTest {

    @Mock
    private LeatherArmorMeta mockLeatherArmorMeta;

    private RelluBoots buildRelluBoots() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockLeatherArmorMeta))) {
            return new RelluBoots();
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
        assertEquals(Material.LEATHER_BOOTS, buildRelluBoots().getMaterial());
    }

    @Test
    void constructorSetsCorrectAmount() {
        assertEquals(1, buildRelluBoots().getAmount());
    }

    @Test
    void constructorSetsCorrectDisplayName() {
        assertEquals(ItemConstants.PLUGIN_ITEM_RELLU_BOOTS, buildRelluBoots().getDisplayName());
    }

    @Test
    void constructorSetsCorrectItemType() {
        assertEquals(ItemHelper.Type.ARMOR, buildRelluBoots().getItemType());
    }

    @Test
    void constructorSetsCorrectRarity() {
        assertEquals(ItemHelper.Rarity.LEGENDARY, buildRelluBoots().getRarity());
    }

    @Test
    void constructorInitializesAllFieldsCorrectly() {
        RelluBoots relluBoots = buildRelluBoots();
        assertAll(
                () -> assertEquals(Material.LEATHER_BOOTS, relluBoots.getMaterial()),
                () -> assertEquals(1, relluBoots.getAmount()),
                () -> assertEquals(ItemConstants.PLUGIN_ITEM_RELLU_BOOTS, relluBoots.getDisplayName()),
                () -> assertEquals(ItemHelper.Type.ARMOR, relluBoots.getItemType()),
                () -> assertEquals(ItemHelper.Rarity.LEGENDARY, relluBoots.getRarity())
        );
    }

    @Test
    void getItemMetaReturnsNonNullAfterConstruction() {
        assertNotNull(buildRelluBoots().getItemMeta());
    }

    @Test
    void getItemMetaDisplayNameIsSetDuringConstruction() {
        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockLeatherArmorMeta))) {
            new RelluBoots();
            verify(mockLeatherArmorMeta).setDisplayName(ItemConstants.PLUGIN_ITEM_RELLU_BOOTS);
        }
    }

    @Test
    @Disabled("Enchantment registry is not available outside a Spigot server environment")
    void initSetsCorrectArmorColorAndEnchantments() {
        when(mockLeatherArmorMeta.addEnchant(any(Enchantment.class), anyInt(), anyBoolean())).thenReturn(false);

        try (MockedConstruction<ItemStack> ignored = mockConstruction(ItemStack.class,
                (mock, context) -> when(mock.getItemMeta()).thenReturn(mockLeatherArmorMeta))) {
            RelluBoots relluBoots = new RelluBoots();
            assertDoesNotThrow(() -> relluBoots.init());
            verify(mockLeatherArmorMeta).setColor(Color.fromRGB(227, 59, 46));
            verify(mockLeatherArmorMeta).setUnbreakable(true);
        }
    }
}