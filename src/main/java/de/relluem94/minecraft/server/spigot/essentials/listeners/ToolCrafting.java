package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem.Rarity;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Listener that assigns rarity lore to crafted and smithed tools and armor.
 *
 * <p>Handles {@link PrepareItemCraftEvent} and
 * {@link PrepareSmithingEvent} to apply the appropriate
 * {@link Rarity}
 * label to the resulting item's lore based on its material tier.</p>
 *
 * @author rellu
 */
@ListenerName("ToolCrafting")
public class ToolCrafting implements ListenerConstruct {

  @Override
  public void injectContext(ServiceContext context) {

  }

  private final Material[] netherite = new Material[]{Material.NETHERITE_HOE,
      Material.NETHERITE_AXE, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL,
      Material.NETHERITE_SWORD, Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE,
      Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS};
  private final Material[] diamond = new Material[]{Material.DIAMOND_HOE, Material.DIAMOND_AXE,
      Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_SWORD,
      Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS,
      Material.DIAMOND_BOOTS, Material.DIAMOND_HORSE_ARMOR};
  private final Material[] iron = new Material[]{Material.IRON_HOE, Material.IRON_AXE,
      Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_SWORD, Material.IRON_HELMET,
      Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
      Material.IRON_HORSE_ARMOR};
  private final Material[] gold = new Material[]{Material.GOLDEN_HOE, Material.GOLDEN_AXE,
      Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL, Material.GOLDEN_SWORD,
      Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS,
      Material.GOLDEN_BOOTS, Material.GOLDEN_HORSE_ARMOR};
  private final Material[] chainmail = new Material[]{Material.STONE_HOE, Material.STONE_AXE,
      Material.STONE_PICKAXE, Material.STONE_SHOVEL, Material.STONE_SWORD,
      Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS,
      Material.CHAINMAIL_BOOTS};
  private final Material[] leather = new Material[]{Material.SHIELD, Material.SHEARS,
      Material.FLINT_AND_STEEL, Material.COMPASS, Material.CLOCK, Material.FISHING_ROD,
      Material.WOODEN_HOE, Material.WOODEN_AXE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL,
      Material.WOODEN_SWORD, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
      Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.LEATHER_HORSE_ARMOR};

  /**
   * Upgrades the rarity lore of a netherite smithing result from
   * {@link Rarity#RARE} to {@link Rarity#EPIC}.
   *
   * <p>Replaces the existing RARE rarity entry in the item lore with an EPIC rarity entry
   * when the smithing result matches a netherite material.</p>
   *
   * @param e the {@link org.bukkit.event.inventory.PrepareSmithingEvent} fired when a smithing
   *          table result is prepared
   */
  @EventHandler
  public void addRarityToTools(@NotNull PrepareSmithingEvent e) {
    if (e.getResult() != null) {
      ItemMeta im = e.getResult().getItemMeta();

      for (Material m : netherite) {
        if (e.getResult().getType().equals(m) && im != null) {
          List<String> lore = im.getLore();
          if (lore != null) {
            lore.add(Rarity.EPIC.getPrefix() + Rarity.EPIC.getDisplayName());
            lore.remove(Rarity.RARE.getPrefix() + Rarity.RARE.getDisplayName());
            im.setLore(lore);
          }

          e.getResult().setItemMeta(im);
        }
      }
    }
  }

  /**
   * Assigns a rarity lore entry to the result of a crafting recipe based on its material tier.
   *
   * <p>Maps each material tier to its corresponding
   * {@link Rarity}:
   * <ul>
   *   <li>Netherite → {@link Rarity#EPIC}</li>
   *   <li>Diamond → {@link Rarity#RARE}</li>
   *   <li>Iron, Chainmail → {@link Rarity#UNCOMMON}</li>
   *   <li>Gold, Leather → {@link Rarity#COMMON}</li>
   * </ul>
   * </p>
   *
   * @param e the {@link org.bukkit.event.inventory.PrepareItemCraftEvent} fired when a crafting
   *          table result is prepared
   */
  @EventHandler
  public void addRarityToTools(@NotNull PrepareItemCraftEvent e) {
    if (e.getRecipe() != null) {
      addRarity(netherite, e.getRecipe().getResult(), Rarity.EPIC);
      addRarity(diamond, e.getRecipe().getResult(), Rarity.RARE);
      addRarity(iron, e.getRecipe().getResult(), Rarity.UNCOMMON);
      addRarity(gold, e.getRecipe().getResult(), Rarity.COMMON);
      addRarity(chainmail, e.getRecipe().getResult(), Rarity.UNCOMMON);
      addRarity(leather, e.getRecipe().getResult(), Rarity.COMMON);
    }
  }

  private void addRarity(Material @NotNull [] mats, @NotNull ItemStack is, Rarity rarity) {
    ItemMeta im = is.getItemMeta();
    for (Material m : mats) {
      if (is.getType().equals(m) && im != null) {
        im.setLore(List.of(rarity.getPrefix() + rarity.getDisplayName()));
        is.setItemMeta(im);
      }
    }
  }
}
