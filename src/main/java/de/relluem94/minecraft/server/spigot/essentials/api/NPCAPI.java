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
    
    private List<ItemStack> npcItemstack = new ArrayList<>();
    private List<String> npcName = new ArrayList<>();
    private List<String> npcTraderTitle = new ArrayList<>();
    private List<NPC> npcs = new ArrayList<>();


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
    public List<NPC> getNPCs(){
        return npcs;
    }

    /**
     * Adds a NPC
     * @param npc NPC
     */
    public void addNPC(NPC npc){
        npcs.add(npc);
        npcItemstack.add(npc.getItemHelper().getCustomItem());
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
        return npcItemstack;
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