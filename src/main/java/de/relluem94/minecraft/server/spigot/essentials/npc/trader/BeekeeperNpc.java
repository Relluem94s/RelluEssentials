package de.relluem94.minecraft.server.spigot.essentials.npc.trader;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemBuyPrice;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemSellPrice;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.enums.ItemPrice;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class BeekeeperNpc extends TraderNpc {

  private final TranslationService translationService;

  public BeekeeperNpc(ServiceContext serviceContext) {
    super("§dBeekeeper", Profession.NONE, Type.BEEKEEPER);
    translationService = serviceContext.getTranslationService();
  }

  private ItemHelper resolveDisabledItem() {
    return ItemRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
        .orElseThrow();
  }

  private ItemHelper resolveCloseItem() {
    return ItemRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE))
        .orElseThrow();
  }

  @Override
  public Inventory getMainGUI() {
    Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, getTitle()),
        resolveDisabledItem().getCustomItem());

    inv.setItem(10, createTradableItem(Material.BEEHIVE));
    inv.setItem(11, createTradableItem(Material.HONEYCOMB_BLOCK));
    inv.setItem(12, createTradableItem(Material.HONEY_BLOCK));
    inv.setItem(13, createTradableItem(Material.HONEYCOMB));
    inv.setItem(14, createTradableItem(Material.HONEY_BOTTLE));
    inv.setItem(15, createTradableItem(Material.CANDLE));

    inv.setItem(28, createTradableCustomHead(CustomHeads.WHITE_CANDLE, 300, 300));
    inv.setItem(29, createTradableCustomHead(CustomHeads.CYAN_CANDLE, 300, 300));
    inv.setItem(30, createTradableCustomHead(CustomHeads.RED_CANDLE, 300, 300));
    inv.setItem(31, createTradableCustomHead(CustomHeads.BLUE_CANDLE, 300, 300));
    inv.setItem(32, createTradableCustomHead(CustomHeads.GRAY_CANDLE, 300, 300));
    inv.setItem(33, createTradableCustomHead(CustomHeads.LIME_CANDLE, 300, 300));
    inv.setItem(34, createTradableCustomHead(CustomHeads.MAGENTA_CANDLE, 300, 300));

    inv.setItem(37, createTradableCustomHead(CustomHeads.PINK_CANDLE, 300, 300));
    inv.setItem(38, createTradableCustomHead(CustomHeads.BLACK_CANDLE, 300, 300));
    inv.setItem(39, createTradableCustomHead(CustomHeads.GREEN_CANDLE, 300, 300));
    inv.setItem(40, createTradableCustomHead(CustomHeads.ORANGE_CANDLE, 300, 300));
    inv.setItem(41, createTradableCustomHead(CustomHeads.BROWN_CANDLE, 300, 300));
    inv.setItem(42, createTradableCustomHead(CustomHeads.LIGHT_BLUE_CANDLE, 300, 300));
    inv.setItem(43, createTradableCustomHead(CustomHeads.LIGHT_GRAY_CANDLE, 300, 300));

    inv.setItem(53, resolveCloseItem().getCustomItem());

    return inv;
  }

  private ItemStack createTradableCustomHead(CustomHeads head, int buyPrice, int sellPrice) {
    ItemStack item = PlayerHeadHelper.getCustomSkull(head);
    ItemMeta meta = item.getItemMeta();
    if (meta == null) {
      return item;
    }

    List<String> lore = new ArrayList<>(meta.getLore() != null ? meta.getLore() : List.of());
    lore.add(translationService.get(MessageKey.PLUGIN_ITEM_BUY_PRICE_MESSAGE,
        PLUGIN_NAME_MONEY,
        String.valueOf(buyPrice),
        PLUGIN_NAME_MONEY,
        String.valueOf(buyPrice * 64)));
    lore.add(translationService.get(MessageKey.PLUGIN_ITEM_SELL_PRICE_MESSAGE,
        PLUGIN_NAME_MONEY,
        String.valueOf(sellPrice),
        PLUGIN_NAME_MONEY,
        String.valueOf(sellPrice * 64)));
    meta.setLore(lore);

    meta.getPersistentDataContainer().set(itemBuyPrice(), PersistentDataType.INTEGER, buyPrice);
    meta.getPersistentDataContainer().set(itemSellPrice(), PersistentDataType.INTEGER, sellPrice);

    item.setItemMeta(meta);
    return item;
  }


  private ItemStack createTradableItem(Material material) {
    ItemStack item = new ItemStack(material, 1);
    ItemMeta meta = item.getItemMeta();
    if (meta == null) {
      return item;
    }

    ItemPrice price = ItemPrice.from(material);
    int buyPrice = price.getBuyPrice();
    int sellPrice = price.getSellPrice();

    List<String> lore = new ArrayList<>(meta.getLore() != null ? meta.getLore() : List.of());
    lore.add(translationService.get(MessageKey.PLUGIN_ITEM_BUY_PRICE_MESSAGE,
        PLUGIN_NAME_MONEY,
        String.valueOf(buyPrice),
        PLUGIN_NAME_MONEY,
        String.valueOf(buyPrice * 64)));
    lore.add(translationService.get(MessageKey.PLUGIN_ITEM_SELL_PRICE_MESSAGE,
        PLUGIN_NAME_MONEY,
        String.valueOf(sellPrice),
        PLUGIN_NAME_MONEY,
        String.valueOf(sellPrice * 64)));
    meta.setLore(lore);

    item.setItemMeta(meta);
    return item;
  }
}