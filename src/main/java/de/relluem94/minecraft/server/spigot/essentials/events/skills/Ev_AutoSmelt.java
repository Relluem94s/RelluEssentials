package de.relluem94.minecraft.server.spigot.essentials.events.skills;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.autosmelt;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.telekenesis;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Ev_AutoSmelt implements Listener {
    
    @EventHandler
    public void onBreakSmelt(BlockBreakEvent e) {
        Player p = e.getPlayer();

        Block b = e.getBlock(); //TODO Check blockfaces for items like torches.
        
        if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(autosmelt)) {
            if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(telekenesis)) {
                if (p.getInventory().firstEmpty() >= 0) {
                    for (ItemStack im : b.getDrops()) {
                        p.getInventory().addItem(smelt(im));
                    }

                } else {
                    for (ItemStack im : b.getDrops()) {
                        p.getWorld().dropItem(p.getLocation(), smelt(im));
                    }
                }
            } else {
                for (ItemStack im : b.getDrops()) {
                    b.getWorld().dropItemNaturally(b.getLocation(), smelt(im));
                }
            }
            e.setDropItems(false);
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
