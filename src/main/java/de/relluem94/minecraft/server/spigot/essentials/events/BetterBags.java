package de.relluem94.minecraft.server.spigot.essentials.events;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.MultipleFacing;
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
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
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
        Block b = e.getBlock();
        Material m = b.getType();
        
        if(EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), CustomEnchants.delicate)){
            if(m.equals(Material.PUMPKIN_STEM) || m.equals(Material.MELON_STEM) || m.equals(Material.ATTACHED_PUMPKIN_STEM) || m.equals(Material.ATTACHED_MELON_STEM)){
                e.setCancelled(true);
            } 
            
            if(e.getBlock().getBlockData() instanceof Ageable age){
                if(age.getAge() != age.getMaximumAge() && (!m.equals(Material.SUGAR_CANE))){
                    e.setCancelled(true);
                }
            }

            if(m.equals(Material.TORCH) || m.equals(Material.LILY_PAD)){
                e.setCancelled(true);
            }
        }

        if(EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), CustomEnchants.telekinesis)){
            int dropCount = 0;

            if(isChorusPlant(b) && b.getRelative(BlockFace.DOWN).getType().equals(Material.END_STONE)){

                List<Block> blocks = new ArrayList<>(getChorusBlocks(b, 0, null));

                if(blocks.size() <= 50){
                    e.setCancelled(true);
                    blocks.forEach(block -> block.setType(Material.AIR));
                    b.setType(Material.AIR);

                    Item item = e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.CHORUS_FRUIT, blocks.size()+1));
                    EntityPickupItemEvent entityPickupItemEvent = new EntityPickupItemEvent(p, item, blocks.size()+1);
                    Bukkit.getPluginManager().callEvent(entityPickupItemEvent);
                }
            }

            if(isSugarCaneOrIsBamboo(b)){
                while(isSugarCaneOrIsBamboo(b.getRelative(BlockFace.UP))){
                    dropCount++;
                    b.setType(Material.AIR);
                    b = b.getRelative(BlockFace.UP);
                }

                if(isSugarCaneOrIsBamboo(b)){
                    b.setType(Material.AIR);
                    dropCount++;
                }

                if(!m.equals(Material.AIR)){
                    Item item = e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(m, dropCount));

                    EntityPickupItemEvent entityPickupItemEvent = new EntityPickupItemEvent(p, item, dropCount);
                    Bukkit.getPluginManager().callEvent(entityPickupItemEvent);
                }
            }
        }
    }

    private boolean isChorusPlant(Block block){
        return block.getType().equals(Material.CHORUS_PLANT);
    }

    private Set<Block> getChorusBlocks(Block b, int count, BlockFace prevBlockFace){
        Set<Block> blocks = new LinkedHashSet<>();

        count ++;
        if(count == 30){
            return blocks;
        }

        if(isChorusPlant(b)){
            if(b.getBlockData() instanceof MultipleFacing bdf){
                for(BlockFace bf : bdf.getFaces()){
                    Block block = b.getRelative(bf);
                    if(bf.equals(BlockFace.DOWN)){continue;}
                    if((bf.equals(prevBlockFace))){continue;}
                    if(blocks.add(block)){
                        blocks.addAll(getChorusBlocks(block, 28, bf.getOppositeFace()));
                    }
                }
            }
        }

        return blocks;
    }

    private boolean isSugarCaneOrIsBamboo(Block b){
        return b.getType().equals(Material.SUGAR_CANE) || b.getType().equals(Material.BAMBOO);
    }

    private final Random random = new Random();

    @EventHandler
    public void onBlockDrop(BlockDropItemEvent e) {
        Player p = e.getPlayer();
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);

        for(Item i : e.getItems()){
            if(RelluEssentials.getInstance().dropMap.containsKey(i.getItemStack().getType())){
                if(i.getItemStack().getAmount() == 1){ 
                    DoubleStore ds = RelluEssentials.getInstance().dropMap.get(i.getItemStack().getType());
                    i.getItemStack().setAmount(random.nextInt((int)ds.getSecondValue()) + (int)ds.getValue());
                }
            }            
        }

        if(EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), CustomEnchants.replenishment)){
            for(int i = 0; i < e.getItems().size(); i++){
                if(e.getItems().get(i) != null && RelluEssentials.getInstance().crops.containsKey(e.getItems().get(i).getItemStack().getType())){
                    e.getBlock().setType(RelluEssentials.getInstance().crops.get(e.getItems().get(i).getItemStack().getType()));

                    if(e.getBlock().getBlockData() instanceof Cocoa c){
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
        
        if(BagHelper.hasBags(pe.getId())){
            List<Item> lis = BagHelper.collectItems(e.getItems(), e.getPlayer(), pe);
            e.getItems().removeAll(lis);  
        }

        if(EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), CustomEnchants.telekinesis)){
            List<Item> lis = new ArrayList<>();
            for(Item i: e.getItems()){
                if(p.getInventory().firstEmpty() != -1){
                    p.getInventory().addItem(i.getItemStack());
                    lis.add(i);
                }
            }
            e.getItems().removeAll(lis);
        }

        if(EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), CustomEnchants.autosmelt)){
            for(int i = 0; i < e.getItems().size(); i++){
                ItemStack is = e.getItems().get(i).getItemStack().clone();
                if(e.getItems().get(i) != null && ItemHelper.getSmeltedItemStack(is) != null){
                    e.getItems().get(i).getItemStack().setType(ItemHelper.getSmeltedItemStack(is).getType());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClickItem(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player p && e.getCurrentItem() != null){
            if (e.getView().getTitle().startsWith(Strings.PLUGIN_NAME_PREFIX + Strings.PLUGIN_FORMS_SPACER_MESSAGE) && e.getView().getTitle().endsWith(" Bag")) {

                PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
                BagTypeEntry bte = BagHelper.getBagTypeByName(e.getView().getTitle());

                if(bte == null){
                    return;
                }

                BagEntry be = BagHelper.getBag(pe.getId(), bte.getId());

                if(be == null){
                    return;
                }

                ItemStack is = e.getCurrentItem();

                int slot = BagHelper.getSlotByItemStack(be, is);
                if(slot != -1 &&  e.getClickedInventory() != null){
                    int value = be.getSlotValue(slot);
                    boolean isRightClick = e.isRightClick();
                    boolean isBagInventory = e.getClickedInventory().getType().equals(InventoryType.CHEST);

                    if(!isBagInventory){
                        if(isRightClick){
                            for(ItemStack fis : p.getInventory().getContents()){
                                if(fis != null &&  ItemHelper.getCleanItemStack(fis).equals(ItemHelper.getCleanItemStack(is))){
                                    value = be.getSlotValue(slot);
                                    be.setSlotValue(slot, value + fis.getAmount());
                                    be.setHasToBeUpdated(true);
                                    p.getInventory().remove(fis);
                                }
                            }
                        }
                        else{
                            be.setSlotValue(slot, value + is.getAmount());
                            be.setHasToBeUpdated(true);
                            Objects.requireNonNull(p.getInventory().getItem(e.getSlot())).setAmount(0);
                        }
                        p.updateInventory();
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
                                        be.setHasToBeUpdated(true);
                                        p.getInventory().addItem(cleanIS);
                                        p.updateInventory();
                                        if(p.getInventory().firstEmpty() == -1){
                                            break;
                                        }
                                    }
                                    else{
                                        cleanIS.setAmount(value);
                                        be.setSlotValue(slot, 0);
                                        be.setHasToBeUpdated(true);
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
                                    }
                                    else{
                                        cleanIS.setAmount(value);
                                        be.setSlotValue(slot, 0);
                                    }
                                    be.setHasToBeUpdated(true);
                                    p.getInventory().addItem(cleanIS);
                                    p.updateInventory();
                                }
                            }
                        }
                    }

                    p.openInventory(Objects.requireNonNull(BagHelper.getBag(be.getBagTypeId(), pe)));
                }                
                e.setCancelled(true);
            }
            else if (e.getView().getTitle().equals(BagHelper.MAIN_GUI)) {
                e.setCancelled(true);
                String name = Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName();
                BagTypeEntry bte = BagHelper.getBagTypeByName(name);
                if(bte != null){
                    PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(e.getWhoClicked().getUniqueId());
                    e.getWhoClicked().openInventory(Objects.requireNonNull(BagHelper.getBag(bte.getId(), pe)));
                }
            }
        }
    }


    @EventHandler
    public void onItemCollect(EntityPickupItemEvent e) {
        if(e.getEntity() instanceof Player p){

            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);

            ItemStack is = e.getItem().getItemStack();
            if(CustomItems.coins.almostEquals(is)){
                ItemMeta im = is.getItemMeta();

                if(im != null && im.getPersistentDataContainer().has(ItemConstants.PLUGIN_ITEM_COINS_NAMESPACE, PersistentDataType.INTEGER)){
                    int coins = im.getPersistentDataContainer().get(ItemConstants.PLUGIN_ITEM_COINS_NAMESPACE, PersistentDataType.INTEGER) * is.getAmount();
                    ChatHelper.sendMessageInActionBar(p, String.format(Strings.PLUGIN_COMMAND_PURSE_GAIN, StringHelper.formatInt(coins), StringHelper.formatDouble(pe.getPurse() + coins)));
                    pe.setPurse(pe.getPurse() + coins);

                    p.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.PLAYERS, 1F, 1);

                    pe.setUpdatedBy(pe.getId());
                    pe.setHasToBeUpdated(false);

                    e.getItem().getItemStack().setAmount(0);
                    e.setCancelled(true);
                }
            }

            if(BagHelper.hasBags(pe.getId()) && BagHelper.collectItem(e.getItem(), p, pe)){
                p.getInventory().remove(is);
                p.updateInventory();
                e.setCancelled(true);
                e.getItem().remove();
            }
            else{
                p.getInventory().addItem(is);
                p.updateInventory();
                e.setCancelled(true);
                e.getItem().remove();
                p.playSound(p, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5f, 1f);
            }
        }
    }
}
