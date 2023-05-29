package de.relluem94.minecraft.server.spigot.essentials.events;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

/* Better Call Soil */
public class BetterSoil implements Listener {

    @EventHandler
    public void onChange(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) {
            Block b = e.getClickedBlock();
            if (b.getType().equals(Material.FARMLAND) && (!e.getPlayer().isSneaking())) {
                    e.setUseInteractedBlock(Event.Result.DENY);
                    e.setCancelled(true);
                
            }
        }
    }

    @EventHandler
    public void onChange(EntityInteractEvent e) {
        Block b = e.getBlock();
        if (b.getType().equals(Material.FARMLAND)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHarvest(PlayerHarvestBlockEvent e){
        Player p = e.getPlayer();
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        
        List<ItemStack> lis = BagHelper.collectItemStacks(e.getItemsHarvested(), e.getPlayer(), pe);
        e.getItemsHarvested().removeAll(lis);

        if(EnchantmentHelper.hasEnchant(e.getPlayer().getInventory().getItemInMainHand(), CustomEnchants.telekinesis)){
            for(ItemStack is : e.getItemsHarvested()){
                p.getInventory().addItem(is);
            }
            e.getItemsHarvested().clear();
        }


    }
}
