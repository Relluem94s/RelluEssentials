package de.relluem94.minecraft.server.spigot.essentials.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.rellulib.stores.DoubleStore;
public class BetterBags implements Listener {

    

    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchants() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.delicate)){
            if(e.getBlock().getType().equals(Material.PUMPKIN_STEM) || e.getBlock().getType().equals(Material.MELON_STEM)){
                e.setCancelled(true);
            } 
            
            if(e.getBlock().getBlockData() instanceof Ageable){
                Ageable age = (Ageable) e.getBlock().getBlockData();
                if(age.getAge() != age.getMaximumAge()){
                    if(!e.getBlock().getType().equals(Material.SUGAR_CANE)){
                        e.setCancelled(true);
                    }
                }
            }
        }

        /*
        if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchants() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.telekinesis)){
            Block block = e.getBlock();
            int dropCount = 2;
            while(block.getRelative(BlockFace.UP).getType().equals(Material.SUGAR_CANE) || block.getRelative(BlockFace.UP).getType().equals(Material.BAMBOO)){
                dropCount++;
                block.setType(Material.AIR);
                block = block.getRelative(BlockFace.UP);
            }
            e.getBlock().getDrops().add(new ItemStack(block.getType(), dropCount));
        }
         */
    }

    private Random r = new Random();

    @EventHandler
    public void onBlockDrop(BlockDropItemEvent e) {
        Player p = e.getPlayer();
       
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);

        for(Item i : e.getItems()){
            if(RelluEssentials.getInstance().dropMap.containsKey(i.getItemStack().getType())){
                if(i.getItemStack().getAmount() == 1){ 
                    DoubleStore ds = RelluEssentials.getInstance().dropMap.get(i.getItemStack().getType());
                    i.getItemStack().setAmount(r.nextInt((int)ds.getSecondValue()) + (int)ds.getValue());
                }
            }            
        }

        if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchants() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.replenishment)){
            for(int i = 0; i < e.getItems().size(); i++){
                if(e.getItems().get(i) != null && RelluEssentials.getInstance().crops.containsKey(e.getItems().get(i).getItemStack().getType())){
                    e.getBlock().setType(RelluEssentials.getInstance().crops.get(e.getItems().get(i).getItemStack().getType()));

                    if(e.getBlock().getBlockData() instanceof Cocoa){
                        Cocoa c = (Cocoa) e.getBlock().getBlockData();
                        Block cocoa = e.getBlock();
                        Block wood = cocoa.getRelative(c.getFacing().getOppositeFace());
                        if(!wood.getType().equals(Material.JUNGLE_LOG)){
                            for(BlockFace f : BlockFace.values()){
                                wood = e.getBlock().getRelative(f);
                                if(wood.getType().equals(Material.JUNGLE_LOG)){
                                    c.setFacing(f);
                                    cocoa.setBlockData(c);
                                    break;
                                }
                            }                            
                        }
                        else{
                            c.setFacing(c.getFacing().getOppositeFace());
                            cocoa.setBlockData(c);
                        }
                    }
                    int oldAmount =  e.getItems().get(i).getItemStack().getAmount();
                    e.getItems().get(i).getItemStack().setAmount(oldAmount -1);
                }
            }
        }
        else if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchants() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.autosmelt)){
            for(int i = 0; i < e.getItems().size(); i++){
                ItemStack is = e.getItems().get(i).getItemStack().clone();
                if(e.getItems().get(i) != null && ItemHelper.getSmeltedItemStack(is) != null){
                    e.getItems().get(i).getItemStack().setType(ItemHelper.getSmeltedItemStack(is).getType());
                }
            }
        }

        if(BagHelper.hasBags(pe.getID())){
            List<Item> li = new ArrayList<>();
            li.addAll(e.getItems());
            e.getItems().removeAll(BagHelper.collectItems(li, p, pe));
        }

        if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchants() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.telekinesis)){
            List<Item> lis = new ArrayList<>();
            for(Item i: e.getItems()){
                if(p.getInventory().firstEmpty() != -1){
                    p.getInventory().addItem(i.getItemStack());
                    lis.add(i);
                }
            }
            e.getItems().removeAll(lis);
        }
        
    }

    @EventHandler
    public void onInventoryClickItem(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player && e.getCurrentItem() != null){
            Player p = (Player) e.getWhoClicked();
            if (e.getView().getTitle().startsWith(Strings.PLUGIN_NAME_PREFIX + Strings.PLUGIN_FORMS_SPACER_MESSAGE) && e.getView().getTitle().endsWith(" Bag")) {

                PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
                BagEntry be = BagHelper.getBag(pe.getID(), BagHelper.getBagTypeByName(e.getView().getTitle()).getId());

                ItemStack is = e.getCurrentItem();

                int slot = BagHelper.getSlotByItemStack(be, is);
                if(slot != -1){
                    int value = be.getSlotValue(slot);
                    boolean isRightClick = e.isRightClick();
                    boolean isBagInventory = e.getClickedInventory().getType().equals(InventoryType.CHEST);

                    if(!isBagInventory){
                        if(isRightClick){
                            for(ItemStack fis : p.getInventory().getContents()){
                                if(fis != null &&  ItemHelper.getCleanItemStack(fis).equals(ItemHelper.getCleanItemStack(is))){
                                    value = be.getSlotValue(slot);
                                    be.setSlotValue(slot, value + fis.getAmount());
                                    be.setToBeUpdated(true);
                                    p.getInventory().remove(fis);
                                }
                            }
                            p.updateInventory();
                        }
                        else{
                            be.setSlotValue(slot, value + is.getAmount());
                            be.setToBeUpdated(true);

                            p.getInventory().getItem(e.getSlot()).setAmount(0);
                            p.updateInventory();
                        }
                    }
                    else{
                        ItemStack cleanIS = ItemHelper.getCleanItemStack(is);
                        if(value > 0){
                            if(isRightClick){
                                while(p.getInventory().firstEmpty() != -1){
                                    if(p.getInventory().firstEmpty() == -1){
                                        break;
                                    }

                                    if(value >= is.getMaxStackSize()){
                                        value -= is.getMaxStackSize();
                                        cleanIS.setAmount(is.getMaxStackSize());
                                        be.setSlotValue(slot, value);
                                        be.setToBeUpdated(true);
                                        p.getInventory().addItem(cleanIS);
                                        p.updateInventory();
                                        if(p.getInventory().firstEmpty() == -1){
                                            break;
                                        }
                                    }
                                    else{
                                        cleanIS.setAmount(value);
                                        be.setSlotValue(slot, 0);
                                        be.setToBeUpdated(true);
                                        p.getInventory().addItem(cleanIS);
                                        p.updateInventory();
                                        break;
                                    }
                                }
                            }
                            else{
                                if(p.getInventory().firstEmpty() != -1){
                                    if(value >= is.getMaxStackSize()){
                                        cleanIS.setAmount(is.getMaxStackSize());
                                        be.setSlotValue(slot, value-is.getMaxStackSize());
                                        be.setToBeUpdated(true);
                                        p.getInventory().addItem(cleanIS);
                                        p.updateInventory();
                                    }
                                    else{
                                        cleanIS.setAmount(value);
                                        be.setSlotValue(slot, 0);
                                        be.setToBeUpdated(true);
                                        p.getInventory().addItem(cleanIS);
                                        p.updateInventory();
                                    }
                                }
                            }
                        }
                    }

                    p.openInventory(BagHelper.getBag(be.getBagTypeId(), pe));
                }                
                e.setCancelled(true);
            }
            else if (e.getView().getTitle().equals(BagHelper.MAIN_GUI)) {
                e.setCancelled(true);
                String name = e.getCurrentItem().getItemMeta().getDisplayName();
                BagTypeEntry bte = BagHelper.getBagTypeByName(name);
                if(bte != null){
                    PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(e.getWhoClicked().getUniqueId());
                    e.getWhoClicked().openInventory(BagHelper.getBag(bte.getId(), pe));
                }
            }
        }
    }


    @EventHandler
    public void onItemCollect(EntityPickupItemEvent e) {
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);

            ItemStack is = e.getItem().getItemStack();
            if(CustomItems.coins.almostEquals(is)){
                ItemMeta im = is.getItemMeta();

                if(im.getPersistentDataContainer().has(ItemConstants.PLUGIN_ITEM_COINS_NAMESPACE, PersistentDataType.INTEGER)){
                    int coins = im.getPersistentDataContainer().get(ItemConstants.PLUGIN_ITEM_COINS_NAMESPACE, PersistentDataType.INTEGER) * is.getAmount();
                    ChatHelper.sendMessageInActionBar(p, String.format(Strings.PLUGIN_COMMAND_PURSE_GAIN, StringHelper.formatInt(coins), StringHelper.formatDouble(pe.getPurse() + coins)));
                    pe.setPurse(pe.getPurse() + coins);
                    pe.setUpdatedBy(pe.getID());
                    pe.setToBeUpdated(false);
                    e.setCancelled(true);
                    e.getItem().remove();
                }
            }

            if(BagHelper.hasBags(pe.getID()) && BagHelper.collectItem(e.getItem(), p, pe)){
                p.getInventory().remove(is);
                p.updateInventory();
                e.setCancelled(true);
                e.getItem().remove();
            }
        }
    }
/*
    @EventHandler
    public void test5555(BlockFromToEvent e){
        System.out.println(e.getEventName() + " " + e.getToBlock().getType().name() + " << " + e.getBlock().getType().name());
    }

    @EventHandler
    public void test8888(BlockPhysicsEvent e){
        System.out.println(e.getEventName() + " " + e.getSourceBlock() + " >> " + e.getBlock().getType().name());
    }
 */
}
