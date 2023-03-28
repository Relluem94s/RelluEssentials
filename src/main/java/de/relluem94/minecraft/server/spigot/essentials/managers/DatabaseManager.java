package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import org.bukkit.inventory.ItemStack;

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

public class DatabaseManager implements IEnable{

    private DatabaseHelper dBH;
    private PluginInformationEntry pie;

    public DatabaseManager(String host, String user, String password, int port){
        dBH = new DatabaseHelper(host, user, password, port);
    }

    @Override
    public void enable() {
        pie = dBH.getPluginInformation();
        RelluEssentials.getInstance().setPluginInformation(pie);
        dBH.init();

        RelluEssentials.getInstance().locationTypeEntryList.addAll(dBH.getLocationTypes());

        for(DropEntry de : dBH.getDrops()){
            RelluEssentials.getInstance().dropMap.put(de.getMaterial(), new DoubleStore(de.getMin(), de.getMax()));
        }

        for(CropEntry ce : dBH.getCrops()){
            RelluEssentials.getInstance().crops.put(ce.getSeed(), ce.getPlant());
        }

        RelluEssentials.getInstance().setPlayerAPI(new PlayerAPI(dBH.getBags()));
        RelluEssentials.getInstance().setProtectionAPI(new ProtectionAPI(dBH.getProtectionLocks(), dBH.getProtections()));
        RelluEssentials.getInstance().setNpcAPI(new NPCAPI());
        RelluEssentials.getInstance().getNpcAPI().init(dBH.getNPCs());
        RelluEssentials.getInstance().setBagAPI(new BagAPI(dBH.getBagTypes()));
        RelluEssentials.getInstance().setBankAPI(new BankAPI(dBH.getBankTiers()));
        RelluEssentials.getInstance().setWarpAPI(new WarpAPI(dBH.getWarps()));

        for(WorldGroupEntry wge: dBH.getWorldGroups()){
            for(WorldEntry we: dBH.getWorldByGroup(wge)){
                consoleSendMessage(PLUGIN_NAME_CONSOLE,"Adding World: " + wge.getName() + " " + we.getName());
                RelluEssentials.getInstance().worldsMap.put(wge, we);
            }
        }

        RelluEssentials.getInstance().groupEntryList.addAll(dBH.getGroups());

        for(int i = 0; i < RelluEssentials.getInstance().getBagAPI().getBagTypeEntryList().size(); i++){
            ItemStack[] isa = BagHelper.getItemStacks(RelluEssentials.getInstance().getBagAPI().getBagTypeEntryList().get(i));
            for(ItemStack is : isa){
                RelluEssentials.getInstance().bagBlocks2collect.add(is);
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