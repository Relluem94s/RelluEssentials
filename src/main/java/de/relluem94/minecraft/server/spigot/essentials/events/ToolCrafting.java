package de.relluem94.minecraft.server.spigot.essentials.events;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;

/**
 *
 * @author rellu
 */
public class ToolCrafting implements Listener {

    private final Material[] netherite = new Material[]{Material.NETHERITE_HOE, Material.NETHERITE_AXE, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_SWORD, Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS};
    private final Material[] diamond = new Material[]{Material.DIAMOND_HOE, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_SWORD, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.DIAMOND_HORSE_ARMOR};
    private final Material[] iron = new Material[]{Material.IRON_HOE, Material.IRON_AXE, Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_SWORD, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.IRON_HORSE_ARMOR};
    private final Material[] gold = new Material[]{Material.GOLDEN_HOE, Material.GOLDEN_AXE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL, Material.GOLDEN_SWORD, Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS, Material.GOLDEN_HORSE_ARMOR};
    private final Material[] chainmail = new Material[]{Material.STONE_HOE, Material.STONE_AXE, Material.STONE_PICKAXE, Material.STONE_SHOVEL, Material.STONE_SWORD, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS};
    private final Material[] leather = new Material[]{Material.SHIELD, Material.SHEARS, Material.FLINT_AND_STEEL, Material.COMPASS, Material.CLOCK, Material.FISHING_ROD, Material.WOODEN_HOE, Material.WOODEN_AXE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL, Material.WOODEN_SWORD, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.LEATHER_HORSE_ARMOR};

    @EventHandler
    public void addRarityToTools(PrepareSmithingEvent e) {
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

    private void addRarity(Material[] mats, ItemStack is, Rarity rarity){
        ItemMeta im = is.getItemMeta();
        for (Material m : mats) {
            if (is.getType().equals(m) && im != null) {
                im.setLore(List.of(rarity.getPrefix() + rarity.getDisplayName()));
                is.setItemMeta(im);
            }
        }
    }

    @EventHandler
    public void addRarityToTools(PrepareItemCraftEvent e) {
        if (e.getRecipe() != null) {
            addRarity(netherite, e.getRecipe().getResult(), Rarity.EPIC);
            addRarity(diamond, e.getRecipe().getResult(), Rarity.RARE);
            addRarity(iron, e.getRecipe().getResult(), Rarity.UNCOMMON);
            addRarity(gold, e.getRecipe().getResult(), Rarity.COMMON);
            addRarity(chainmail, e.getRecipe().getResult(), Rarity.UNCOMMON);
            addRarity(leather, e.getRecipe().getResult(), Rarity.COMMON);
        }
    }
}
