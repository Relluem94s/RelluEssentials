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
        ItemStack itemStackSlotOne = e.getInventory().getItem(1);
        @SuppressWarnings("all")
        String renameText = e.getView().getRenameText();

        if(itemStackSlotZero == null){
            return;
        }

        if(itemStackSlotOne == null){
            return;
        }

        if(renameText == null){
            return;
        }

        if(renameText.equals(ItemHelper.getItemName(itemStackSlotZero))){
            System.out.println("test22");
            return;
        }
        else{
            System.out.println("test22");
        }

        if(
            itemStackSlotZero.equals(CustomEnchants.autosmelt.getBook().getCustomItem()) ||
            itemStackSlotZero.equals(CustomEnchants.telekinesis.getBook().getCustomItem()) ||
            itemStackSlotZero.equals(CustomEnchants.delicate.getBook().getCustomItem()) ||
            itemStackSlotZero.equals(CustomEnchants.replenishment.getBook().getCustomItem()) ||
            itemStackSlotZero.equals(CustomEnchants.thunderstrike.getBook().getCustomItem())
        ){
            e.setResult(null);
        }
        
        try{
            if(e.getResult() != null){
                if(itemStackSlotZero.hasItemMeta() && hasEnchant(itemStackSlotZero, CustomEnchants.autosmelt)){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.autosmelt.removeFrom(is);
                    CustomEnchants.autosmelt.addTo(is);
                    e.setResult(is);
                }
                if(itemStackSlotZero.hasItemMeta() && hasEnchant(itemStackSlotZero, CustomEnchants.telekinesis)){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.telekinesis.removeFrom(is);
                    CustomEnchants.telekinesis.addTo(is);
                    e.setResult(is);
                }
                if(itemStackSlotZero.hasItemMeta() && hasEnchant(itemStackSlotZero, CustomEnchants.replenishment)){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.replenishment.removeFrom(is);
                    CustomEnchants.replenishment.addTo(is);
                    e.setResult(is);
                }
                if(itemStackSlotZero.hasItemMeta() && hasEnchant(itemStackSlotZero, CustomEnchants.delicate)){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.delicate.removeFrom(is);
                    CustomEnchants.delicate.addTo(is);
                    e.setResult(is);
                }
                if(itemStackSlotZero.hasItemMeta() && hasEnchant(itemStackSlotZero, CustomEnchants.thunderstrike)){
                    ItemStack is = e.getResult().clone();
                    CustomEnchants.thunderstrike.removeFrom(is);
                    CustomEnchants.thunderstrike.addTo(is);
                    e.setResult(is);
                }
            }

            System.out.println("TEST_22");
            if(itemStackSlotOne.equals(CustomEnchants.autosmelt.getBook().getCustomItem()) &! hasEnchant(itemStackSlotZero, CustomEnchants.autosmelt)){
                ItemStack is = itemStackSlotZero.clone();
                CustomEnchants.autosmelt.addTo(is);
                e.setResult(is);
            }
            else if(itemStackSlotOne.equals(CustomEnchants.telekinesis.getBook().getCustomItem()) &! hasEnchant(itemStackSlotZero, CustomEnchants.telekinesis)){
                ItemStack is = itemStackSlotZero.clone();
                CustomEnchants.telekinesis.addTo(is);
                e.setResult(is);
            }
            else if(itemStackSlotOne.equals(CustomEnchants.replenishment.getBook().getCustomItem()) &! hasEnchant(itemStackSlotZero, CustomEnchants.replenishment)){
                ItemStack is = itemStackSlotZero.clone();
                CustomEnchants.replenishment.addTo(is);
                e.setResult(is);
            }
            else if(itemStackSlotOne.equals(CustomEnchants.delicate.getBook().getCustomItem()) &! hasEnchant(itemStackSlotZero, CustomEnchants.delicate)){
                ItemStack is = itemStackSlotZero.clone();
                CustomEnchants.delicate.addTo(is);
                e.setResult(is);
            }
            else if(itemStackSlotOne.equals(CustomEnchants.thunderstrike.getBook().getCustomItem()) &! hasEnchant(itemStackSlotZero, CustomEnchants.thunderstrike)){
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