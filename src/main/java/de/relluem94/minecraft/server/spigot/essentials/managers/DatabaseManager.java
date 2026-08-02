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
import de.relluem94.minecraft.server.spigot.essentials.registry.BagRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.BankTierRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ProtectionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.TraderNpcRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repository.WarpRepository;
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
  public void enable() {
    PluginInformationEntry pie = databaseHelper.getPluginInformation();
    RelluEssentials.getInstance().setPluginInformation(pie);
    databaseHelper.init();

    RelluEssentials.getInstance().locationTypeEntryList.addAll(databaseHelper.getLocationTypes());

    for (DropEntry de : databaseHelper.getDrops()) {
      RelluEssentials.getInstance().dropMap.put(de.getMaterial(),
          new DoubleStore<>(de.getMin(), de.getMax()));
    }

    for (CropEntry ce : databaseHelper.getCrops()) {
      RelluEssentials.getInstance().crops.put(ce.getSeed(), ce.getPlant());
    }

    RelluEssentials.getInstance().setPlayerRegistry(new PlayerRegistry(databaseHelper.getBags()));
    RelluEssentials.getInstance()
        .setProtectionRegistry(new ProtectionRegistry(databaseHelper.getProtectionLocks(),
            databaseHelper.getProtections()));
    RelluEssentials.getInstance().setTraderNpcRegistry(new TraderNpcRegistry());
    RelluEssentials.getInstance().getTraderNpcRegistry().init(databaseHelper.getTraderNPCs());
    RelluEssentials.getInstance().setBagRegistry(new BagRegistry(databaseHelper.getBagTypes()));
    RelluEssentials.getInstance()
        .setBankTierRegistry(new BankTierRegistry(databaseHelper.getBankTiers()));
    RelluEssentials.getInstance().setWarpRepository(new WarpRepository(databaseHelper.getWarps()));

    RelluEssentials.settingEntriesList.addAll(databaseHelper.getAllSettings());

    for (WorldGroupEntry wge : databaseHelper.getWorldGroups()) {
      for (WorldEntry we : databaseHelper.getWorldByGroup(wge)) {
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

        consoleSendMessage(PLUGIN_NAME_CONSOLE,
            languageHelper.get(MessageKey.PLUGIN_DATABASE_ADDING_WORLD, wge.getName(), we.getName(),
                wge.getSettings().size()));
      }
    }

    RelluEssentials.getInstance().groupEntryList.addAll(databaseHelper.getGroups());

    for (int i = 0; i < RelluEssentials.getInstance().getBagRegistry().getBagTypeEntryList().size();
        i++) {
      ItemStack[] isa = BagHelper.getItemStacks(
          RelluEssentials.getInstance().getBagRegistry().getBagTypeEntryList().get(i));
      Collections.addAll(RelluEssentials.getInstance().bagBlocks2collect, isa);
    }
  }

  /**
   * Initializes registries and repositories after the world has been loaded. Runs with a 1-tick
   * delay to ensure the world is fully available.
   */
  public void afterWorldLoaded() {
    new BukkitRunnable() {
      @Override
      public void run() {
        RelluEssentials.getInstance()
            .setProtectionRegistry(new ProtectionRegistry(databaseHelper.getProtectionLocks(),
                databaseHelper.getProtections()));
        RelluEssentials.getInstance()
            .setWarpRepository(new WarpRepository(databaseHelper.getWarps()));
        RelluEssentials.getInstance().getPlayerService().reloadPlayerHomes();
      }
    }.runTaskLater(RelluEssentials.getInstance(), 1L);
  }
}