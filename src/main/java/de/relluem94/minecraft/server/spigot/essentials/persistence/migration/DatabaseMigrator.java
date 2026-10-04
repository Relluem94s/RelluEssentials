package de.relluem94.minecraft.server.spigot.essentials.persistence.migration;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.PersistenceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.MiscMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.migration.ConfigMigrationService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Handles the sequential application of database patches to migrate the database schema
 * and data from one version to the next.
 *
 * <p>Each patch method corresponds to a specific version upgrade and executes the required
 * SQL scripts in order. After all applicable patches have been applied, the in-memory
 * player cache is refreshed and the caller is notified via the provided callback.</p>
 *
 * @author rellu
 */
public class DatabaseMigrator {

  private static final String INSERT_NEW_DB_VERSION = "insertNewDBVersion.sql";
  private static final String UPDATE_OLD_PLUGIN_INFORMATION = "updateOldPluginInformation.sql";

  private final QueryExecutor queryExecutor;
  private final PlayerService playerService;
  private final Consumer<PluginInformationEntry> onPatchingFinished;
  private final ConfigMigrationService configMigrationService;
  private final PersistenceContext persistenceContext;
  private final ConsoleCommandSender consoleSender;

  /**
   * Creates a new {@code DatabaseMigrator} with all required dependencies.
   *
   * @param persistenceContext     the persistence context providing access to all DAOs
   * @param queryExecutor          the executor used to run SQL scripts and queries
   * @param playerService          the service used to manage the in-memory player cache
   * @param onPatchingFinished     callback invoked with the current {@link PluginInformationEntry}
   *                               after all patches have been successfully applied
   * @param configMigrationService the service used to read legacy configuration data during migration
   * @param consoleSender          the console sender used to log migration progress messages
   */
  public DatabaseMigrator(PersistenceContext persistenceContext, QueryExecutor queryExecutor,
      PlayerService playerService, Consumer<PluginInformationEntry> onPatchingFinished,
      ConfigMigrationService configMigrationService, ConsoleCommandSender consoleSender) {
    this.queryExecutor = queryExecutor;
    this.playerService = playerService;
    this.onPatchingFinished = onPatchingFinished;
    this.configMigrationService = configMigrationService;
    this.persistenceContext = persistenceContext;
    this.consoleSender = consoleSender;
  }

  /**
   * Loads the current plugin information from the database, including the stored database version.
   *
   * <p>If the plugin information table does not yet exist or any error occurs during the query,
   * a fallback {@link PluginInformationEntry} with a database version of {@code -1} is returned,
   * indicating that the database has not been initialized yet.</p>
   *
   * @return the current {@link PluginInformationEntry} from the database,
   *         or a fallback entry with version {@code -1} if the table is unavailable
   */
  public PluginInformationEntry loadPluginInformation() {
    PluginInformationEntry fallback = new PluginInformationEntry();
    try {
      PluginInformationEntry result = queryExecutor.querySingle(
          "getPluginInformation.sql", _ -> {
          }, MiscMapper::mapPluginInformation);
      return Optional
          .ofNullable(result)
          .orElse(fallback);
    } catch (Exception ex) {
      consoleSender.sendMessage(Constants.PLUGIN_NAME_CONSOLE + " Init Database..");
      fallback.setDbVersion(-1);
      return fallback;
    }
  }

  private void finishPatching() {
    List<PlayerEntry> players = persistenceContext.getPlayerDao().findAll();
    players.forEach(p -> playerService.putPlayerEntry(UUID.fromString(p.getUuid()), p));

    PluginInformationEntry pluginInformation = loadPluginInformation();
    onPatchingFinished.accept(pluginInformation);
  }

  private void executeScript(String script) {
    queryExecutor.executeScript(script);
  }

  private void patch1() {
    String v = "patches/v1/";
    consoleSender.sendMessage(Constants.PLUGIN_NAME_CONSOLE + " applying " + v);
    executeScript(v + "createGroup.sql");
    executeScript(v + "createPlayer.sql");
    executeScript(v + "createLocationType.sql");
    executeScript(v + "createLocation.sql");
    executeScript(v + "createBlockHistory.sql");
    executeScript(v + "createPluginInformation.sql");
    executeScript(v + "insertGroups.sql");
    executeScript(v + "insertPlayers.sql");
    executeScript(v + "insertLocationTypes.sql");
    executeScript(v + "insertPluginInformation.sql");

    if (configMigrationService.legacyConfigExists("players")) {
      List<PlayerEntry> pe = configMigrationService.getPlayers("players");
      pe.forEach(persistenceContext.getPlayerDao()::insert);

      for (PlayerEntry p : pe) {
        PlayerEntry pu = playerService.getPlayerEntry(UUID.fromString(p.getUuid()));
        pu.setAfk(p.isAfk());
        pu.setFlying(p.isFlying());
        pu.setCustomName(p.getCustomName());
        pu.setUpdatedBy(1);
        persistenceContext.getPlayerDao().update(pu);

        List<LocationEntry> lel = configMigrationService.getHomes("players", pu);
        lel.forEach(persistenceContext.getLocationDao()::insertLocation);
      }
    }
  }

  private void patch2() {
    String v = "patches/v2/";
    consoleSender.sendMessage(Constants.PLUGIN_NAME_CONSOLE + " applying " + v);
    executeScript(v + "dropBlockHistory.sql");
    executeScript(v + "createBlockHistory.sql");
    executeScript(v + INSERT_NEW_DB_VERSION);
    executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
  }

  private void patch3() {
    String v = "patches/v3/";
    consoleSender.sendMessage(Constants.PLUGIN_NAME_CONSOLE + " applying " + v);
    executeScript(v + "dropPlayerConstraint.sql");
    executeScript(v + "updateAdminGroup.sql");
    executeScript(v + "updateModGroup.sql");
    executeScript(v + "updateVipGroup.sql");
    executeScript(v + "updateAdminGroupPlayer.sql");
    executeScript(v + "updateModGroupPlayer.sql");
    executeScript(v + "updateVipGroupPlayer.sql");
    executeScript(v + "addPlayerConstraint.sql");
    executeScript(v + INSERT_NEW_DB_VERSION);
    executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
  }

  private void patch4() {
    String v = "patches/v4/";
    consoleSender.sendMessage(Constants.PLUGIN_NAME_CONSOLE + " applying " + v);
    executeScript(v + "addBankTier.sql");
    executeScript(v + "addBankAccount.sql");
    executeScript(v + "addBagType.sql");
    executeScript(v + "addBag.sql");
    executeScript(v + "addBankTransaction.sql");
    executeScript(v + "addPermission.sql");
    executeScript(v + "addPermissionGroup.sql");
    executeScript(v + "addPermissionPlayer.sql");
    executeScript(v + "addProtections.sql");
    executeScript(v + "addSkills.sql");
    executeScript(v + "addSkillsPlayer.sql");
    executeScript(v + "addNPC.sql");
    executeScript(v + "addProtectionLocks.sql");
    executeScript(v + "updatePlayer.sql");
    executeScript(v + "insertProtectionLocks.sql");
    executeScript(v + "insertNPC.sql");
    executeScript(v + "insertSkills.sql");
    executeScript(v + "insertBankTier.sql");
    executeScript(v + "insertBagType.sql");
    executeScript(v + "insertLocationTypes.sql");
    executeScript(v + "alterPlayer.sql");
    executeScript(v + "alterBankAccount.sql");
    executeScript(v + "alterBankTier.sql");
    executeScript(v + "alterBankTransaction.sql");
    executeScript(v + INSERT_NEW_DB_VERSION);
    executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
  }

  private void patch5() {
    String v = "patches/v5/";
    consoleSender.sendMessage(Constants.PLUGIN_NAME_CONSOLE + " applying " + v);
    executeScript(v + "addSetting.sql");
    executeScript(v + "addPluginSetting.sql");
    executeScript(v + "addSettingPlayer.sql");
    executeScript(v + "addWorldGroup.sql");
    executeScript(v + "addWorld.sql");
    executeScript(v + "addWorldGroupInventory.sql");
    executeScript(v + "addWorldGroupSetting.sql");
    executeScript(v + "addCrops.sql");
    executeScript(v + "addDrops.sql");
    executeScript(v + "addPlayerPartner.sql");
    executeScript(v + "insertSkills.sql");
    executeScript(v + "insertSettings.sql");
    executeScript(v + "insertWorldGroup.sql");
    executeScript(v + "insertWorlds.sql");
    executeScript(v + "insertWorldGroupSetting.sql");
    executeScript(v + "insertBagType.sql");
    executeScript(v + "insertCrops.sql");
    executeScript(v + "insertDrops.sql");
    executeScript(v + "addPlayerName.sql");
    executeScript(v + "changePlayerCustomName.sql");
    executeScript(v + INSERT_NEW_DB_VERSION);
    executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
  }

  private void patch6() {
    String v = "patches/v6/";
    consoleSender.sendMessage(Constants.PLUGIN_NAME_CONSOLE + " applying " + v);
    executeScript(v + "updateNPCStick.sql");
    executeScript(v + "updateNPCRedSand.sql");
    executeScript(v + "updateNPCBambooBlock.sql");
    executeScript(v + "updateNPCBamboo.sql");
    executeScript(v + "alterBagType.sql");
    executeScript(v + "alterBag.sql");
    executeScript(v + "alterLumberjackBag.sql");
    executeScript(v + "insertNetherBagType.sql");
    executeScript(v + "alterLumberjackNPC.sql");
    executeScript(v + "alterFarmingBag.sql");
    executeScript(v + "alterMiningBag.sql");
    executeScript(v + INSERT_NEW_DB_VERSION);
    executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
  }

  private void patch7() {
    String v = "patches/v7/";
    consoleSender.sendMessage(Constants.PLUGIN_NAME_CONSOLE + " applying " + v);
    executeScript(v + "alterFarmingBag.sql");
    executeScript(v + "alterFarmingBagType.sql");
    executeScript(v + "alterMiningBagType.sql");
    executeScript(v + INSERT_NEW_DB_VERSION);
    executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
  }

  private void patch8() {
    String v = "patches/v8/";
    consoleSender.sendMessage(Constants.PLUGIN_NAME_CONSOLE + " applying " + v);
    executeScript(v + "insertProtectionLocks.sql");
    executeScript(v + INSERT_NEW_DB_VERSION);
    executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
  }

  private void patch9() {
    String v = "patches/v9/";
    consoleSender.sendMessage(Constants.PLUGIN_NAME_CONSOLE + " applying " + v);
    executeScript(v + "updateProtections.sql");
    executeScript(v + "fixProtections.sql");
    executeScript(v + INSERT_NEW_DB_VERSION);
    executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
  }

  private void patch10() {
    String v = "patches/v10/";
    consoleSender.sendMessage(Constants.PLUGIN_NAME_CONSOLE + " applying " + v);
    executeScript(v + "RE-266_fixDeletedLocationsFromProtections.sql");
    executeScript(v + "alterMonsterBag.sql");
    executeScript(v + "insertProtectionLocks.sql");
    executeScript(v + "insertAnimalBagType.sql");
    executeScript(v + "insertSettings.sql");
    executeScript(v + "insertWorldGroupSettingsCloudsailor.sql");
    executeScript(v + "insertWorldGroupSettingsCoinsLose.sql");
    executeScript(v + "insertWorldGroupSettingsEntityDropsCoins.sql");
    executeScript(v + "insertWorldGroupSettingsDeathPoint.sql");
    executeScript(v + "insertWorldGroupSettingsScoreBoardShow.sql");
    executeScript(v + "insertWorldGroupSettingsDisablePhantomSpawning.sql");
    executeScript(v + "updateFischerNPCTurtleScute.sql");
    executeScript(v + "updateFloristNPCShortGrass.sql");
    executeScript(v + "updateWorldGroupSettings_newColumn.sql");
    executeScript(v + "updateWorldGroupSettings_moveValues.sql");
    executeScript(v + "updateWorldGroupSettings_removeColumnAndRename.sql");
    executeScript(v + "updatePlayerSettings_newColumn.sql");
    executeScript(v + "updatePlayerSettings_moveValues.sql");
    executeScript(v + "updatePlayerSettings_removeColumnAndRename.sql");
    executeScript(v + "updatePluginSetting_newColumn.sql");
    executeScript(v + "updatePluginSetting_removeColumnAndRename.sql");
    executeScript(v + "createCustomNPC.sql");
    executeScript(v + "createCustomNPCDialogue.sql");
    executeScript(v + INSERT_NEW_DB_VERSION);
    executeScript(v + UPDATE_OLD_PLUGIN_INFORMATION);
  }

  /**
   * Applies all database patches starting from the given version index up to the latest patch.
   *
   * <p>Each patch is applied in sequential order. If the provided version is already at or beyond
   * the latest available patch, no patches are applied and the method returns immediately.
   * Once all applicable patches have been executed, the player cache is refreshed and
   * the registered callback is notified.</p>
   *
   * @param version the current database version, used to determine which patches still need to be applied
   */
  public void applyPatch(int version) {
    List<Runnable> allPatches = List.of(
        this::patch1,
        this::patch2,
        this::patch3,
        this::patch4,
        this::patch5,
        this::patch6,
        this::patch7,
        this::patch8,
        this::patch9,
        this::patch10
    );

    int startIndex = Math.max(version, 0);

    if (startIndex >= allPatches.size()) {
      return;
    }

    allPatches.subList(startIndex, allPatches.size()).forEach(Runnable::run);
    finishPatching();
  }
}