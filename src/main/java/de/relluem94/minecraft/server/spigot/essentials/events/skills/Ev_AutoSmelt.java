package de.relluem94.minecraft.server.spigot.essentials.events.skills;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.autosmelt;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.telekenesis;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_EMPTY_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_FULL_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT_SEPERATOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LOW_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_MAX;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.enums.ItemRarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.enums.ItemType;
import java.util.Arrays;
import java.util.List;
import net.minecraft.server.v1_16_R3.Enchantment.Rarity;
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
            
            for(ItemStack is : matrix){
                if(is != null){
                    consoleSendMessage("Type: ", is.getType().name() + " " + is.getAmount() + " " + is.getItemMeta().getDisplayName());
                    if(is.getType().equals(Material.NETHERITE_PICKAXE)){
                        pickaxe = is;
                    }
                }
                
            }

            consoleSendMessage("§aType: §r", pickaxe.getType().name() + " " + pickaxe.getAmount());
            
            
            consoleSendMessage("asf: " + 
                               matrix.contains(CustomItems.autoSmeltFurnace.getCustomItem()) + " ast: " +
                               matrix.contains(CustomItems.autoSmeltTank.getCustomItem()), " pick: " + 
                               matrix.contains(pickaxe) + " pick2: " + 
                               matrix.contains(CustomItems.autoSmeltNetheritePickAxe.getCustomItem()) + " is: " + 
                               matrix.contains(new ItemStack(Material.NETHERITE_PICKAXE, 1))
            );
            
            
            if(matrix.contains(CustomItems.autoSmeltFurnace.getCustomItem()) && matrix.contains(CustomItems.autoSmeltTank.getCustomItem()) && matrix.contains(pickaxe)){
                int asnp = matrix.indexOf(pickaxe);
                int asf = matrix.indexOf(CustomItems.autoSmeltFurnace.getCustomItem());
                int ast = matrix.indexOf(CustomItems.autoSmeltTank.getCustomItem());

                
                

                
                if(!matrix.get(asnp).getEnchantments().isEmpty() && matrix.get(asnp).getEnchantments().containsKey(autosmelt)){
                    e.getInventory().setResult(null);
                    consoleSendMessage("§acase: ", "§c1");
                }
                else{
                    consoleSendMessage("§acase: ", "§c2");
                }
                
                
                
            }
            else if(matrix.contains(CustomItems.autoSmeltNetheritePickAxe.getCustomItem())) {
                consoleSendMessage("§acase: ", "§c3");
            }
            else{
                e.getInventory().setResult(null);
                consoleSendMessage("§acase: ", "§c4");
                
            }
            
           
            if (e.getInventory().getResult() != null) {
                ItemStack i = e.getInventory().getResult();
                ItemMeta im = i.getItemMeta();
                im.setLore(setFuel(im.getLore(), PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_FULL_COLOR + PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_MAX));
                i.setItemMeta(im);
            }
        }
        consoleSendMessage(">>", "-----------------------------------------------------------------------");
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
                if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(telekenesis)) {
                    if (p.getInventory().firstEmpty() >= 0) {
                        for (ItemStack is : b.getDrops()) {
                            p.getInventory().addItem(smelt(is));
                        }

                    } else {
                        for (ItemStack is : b.getDrops()) {
                            p.getWorld().dropItem(p.getLocation(), smelt(is));
                        }
                    }
                } else {
                    for (ItemStack is : b.getDrops()) {
                        b.getWorld().dropItemNaturally(b.getLocation(), smelt(is));
                    }
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
