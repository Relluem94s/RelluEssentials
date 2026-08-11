package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.DatabaseHelperFactory;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.SettingRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.WorldGroupSettingRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.PluginInformationRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.SettingRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.WorldGroupSettingRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginInformationService;
import de.relluem94.minecraft.server.spigot.essentials.services.SettingService;
import de.relluem94.minecraft.server.spigot.essentials.services.WorldGroupService;
import java.sql.SQLException;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

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
  public DatabaseManager(ServiceContext serviceContext, String host, String user, String password,
      int port) {
    try {
      databaseHelper = DatabaseHelperFactory.createForProduction(host, port, user, password,
          serviceContext);
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

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentialsPlugin.getServiceContext();

    PluginInformationRepository pluginInformationRepository = new PluginInformationRepository(
        databaseHelper);
    PluginInformationService pluginInformationService = new PluginInformationService(
        pluginInformationRepository);
    pluginInformationService.load();
    serviceContext.setPluginInformationService(pluginInformationService);

    databaseHelper.init();

    SettingRepository settingRepository = new SettingRepository(databaseHelper);
    SettingRegistry settingRegistry = new SettingRegistry();
    SettingService settingService = new SettingService(settingRegistry, settingRepository);
    settingService.loadAll();
    serviceContext.setSettingService(settingService);

    relluEssentialsPlugin.getLocationTypeEntryList().addAll(databaseHelper.getLocationTypes());

    WorldGroupSettingRegistry worldGroupSettingRegistry = new WorldGroupSettingRegistry();
    WorldGroupSettingRepository worldGroupSettingRepository = new WorldGroupSettingRepository(
        databaseHelper);
    WorldGroupService worldGroupService = new WorldGroupService(worldGroupSettingRegistry,
        worldGroupSettingRepository);
    worldGroupService.loadAll();
    serviceContext.setWorldGroupService(worldGroupService);

    worldGroupService.getWorldsMap().entries().forEach(entry -> {
      WorldGroupEntry worldGroupEntry = entry.getKey();
      WorldEntry worldEntry = entry.getValue();
      consoleSendMessage(PLUGIN_NAME_CONSOLE,
          serviceContext.getTranslationService()
              .get(MessageKey.PLUGIN_DATABASE_ADDING_WORLD, worldGroupEntry.getName(),
                  worldEntry.getName(),
                  worldGroupEntry.getSettings().size()));
    });
  }
}