package de.relluem94.minecraft.server.spigot.essentials.events;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
/**
 *
 * @author rellu
 */
public class CustomEnchantment implements Listener {

    @EventHandler
    public void enchantApply(PrepareAnvilEvent e){
        if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null &&  !e.getInventory().getRenameText().equals(ItemHelper.getItemName(e.getInventory().getItem(0)))){
            if(
                e.getInventory().getItem(1).equals(CustomEnchants.autosmelt.getBook().getCustomItem()) ||
                e.getInventory().getItem(1).equals(CustomEnchants.telekinesis.getBook().getCustomItem()) ||
                e.getInventory().getItem(1).equals(CustomEnchants.delicate.getBook().getCustomItem()) ||
                e.getInventory().getItem(1).equals(CustomEnchants.replenishment.getBook().getCustomItem()) || 
                e.getInventory().getItem(1).equals(CustomEnchants.thunderstrike.getBook().getCustomItem()) || 
                e.getInventory().getItem(1).equals(CustomEnchants.lifesteal.getBook().getCustomItem())
            ){
                e.setResult(null);
            }
            
        }

        try{
            if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && e.getResult() != null){
                if(e.getInventory().getItem(0).hasItemMeta() && e.getInventory().getItem(0).getItemMeta().hasEnchant((CustomEnchants.autosmelt))){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.autosmelt.removeFrom(is);
                    CustomEnchants.autosmelt.addTo(is);
                    e.setResult(is);
                }
                else if(e.getInventory().getItem(0).hasItemMeta() && e.getInventory().getItem(0).getItemMeta().hasEnchant((CustomEnchants.telekinesis))){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.telekinesis.removeFrom(is);
                    CustomEnchants.telekinesis.addTo(is);
                    e.setResult(is);
                }
                else if(e.getInventory().getItem(0).hasItemMeta() && e.getInventory().getItem(0).getItemMeta().hasEnchant((CustomEnchants.replenishment))){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.replenishment.removeFrom(is);
                    CustomEnchants.replenishment.addTo(is);
                    e.setResult(is);
                }
                else if(e.getInventory().getItem(0).hasItemMeta() && e.getInventory().getItem(0).getItemMeta().hasEnchant((CustomEnchants.delicate))){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.delicate.removeFrom(is);
                    CustomEnchants.delicate.addTo(is);
                    e.setResult(is);
                }
                else if(e.getInventory().getItem(0).hasItemMeta() && e.getInventory().getItem(0).getItemMeta().hasEnchant((CustomEnchants.thunderstrike))){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.thunderstrike.removeFrom(is);
                    CustomEnchants.thunderstrike.addTo(is);
                    e.setResult(is);
                }
                else if(e.getInventory().getItem(0).hasItemMeta() && e.getInventory().getItem(0).getItemMeta().hasEnchant((CustomEnchants.lifesteal))){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.lifesteal.removeFrom(is);
                    CustomEnchants.lifesteal.addTo(is);
                    e.setResult(is);
                }
            }
            
            
            if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && e.getInventory().getItem(1).equals(CustomEnchants.autosmelt.getBook().getCustomItem())){
                ItemStack is = e.getInventory().getItem(0).clone();
                CustomEnchants.autosmelt.addTo(is);
                e.setResult(is);
            }
            else if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && e.getInventory().getItem(1).equals(CustomEnchants.telekinesis.getBook().getCustomItem())){
                ItemStack is = e.getInventory().getItem(0).clone();
                CustomEnchants.telekinesis.addTo(is);
                e.setResult(is);
            }
            else if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && e.getInventory().getItem(1).equals(CustomEnchants.replenishment.getBook().getCustomItem())){
                ItemStack is = e.getInventory().getItem(0).clone();
                CustomEnchants.replenishment.addTo(is);
                e.setResult(is);
            }
            else if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && e.getInventory().getItem(1).equals(CustomEnchants.delicate.getBook().getCustomItem())){
                ItemStack is = e.getInventory().getItem(0).clone();
                CustomEnchants.delicate.addTo(is);
                e.setResult(is);
            }
            else if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && e.getInventory().getItem(1).equals(CustomEnchants.thunderstrike.getBook().getCustomItem())){
                ItemStack is = e.getInventory().getItem(0).clone();
                CustomEnchants.thunderstrike.addTo(is);
                e.setResult(is);
            }
            else if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && e.getInventory().getItem(1).equals(CustomEnchants.lifesteal.getBook().getCustomItem())){
                ItemStack is = e.getInventory().getItem(0).clone();
                CustomEnchants.lifesteal.addTo(is);
                e.setResult(is);
            }
        }
        catch(IllegalArgumentException ex){
            Logger.getLogger(CustomEnchantment.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }
}