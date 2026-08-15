package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.LocationMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.RowMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.loader.SqlResourceLoader;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Legacy DatabaseHelper will be removed.
 *
 * @author rellu
 */
@Deprecated
public class DatabaseHelper {

  private final DataSource dataSource;
  private final SqlResourceLoader sqlResourceLoader;
  private final ServiceContext serviceContext;

  public DatabaseHelper(DataSource dataSource,
      SqlResourceLoader sqlResourceLoader, ServiceContext serviceContext) {
    this.dataSource = dataSource;
    this.sqlResourceLoader = sqlResourceLoader;
    this.serviceContext = serviceContext;
  }

  private <T> List<T> queryList(String sqlFile, StatementConfigurer configurer,
      RowMapper<T> mapper) {
    List<T> results = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          results.add(mapper.map(rs));
        }
      }
    } catch (SQLException | FileNotFoundException ex) {}
    return results;
  }

  public List<LocationEntry> getWarps() {
    return queryList("getWarps.sql", _ -> {
    }, rs -> LocationMapper.mapLocation(rs, serviceContext.getLocationTypeService()));
  }
}