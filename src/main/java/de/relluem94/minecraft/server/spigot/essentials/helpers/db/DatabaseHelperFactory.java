package de.relluem94.minecraft.server.spigot.essentials.helpers.db;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseConstants.PLUGIN_DATABASE_NAME;

import com.mysql.cj.jdbc.MysqlDataSource;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.PersistenceContext;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ConfigHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PatchHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.loader.ClasspathSqlResourceLoader;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.helpers.IPatchHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginInformationService;
import java.sql.SQLException;
import java.util.function.Consumer;

@Deprecated
public class DatabaseHelperFactory {

  private DatabaseHelperFactory() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static DatabaseHelper createForProduction(String host, int port, String user,
      String password, ServiceContext serviceContext, PersistenceContext persistenceContext) throws SQLException {
    MysqlDataSource dataSource = buildDataSource(host, port, user, password, PLUGIN_DATABASE_NAME);
    MysqlDataSource dataSourceNoSchema = buildDataSource(host, port, user, password, null);
    ClasspathSqlResourceLoader sqlResourceLoader = new ClasspathSqlResourceLoader();

    DatabaseHelper databaseHelper = new DatabaseHelper(dataSource, dataSourceNoSchema,
        sqlResourceLoader, serviceContext);

    IPatchHelper patchHelper = new PatchHelper(
        persistenceContext,
        databaseHelper,
        serviceContext.getPlayerService(),
        patchedInformation -> {
          PluginInformationService service = serviceContext.getPluginInformationService();
          if (service != null) {
            service.applyPatchedInformation(patchedInformation);
          }
        },
        new ConfigHelper("players")
    );

    databaseHelper.setPatchHelper(patchHelper);
    return databaseHelper;
  }

  @SuppressWarnings("unused")
  public static DatabaseHelper createForTest(String host, int port, ServiceContext serviceContext, PersistenceContext persistenceContext)
      throws SQLException {
    MysqlDataSource dataSource = buildDataSource(host, port, "root", "", PLUGIN_DATABASE_NAME);
    MysqlDataSource dataSourceNoSchema = buildDataSource(host, port, "root", "", null);
    ClasspathSqlResourceLoader sqlResourceLoader = new ClasspathSqlResourceLoader();

    DatabaseHelper databaseHelper = new DatabaseHelper(dataSource, dataSourceNoSchema,
        sqlResourceLoader, new ServiceContext());

    IPatchHelper patchHelper = new PatchHelper(
        persistenceContext,
        databaseHelper,
        serviceContext.getPlayerService(),
        noOpPluginInfoConsumer(),
        new ConfigHelper("players")
    );

    databaseHelper.setPatchHelper(patchHelper);
    return databaseHelper;
  }

  public static MysqlDataSource buildDataSource(String host, int port, String user, String password) {
    try {
      return buildDataSource(host, port, user, password, null);
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to build data source", e);
    }
  }

  public static MysqlDataSource buildDataSource(String host, int port, String user,
      String password, String schema) throws SQLException {
    MysqlDataSource ds = new MysqlDataSource();
    ds.setServerName(host);
    ds.setPort(port);
    ds.setUser(user);
    ds.setPassword(password);
    ds.setUseSSL(false);
    ds.setAllowPublicKeyRetrieval(true);
    if (schema != null) {
      ds.setDatabaseName(schema);
    }
    return ds;
  }

  static Consumer<PluginInformationEntry> noOpPluginInfoConsumer() {
    return _ -> {
    };
  }
}