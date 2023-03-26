package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.api.BagAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.BankAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.NPCAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.ProtectionAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.WarpAPI;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.CropEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.DropEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupEntry;
import de.relluem94.rellulib.stores.DoubleStore;


import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import org.bukkit.inventory.ItemStack;

public class DatabaseManager implements IEnable{

    private DatabaseHelper dBH;
    private PluginInformationEntry pie;

    public DatabaseManager(String host, String user, String password, int port){
        dBH = new DatabaseHelper(host, user, password, port);
    }

    @Override
    public void enable() {
        pie = RelluEssentials.getInstance().getDatabaseHelper().getPluginInformation();
        RelluEssentials.getInstance().getDatabaseHelper().init();

        RelluEssentials.locationTypeEntryList.addAll(RelluEssentials.getInstance().getDatabaseHelper().getLocationTypes());

        for(DropEntry de : RelluEssentials.getInstance().getDatabaseHelper().getDrops()){
            RelluEssentials.dropMap.put(de.getMaterial(), new DoubleStore(de.getMin(), de.getMax()));
        }

        for(CropEntry ce : RelluEssentials.getInstance().getDatabaseHelper().getCrops()){
            RelluEssentials.crops.put(ce.getSeed(), ce.getPlant());
        }

        new PlayerAPI(RelluEssentials.getInstance().getDatabaseHelper().getBags());
        new ProtectionAPI(RelluEssentials.getInstance().getDatabaseHelper().getProtectionLocks(), RelluEssentials.getInstance().getDatabaseHelper().getProtections());
        new NPCAPI(RelluEssentials.getInstance().getDatabaseHelper().getNPCs());
        new BagAPI(RelluEssentials.getInstance().getDatabaseHelper().getBagTypes());
        new BankAPI(RelluEssentials.getInstance().getDatabaseHelper().getBankTiers());
        new WarpAPI(RelluEssentials.getInstance().getDatabaseHelper().getWarps());

        for(WorldGroupEntry wge: RelluEssentials.getInstance().getDatabaseHelper().getWorldGroups()){
            for(WorldEntry we: RelluEssentials.getInstance().getDatabaseHelper().getWorldByGroup(wge)){
                consoleSendMessage(PLUGIN_NAME_CONSOLE,"Adding World: " + wge.getName() + " " + we.getName());
                RelluEssentials.worldsMap.put(wge, we);
            }
        }

        RelluEssentials.groupEntryList.addAll(RelluEssentials.getInstance().getDatabaseHelper().getGroups());

        for(int i = 0; i < BagAPI.getBagTypeEntryList().size(); i++){
            ItemStack[] isa = BagHelper.getItemStacks(BagAPI.getBagTypeEntryList().get(i));
            for(ItemStack is : isa){
                RelluEssentials.bagBlocks2collect.add(is);
            }
        }
    }

    public DatabaseHelper getDatabaseHelper() {
        return dBH;
    }

    public PluginInformationEntry getPluginInformation() {
        return pie;
    }

    
}