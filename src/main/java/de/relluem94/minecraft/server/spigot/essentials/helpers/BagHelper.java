package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_BAGS_SAVED;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_BAG_AMOUNT;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_BAG_CLICK_TO_BUY;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_BAG_CLICK_TO_OPEN;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_BAG_COST_TO_BUY;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_BAG_GUI_TITLE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_BAG_RETRIEVE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_FORMS_SPACER_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_INTERNAL_UTILITY_CLASS;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME_CHAT_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME_PREFIX;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessageInChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;




public class BagHelper {

    private BagHelper() {
        throw new IllegalStateException(PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static final int BAG_SIZE = 28;

    public static Inventory getBag(int type, PlayerEntry pe) {
        BagEntry be = getBag(pe.getId(), type);

        if(be == null){
            return null;
        }

        Inventory inv = InventoryHelper.createInventory(54, PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ be.getBagType().getDisplayName());
        inv = InventoryHelper.fillInventory(inv, CustomItems.npc_gui_disabled.getCustomItem());

        inv.setItem(10, getItemStack(be, 0));
        inv.setItem(11, getItemStack(be, 1));
        inv.setItem(12, getItemStack(be,2));
        inv.setItem(13, getItemStack(be,3));
        inv.setItem(14, getItemStack(be,4));
        inv.setItem(15, getItemStack(be,5));
        inv.setItem(16, getItemStack(be,6));

        inv.setItem(19, getItemStack(be,7));
        inv.setItem(20, getItemStack(be,8));
        inv.setItem(21, getItemStack(be,9));
        inv.setItem(22, getItemStack(be,10));
        inv.setItem(23, getItemStack(be,11));
        inv.setItem(24, getItemStack(be,12));
        inv.setItem(25, getItemStack(be,13));

        inv.setItem(28, getItemStack(be, 14));
        inv.setItem(29, getItemStack(be, 15));
        inv.setItem(30, getItemStack(be,16));
        inv.setItem(31, getItemStack(be,17));
        inv.setItem(32, getItemStack(be,18));
        inv.setItem(33, getItemStack(be,19));
        inv.setItem(34, getItemStack(be,20));

        inv.setItem(37, getItemStack(be,21));
        inv.setItem(38, getItemStack(be,22));
        inv.setItem(39, getItemStack(be,23));
        inv.setItem(40, getItemStack(be,24));
        inv.setItem(41, getItemStack(be,25));
        inv.setItem(42, getItemStack(be,26));
        inv.setItem(43, getItemStack(be,27));


        return inv;
    }

    public static final String MAIN_GUI = PLUGIN_BAG_GUI_TITLE;

    public static Inventory getBags(boolean npc, String title){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, title), CustomItems.npc_gui_disabled.getCustomItem());
        ListIterator<BagTypeEntry> bteli = RelluEssentials.getInstance().getBagAPI().getBagTypeEntryList().listIterator();

        int slot = 0;
        while(bteli.hasNext()){
            slot = InventoryHelper.getNextSlot(slot);
            BagTypeEntry bte = bteli.next();
            inv.setItem(slot, BagHelper.getItem(bte, npc).getCustomItem());
            slot++;
        }
        return inv;
    }

    public static Inventory getBags(PlayerEntry pe){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());
        ListIterator<BagTypeEntry> bteli = RelluEssentials.getInstance().getBagAPI().getBagTypeEntryList().listIterator();

        int slot = 0;
        while(bteli.hasNext()){
            slot = InventoryHelper.getNextSlot(slot);
            BagTypeEntry bte = bteli.next();
            if(hasBag(pe.getId(), bte.getId())){
                inv.setItem(slot, BagHelper.getItem(bte, false).getCustomItem());
                slot++;
            }
        }
        return inv;
    }

    public static ItemHelper getItem(BagTypeEntry bte, boolean npc){
        String[] lore;
        if(npc){
            lore = new String[]{PLUGIN_BAG_CLICK_TO_BUY, String.format(PLUGIN_BAG_COST_TO_BUY,  bte.getCost())};
        }
        else{
            lore = new String[]{PLUGIN_BAG_CLICK_TO_OPEN};
        }
        return new ItemHelper(PlayerHeadHelper.getCustomSkull(CustomHeads.BAG), bte.getDisplayName(), Type.NPC_GUI, Rarity.NONE, Arrays.asList(lore));
    }

    public static ItemStack[] getItemStacks(BagTypeEntry bte){
        ItemStack[] isa = new ItemStack[BAG_SIZE];
        for(int i = 0; i < BAG_SIZE; i++){
            isa[i] = getItemStack(bte, i);
        }
        return isa;
    }


    private static ItemStack getItemStack(BagTypeEntry bte, int slot){
        String name = bte.getSlotName(slot);
        Material mat = Material.AIR;

        if(name == null){
            return CustomItems.npc_gui_disabled.getCustomItem();
        }

        if(Material.matchMaterial(name) != null){
            mat = Material.matchMaterial(name);
        }

        return new ItemStack(mat, 1);
    }

    private static ItemStack getItemStack(BagEntry be, int slot){
        String name = be.getBagType().getSlotName(slot);
        int value =  be.getSlotValue(slot);

        Material mat = Material.AIR;

        if(name == null){
            return CustomItems.npc_gui_disabled.getCustomItem();
        }

        if(Material.matchMaterial(name) != null){
            mat = Material.matchMaterial(name);
        }

        if(mat.equals(Material.AIR)){
            return CustomItems.npc_gui_disabled.getCustomItem();
        }

        ItemStack is = new ItemStack(mat, 1);
        ItemMeta im = is.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(String.format(PLUGIN_BAG_AMOUNT, value));
        lore.add(PLUGIN_BAG_RETRIEVE);

        im.setLore(lore);
        is.setItemMeta(im);

        return is;
    }


    public static int getSlotByItemStack(BagEntry be, ItemStack is){
        List<ItemStack> isa = Arrays.asList(getItemStacks(be.getBagType()));
        ItemStack checkWithoutAmount = ItemHelper.getCleanItemStack(is);
        int slot = -1;

        if(isa.contains(checkWithoutAmount)){
            for(int o = 0; o < BAG_SIZE; o++){
                if(isa.get(o).equals(checkWithoutAmount)){
                    slot = o;
                }
            }
        }       

        return slot;
    }

    public static boolean hasBag(int type, PlayerEntry pe){
        return RelluEssentials.getInstance().getDatabaseHelper().getBag(type, pe.getId()) != null;
    }

    public static boolean hasBag(int playerFK, int bagType){
        if(RelluEssentials.getInstance().getPlayerAPI().getPlayerBagMap().containsKey(playerFK)){
            Collection<BagEntry> cbe = RelluEssentials.getInstance().getPlayerAPI().getPlayerBagMap().get(playerFK);
            for(BagEntry be : cbe){
                if(be.getPlayerId() == playerFK && be.getBagType().getId() == bagType){
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasBags(int playerFK){
        return RelluEssentials.getInstance().getPlayerAPI().getPlayerBagMap().containsKey(playerFK);
    }


    public static BagEntry getBag(int playerFK, int bagType){
        if(RelluEssentials.getInstance().getPlayerAPI().getPlayerBagMap().containsKey(playerFK)){
            Collection<BagEntry> cbe = RelluEssentials.getInstance().getPlayerAPI().getPlayerBagMap().get(playerFK);
            for(BagEntry be : cbe){
                if(be.getPlayerId() == playerFK && be.getBagType().getId() == bagType){
                    return be;
                }
            }
        }
        return null;
    }

    public static BagTypeEntry getBagTypeByName(String name){
        for(BagTypeEntry bte : RelluEssentials.getInstance().getBagAPI().getBagTypeEntryList()){
            if(name.contains(bte.getDisplayName()) || bte.getDisplayName().contains(name)){
                return bte;
            }
        }
        return null;
    }

    public static Collection<BagEntry> getBags(int playerFK){
        Collection<BagEntry> cbe = new ArrayList<>();

        if(RelluEssentials.getInstance().getPlayerAPI().getPlayerBagMap().containsKey(playerFK)){
            cbe = RelluEssentials.getInstance().getPlayerAPI().getPlayerBagMap().get(playerFK);
        }

        return cbe;
    }

    public static BagTypeEntry getBagTypeById(int id) {
        for(BagTypeEntry bte : RelluEssentials.getInstance().getBagAPI().getBagTypeEntryList()){
            if(bte.getId() == id){
                return bte;
            }
        }
        return null;
    }


    public static List<Item> collectItems(List<Item> li, Player p, PlayerEntry pe){
        ListIterator<Item> lii = li.listIterator();
        List<Item> lio = new ArrayList<>();
        while(lii.hasNext()){
            Item i = lii.next();
            ItemStack checkWithoutAmount = i.getItemStack().clone();
            checkWithoutAmount.setAmount(1);
            if(RelluEssentials.getInstance().bagBlocks2collect.contains(checkWithoutAmount)){
                Collection<BagEntry> bel = BagHelper.getBags(pe.getId());
                for(BagEntry be: bel){
                    int slot = BagHelper.getSlotByItemStack(be, checkWithoutAmount);
                    if(slot != -1){
                        be.setSlotValue(slot, be.getSlotValue(slot) + i.getItemStack().getAmount());
                        be.setHasToBeUpdated(true);
                        ChatHelper.sendMessageInActionBar(p, String.format(EventConstants.PLUGIN_EVENT_BAG_COLLECT, i.getItemStack().getAmount(), i.getName()));
                        p.playSound(p, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1);
                        lio.add(i);
                    }
                }
            }
        }
        return lio;
    }

    public static List<ItemStack> collectItemStacks(List<ItemStack> li, Player p, PlayerEntry pe){
        ListIterator<ItemStack> lii = li.listIterator();
        List<ItemStack> lio = new ArrayList<>();
        while(lii.hasNext()){
            ItemStack i = lii.next();
            ItemStack checkWithoutAmount = i.clone();
            checkWithoutAmount.setAmount(1);
            if(RelluEssentials.getInstance().bagBlocks2collect.contains(checkWithoutAmount)){
                Collection<BagEntry> bel = BagHelper.getBags(pe.getId());
                for(BagEntry be: bel){
                    int slot = BagHelper.getSlotByItemStack(be, checkWithoutAmount);
                    if(slot != -1){
                        be.setSlotValue(slot, be.getSlotValue(slot) + i.getAmount());
                        be.setHasToBeUpdated(true);
                        ChatHelper.sendMessageInActionBar(p, String.format(EventConstants.PLUGIN_EVENT_BAG_COLLECT, i.getAmount(), i.getType().name().replace("_", " ").toLowerCase()));
                        p.playSound(p, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1);
                        lio.add(i);
                    }
                }
            }
        }
        return lio;
    }

    public static boolean collectItem(Item item, Player p, PlayerEntry pe) {
        ItemStack checkWithoutAmount = item.getItemStack().clone();
        checkWithoutAmount.setAmount(1);
        if(!RelluEssentials.getInstance().bagBlocks2collect.contains(checkWithoutAmount)){
            return false;
        }

        Collection<BagEntry> bel = BagHelper.getBags(pe.getId());
        for(BagEntry be: bel){
            int slot = BagHelper.getSlotByItemStack(be, checkWithoutAmount);
            if(slot != -1){
                be.setSlotValue(slot, be.getSlotValue(slot) + item.getItemStack().getAmount());
                be.setHasToBeUpdated(true);
                ChatHelper.sendMessageInActionBar(p, String.format(EventConstants.PLUGIN_EVENT_BAG_COLLECT, item.getItemStack().getAmount(), item.getName()));
                p.playSound(p, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1);
                item.getItemStack().setAmount(0);
                return true;
            }
        }

        return false;
    }

    public static void saveBags(){
        int updatedBags = 0;

        for(BagEntry be : RelluEssentials.getInstance().getPlayerAPI().getPlayerBagMap().values()){
            if(be.isHasToBeUpdated()){
                RelluEssentials.getInstance().getDatabaseHelper().updateBagEntry(be);
                be.setHasToBeUpdated(false);
                updatedBags++;
            }
        }
        if(updatedBags != 0){
            sendMessageInChannel(String.format(PLUGIN_BAGS_SAVED, BetterChatFormat.ADMIN_CHANNEL, updatedBags), PLUGIN_NAME_CHAT_CONSOLE, BetterChatFormat.ADMIN_CHANNEL, Groups.getGroup("admin"));
        }
    }
}