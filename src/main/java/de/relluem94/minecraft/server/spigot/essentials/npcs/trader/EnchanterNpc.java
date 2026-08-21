package de.relluem94.minecraft.server.spigot.essentials.npcs.trader;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_AUTOSELL_HOPPER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemBuyPrice;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemCost;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemSellPrice;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.EnchantmentRegistry;
import java.util.List;
import java.util.Objects;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

/**
 * Represents the Enchanter NPC trader, which provides a GUI for purchasing
 * custom enchantments, magic water buckets and autosell hoppers.
 * The NPC appears as a librarian villager.
 */
public class EnchanterNpc extends TraderNpc {

  private final ServiceContext serviceContext;
  private final RelluEssentials relluEssentials;

  private record ItemCostData(int cost, List<String> lore) {}

  /**
   * Creates a new EnchanterNpc with a predefined display name,
   * librarian profession and the enchanter trader type.
   */
  public EnchanterNpc(ServiceContext serviceContext, RelluEssentials relluEssentials) {
    super("§dEnchanter", Profession.LIBRARIAN, Type.ENCHANTER);
    this.serviceContext = serviceContext;
    this.relluEssentials = relluEssentials;
  }

  private @NonNull @Unmodifiable List<EnchantmentHelper> resolveRegisteredEnchantments() {
    return EnchantmentRegistry.findAll();
  }

  private @NonNull ItemHelper resolveDisabledItem() {
    return serviceContext.getItemService().find(RegistryKey.of(relluEssentials, PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
        .orElseThrow();
  }

  private @NonNull ItemHelper resolveCloseItem() {
    return serviceContext.getItemService().find(RegistryKey.of(relluEssentials, PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE)).orElseThrow();
  }

  /**
   * Builds and returns the main GUI inventory for the Enchanter NPC.
   * The inventory is pre-filled with disabled placeholder items and then
   * populated with enchantment books for each registered enchantment,
   * followed by a magic water bucket and an autosell hopper.
   *
   * @return the fully populated {@link Inventory} representing the enchanter shop GUI
   */
  @Override
  public Inventory getMainGUI() {
    Inventory inv = InventoryHelper.fillInventory(
        InventoryHelper.createInventory(54, getTitle()), resolveDisabledItem().getCustomItem());

    List<EnchantmentHelper> enchantments = resolveRegisteredEnchantments();

    int slot = 0;
    for (EnchantmentHelper enchant : enchantments) {
      slot = InventoryHelper.getNextSlot(slot);
      ItemStack book = enchant.createEnchantedBook();
      applyAdditionalLoreToItemStack(book, buildCostDataFromCost(enchant.getCost()));
      inv.setItem(slot, book);
      slot++;
    }


    int magicWaterSlot = InventoryHelper.getNextSlot(slot);
    serviceContext.getItemService().find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET))
        .ifPresent(item -> {
          ItemStack magicWater = item.getCustomItem().clone();
          applyAdditionalLoreToItemStack(magicWater, buildCostData(magicWater));
          inv.setItem(magicWaterSlot, magicWater);
        });

    int autoSellSlot = InventoryHelper.getNextSlot(magicWaterSlot + 1);
    serviceContext.getItemService().find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_AUTOSELL_HOPPER))
        .ifPresent(item -> {
          ItemStack hopper = item.getCustomItem().clone();
          applyAdditionalLoreToItemStack(hopper, buildCostData(hopper));
          inv.setItem(autoSellSlot, hopper);
        });

    inv.setItem(53, resolveCloseItem().getCustomItem());

    return inv;
  }

  private void applyAdditionalLoreToItemStack(@NonNull ItemStack itemStack, ItemCostData costData) {
    if (costData.lore().isEmpty()) {
      return;
    }
    ItemMeta meta = itemStack.getItemMeta();
    if (meta == null) {
      return;
    }
    List<String> existingLore = meta.getLore() != null ? new java.util.ArrayList<>(meta.getLore()) : new java.util.ArrayList<>();
    existingLore.addAll(costData.lore());
    meta.setLore(existingLore);

    Objects.requireNonNull(meta).getPersistentDataContainer()
        .set(itemSellPrice(), PersistentDataType.INTEGER, costData.cost());
    Objects.requireNonNull(meta).getPersistentDataContainer()
        .set(itemBuyPrice(), PersistentDataType.INTEGER, costData.cost());

    itemStack.setItemMeta(meta);
  }

  private ItemCostData buildCostData(@NonNull ItemStack itemStack) {
    return ItemHelper.resolveCostFromItemStack(itemStack, itemCost())
        .map(this::buildCostDataFromCost)
        .orElse(new ItemCostData(0, List.of()));
  }

  private ItemCostData buildCostDataFromCost(int cost) {
    return new ItemCostData(cost, List.of(
        serviceContext.getTranslationService().get(MessageKey.PLUGIN_ITEM_BUY_PRICE_MESSAGE,
            PLUGIN_NAME_MONEY,
            String.valueOf(cost),
            PLUGIN_NAME_MONEY,
            String.valueOf(cost * 64)),
        serviceContext.getTranslationService().get(MessageKey.PLUGIN_ITEM_SELL_PRICE_MESSAGE,
            PLUGIN_NAME_MONEY,
            String.valueOf(cost),
            PLUGIN_NAME_MONEY,
            String.valueOf(cost * 64))
    ));
  }
}