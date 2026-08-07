package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
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
import de.relluem94.minecraft.server.spigot.essentials.registry.ProtectionRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.TraderNpcRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repository.BagTypeRepository;
import de.relluem94.minecraft.server.spigot.essentials.repository.WarpRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.sql.SQLException;
import java.util.Collections;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
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
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;

    TranslationService translationService = relluEssentialsPlugin.getTranslationService();

    PluginInformationEntry pie = databaseHelper.getPluginInformation();
    relluEssentialsPlugin.setPluginInformation(pie);
    databaseHelper.init();

    relluEssentialsPlugin.locationTypeEntryList.addAll(databaseHelper.getLocationTypes());

    for (DropEntry de : databaseHelper.getDrops()) {
      relluEssentialsPlugin.dropMap.put(de.getMaterial(),
          new DoubleStore<>(de.getMin(), de.getMax()));
    }

    for (CropEntry ce : databaseHelper.getCrops()) {
      relluEssentialsPlugin.crops.put(ce.getSeed(), ce.getPlant());
    }

    relluEssentialsPlugin
        .setProtectionRegistry(new ProtectionRegistry(databaseHelper.getProtectionLocks(),
            databaseHelper.getProtections()));
    relluEssentialsPlugin.setTraderNpcRegistry(new TraderNpcRegistry(translationService));
    relluEssentialsPlugin.getTraderNpcRegistry().init(databaseHelper.getTraderNPCs());
    relluEssentialsPlugin.setBagTypeRegistry(new BagTypeRegistry(new BagTypeRepository(databaseHelper.getBagTypes())));
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
            translationService.get(MessageKey.PLUGIN_DATABASE_ADDING_WORLD, wge.getName(), we.getName(),
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

    for (int i = 0; i < plugin.getBagTypeRegistry().getAll().size(); i++) {
      ItemStack[] isa = plugin.getBagService().getItemStacks(
          plugin.getBagTypeRegistry().getAll().get(i));
      Collections.addAll(plugin.bagBlocks2collect, isa);
    }
  }

  public void setGroupService(GroupService groupService){
    databaseHelper.setGroupService(groupService);
  }
}