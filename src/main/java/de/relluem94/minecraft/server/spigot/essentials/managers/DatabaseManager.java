package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseConstants.PLUGIN_DATABASE_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import com.mysql.cj.jdbc.MysqlDataSource;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.PersistenceContext;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ConfigHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.BagDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.BankDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.CropDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.DropDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.LocationDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.LocationTypeDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.NpcDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.PlayerDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.PluginInformationDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.ProtectionDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.SettingDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.WorldGroupDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.SchemaBootstrap;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.loader.ClasspathSqlResourceLoader;
import de.relluem94.minecraft.server.spigot.essentials.persistence.migration.DatabaseMigrator;
import de.relluem94.minecraft.server.spigot.essentials.registries.LocationTypeRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.SettingRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.WorldGroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.PluginInformationRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.SettingRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.WorldGroupRepository;
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
public class DatabaseManager implements Enable {

  private final DataSource dataSource;
  @Getter
  private DatabaseHelper databaseHelper;

  /**
   * Creates a new DatabaseManager and establishes a database connection.
   *
   * @param host     the database host
   * @param user     the database user
   * @param password the database password
   * @param port     the database port
   * @throws RuntimeException if the database connection fails
   */
  public DatabaseManager(String host, String user, String password,
      int port) {
    try {
      SchemaBootstrap bootstrap = new SchemaBootstrap(
          "jdbc:mysql://" + host + ":" + port,
          user,
          password,
          "rellu_essentials"
      );
      bootstrap.ensureSchemaExists();

      dataSource = buildDataSource(host, port, user, password);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentialsPlugin.getServiceContext();
    PersistenceContext persistenceContext = relluEssentialsPlugin.getPersistenceContext();

    ClasspathSqlResourceLoader sqlResourceLoader = new ClasspathSqlResourceLoader();
    QueryExecutor queryExecutor = new QueryExecutor(dataSource, sqlResourceLoader);

    databaseHelper = new DatabaseHelper(dataSource,
        sqlResourceLoader, serviceContext);

    persistenceContext.setCropDao(new CropDao(queryExecutor));
    persistenceContext.setDropDao(new DropDao(queryExecutor));
    persistenceContext.setLocationDao(new LocationDao(queryExecutor, serviceContext));
    persistenceContext.setLocationTypeDao(new LocationTypeDao(queryExecutor));
    persistenceContext.setNpcDao(new NpcDao(queryExecutor));
    persistenceContext.setPlayerDao(new PlayerDao(queryExecutor, serviceContext));
    persistenceContext.setPluginInformationDao(new PluginInformationDao(queryExecutor));
    persistenceContext.setProtectionDao(new ProtectionDao(queryExecutor));
    persistenceContext.setSettingDao(new SettingDao(queryExecutor));
    persistenceContext.setBagDao(new BagDao(queryExecutor));
    persistenceContext.setBankDao(new BankDao(queryExecutor));

    PluginInformationRepository pluginInformationRepository = new PluginInformationRepository(
        persistenceContext.getPluginInformationDao());
    PluginInformationService pluginInformationService = new PluginInformationService(
        pluginInformationRepository);

    pluginInformationService.load();
    serviceContext.setPluginInformationService(pluginInformationService);

    LocationTypeRegistry locationTypeRegistry = new LocationTypeRegistry();
    locationTypeRegistry.initialize(persistenceContext.getLocationTypeDao().findAll());
    LocationTypeService locationTypeService = new LocationTypeService(locationTypeRegistry);
    serviceContext.setLocationTypeService(locationTypeService);

    patch(persistenceContext, serviceContext, queryExecutor);

    SettingRepository settingRepository = new SettingRepository(persistenceContext.getSettingDao());
    SettingRegistry settingRegistry = new SettingRegistry();
    SettingService settingService = new SettingService(settingRegistry, settingRepository);
    settingService.loadAll();
    serviceContext.setSettingService(settingService);

    persistenceContext.setWorldGroupDao(new WorldGroupDao(queryExecutor, serviceContext));

        WorldGroupRegistry worldGroupRegistry = new WorldGroupRegistry();
    WorldGroupRepository worldGroupRepository = new WorldGroupRepository(
        persistenceContext.getWorldGroupDao());
    WorldGroupService worldGroupService = new WorldGroupService(worldGroupRegistry,
        worldGroupRepository);
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

  private void patch(PersistenceContext persistenceContext, ServiceContext serviceContext,
      QueryExecutor queryExecutor) {
    DatabaseMigrator databaseMigrator = new DatabaseMigrator(
        persistenceContext,
        queryExecutor,
        serviceContext.getPlayerService(),
        patchedInformation -> {
          PluginInformationService service = serviceContext.getPluginInformationService();
          if (service != null) {
            service.applyPatchedInformation(patchedInformation);
          }
        },
        new ConfigHelper("players")
    );

    databaseMigrator.applyPatch(databaseMigrator.loadPluginInformation().getDbVersion());
  }

  private MysqlDataSource buildDataSource(String host, int port, String user,
      String password) throws SQLException {
    MysqlDataSource ds = new MysqlDataSource();
    ds.setServerName(host);
    ds.setPort(port);
    ds.setUser(user);
    ds.setPassword(password);
    ds.setUseSSL(false);
    ds.setDatabaseName(PLUGIN_DATABASE_NAME);
    ds.setAllowPublicKeyRetrieval(true);
    return ds;
  }
}