package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.InventoryConstants.PLUGIN_INVENTORY_ADMIN_TOOLS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_AUTOSELLHOPER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_CLOUDBOOTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_CLOUDBOOTS_LORE1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_CLOUDBOOTS_LORE2;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_CLOUDSAILOR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_CLOUDSAILOR_LORE1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_CLOUDSAILOR_LORE2;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_COINS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_COINS_LORE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_GRAPPLINGHOCK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_MAGIC_WATER_BUCKET;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_MAGIC_WATER_BUCKET_LORE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_AUTOSELL_HOPPER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_COINS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_GRAPPLINGHOOK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_POSITION_AXE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_RELLU_BOOTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_RELLU_CHESTPLATE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_RELLU_HELMET;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_RELLU_LEGGINGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_RELLU_PICKAXE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_RELLU_SHIELD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_RELLU_SWORD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_WORLDSELECTOR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_GUI_CLOSE_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_GUI_DISABLED_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_POSITION_AXE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_POSITION_AXE_LORE1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_POSITION_AXE_LORE2;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_BOOTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_CHESTPLATE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_HELMET;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_LEGGINGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_PICKAXE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_SHIELD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_RELLU_SWORD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_WORLDSELECTOR;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.enums.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.EnchantmentRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.InventoryRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;

/**
 * Registers and initializes all custom items and admin tool inventories for the plugin.
 */
public class ItemManager implements Enable {

  private final Plugin plugin;

  /**
   * Creates a new CustomItemManager instance.
   *
   * @param plugin the plugin instance used for item registration and registry initialization
   */
  public ItemManager(Plugin plugin) {
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
            List.of(PLUGIN_ITEM_MAGIC_WATER_BUCKET_LORE), 5000));

    ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_COINS,
        new ItemHelper(Material.GOLD_NUGGET, 1, PLUGIN_ITEM_COINS,
            ItemHelper.Type.MONEY, ItemHelper.Rarity.COMMON,
            List.of(PLUGIN_ITEM_COINS_LORE)));

    ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR,
        new ItemHelper(Material.FEATHER, 1, PLUGIN_ITEM_CLOUDSAILOR,
            ItemHelper.Type.GADGET, ItemHelper.Rarity.EPIC,
            List.of(PLUGIN_ITEM_CLOUDSAILOR_LORE1, PLUGIN_ITEM_CLOUDSAILOR_LORE2), 10000));

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
            ItemHelper.Type.TOOL, ItemHelper.Rarity.LEGENDARY, 50000));

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

    ItemHelper relluChestplateItem = new ItemHelper(Material.LEATHER_CHESTPLATE, 1,
        PLUGIN_ITEM_RELLU_CHESTPLATE,
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

    ItemHelper relluHelmetItem = new ItemHelper(Material.LEATHER_HELMET, 1,
        PLUGIN_ITEM_RELLU_HELMET,
        ItemHelper.Type.ARMOR, ItemHelper.Rarity.LEGENDARY) {
      @Override
      public void init() {
        LeatherArmorMeta relluHelmetMeta = (LeatherArmorMeta) getItemMeta();
        relluHelmetMeta.setColor(Color.fromRGB(243, 125, 0));
        relluHelmetMeta.addEnchant(Enchantment.THORNS, 94, true);
        relluHelmetMeta.addEnchant(Enchantment.LOOTING, 94, true);
        relluHelmetMeta.addEnchant(Enchantment.PROTECTION, 94, true);
        relluHelmetMeta.setUnbreakable(true);
        setItemMeta(relluHelmetMeta);
      }
    };

    ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_HELMET, relluHelmetItem);

    ItemHelper relluLeggingsItem = new ItemHelper(Material.LEATHER_LEGGINGS, 1,
        PLUGIN_ITEM_RELLU_LEGGINGS,
        ItemHelper.Type.ARMOR, ItemHelper.Rarity.LEGENDARY) {
      @Override
      public void init() {
        LeatherArmorMeta relluLeggingsMeta = (LeatherArmorMeta) getItemMeta();
        relluLeggingsMeta.setColor(Color.fromRGB(152, 216, 1));
        relluLeggingsMeta.addEnchant(Enchantment.THORNS, 94, true);
        relluLeggingsMeta.addEnchant(Enchantment.LOOTING, 94, true);
        relluLeggingsMeta.addEnchant(Enchantment.PROTECTION, 94, true);
        relluLeggingsMeta.setUnbreakable(true);
        setItemMeta(relluLeggingsMeta);
      }
    };

    ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_LEGGINGS, relluLeggingsItem);

    ItemHelper relluShieldItem = new ItemHelper(Material.SHIELD, 1, PLUGIN_ITEM_RELLU_SHIELD,
        ItemHelper.Type.ARMOR, ItemHelper.Rarity.LEGENDARY) {
      @Override
      public void init() {
        BlockStateMeta blockStateMeta = (BlockStateMeta) getItemMeta();
        Banner banner = (Banner) blockStateMeta.getBlockState();
        banner.setBaseColor(DyeColor.PURPLE);
        banner.update();
        blockStateMeta.setBlockState(banner);
        blockStateMeta.addEnchant(Enchantment.SHARPNESS, 94, true);
        blockStateMeta.addEnchant(Enchantment.THORNS, 94, true);
        blockStateMeta.addEnchant(Enchantment.LOOTING, 94, true);
        blockStateMeta.addEnchant(Enchantment.PROTECTION, 94, true);
        blockStateMeta.setUnbreakable(true);
        setItemMeta(blockStateMeta);
      }
    };

    ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_SHIELD, relluShieldItem);

    ItemHelper relluPickaxeItem = new ItemHelper(Material.NETHERITE_PICKAXE, 1,
        PLUGIN_ITEM_RELLU_PICKAXE,
        ItemHelper.Type.TOOL, ItemHelper.Rarity.LEGENDARY) {
      @Override
      public void init() {
        getItemMeta().addEnchant(Enchantment.LOOTING, 94, true);
        ItemMeta relluPickaxeMeta = getItemMeta();
        relluPickaxeMeta.addEnchant(Enchantment.LOOTING, 94, true);
        relluPickaxeMeta.addEnchant(Enchantment.EFFICIENCY, 94, true);
        relluPickaxeMeta.setUnbreakable(true);
        setItemMeta(relluPickaxeMeta);
      }

      @Override
      public ItemStack postInit(ItemStack is) {
        EnchantmentRegistry.find(
                RegistryKey.of(plugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS))
            .ifPresent(enchant -> enchant.addTo(is));
        return is;
      }
    };

    ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_PICKAXE, relluPickaxeItem);

    ItemHelper relluSwordItem = new ItemHelper(Material.NETHERITE_SWORD, 1, PLUGIN_ITEM_RELLU_SWORD,
        ItemHelper.Type.WEAPON, ItemHelper.Rarity.LEGENDARY) {
      @Override
      public void init() {
        ItemMeta relluSwordMeta = getItemMeta();
        relluSwordMeta.addEnchant(Enchantment.SHARPNESS, 94, true);
        relluSwordMeta.addEnchant(Enchantment.SWEEPING_EDGE, 94, true);
        relluSwordMeta.addEnchant(Enchantment.LOOTING, 94, true);
        relluSwordMeta.addEnchant(Enchantment.PROTECTION, 94, true);
        relluSwordMeta.setUnbreakable(true);
        setItemMeta(relluSwordMeta);
      }

      @Override
      public ItemStack postInit(ItemStack is) {
        EnchantmentRegistry.find(
                RegistryKey.of(plugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS))
            .ifPresent(enchant -> enchant.addTo(is));
        EnchantmentRegistry.find(
                RegistryKey.of(plugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE))
            .ifPresent(enchant -> enchant.addTo(is));
        return is;
      }
    };

    ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_SWORD, relluSwordItem);

    ItemRegistry.register(plugin, PLUGIN_ITEM_NAMESPACE_WORLDSELECTOR,
        new ItemHelper(PlayerHeadHelper.getCustomSkull(CustomHeads.GLOBE),
            PLUGIN_ITEM_WORLDSELECTOR,
            ItemHelper.Type.GADGET, ItemHelper.Rarity.RARE));

    InventoryRegistry.create(
            plugin,
            PLUGIN_INVENTORY_ADMIN_TOOLS,
            Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dAdmin Tools",
            9,
            ItemHelper.Type.NONE
        ).withFixedItem(
            ItemRegistry.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_POSITION_AXE))
                .orElseThrow())
        .withFixedItem(
            ItemRegistry.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET))
                .orElseThrow())
        .withFixedItem(ItemRegistry.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR))
            .orElseThrow())
        .withFixedItem(ItemRegistry.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS))
            .orElseThrow());

    int itemCount = ItemRegistry.getAll().size();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_ITEMS_REGISTERED, itemCount));

    int inventoryCount = InventoryRegistry.getAllByNamespace(plugin.getName()).size();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_INVENTORIES_REGISTERED, inventoryCount));
  }
}