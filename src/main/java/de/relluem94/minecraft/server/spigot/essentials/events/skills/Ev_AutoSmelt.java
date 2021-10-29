package de.relluem94.minecraft.server.spigot.essentials.events.skills;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.autosmelt;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.telekenesis;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT_SEPERATOR;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
    public void onBreakSmelt(BlockBreakEvent e) {
        Player p = e.getPlayer();

        Block b = e.getBlock(); //TODO Check blockfaces for items like torches.

        if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(autosmelt)) {

            boolean hasFuel = true;
            int fuel = 0;
            ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
            List<String> lore = im.getLore();
            for (int i = 0; i < lore.size(); i++) {
                if (lore.get(i).startsWith(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK)) {
                    fuel = Integer.parseInt(lore.get(i).replace(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK, "").split(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT_SEPERATOR)[0]);
                    if(fuel == 0){
                        hasFuel = false;
                    }
                    else{
                        fuel--;
                        lore.set(i, PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK + String.format(PLUGIN_ENCHANTMENT_AUTOSMELT_LAVA_TANK_FUEL_LEFT, fuel));
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
                sendMessage(p, "No Fuel Left in your Smelter Pickaxe thingi, better text in Strings ;)");
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
