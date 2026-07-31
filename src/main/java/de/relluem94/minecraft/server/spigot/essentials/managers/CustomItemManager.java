package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.InventoryRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.constants.InventoryConstants.PLUGIN_INVENTORY_ADMIN_TOOLS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.*;

public class CustomItemManager implements IEnable{

    private final Plugin plugin;
    public CustomItemManager(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void enable() {
        ItemRegistry.initialize(plugin);

        ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_POSITION_AXE,
                new ItemHelper(Material.COPPER_AXE, 1, PLUGIN_ITEM_POSITION_AXE,
                        ItemHelper.Type.ADMIN_TOOL, ItemHelper.Rarity.LEGENDARY,
                        List.of(PLUGIN_ITEM_POSITION_AXE_LORE1, PLUGIN_ITEM_POSITION_AXE_LORE2)));

        ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET,
                new ItemHelper(Material.WATER_BUCKET, 1, PLUGIN_ITEM_MAGIC_WATER_BUCKET,
                        ItemHelper.Type.GADGET, ItemHelper.Rarity.EPIC,
                        List.of(PLUGIN_ITEM_MAGIC_WATER_BUCKET_LORE)));

        ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_COINS,
                new ItemHelper(Material.GOLD_NUGGET, 1, PLUGIN_ITEM_COINS,
                        ItemHelper.Type.MONEY, ItemHelper.Rarity.COMMON,
                        List.of(PLUGIN_ITEM_COINS_LORE)));

        ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR,
                new ItemHelper(Material.FEATHER, 1, PLUGIN_ITEM_CLOUDSAILOR,
                        ItemHelper.Type.GADGET, ItemHelper.Rarity.EPIC,
                        List.of(PLUGIN_ITEM_CLOUDSAILOR_LORE1, PLUGIN_ITEM_CLOUDSAILOR_LORE2)));

        ItemHelper cloudBootsItem = new ItemHelper(Material.LEATHER_BOOTS, 1, PLUGIN_ITEM_CLOUDBOOTS,
                ItemHelper.Type.ARMOR, ItemHelper.Rarity.LEGENDARY,
                List.of(PLUGIN_ITEM_CLOUDBOOTS_LORE1, PLUGIN_ITEM_CLOUDBOOTS_LORE2)) {
            @Override
            public void init() {
                LeatherArmorMeta cloudBootsMeta = (LeatherArmorMeta) getItemMeta();
                cloudBootsMeta.setColor(Color.SILVER);
                cloudBootsMeta.setUnbreakable(true);
                cloudBootsMeta.addEnchant(Enchantment.PROTECTION, 3, true);
                setItemMeta(cloudBootsMeta);
            }
        };

        ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS, cloudBootsItem);

        ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED,
                new ItemHelper(Material.BLACK_STAINED_GLASS_PANE, 1, PLUGIN_ITEM_NPC_GUI_DISABLED_NAME,
                        ItemHelper.Type.NPC_GUI, ItemHelper.Rarity.NONE));

        ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE,
                new ItemHelper(Material.BARRIER, 1, PLUGIN_ITEM_NPC_GUI_CLOSE_NAME,
                        ItemHelper.Type.NPC_GUI, ItemHelper.Rarity.NONE));

        ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_GRAPPLINGHOOK,
                new ItemHelper(Material.FISHING_ROD, 1, PLUGIN_ITEM_GRAPPLINGHOCK,
                        ItemHelper.Type.GADGET, ItemHelper.Rarity.UNCOMMON));
      
        ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_AUTOSELL_HOPPER,
                new ItemHelper(Material.HOPPER, 1, PLUGIN_ITEM_AUTOSELLHOPER,
                        ItemHelper.Type.TOOL, ItemHelper.Rarity.LEGENDARY));

        ItemHelper relluBootsItem = new ItemHelper(Material.LEATHER_BOOTS, 1, PLUGIN_ITEM_RELLU_BOOTS,
                ItemHelper.Type.ARMOR, ItemHelper.Rarity.LEGENDARY) {
            @Override
            public void init() {
                LeatherArmorMeta relluBootsMeta = (LeatherArmorMeta) getItemMeta();
                relluBootsMeta.setColor(Color.fromRGB(227, 59, 46));
                relluBootsMeta.addEnchant(Enchantment.THORNS, 94, true);
                relluBootsMeta.addEnchant(Enchantment.LOOTING, 94, true);
                relluBootsMeta.addEnchant(Enchantment.PROTECTION, 94, true);
                relluBootsMeta.setUnbreakable(true);
                setItemMeta(relluBootsMeta);
            }
        };

        ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_BOOTS, relluBootsItem);

        ItemHelper relluChestplateItem = new ItemHelper(Material.LEATHER_CHESTPLATE, 1, PLUGIN_ITEM_RELLU_CHESTPLATE,
                ItemHelper.Type.ARMOR, ItemHelper.Rarity.LEGENDARY) {
            @Override
            public void init() {
                LeatherArmorMeta relluChestplateMeta = (LeatherArmorMeta) getItemMeta();
                relluChestplateMeta.setColor(Color.fromRGB(72, 179, 177));
                relluChestplateMeta.addEnchant(Enchantment.THORNS, 94, true);
                relluChestplateMeta.addEnchant(Enchantment.LOOTING, 94, true);
                relluChestplateMeta.addEnchant(Enchantment.PROTECTION, 94, true);
                relluChestplateMeta.setUnbreakable(true);
                setItemMeta(relluChestplateMeta);
            }
        };

        ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_CHESTPLATE, relluChestplateItem);




        InventoryRegistry.create(
                plugin,
                PLUGIN_INVENTORY_ADMIN_TOOLS,
                Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dAdmin Tools",
                9,
                ItemHelper.Type.NONE
        ).withFixedItem(ItemRegistry.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_POSITION_AXE)).orElseThrow())
                .withFixedItem(ItemRegistry.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET)).orElseThrow())
                .withFixedItem(ItemRegistry.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_COINS)).orElseThrow())
                .withFixedItem(ItemRegistry.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR)).orElseThrow())
                .withFixedItem(ItemRegistry.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS)).orElseThrow());
    }
}