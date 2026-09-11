package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.TraderNpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.TraderNpcMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.loader.SqlResourceLoader;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

/**
 * Data Access Object for {@link TraderNpcEntry} persistence operations.
 *
 * <p>Handles all database interactions related to trader NPCs,
 * including loading NPC configurations from the underlying data store.</p>
 */
public class TraderNpcDao {

  private final DataSource dataSource;
  private final SqlResourceLoader sqlResourceLoader;

  public TraderNpcDao(DataSource dataSource, SqlResourceLoader sqlResourceLoader) {
    this.dataSource = dataSource;
    this.sqlResourceLoader = sqlResourceLoader;
  }

  /**
   * Retrieves all {@link TraderNpcEntry} records from the database.
   *
   * @return a list of all trader NPC entries; never {@code null}, may be empty
   */
  public List<TraderNpcEntry> findAll() {
    List<TraderNpcEntry> results = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/getNPCs.sql"))) {
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          results.add(TraderNpcMapper.mapNPC(rs,
              key -> Registry.VILLAGER_PROFESSION.get(NamespacedKey.minecraft(key))));
        }
      }
    } catch (SQLException | FileNotFoundException ex) {}
    return results;
  }
}