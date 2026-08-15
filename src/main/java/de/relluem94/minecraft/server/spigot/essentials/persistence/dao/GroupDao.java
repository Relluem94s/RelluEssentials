package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.PlayerMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;

/**
 * Data Access Object for managing {@link GroupEntry} persistence operations.
 *
 * @author rellu
 */
public class GroupDao {

  private final QueryExecutor queryExecutor;

  /**
   * Creates a new {@code GroupDao} with the given {@link QueryExecutor}.
   *
   * @param queryExecutor the executor used to run SQL queries
   */
  public GroupDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  /**
   * Retrieves all {@link GroupEntry} records from the database.
   *
   * @return a list of all group entries
   */
  public List<GroupEntry> findAll() {
    return queryExecutor.queryList("getGroups.sql", _ -> {
    }, PlayerMapper::mapGroup);
  }

  /**
   * Persists a new {@link GroupEntry} to the database.
   *
   * @param groupEntry the group entry to insert
   */
  public void insert(GroupEntry groupEntry) {
    queryExecutor.executeUpdate("insertGroup.sql", ps -> {
      ps.setInt(1, groupEntry.getId());
      ps.setString(2, groupEntry.getName());
      ps.setString(3, groupEntry.getPrefix());
    });
  }
}