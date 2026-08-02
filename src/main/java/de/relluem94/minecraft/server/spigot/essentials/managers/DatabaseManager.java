package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.DatabaseHelperFactory;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.CropEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.DropEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.WorldGroupSettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.BagTypeRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.BankTierRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ProtectionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.TraderNpcRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repository.BagTypeRepository;
import de.relluem94.minecraft.server.spigot.essentials.repository.GroupRepository;
import de.relluem94.minecraft.server.spigot.essentials.repository.WarpRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.sql.SQLException;
import java.util.Collections;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NonNull;

/**
 * Manages database access and initializes all registries and plugin data on startup.
 */
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
   *
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
  public void enable(RelluEssentials plugin) {
    PluginInformationEntry pie = databaseHelper.getPluginInformation();
    plugin.setPluginInformation(pie);
    databaseHelper.init();

    PlayerRegistry playerRegistry = new PlayerRegistry(databaseHelper.getBags());
    plugin.setPlayerRegistry(playerRegistry);

    GroupRepository groupRepository = new GroupRepository(databaseHelper.getGroups());
    GroupRegistry groupRegistry = new GroupRegistry(groupRepository);
    GroupService groupService = new GroupService(groupRegistry, groupRepository, playerRegistry);

    plugin.setGroupRegistry(groupRegistry);
    plugin.setGroupService(groupService);
    databaseHelper.setGroupService(groupService);

    plugin.locationTypeEntryList.addAll(databaseHelper.getLocationTypes());

    for (DropEntry de : databaseHelper.getDrops()) {
      plugin.dropMap.put(de.getMaterial(),
          new DoubleStore<>(de.getMin(), de.getMax()));
    }

    for (CropEntry ce : databaseHelper.getCrops()) {
      plugin.crops.put(ce.getSeed(), ce.getPlant());
    }

    plugin
        .setProtectionRegistry(new ProtectionRegistry(databaseHelper.getProtectionLocks(),
            databaseHelper.getProtections()));
    plugin.setTraderNpcRegistry(new TraderNpcRegistry());
    plugin.getTraderNpcRegistry().init(databaseHelper.getTraderNPCs());
    plugin.setBagTypeRegistry(new BagTypeRegistry(new BagTypeRepository(databaseHelper.getBagTypes())));
    plugin
        .setBankTierRegistry(new BankTierRegistry(databaseHelper.getBankTiers()));
    plugin.setWarpRepository(new WarpRepository(databaseHelper.getWarps()));

    RelluEssentials.settingEntriesList.addAll(databaseHelper.getAllSettings());

    for (WorldGroupEntry wge : databaseHelper.getWorldGroups()) {
      for (WorldEntry we : databaseHelper.getWorldByGroup(wge)) {
        plugin.worldsMap.put(wge, we);

        if (getWorldNameBySetting(wge, "COLLECT_BAG")) {
          plugin.collectBagWorlds.add(we.getName());
        }

        if (getWorldNameBySetting(wge, "USE_CLOUDSAILOR")) {
          plugin.useCloudsailorWorlds.add(we.getName());
        }

        if (getWorldNameBySetting(wge, "DEATH_LOSE_COINS")) {
          plugin.deathLoseCoins.add(we.getName());
        }

        if (getWorldNameBySetting(wge, "ORE_RESPAWN")) {
          plugin.oreRespawn.add(we.getName());
        }

        if (getWorldNameBySetting(wge, "DEATH_CREATE_HOME")) {
          plugin.deathCreateHome.add(we.getName());
        }

        if (getWorldNameBySetting(wge, "SCOREBOARD_SHOW")) {
          plugin.scoreboardShow.add(we.getName());
        }

        consoleSendMessage(PLUGIN_NAME_CONSOLE,
            languageHelper.get(MessageKey.PLUGIN_DATABASE_ADDING_WORLD, wge.getName(), we.getName(),
                wge.getSettings().size()));
      }
    }

    for (int i = 0; i < plugin.getBagTypeRegistry().getAll().size(); i++) {
      ItemStack[] isa = BagHelper.getItemStacks(
          plugin.getBagTypeRegistry().getAll().get(i));
      Collections.addAll(plugin.bagBlocks2collect, isa);
    }
  }

  /**
   * Initializes registries and repositories after the world has been loaded. Runs with a 1-tick
   * delay to ensure the world is fully available.
   */
  public void afterWorldLoaded(RelluEssentials plugin) {
    new BukkitRunnable() {
      @Override
      public void run() {
        plugin
            .setProtectionRegistry(new ProtectionRegistry(databaseHelper.getProtectionLocks(),
                databaseHelper.getProtections()));
        plugin
            .setWarpRepository(new WarpRepository(databaseHelper.getWarps()));
        plugin.getPlayerService().reloadPlayerHomes();
      }
    }.runTaskLater(plugin, 1L);
  }
}