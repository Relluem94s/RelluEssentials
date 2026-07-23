package de.relluem94.minecraft.server.spigot.essentials.helpers.db;

import com.mysql.cj.jdbc.MysqlDataSource;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ConfigHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PatchHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.loader.ClasspathSqlResourceLoader;
import de.relluem94.minecraft.server.spigot.essentials.helpers.interfaces.IPatchHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;

import java.sql.SQLException;
import java.util.function.Consumer;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseConstants.PLUGIN_DATABASE_NAME;

public class DatabaseHelperFactory {
    private DatabaseHelperFactory() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static DatabaseHelper createForProduction(String host, int port, String user,
                                                     String password, PlayerAPI playerAPI) throws SQLException {
        MysqlDataSource dataSource = buildDataSource(host, port, user, password, PLUGIN_DATABASE_NAME);
        MysqlDataSource dataSourceNoSchema = buildDataSource(host, port, user, password, null);
        ClasspathSqlResourceLoader sqlResourceLoader = new ClasspathSqlResourceLoader();

        DatabaseHelper databaseHelper = new DatabaseHelper(dataSource, dataSourceNoSchema, sqlResourceLoader);

        IPatchHelper patchHelper = new PatchHelper(
                databaseHelper,
                playerAPI,
                RelluEssentials.getInstance()::setPluginInformation,
                new ConfigHelper("players")
        );

        databaseHelper.setPatchHelper(patchHelper);
        return databaseHelper;
    }

    @SuppressWarnings("unused")
    public static DatabaseHelper createForTest(String host, int port, PlayerAPI playerAPI) throws SQLException {
        MysqlDataSource dataSource = buildDataSource(host, port, "root", "", PLUGIN_DATABASE_NAME);
        MysqlDataSource dataSourceNoSchema = buildDataSource(host, port, "root", "", null);
        ClasspathSqlResourceLoader sqlResourceLoader = new ClasspathSqlResourceLoader();

        DatabaseHelper databaseHelper = new DatabaseHelper(dataSource, dataSourceNoSchema, sqlResourceLoader);

        IPatchHelper patchHelper = new PatchHelper(
                databaseHelper,
                playerAPI,
                noOpPluginInfoConsumer(),
                new ConfigHelper("players")
        );

        databaseHelper.setPatchHelper(patchHelper);
        return databaseHelper;
    }

    private static MysqlDataSource buildDataSource(String host, int port, String user, String password, String schema) throws SQLException {
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
        return _ -> {};
    }
}