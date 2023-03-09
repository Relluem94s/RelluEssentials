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
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupEntry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.pie;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import org.bukkit.inventory.ItemStack;

public class DatabaseManager implements IManager{

    @Override
    public void manage() {
        dBH = new DatabaseHelper(
            RelluEssentials.getInstance().getConfig().getString("database.host"), 
            RelluEssentials.getInstance().getConfig().getString("database.user"), 
            RelluEssentials.getInstance().getConfig().getString("database.password"), 
            RelluEssentials.getInstance().getConfig().getInt("database.port")
        );
        pie = dBH.getPluginInformation();
        dBH.init();

        RelluEssentials.locationTypeEntryList.addAll(dBH.getLocationTypes());


        new PlayerAPI(dBH.getBags());
        new ProtectionAPI(dBH.getProtectionLocks(), dBH.getProtections());
        new NPCAPI(dBH.getNPCs());
        new BagAPI(dBH.getBagTypes());
        new BankAPI(dBH.getBankTiers());
        new WarpAPI(dBH.getWarps());

        for(WorldGroupEntry wge: dBH.getWorldGroups()){
            for(WorldEntry we: dBH.getWorldByGroup(wge)){
                consoleSendMessage(PLUGIN_NAME_CONSOLE,"Adding World: " + wge.getName() + " " + we.getName());
                RelluEssentials.worldsMap.put(wge, we);
            }
        }

        

        RelluEssentials.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(RelluEssentials.getInstance(), () -> {
            RelluEssentials.locationEntryList.addAll(dBH.getLocations());
        });

        RelluEssentials.groupEntryList.addAll(dBH.getGroups());

        for(int i = 0; i < BagAPI.getBagTypeEntryList().size(); i++){
            ItemStack[] isa = BagHelper.getItemStacks(BagAPI.getBagTypeEntryList().get(i));
            for(ItemStack is : isa){
                RelluEssentials.bagBlocks2collect.add(is);
            }
        }

        System.gc();
    }
    
}
