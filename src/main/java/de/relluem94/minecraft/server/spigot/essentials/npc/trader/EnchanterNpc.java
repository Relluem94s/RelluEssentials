package de.relluem94.minecraft.server.spigot.essentials.npc.trader;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_AUTOSELL_HOPPER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.EnchantmentRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

/**
 * Represents the Enchanter NPC trader, which provides a GUI for purchasing
 * custom enchantments, magic water buckets and autosell hoppers.
 * The NPC appears as a librarian villager.
 */
public class EnchanterNpc extends TraderNpc {

  /**
   * Creates a new EnchanterNpc with a predefined display name,
   * librarian profession and the enchanter trader type.
   */
  public EnchanterNpc() {
    super("§dEnchanter", Profession.LIBRARIAN, Type.ENCHANTER);
  }

  private List<EnchantmentHelper> resolveRegisteredEnchantments() {
    return Stream.of(
            EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_SCAVENGERS
        )
        .map(key -> EnchantmentRegistry.find(RegistryKey.of(RelluEssentials.getInstance(), key)))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private ItemHelper resolveDisabledItem() {
    return ItemRegistry.find(RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
        .orElseThrow();
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
      inv.setItem(slot, enchant.getBook().getCustomItem());
      slot++;
    }

    int finalSlot = InventoryHelper.getNextSlot(slot);
    ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET))
        .ifPresent(item -> inv.setItem(finalSlot, item.getCustomItem()));
    slot++;
    int autoSellSlot = InventoryHelper.getNextSlot(slot);
    ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_AUTOSELL_HOPPER))
        .ifPresent(item -> inv.setItem(autoSellSlot, item.getCustomItem()));

    return inv;
  }
}