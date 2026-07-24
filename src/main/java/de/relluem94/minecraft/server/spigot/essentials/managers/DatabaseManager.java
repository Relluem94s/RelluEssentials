package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.api.*;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.DatabaseHelperFactory;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.*;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NonNull;

import java.sql.SQLException;
import java.util.Collections;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

public class DatabaseManager implements IEnable{

    private final DatabaseHelper dBH;

    public DatabaseManager(String host, String user, String password, int port){
        try {
            dBH = DatabaseHelperFactory.createForProduction(host, port, user, password, RelluEssentials.getInstance().getPlayerAPI());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    public DatabaseManager(DatabaseHelper databaseHelper) {
        this.dBH = databaseHelper;
    }

    @Override
    public void enable() {
        PluginInformationEntry pie = dBH.getPluginInformation();
        RelluEssentials.getInstance().setPluginInformation(pie);
        dBH.init();

        RelluEssentials.getInstance().locationTypeEntryList.addAll(dBH.getLocationTypes());

        for(DropEntry de : dBH.getDrops()){
            RelluEssentials.getInstance().dropMap.put(de.getMaterial(), new DoubleStore<>(de.getMin(), de.getMax()));
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

        RelluEssentials.settingEntriesList.addAll(dBH.getAllSettings());

        for(WorldGroupEntry wge: dBH.getWorldGroups()){
            for(WorldEntry we: dBH.getWorldByGroup(wge)){
                RelluEssentials.getInstance().worldsMap.put(wge, we);

                if (getWorldNameBySetting(wge, "COLLECT_BAG")) {
                    RelluEssentials.getInstance().collectBagWorlds.add(we.getName());
                }

                if (getWorldNameBySetting(wge, "USE_CLOUDSAILOR")) {
                    RelluEssentials.getInstance().useCloudsailorWorlds.add(we.getName());
                }

                if (getWorldNameBySetting(wge, "DEATH_LOSE_COINS")) {
                    RelluEssentials.getInstance().deathLoseCoins.add(we.getName());
                }

                if (getWorldNameBySetting(wge, "ORE_RESPAWN")) {
                    RelluEssentials.getInstance().oreRespawn.add(we.getName());
                }

                if (getWorldNameBySetting(wge, "DEATH_CREATE_HOME")) {
                    RelluEssentials.getInstance().deathCreateHome.add(we.getName());
                }

                if (getWorldNameBySetting(wge, "SCOREBOARD_SHOW")) {
                    RelluEssentials.getInstance().scoreboardShow.add(we.getName());
                }

                consoleSendMessage(PLUGIN_NAME_CONSOLE,languageHelper.get(MessageKey.PLUGIN_DATABASE_ADDING_WORLD, wge.getName(), we.getName(), wge.getSettings().size()));
            }
        }

        RelluEssentials.getInstance().groupEntryList.addAll(dBH.getGroups());

        for(int i = 0; i < RelluEssentials.getInstance().getBagAPI().getBagTypeEntryList().size(); i++){
            ItemStack[] isa = BagHelper.getItemStacks(RelluEssentials.getInstance().getBagAPI().getBagTypeEntryList().get(i));
            Collections.addAll(RelluEssentials.getInstance().bagBlocks2collect, isa);
        }
    }

    private static boolean getWorldNameBySetting(@NonNull WorldGroupEntry wge, String setting) {
        return wge.getSettings().stream()
                .filter(s -> setting.equals(s.getSettingEntry().getName()))
                .findFirst()
                .map(WorldGroupSettingEntry::isValue)
                .orElse(false);
    }

    public void afterWorldLoaded(){
        new BukkitRunnable() {
            @Override
            public void run() {
                RelluEssentials.getInstance().setProtectionAPI(new ProtectionAPI(dBH.getProtectionLocks(), dBH.getProtections()));
                RelluEssentials.getInstance().setWarpAPI(new WarpAPI(dBH.getWarps()));
                RelluEssentials.getInstance().getPlayerAPI().reloadPlayerHomes();
            }
        }.runTaskLater(RelluEssentials.getInstance(), 1L);
    }

    public DatabaseHelper getDatabaseHelper() {
        return dBH;
    }
}