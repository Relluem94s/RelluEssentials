package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC.Type;

public class NPCAPI {
    
    private static List<ItemStack> npc_itemstack = new ArrayList<>();
    private static List<String> npc_name = new ArrayList<>();
    private static List<String> npc_trader_title = new ArrayList<>();
    private static List<NPC> npcs = new ArrayList<>();


    public NPCAPI(List<NPCEntry> npcEntryList) {
        for(NPCEntry ne : npcEntryList){
            new NPC(ne){
                @Override
                public Inventory getMainGUI(){
                    Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(NPCHelper.INV_SIZE, getTitle()), CustomItems.npc_gui_disabled.getCustomItem());
                    int slot = 0;
                    for(int i = 0; i < ne.getSlotNames().length; i++){
                        slot = InventoryHelper.getNextSlot(slot);
                        if(!ne.getSlotName(i).equals("AIR")){
                            inv.setItem(slot, new ItemStack(Material.valueOf(ne.getSlotName(i)),1));
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
    public static List<NPC> getNPCs(){
        return npcs;
    }

    /**
     * Adds a NPC
     * @param npc NPC
     */
    public static void addNPC(NPC npc){
        npcs.add(npc);
        npc_itemstack.add(npc.getItemHelper().getCustomItem());
        npc_name.add(npc.getName());

        if(npc.getType().equals(Type.TRADER)){
            npc_trader_title.add(npc.getTitle());
        }
    }

    /**
     * Gives back a List of ItemStacks (Spawn Eggs)
     * @return List of ItemStack
     */
    public static List<ItemStack> getNPCItemStackList(){
        return npc_itemstack;
    }

    /**
     * Gives back a List of Strings with NPC Names
     * @return List of Strings
     */
    public static List<String> getNPCNameList(){
        return npc_name;
    }

    /**
     * Gives back a List of Strings with Trader NPC GUI Titles
     * @return List of Strings
     */
    public static List<String> getNPCTraderTitleList(){
        return npc_trader_title;
    }

    /**
     * Gives back a NPC from index
     * @param index int
     * @return NPC
     */
    public static NPC getNPC(int index) {
        return npcs.get(index);
    }
}