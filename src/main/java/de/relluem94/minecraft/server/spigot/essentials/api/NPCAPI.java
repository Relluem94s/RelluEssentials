package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.NPC.NPC;
import de.relluem94.minecraft.server.spigot.essentials.NPC.NPC.Type;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;

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

    public static List<NPC> getNPCs(){
        return npcs;
    }

    public static void addNPC(NPC npc){
        npcs.add(npc);
        npc_itemstack.add(npc.getItemHelper().getCustomItem());
        npc_name.add(npc.getName());

        if(npc.getType().equals(Type.TRADER)){
            npc_trader_title.add(npc.getTitle());
        }
    }

    public static List<ItemStack> getNPCItemStackList(){
        return npc_itemstack;
    }

    public static List<String> getNPCNameList(){
        return npc_name;
    }

    public static List<String> getNPCTraderTitleList(){
        return npc_trader_title;
    }

    public static NPC getNPC(int i) {
        return npcs.get(i);
    }
}