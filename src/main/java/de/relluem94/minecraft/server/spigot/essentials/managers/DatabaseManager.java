package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.DatabaseHelperFactory;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.CropEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.DropEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupSettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.BankTierRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ProtectionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.TraderNpcRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.CropRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.DropRuleRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.WarpRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.BlockDropService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.sql.SQLException;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

/**
 * Manages database access and initializes all registries and plugin data on startup.
 */
@SuppressWarnings("ClassCanBeRecord")
public class DatabaseManager implements Enable {

  @Getter
  private final DatabaseHelper databaseHelper;

  /**
   * Creates a new DatabaseManager and establishes a database connection.
   *
   * @param host     the database host
   * @param user     the database user
   * @param password the database password
   * @param port     the database port
   * @throws RuntimeException if the database connection fails
   */
  public DatabaseManager(String host, String user, String password, int port) {
    try {
      databaseHelper = DatabaseHelperFactory.createForProduction(host, port, user, password,
          RelluEssentials.getInstance().getPlayerRegistry());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates a new DatabaseManager with an existing DatabaseHelper instance.
   *
   * @param databaseHelper the pre-configured database helper to use
   */
  @SuppressWarnings("unused")
  public DatabaseManager(DatabaseHelper databaseHelper) {
    this.databaseHelper = databaseHelper;
  }

  private static boolean getWorldNameBySetting(@NonNull WorldGroupEntry wge, String setting) {
    return wge.getSettings().stream()
        .filter(s -> setting.equals(s.getSettingEntry().getName()))
        .findFirst()
        .map(WorldGroupSettingEntry::isValue)
        .orElse(false);
  }

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;

    PluginInformationEntry pie = databaseHelper.getPluginInformation();
    relluEssentialsPlugin.setPluginInformation(pie);
    databaseHelper.init();

    relluEssentialsPlugin.locationTypeEntryList.addAll(databaseHelper.getLocationTypes());

    DropRuleRepository dropRuleRepository = new DropRuleRepository();
    for (DropEntry de : databaseHelper.getDrops()) {
      dropRuleRepository.register(de.getMaterial(), new DoubleStore<>(de.getMin(), de.getMax()));
    }

    CropRepository cropRepository = new CropRepository();
    for (CropEntry ce : databaseHelper.getCrops()) {
      cropRepository.register(ce.getSeed(), ce.getPlant());
    }

    BlockDropService blockDropService = new BlockDropService(dropRuleRepository, cropRepository);
    relluEssentialsPlugin.setBlockDropService(blockDropService);
    relluEssentialsPlugin.getServiceContext().setBlockDropService(blockDropService);

    relluEssentialsPlugin
        .setProtectionRegistry(new ProtectionRegistry(databaseHelper.getProtectionLocks(),
            databaseHelper.getProtections()));
    relluEssentialsPlugin.setTraderNpcRegistry(
        new TraderNpcRegistry(relluEssentialsPlugin.getTranslationService()));
    relluEssentialsPlugin.getTraderNpcRegistry().init(databaseHelper.getTraderNPCs());
    relluEssentialsPlugin
        .setBankTierRegistry(new BankTierRegistry(databaseHelper.getBankTiers()));
    relluEssentialsPlugin.setWarpRepository(new WarpRepository(databaseHelper.getWarps()));

    RelluEssentials.settingEntriesList.addAll(databaseHelper.getAllSettings());

    for (WorldGroupEntry wge : databaseHelper.getWorldGroups()) {
      for (WorldEntry we : databaseHelper.getWorldByGroup(wge)) {
        relluEssentialsPlugin.worldsMap.put(wge, we);

        if (getWorldNameBySetting(wge, "COLLECT_BAG")) {
          relluEssentialsPlugin.collectBagWorlds.add(we.getName());
        }

        if (getWorldNameBySetting(wge, "USE_CLOUDSAILOR")) {
          relluEssentialsPlugin.useCloudsailorWorlds.add(we.getName());
        }

        if (getWorldNameBySetting(wge, "DEATH_LOSE_COINS")) {
          relluEssentialsPlugin.deathLoseCoins.add(we.getName());
        }

        if (getWorldNameBySetting(wge, "ORE_RESPAWN")) {
          relluEssentialsPlugin.oreRespawn.add(we.getName());
        }

        if (getWorldNameBySetting(wge, "DEATH_CREATE_HOME")) {
          relluEssentialsPlugin.deathCreateHome.add(we.getName());
        }

        if (getWorldNameBySetting(wge, "SCOREBOARD_SHOW")) {
          relluEssentialsPlugin.scoreboardShow.add(we.getName());
        }

        consoleSendMessage(PLUGIN_NAME_CONSOLE,
            relluEssentialsPlugin.getTranslationService()
                .get(MessageKey.PLUGIN_DATABASE_ADDING_WORLD, wge.getName(),
                    we.getName(),
                    wge.getSettings().size()));
      }
    }


  }

  /**
   * Initializes registries and repositories after the world has been loaded. Runs with a 1-tick
   * delay to ensure the world is fully available.
   */
  public void afterWorldLoaded(@NonNull RelluEssentials plugin) {
    plugin.getSchedulerService().runTaskLater(() -> {
      plugin
          .setProtectionRegistry(new ProtectionRegistry(databaseHelper.getProtectionLocks(),
              databaseHelper.getProtections()));
      plugin
          .setWarpRepository(new WarpRepository(databaseHelper.getWarps()));
      plugin.getPlayerService().reloadPlayerHomes();
    }, 1L);
  }

  public void setGroupService(GroupService groupService) {
    databaseHelper.setGroupService(groupService);
  }
}