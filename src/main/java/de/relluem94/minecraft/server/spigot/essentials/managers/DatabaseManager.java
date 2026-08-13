package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseConstants.PLUGIN_DATABASE_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.PersistenceContext;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.DatabaseHelperFactory;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.loader.ClasspathSqlResourceLoader;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.LocationDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.NpcDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.PlayerDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.ProtectionDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.registries.LocationTypeRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.SettingRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.WorldGroupSettingRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.PluginInformationRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.SettingRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.WorldGroupSettingRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.LocationTypeService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginInformationService;
import de.relluem94.minecraft.server.spigot.essentials.services.SettingService;
import de.relluem94.minecraft.server.spigot.essentials.services.WorldGroupService;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

/**
 * Manages database access and initializes all registries and plugin data on startup.
 */
@SuppressWarnings("ClassCanBeRecord")
public class DatabaseManager implements Enable {

  @Getter
  private final DatabaseHelper databaseHelper;
  private final DataSource dataSource;

  /**
   * Creates a new DatabaseManager and establishes a database connection.
   *
   * @param host     the database host
   * @param user     the database user
   * @param password the database password
   * @param port     the database port
   * @throws RuntimeException if the database connection fails
   */
  public DatabaseManager(PersistenceContext persistenceContext, ServiceContext serviceContext, String host, String user, String password,
      int port) {
    try {
      dataSource = DatabaseHelperFactory.buildDataSource(host, port, user, password,
          PLUGIN_DATABASE_NAME);
      databaseHelper = DatabaseHelperFactory.createForProduction(host, port, user, password,
          serviceContext, persistenceContext);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentialsPlugin.getServiceContext();
    PersistenceContext persistenceContext = relluEssentialsPlugin.getPersistenceContext();

    PluginInformationRepository pluginInformationRepository = new PluginInformationRepository(
        databaseHelper);
    PluginInformationService pluginInformationService = new PluginInformationService(
        pluginInformationRepository);
    pluginInformationService.load();
    serviceContext.setPluginInformationService(pluginInformationService);

    ClasspathSqlResourceLoader sqlResourceLoader = new ClasspathSqlResourceLoader();
    QueryExecutor queryExecutor = new QueryExecutor(dataSource, sqlResourceLoader);

    LocationTypeRegistry locationTypeRegistry = new LocationTypeRegistry();
    locationTypeRegistry.initialize(databaseHelper.getLocationTypes());
    LocationTypeService locationTypeService = new LocationTypeService(locationTypeRegistry);
    serviceContext.setLocationTypeService(locationTypeService);


    persistenceContext.setNpcDao(new NpcDao(queryExecutor));
    persistenceContext.setPlayerDao(new PlayerDao(queryExecutor, serviceContext));
    persistenceContext.setLocationDao(new LocationDao(queryExecutor, serviceContext));
    persistenceContext.setProtectionDao(new ProtectionDao(queryExecutor));

    databaseHelper.init();

    SettingRepository settingRepository = new SettingRepository(databaseHelper);
    SettingRegistry settingRegistry = new SettingRegistry();
    SettingService settingService = new SettingService(settingRegistry, settingRepository);
    settingService.loadAll();
    serviceContext.setSettingService(settingService);



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