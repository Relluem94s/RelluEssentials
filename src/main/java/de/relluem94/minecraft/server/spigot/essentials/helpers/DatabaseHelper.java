package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.LocationMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.PlayerMapper;
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
import org.jetbrains.annotations.NotNull;

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

  private void executeUpdate(String sqlFile, StatementConfigurer configurer) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      ps.execute();
    } catch (SQLException | FileNotFoundException ex) {}
  }

  public List<LocationEntry> getWarps() {
    return queryList("getWarps.sql", _ -> {
    }, rs -> LocationMapper.mapLocation(rs, serviceContext.getLocationTypeService()));
  }

  public List<GroupEntry> getGroups() {
    return queryList("getGroups.sql", _ -> {
    }, PlayerMapper::mapGroup);
  }

  public void insertGroup(@NotNull GroupEntry ge) {
    executeUpdate("insertGroup.sql", ps -> {
      ps.setInt(1, ge.getId());
      ps.setString(2, ge.getName());
      ps.setString(3, ge.getPrefix());
    });
  }
}