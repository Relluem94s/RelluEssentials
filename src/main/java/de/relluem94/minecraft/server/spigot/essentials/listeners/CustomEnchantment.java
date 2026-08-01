package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper.hasEnchant;

import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.registry.EnchantmentRegistry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

/**
 * Listener that handles the application of custom enchantments via the anvil UI. Intercepts
 * {@link PrepareAnvilEvent} to support enchanted books carrying plugin-specific enchantments stored
 * in the item's PersistentDataContainer.
 *
 * @author rellu
 */
public class CustomEnchantment implements Listener {

  private @NonNull @Unmodifiable List<EnchantmentHelper> resolveRegisteredEnchantments() {
    return EnchantmentRegistry.findAll();
  }

  /**
   * Checks whether itemStackSlotOne is an enchanted book that carries the desired custom
   * enchantment in its PersistentDataContainer.
   */
  private boolean isBookWithEnchant(ItemStack book, EnchantmentHelper enchant) {
    if (book == null) {
      return false;
    }
    if (book.getType() != Material.ENCHANTED_BOOK) {
      return false;
    }
    if (!(book.getItemMeta() instanceof EnchantmentStorageMeta meta)) {
      return false;
    }
    return meta.getPersistentDataContainer().has(enchant.getKey());
  }

  /**
   * Handles the anvil preparation event to apply or preserve custom enchantments.
   *
   * <p>Validates both anvil slots and applies any registered custom enchantment
   * from an enchanted book in slot one onto the item in slot zero, if compatible. Prevents invalid
   * combinations and ensures custom enchantment data is retained in the result item.
   *
   * @param e the {@link PrepareAnvilEvent} fired when the anvil output is calculated
   */
  @EventHandler
  public void enchantApply(PrepareAnvilEvent e) {
    ItemStack itemStackSlotZero = e.getInventory().getItem(0);
    ItemStack itemStackSlotOne = e.getInventory().getItem(1);
    @SuppressWarnings("all") String renameText = e.getView().getRenameText();

    if (itemStackSlotZero == null) {
      return;
    }
    if (itemStackSlotOne == null) {
      return;
    }
    if (renameText == null) {
      return;
    }

    List<EnchantmentHelper> registeredEnchantments = resolveRegisteredEnchantments();

    boolean slotOneIsCustomBook = registeredEnchantments.stream()
        .anyMatch(enchant -> isBookWithEnchant(itemStackSlotOne, enchant));

    if (!slotOneIsCustomBook) {
      if (!renameText.equals(ItemHelper.getItemName(itemStackSlotZero))) {
        e.setResult(null);
        return;
      }
    }

    boolean slotZeroIsCustomBook = registeredEnchantments.stream()
        .anyMatch(enchant -> isBookWithEnchant(itemStackSlotZero, enchant));

    if (slotZeroIsCustomBook) {
      e.setResult(null);
      return;
    }

    try {
      if (e.getResult() != null) {
        for (EnchantmentHelper enchant : registeredEnchantments) {
          if (itemStackSlotZero.hasItemMeta() && hasEnchant(itemStackSlotZero, enchant)) {
            ItemStack is = e.getResult().clone();
            enchant.removeFrom(is);
            enchant.addTo(is);
            e.setResult(is);
          }
        }
      }

      for (EnchantmentHelper enchant : registeredEnchantments) {
        if (isBookWithEnchant(itemStackSlotOne, enchant) && !hasEnchant(itemStackSlotZero, enchant)
            && enchant.getItemTarget().includes(itemStackSlotZero)) {
          ItemStack is = itemStackSlotZero.clone();
          enchant.addTo(is);
          e.setResult(is);
          break;
        }
      }

    } catch (IllegalArgumentException ex) {
      Logger.getLogger(CustomEnchantment.class.getName()).log(Level.SEVERE, ex.getMessage());
    }
  }
}