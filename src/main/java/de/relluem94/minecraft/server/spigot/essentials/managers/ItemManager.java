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
import org.bukkit.inventory.meta.BlockStateMeta;
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

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(plugin.getName(), PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS),
        Material.LEATHER_BOOTS
    )
        .amount(1)
        .displayName(PLUGIN_ITEM_CLOUDBOOTS)
        .type(Type.ARMOR)
        .rarity(Rarity.LEGENDARY)
        .lore(List.of(PLUGIN_ITEM_CLOUDBOOTS_LORE1, PLUGIN_ITEM_CLOUDBOOTS_LORE2))
        .metaModifier(meta -> {
          LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
          leatherMeta.setColor(Color.SILVER);
          leatherMeta.setUnbreakable(true);
          leatherMeta.addEnchant(Enchantment.PROTECTION, 3, true);
        })
        .build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(plugin.getName(), PLUGIN_ITEM_NAMESPACE_RELLU_BOOTS),
        Material.LEATHER_BOOTS)
        .amount(1)
        .displayName(PLUGIN_ITEM_RELLU_BOOTS)
        .type(Type.ARMOR)
        .rarity(Rarity.LEGENDARY)
        .metaModifier(meta -> {
          LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
          leatherMeta.setColor(Color.fromRGB(227, 59, 46));
          leatherMeta.addEnchant(Enchantment.THORNS, 94, true);
          leatherMeta.addEnchant(Enchantment.LOOTING, 94, true);
          leatherMeta.addEnchant(Enchantment.PROTECTION, 94, true);
          leatherMeta.setUnbreakable(true);
        })
        .build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(plugin.getName(), PLUGIN_ITEM_NAMESPACE_RELLU_CHESTPLATE),
        Material.LEATHER_CHESTPLATE)
        .amount(1)
        .displayName(PLUGIN_ITEM_RELLU_CHESTPLATE)
        .type(Type.ARMOR)
        .rarity(Rarity.LEGENDARY)
        .metaModifier(meta -> {
          LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
          leatherMeta.setColor(Color.fromRGB(72, 179, 177));
          leatherMeta.addEnchant(Enchantment.THORNS, 94, true);
          leatherMeta.addEnchant(Enchantment.LOOTING, 94, true);
          leatherMeta.addEnchant(Enchantment.PROTECTION, 94, true);
          leatherMeta.setUnbreakable(true);
        })
        .build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(plugin.getName(), PLUGIN_ITEM_NAMESPACE_RELLU_HELMET),
        Material.LEATHER_HELMET)
        .amount(1)
        .displayName(PLUGIN_ITEM_RELLU_HELMET)
        .type(Type.ARMOR)
        .rarity(Rarity.LEGENDARY)
        .metaModifier(meta -> {
          LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
          leatherMeta.setColor(Color.fromRGB(243, 125, 0));
          leatherMeta.addEnchant(Enchantment.THORNS, 94, true);
          leatherMeta.addEnchant(Enchantment.LOOTING, 94, true);
          leatherMeta.addEnchant(Enchantment.PROTECTION, 94, true);
          leatherMeta.setUnbreakable(true);
        })
        .build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(plugin.getName(), PLUGIN_ITEM_NAMESPACE_RELLU_LEGGINGS),
        Material.LEATHER_LEGGINGS)
        .amount(1)
        .displayName(PLUGIN_ITEM_RELLU_LEGGINGS)
        .type(Type.ARMOR)
        .rarity(Rarity.LEGENDARY)
        .metaModifier(meta -> {
          LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
          leatherMeta.setColor(Color.fromRGB(152, 216, 1));
          leatherMeta.addEnchant(Enchantment.THORNS, 94, true);
          leatherMeta.addEnchant(Enchantment.LOOTING, 94, true);
          leatherMeta.addEnchant(Enchantment.PROTECTION, 94, true);
          leatherMeta.setUnbreakable(true);
        })
        .build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(plugin.getName(), PLUGIN_ITEM_NAMESPACE_RELLU_SHIELD),
        Material.SHIELD)
        .amount(1)
        .displayName(PLUGIN_ITEM_RELLU_SHIELD)
        .type(Type.ARMOR)
        .rarity(Rarity.LEGENDARY)
        .metaModifier(meta -> {
          BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
          Banner banner = (Banner) blockStateMeta.getBlockState();
          banner.setBaseColor(DyeColor.PURPLE);
          banner.update();
          blockStateMeta.setBlockState(banner);
          blockStateMeta.addEnchant(Enchantment.SHARPNESS, 94, true);
          blockStateMeta.addEnchant(Enchantment.THORNS, 94, true);
          blockStateMeta.addEnchant(Enchantment.LOOTING, 94, true);
          blockStateMeta.addEnchant(Enchantment.PROTECTION, 94, true);
          blockStateMeta.setUnbreakable(true);
        })
        .build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(plugin.getName(), PLUGIN_ITEM_NAMESPACE_RELLU_PICKAXE),
        Material.NETHERITE_PICKAXE)
        .amount(1)
        .displayName(PLUGIN_ITEM_RELLU_PICKAXE)
        .type(Type.TOOL)
        .rarity(Rarity.LEGENDARY)
        .metaModifier(meta -> {
          meta.addEnchant(Enchantment.LOOTING, 94, true);
          meta.addEnchant(Enchantment.EFFICIENCY, 94, true);
          meta.setUnbreakable(true);
          EnchantmentRegistry.find(
                  RegistryKey.of(plugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS))
              .ifPresent(enchant -> enchant.addTo(meta));
        })
        .build());

    itemService.register(new CustomItemBuilder(
        new RelluEssentialsNamespacedKey(plugin.getName(), PLUGIN_ITEM_NAMESPACE_RELLU_SWORD),
        Material.NETHERITE_SWORD)
        .amount(1)
        .displayName(PLUGIN_ITEM_RELLU_SWORD)
        .type(Type.WEAPON)
        .rarity(Rarity.LEGENDARY)
        .metaModifier(meta -> {
          meta.addEnchant(Enchantment.SHARPNESS, 94, true);
          meta.addEnchant(Enchantment.SWEEPING_EDGE, 94, true);
          meta.addEnchant(Enchantment.LOOTING, 94, true);
          meta.addEnchant(Enchantment.PROTECTION, 94, true);
          meta.setUnbreakable(true);
          EnchantmentRegistry.find(
                  RegistryKey.of(plugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS))
              .ifPresent(enchant -> enchant.addTo(meta));
          EnchantmentRegistry.find(
                  RegistryKey.of(plugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE))
              .ifPresent(enchant -> enchant.addTo(meta));
        })
        .build());

    serviceContext.getInventoryService().create(plugin, PLUGIN_INVENTORY_ADMIN_TOOLS,
            Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dAdmin Tools", 9,
            ItemHelper.Type.NONE).withFixedItem(
            itemService.find(new RelluEssentialsNamespacedKey(plugin.getName(), PLUGIN_ITEM_NAMESPACE_POSITION_AXE)).orElseThrow())
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