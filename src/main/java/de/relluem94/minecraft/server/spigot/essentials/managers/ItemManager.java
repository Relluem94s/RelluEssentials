package de.relluem94.minecraft.server.spigot.essentials.managers;

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

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.builders.CustomItemBuilder;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem.Type;
import de.relluem94.minecraft.server.spigot.essentials.registries.EnchantmentRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.ItemService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
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

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentials = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentials.getServiceContext();
    ItemService itemService = serviceContext.getItemService();

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(plugin.getName(), PLUGIN_ITEM_POSITION_AXE),
        Material.COPPER_AXE).rarity(Rarity.LEGENDARY).displayName(PLUGIN_ITEM_POSITION_AXE)
        .amount(1).type(Type.ADMIN_TOOL)
        .lore(List.of(PLUGIN_ITEM_POSITION_AXE_LORE1, PLUGIN_ITEM_POSITION_AXE_LORE2)).build());

    itemService.register(new CustomItemBuilder(new RelluEssentialsNamespacedKey(plugin.getName(),
        PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET), Material.WATER_BUCKET).amount(1)
        .displayName(PLUGIN_ITEM_MAGIC_WATER_BUCKET).type(CustomItem.Type.GADGET)
        .rarity(CustomItem.Rarity.EPIC).lore(List.of(PLUGIN_ITEM_MAGIC_WATER_BUCKET_LORE))
        .cost(5000).build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(plugin.getName(), PLUGIN_ITEM_NAMESPACE_COINS),
        Material.GOLD_NUGGET).amount(1).displayName(PLUGIN_ITEM_COINS).type(Type.MONEY)
        .rarity(Rarity.COMMON).lore(List.of(PLUGIN_ITEM_COINS_LORE)).build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(plugin.getName(), PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR),
        Material.FEATHER).amount(1).displayName(PLUGIN_ITEM_CLOUDSAILOR).type(Type.GADGET)
        .rarity(Rarity.EPIC)
        .lore(List.of(PLUGIN_ITEM_CLOUDSAILOR_LORE1, PLUGIN_ITEM_CLOUDSAILOR_LORE2)).cost(10000)
        .build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(
            plugin.getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED
        ),
        Material.BLACK_STAINED_GLASS_PANE
    )
        .amount(1)
        .displayName(PLUGIN_ITEM_NPC_GUI_DISABLED_NAME)
        .type(Type.NPC_GUI)
        .rarity(Rarity.NONE)
        .build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(
            plugin.getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE
        ),
        Material.BARRIER
    )
        .amount(1)
        .displayName(PLUGIN_ITEM_NPC_GUI_CLOSE_NAME)
        .type(Type.NPC_GUI)
        .rarity(Rarity.NONE)
        .build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(
            plugin.getName(),
            PLUGIN_ITEM_NAMESPACE_GRAPPLINGHOOK
        ),
        Material.FISHING_ROD
    )
        .amount(1)
        .displayName(PLUGIN_ITEM_GRAPPLINGHOCK)
        .type(Type.GADGET)
        .rarity(Rarity.UNCOMMON)
        .build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(
            plugin.getName(),
            PLUGIN_ITEM_NAMESPACE_AUTOSELL_HOPPER
        ),
        Material.HOPPER
    )
        .amount(1)
        .displayName(PLUGIN_ITEM_AUTOSELLHOPER)
        .type(Type.TOOL)
        .rarity(Rarity.LEGENDARY)
        .cost(50000)
        .build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(
            plugin.getName(),
            PLUGIN_ITEM_NAMESPACE_WORLDSELECTOR
        ),
        Material.PLAYER_HEAD
    )
            .metaModifier(
                PlayerHeadHelper.customHeadModifier(CustomHeads.GLOBE)
            )
        .amount(1)
        .displayName(PLUGIN_ITEM_WORLDSELECTOR)
        .type(Type.GADGET)
        .rarity(Rarity.RARE)
        .build());


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

    itemService.register(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS), cloudBootsItem);

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

    itemService.register(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_BOOTS), relluBootsItem);

    ItemHelper relluChestplateItem = new ItemHelper(Material.LEATHER_CHESTPLATE, 1,
        PLUGIN_ITEM_RELLU_CHESTPLATE, ItemHelper.Type.ARMOR, ItemHelper.Rarity.LEGENDARY) {
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

    itemService.register(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_CHESTPLATE),
        relluChestplateItem);

    ItemHelper relluHelmetItem = new ItemHelper(Material.LEATHER_HELMET, 1,
        PLUGIN_ITEM_RELLU_HELMET, ItemHelper.Type.ARMOR, ItemHelper.Rarity.LEGENDARY) {
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

    itemService.register(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_HELMET),
        relluHelmetItem);

    ItemHelper relluLeggingsItem = new ItemHelper(Material.LEATHER_LEGGINGS, 1,
        PLUGIN_ITEM_RELLU_LEGGINGS, ItemHelper.Type.ARMOR, ItemHelper.Rarity.LEGENDARY) {
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

    itemService.register(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_LEGGINGS),
        relluLeggingsItem);

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

    itemService.register(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_SHIELD),
        relluShieldItem);

    ItemHelper relluPickaxeItem = new ItemHelper(Material.NETHERITE_PICKAXE, 1,
        PLUGIN_ITEM_RELLU_PICKAXE, ItemHelper.Type.TOOL, ItemHelper.Rarity.LEGENDARY) {
      @Override
      public void init() {
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

    itemService.register(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_PICKAXE),
        relluPickaxeItem);

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

    itemService.register(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_RELLU_SWORD), relluSwordItem);

    serviceContext.getInventoryService().create(plugin, PLUGIN_INVENTORY_ADMIN_TOOLS,
            Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dAdmin Tools", 9,
            ItemHelper.Type.NONE).withFixedItem(
            itemService.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_POSITION_AXE)).orElseThrow())
        .withFixedItem(
            itemService.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET))
                .orElseThrow()).withFixedItem(
            itemService.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR)).orElseThrow())
        .withFixedItem(itemService.find(RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS))
            .orElseThrow());

    TranslationService translationService = serviceContext.getTranslationService();

    int itemCount = itemService.getAll().size();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_ITEMS_REGISTERED, itemCount));

    int inventoryCount = serviceContext.getInventoryService().getAllByNamespace(plugin.getName())
        .size();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_INVENTORIES_REGISTERED, inventoryCount));



    Object o = serviceContext.getItemService()
        .findByIdentifier(PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED).orElseThrow();
  }
}