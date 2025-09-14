package de.relluem94.minecraft.server.spigot.essentials.events;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import org.bukkit.inventory.view.AnvilView;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper.hasEnchant;

/**
 *
 * @author rellu
 */
public class CustomEnchantment implements Listener {

    @EventHandler
    public void enchantApply(PrepareAnvilEvent e){
        ItemStack itemStackSlotZero = e.getInventory().getItem(0);
        ItemStack itemStackSlotOne = e.getInventory().getItem(0);

        if(itemStackSlotZero == null){
            return;
        }


        if(itemStackSlotOne == null){
            return;
        }



        if(!e.getView().getRenameText().equals(ItemHelper.getItemName(itemStackSlotZero))){
            if(
                itemStackSlotOne.equals(CustomEnchants.autosmelt.getBook().getCustomItem()) ||
                itemStackSlotOne.equals(CustomEnchants.telekinesis.getBook().getCustomItem()) ||
                itemStackSlotOne.equals(CustomEnchants.delicate.getBook().getCustomItem()) ||
                itemStackSlotOne.equals(CustomEnchants.replenishment.getBook().getCustomItem()) ||
                itemStackSlotOne.equals(CustomEnchants.thunderstrike.getBook().getCustomItem())
            ){
                System.out.println("TEST_11");
                e.setResult(null);
            }
            else {
                System.out.println("TEST_00");
            }
            
        }

        try{
            if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && e.getResult() != null){
                if(itemStackSlotZero.hasItemMeta() && hasEnchant(itemStackSlotZero, CustomEnchants.autosmelt)){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.autosmelt.removeFrom(is);
                    CustomEnchants.autosmelt.addTo(is);
                    e.setResult(is);
                    System.out.println("TEST_22");
                }
                if(itemStackSlotZero.hasItemMeta() && hasEnchant(itemStackSlotZero, CustomEnchants.telekinesis)){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.telekinesis.removeFrom(is);
                    CustomEnchants.telekinesis.addTo(is);
                    e.setResult(is);
                    System.out.println("TEST_22");
                }
                if(itemStackSlotZero.hasItemMeta() && hasEnchant(itemStackSlotZero, CustomEnchants.replenishment)){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.replenishment.removeFrom(is);
                    CustomEnchants.replenishment.addTo(is);
                    e.setResult(is);
                    System.out.println("TEST_22");
                }
                if(itemStackSlotZero.hasItemMeta() && hasEnchant(itemStackSlotZero, CustomEnchants.delicate)){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.delicate.removeFrom(is);
                    CustomEnchants.delicate.addTo(is);
                    e.setResult(is);
                    System.out.println("TEST_22");
                }
                if(itemStackSlotZero.hasItemMeta() && hasEnchant(itemStackSlotZero, CustomEnchants.thunderstrike)){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.thunderstrike.removeFrom(is);
                    CustomEnchants.thunderstrike.addTo(is);
                    e.setResult(is);
                    System.out.println("TEST_22");
                }
            }
            
            
            if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && e.getInventory().getItem(1).equals(CustomEnchants.autosmelt.getBook().getCustomItem())){
                ItemStack is = itemStackSlotZero.clone();
                CustomEnchants.autosmelt.addTo(is);
                e.setResult(is);
            }
            else if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && e.getInventory().getItem(1).equals(CustomEnchants.telekinesis.getBook().getCustomItem())){
                ItemStack is = itemStackSlotZero.clone();
                CustomEnchants.telekinesis.addTo(is);
                e.setResult(is);
            }
            else if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && itemStackSlotOne.equals(CustomEnchants.replenishment.getBook().getCustomItem())){
                ItemStack is = itemStackSlotZero.clone();
                CustomEnchants.replenishment.addTo(is);
                e.setResult(is);
            }
            else if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && itemStackSlotOne.equals(CustomEnchants.delicate.getBook().getCustomItem())){
                ItemStack is = itemStackSlotZero.clone();
                CustomEnchants.delicate.addTo(is);
                e.setResult(is);
            }
            else if(e.getInventory().getItem(0) != null && e.getInventory().getItem(1) != null && itemStackSlotOne.equals(CustomEnchants.thunderstrike.getBook().getCustomItem())){
                ItemStack is = itemStackSlotZero.clone();
                CustomEnchants.thunderstrike.addTo(is);
                e.setResult(is);
            }
        }
        catch(IllegalArgumentException ex){
            Logger.getLogger(CustomEnchantment.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }
}