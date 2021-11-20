package de.relluem94.minecraft.server.spigot.essentials.events.skills;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.autosmelt;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.telekinesis;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_FULL_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT_SEPERATOR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LOW_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_MAX;

public class Ev_AutoSmelt implements Listener {

    @EventHandler
    public void preventPlacementOfFurnace(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().equals(CustomItems.autoSmeltFurnace.getCustomItem()) || e.getPlayer().getInventory().getItemInOffHand().equals(CustomItems.autoSmeltFurnace.getCustomItem())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void pickaxeCrafting(PrepareItemCraftEvent e) {
        if (e.getRecipe() != null && e.getRecipe().getResult().hasItemMeta() && CustomItems.autoSmeltNetheritePickAxe.almostEquals(e.getRecipe().getResult())) {

            List<ItemStack> matrix = Arrays.asList(e.getInventory().getMatrix());
            ItemStack pickaxe = new ItemStack(Material.APPLE);

            for (ItemStack is : matrix) {
                if (is != null) {
                    if (is.getType().equals(CustomItems.autoSmeltNetheritePickAxe.getMaterial())) {
                        pickaxe = is;
                    }
                }

            }

            if (matrix.contains(CustomItems.autoSmeltFurnace.getCustomItem()) && matrix.contains(CustomItems.autoSmeltTank.getCustomItem()) && matrix.contains(pickaxe)) {
                // Crafting Smelting Pickaxe
                int asnp = matrix.indexOf(pickaxe);
                if (!matrix.get(asnp).getEnchantments().isEmpty() && matrix.get(asnp).getEnchantments().containsKey(autosmelt)) {
                    // Crafting Smelting Pickaxe but is already Smelting Pickaxe
                    e.getInventory().setResult(null);
                } else {
                    // Crafting Smelting Pickaxe
                    e.getInventory().getResult().addUnsafeEnchantments(pickaxe.getEnchantments());
                    ItemMeta im = e.getInventory().getResult().getItemMeta();
                    List<String> lore = pickaxe.getItemMeta().getLore();
                    lore.remove(lore.size() - 1);
                    lore.addAll(im.getLore());
                    im.setLore(lore);
                    e.getInventory().getResult().setItemMeta(im);
                }
            } else if (matrix.contains(pickaxe) && matrix.contains(new ItemStack(Material.LAVA_BUCKET, 1))) {
                // Filling up Smelting Pickaxe
                int asnp = matrix.indexOf(pickaxe);
                if (!matrix.get(asnp).getEnchantments().isEmpty()) {
                    if (!matrix.get(asnp).getEnchantments().containsKey(autosmelt)) {
                        // Filling up Smelting Pickaxe but is no Smelting Pickaxe
                        e.getInventory().setResult(null);
                    } else {
                        // Filling up Smelting Pickaxe
                        e.getInventory().getResult().addUnsafeEnchantments(pickaxe.getEnchantments());
                        ItemMeta im = e.getInventory().getResult().getItemMeta();
                        List<String> lore = pickaxe.getItemMeta().getLore();
                        im.setLore(lore);
                        e.getInventory().getResult().setItemMeta(im);
                    }
                } else {
                    // Filling up Smelting Pickaxe but is no Smelting Pickaxe (no Enchants at all)
                    e.getInventory().setResult(null);
                }

                if (e.getInventory().getResult() != null) {
                    // Sets Fuel Level
                    ItemStack i = e.getInventory().getResult();
                    ItemMeta im = i.getItemMeta();
                    im.setLore(setFuel(im.getLore(), PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_FULL_COLOR + PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_MAX));
                    i.setItemMeta(im);
                }
            } else {
                // Any other case. 
                e.getInventory().setResult(null);
            }
        }
    }

    private List<String> setFuel(List<String> lore, String fuel) {
        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).startsWith(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK)) {
                lore.set(i, PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK + String.format(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT, fuel));
            }
        }

        return lore;
    }

    @EventHandler
    public void onBreakSmelt(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock(); //TODO Check blockfaces for items like torches.

        if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(autosmelt)) {
            // PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LOW_COLOR PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_FULL_COLOR PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_COLOR
            boolean hasFuel = true;
            int fuel = 0;
            ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
            List<String> lore = im.getLore();
            for (int i = 0; i < lore.size(); i++) {
                if (lore.get(i).startsWith(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK)) {
                    fuel = Integer.parseInt(lore.get(i)
                            .replace(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK, "")
                            .split(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT_SEPERATOR)[0]
                            .replace(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_COLOR, "")
                            .replace(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LOW_COLOR, "")
                            .replace(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_FULL_COLOR, "")
                    );
                    if (fuel == 0) {
                        hasFuel = false;
                    } else {
                        fuel--;
                        String color = "";
                        if (fuel >= PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_MAX / 2) {
                            color = PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_FULL_COLOR;
                        } else if (fuel < PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_MAX / 2) {
                            color = PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LOW_COLOR;
                        } else if (fuel == 0) {
                            color = PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_COLOR;
                            sendMessage(p, PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_MESSAGE);
                        }

                        lore.set(i, PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK + String.format(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT, color + fuel));
                    }
                }
            }
            im.setLore(lore);
            p.getInventory().getItemInMainHand().setItemMeta(im);

            if (hasFuel) {
                if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(telekinesis)) {
                    if (p.getInventory().firstEmpty() >= 0) {
                        b.getDrops().forEach(is -> {
                            p.getInventory().addItem(smelt(is));
                        });

                    } else {
                        b.getDrops().forEach(is -> {
                            p.getWorld().dropItem(p.getLocation(), smelt(is));
                        });
                    }
                } else {
                    b.getDrops().forEach(is -> {
                        b.getWorld().dropItemNaturally(b.getLocation(), smelt(is));
                    });
                }
                e.setDropItems(false);
            } else {
                e.setDropItems(true);
            }
        } else {
            e.setDropItems(true);
        }
    }

    private ItemStack smelt(ItemStack im) {
        int amount = (int) (Math.random() * (4 - 1) + 1);
        switch (im.getType()) {
            case COAL:
                amount = (int) (Math.random() * (6 - 2) + 2);
                im.setAmount(amount);
                break;
            case DIAMOND:
                amount = (int) (Math.random() * (5 - 1) + 1);
                im.setAmount(amount);
                break;
            case IRON_ORE:
                amount = (int) (Math.random() * (3 - 1) + 1);
                im.setType(Material.IRON_INGOT);
                im.setAmount(amount);
                break;
            case GOLD_ORE:
                im.setType(Material.GOLD_INGOT);
                break;
            case CACTUS:
                im.setType(Material.GREEN_DYE);
                break;
            case WET_SPONGE:
                im.setType(Material.SPONGE);
                break;
            case COBBLESTONE:
                im.setType(Material.STONE);
                break;
            case STONE:
                im.setType(Material.COBBLESTONE);
                break;
            case NETHERRACK:
                im.setType(Material.NETHER_BRICK);
                break;
            case CLAY:
                im.setType(Material.BRICK);
                im.setAmount(4);
                break;
            case OAK_LOG:
            case BIRCH_LOG:
            case SPRUCE_LOG:
            case DARK_OAK_LOG:
            case JUNGLE_LOG:
            case ACACIA_LOG:
                im.setType(Material.CHARCOAL);
                im.setAmount(amount);
                break;
            default:
                break;
        }
        return im;
    }
}
