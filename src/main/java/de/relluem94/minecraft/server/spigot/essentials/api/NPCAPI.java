package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemPrice;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC.Type;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemBuyPrice;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemSellPrice;

public class NPCAPI {
    
    private final List<ItemStack> npcItemStack = new ArrayList<>();
    private final List<String> npcName = new ArrayList<>();
    private final List<String> npcTraderTitle = new ArrayList<>();
    private final List<NPC> npcs = new ArrayList<>();

    public void init(List<NPCEntry> npcEntryList){
        for(NPCEntry ne : npcEntryList){
            new NPC(ne){
                @Override
                public Inventory getMainGUI(){
                    Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(NPCHelper.INV_SIZE, getTitle()), CustomItems.npc_gui_disabled.getCustomItem());
                    int slot = 0;
                    for(int i = 0; i < ne.getSlotNames().length; i++){
                        slot = InventoryHelper.getNextSlot(slot);
                        if(!ne.getSlotName(i).equals("AIR")){
                            ItemStack itemStack = new ItemStack(Material.valueOf(ne.getSlotName(i)), 1);

                            int buyPricePerItem = ItemPrice.valueOf(itemStack.getType().name()).getBuyPrice();
                            int sellPricePerItem = ItemPrice.valueOf(itemStack.getType().name()).getSellPrice();

                            ItemMeta itemMeta =  itemStack.getItemMeta();
                            Objects.requireNonNull(itemMeta).getPersistentDataContainer().set(itemSellPrice, PersistentDataType.INTEGER, sellPricePerItem);
                            Objects.requireNonNull(itemMeta).getPersistentDataContainer().set(itemBuyPrice, PersistentDataType.INTEGER, buyPricePerItem);
                            itemMeta.setLore(List.of(String.format(PLUGIN_ITEM_BUY_PRICE_MESSAGE, buyPricePerItem, buyPricePerItem*64), String.format(PLUGIN_ITEM_SELL_PRICE_MESSAGE, sellPricePerItem, sellPricePerItem*64)));

                            itemStack.setItemMeta(itemMeta);

                            inv.setItem(slot, itemStack);
                        }
                        slot++;
                    }
                    inv.setItem(53, CustomItems.npc_gui_close.getCustomItem());
                    return inv;
                }
            };
        }
    }

    /**
     * Gives back a List of NPCs
     * @return List of NPC
     */
    public List<NPC> getNPCs(){
        return npcs;
    }

    /**
     * Adds a NPC
     * @param npc NPC
     */
    public void addNPC(NPC npc){
        npcs.add(npc);
        npcItemStack.add(npc.getItemHelper().getCustomItem());
        npcName.add(npc.getName());

        if(npc.getType().equals(Type.TRADER)){
            npcTraderTitle.add(npc.getTitle());
        }
    }

    /**
     * Gives back a List of ItemStacks (Spawn Eggs)
     * @return List of ItemStack
     */
    public List<ItemStack> getNPCItemStackList(){
        return npcItemStack;
    }

    /**
     * Gives back a List of Strings with NPC Names
     * @return List of Strings
     */
    public List<String> getNPCNameList(){
        return npcName;
    }

    /**
     * Gives back a List of Strings with Trader NPC GUI Titles
     * @return List of Strings
     */
    public List<String> getNPCTraderTitleList(){
        return npcTraderTitle;
    }

    /**
     * Gives back a NPC from index
     * @param index int
     * @return NPC
     */
    public NPC getNPC(int index) {
        return npcs.get(index);
    }
}