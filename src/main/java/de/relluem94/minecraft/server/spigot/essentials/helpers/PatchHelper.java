package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import java.util.List;
import java.util.UUID;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

public class PatchHelper {

    private final DatabaseHelper databaseHelper;

    public PatchHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    private void finishPatching(){
        List<PlayerEntry> pel = databaseHelper.getPlayers();
        pel.forEach(p -> RelluEssentials.getInstance().getPlayerAPI().putPlayerEntry(UUID.fromString(p.getUuid()), p));

        RelluEssentials.getInstance().setPluginInformation(databaseHelper.getPluginInformation());
    }

    private void patch1() {
        String v = "patches/v1/";
        consoleSendMessage(Constants.PLUGIN_NAME_CONSOLE, "applying " + v);
        databaseHelper.executeScriptNoSchema(v + "createSchema.sql");
        databaseHelper.executeScript(v + "createGroup.sql");
        databaseHelper.executeScript(v + "createPlayer.sql");
        databaseHelper.executeScript(v + "createLocationType.sql");
        databaseHelper.executeScript(v + "createLocation.sql");
        databaseHelper.executeScript(v + "createBlockHistory.sql");
        databaseHelper.executeScript(v + "createPluginInformation.sql");
        databaseHelper.executeScript(v + "insertGroups.sql");
        databaseHelper.executeScript(v + "insertPlayers.sql");
        databaseHelper.executeScript(v + "insertLocationTypes.sql");
        databaseHelper.executeScript(v + "insertPluginInformation.sql");


        ConfigHelper ch = new ConfigHelper("players");

        if (ch.isConfigFound()) {
            List<PlayerEntry> pe = ch.getPlayers();
            pe.forEach(databaseHelper::insertPlayer);

            for (PlayerEntry p : pe) {
                PlayerEntry pu = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(UUID.fromString(p.getUuid()));
                pu.setAfk(p.isAfk());
                pu.setFlying(p.isFlying());
                pu.setCustomName(p.getCustomName());
                pu.setUpdatedBy(1);
                databaseHelper.updatePlayer(pu);

                List<LocationEntry> lel = ch.getHomes(pu);
                lel.forEach(databaseHelper::insertLocation);
            }
        }
    }

    private static final String INSERT_NEW_DB_VERSION = "insertNewDBVersion.sql";
    private static final String UPDATE_OLD_PLUGIN_INFORMATION = "updateOldPluginInformation.sql";

    private void patch2() {
        String v = "patches/v2/";
        consoleSendMessage(Constants.PLUGIN_NAME_CONSOLE, "applying " + v);
        databaseHelper.executeScript(v + "dropBlockHistory.sql");
        databaseHelper.executeScript(v + "createBlockHistory.sql");
        databaseHelper.executeScript(v + INSERT_NEW_DB_VERSION);
        databaseHelper.executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
    }

    private void patch3() {
        String v = "patches/v3/";
        consoleSendMessage(Constants.PLUGIN_NAME_CONSOLE, "applying " + v);
        databaseHelper.executeScript(v + "dropPlayerConstraint.sql");
        databaseHelper.executeScript(v + "updateAdminGroup.sql"); // changed id of Admin
        databaseHelper.executeScript(v + "updateModGroup.sql"); // changed id of Mod
        databaseHelper.executeScript(v + "updateVipGroup.sql"); // changed id of Vip
        databaseHelper.executeScript(v + "updateAdminGroupPlayer.sql"); // changed id of Admin
        databaseHelper.executeScript(v + "updateModGroupPlayer.sql"); // changed id of Mod
        databaseHelper.executeScript(v + "updateVipGroupPlayer.sql"); // changed id of Vip
        databaseHelper.executeScript(v + "addPlayerConstraint.sql");
        databaseHelper.executeScript(v + INSERT_NEW_DB_VERSION);
        databaseHelper.executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
    }

    private void patch4() {
        String v = "patches/v4/";
        consoleSendMessage(Constants.PLUGIN_NAME_CONSOLE, "applying " + v);
        databaseHelper.executeScript(v + "addBankTier.sql");
        databaseHelper.executeScript(v + "addBankAccount.sql");
        databaseHelper.executeScript(v + "addBagType.sql");
        databaseHelper.executeScript(v + "addBag.sql");
        databaseHelper.executeScript(v + "addBankTransaction.sql");
        databaseHelper.executeScript(v + "addPermission.sql");
        databaseHelper.executeScript(v + "addPermissionGroup.sql");
        databaseHelper.executeScript(v + "addPermissionPlayer.sql");
        databaseHelper.executeScript(v + "addProtections.sql");
        databaseHelper.executeScript(v + "addSkills.sql");
        databaseHelper.executeScript(v + "addSkillsPlayer.sql");
        databaseHelper.executeScript(v + "addNPC.sql");
        databaseHelper.executeScript(v + "addProtectionLocks.sql");
        databaseHelper.executeScript(v + "updatePlayer.sql");
        databaseHelper.executeScript(v + "insertProtectionLocks.sql");
        databaseHelper.executeScript(v + "insertNPC.sql");
        databaseHelper.executeScript(v + "insertSkills.sql");
        databaseHelper.executeScript(v + "insertBankTier.sql");
        databaseHelper.executeScript(v + "insertBagType.sql");
        databaseHelper.executeScript(v + "insertLocationTypes.sql");
        databaseHelper.executeScript(v + "alterPlayer.sql");
        databaseHelper.executeScript(v + "alterBankAccount.sql");
        databaseHelper.executeScript(v + "alterBankTier.sql");
        databaseHelper.executeScript(v + "alterBankTransaction.sql");
        databaseHelper.executeScript(v + INSERT_NEW_DB_VERSION);
        databaseHelper.executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
    }

    private void patch5() {
        String v = "patches/v5/";
        consoleSendMessage(Constants.PLUGIN_NAME_CONSOLE, "applying " + v);
        databaseHelper.executeScript(v + "addSetting.sql");
        databaseHelper.executeScript(v + "addPluginSetting.sql");
        databaseHelper.executeScript(v + "addSettingPlayer.sql");
        databaseHelper.executeScript(v + "addWorldGroup.sql");
        databaseHelper.executeScript(v + "addWorld.sql");
        databaseHelper.executeScript(v + "addWorldGroupInventory.sql");
        databaseHelper.executeScript(v + "addWorldGroupSetting.sql");
        databaseHelper.executeScript(v + "addCrops.sql");
        databaseHelper.executeScript(v + "addDrops.sql");
        databaseHelper.executeScript(v + "addPlayerPartner.sql");
        databaseHelper.executeScript(v + "insertSkills.sql");
        databaseHelper.executeScript(v + "insertSettings.sql");
        databaseHelper.executeScript(v + "insertWorldGroup.sql");
        databaseHelper.executeScript(v + "insertWorlds.sql");
        databaseHelper.executeScript(v + "insertWorldGroupSetting.sql");
        databaseHelper.executeScript(v + "insertBagType.sql");
        databaseHelper.executeScript(v + "insertCrops.sql");
        databaseHelper.executeScript(v + "insertDrops.sql");
        databaseHelper.executeScript(v + "addPlayerName.sql");
        databaseHelper.executeScript(v + "changePlayerCustomName.sql");
        databaseHelper.executeScript(v + INSERT_NEW_DB_VERSION);
        databaseHelper.executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
    }

    private void patch6() {
        String v = "patches/v6/";
        consoleSendMessage(Constants.PLUGIN_NAME_CONSOLE, "applying " + v);
        databaseHelper.executeScript(v + "updateNPCStick.sql");
        databaseHelper.executeScript(v + "updateNPCRedSand.sql");
        databaseHelper.executeScript(v + "updateNPCBambooBlock.sql");
        databaseHelper.executeScript(v + "updateNPCBamboo.sql");
        databaseHelper.executeScript(v + "alterBagType.sql");
        databaseHelper.executeScript(v + "alterBag.sql");
        databaseHelper.executeScript(v + "alterLumberjackBag.sql");
        databaseHelper.executeScript(v + "insertNetherBagType.sql");
        databaseHelper.executeScript(v + "alterLumberjackNPC.sql");
        databaseHelper.executeScript(v + "alterFarmingBag.sql");
        databaseHelper.executeScript(v + "alterMiningBag.sql");
        databaseHelper.executeScript(v + INSERT_NEW_DB_VERSION);
        databaseHelper.executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
    }

    private void patch7() {
        String v = "patches/v7/";
        consoleSendMessage(Constants.PLUGIN_NAME_CONSOLE, "applying " + v);
        databaseHelper.executeScript(v + "alterFarmingBag.sql");
        databaseHelper.executeScript(v + "alterFarmingBagType.sql");
        databaseHelper.executeScript(v + "alterMiningBagType.sql");
        databaseHelper.executeScript(v + INSERT_NEW_DB_VERSION);
        databaseHelper.executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
    }

    private void patch8() {
        String v = "patches/v8/";
        consoleSendMessage(Constants.PLUGIN_NAME_CONSOLE, "applying " + v);
        databaseHelper.executeScript(v + "insertProtectionLocks.sql");
        databaseHelper.executeScript(v + INSERT_NEW_DB_VERSION);
        databaseHelper.executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
    }

    private void patch9() {
        String v = "patches/v9/";
        consoleSendMessage(Constants.PLUGIN_NAME_CONSOLE, "applying " + v);
        databaseHelper.executeScript(v + "updateProtections.sql");
        databaseHelper.executeScript(v + "fixProtections.sql");
        databaseHelper.executeScript(v + INSERT_NEW_DB_VERSION);
        databaseHelper.executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
    }

    private void patch10() {
        String v = "patches/v10/";
        consoleSendMessage(Constants.PLUGIN_NAME_CONSOLE, "applying " + v);
        databaseHelper.executeScript(v + "RE-266_fixDeletedLocationsFromProtections.sql");
        databaseHelper.executeScript(v + INSERT_NEW_DB_VERSION);
        databaseHelper.executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
    }

    public void applyPatch(int version) {
        switch (version) {
            case -1:
            case 0:
                patch1();
                patch2();
                patch3();
                patch4();
                patch5();
                patch6();
                patch7();
                patch8();
                patch9();
                patch10();
                finishPatching();
                break;
            case 1:
                patch2();
                patch3();
                patch4();
                patch5();
                patch6();
                patch7();
                patch8();
                patch9();
                patch10();
                finishPatching();
                break;
            case 2:
                patch3();
                patch4();
                patch5();
                patch6();
                patch7();
                patch8();
                patch9();
                patch10();
                finishPatching();
                break;
            case 3:
                patch4();
                patch5();
                patch6();
                patch7();
                patch8();
                patch9();
                patch10();
                finishPatching();
                break;
            case 4:
                patch5();
                patch6();
                patch7();
                patch8();
                patch9();
                patch10();
                finishPatching();
                break;
            case 5:
                patch6();
                patch7();
                patch8();
                patch9();
                patch10();
                finishPatching();
                break;
            case 6:
                patch7();
                patch8();
                patch9();
                patch10();
                finishPatching();
                break;
            case 7:
                patch8();
                patch9();
                patch10();
                finishPatching();
                break;
            case 8:
                patch9();
                patch10();
                finishPatching();
                break;
            case 9:
                patch10();
                finishPatching();
                break;
            default:
                // Add Scripts here for Development without its own patch version
                break;
        }
    }

}
