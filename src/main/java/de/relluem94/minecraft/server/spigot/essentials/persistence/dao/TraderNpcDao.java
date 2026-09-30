package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.TraderNpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.bukkit.BukkitRegistryAdapter;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.TraderNpcMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;

/**
 * Data Access Object for {@link TraderNpcEntry} persistence operations.
 *
 * <p>Handles all database interactions related to trader NPCs,
 * including loading NPC configurations from the underlying data store.</p>
 *
 * @author rellu
 */
public class TraderNpcDao {

  private final QueryExecutor queryExecutor;
  private final BukkitRegistryAdapter registryAdapter;

  /**
   * Creates a new {@link TraderNpcDao} with the given {@link QueryExecutor}
   * and {@link BukkitRegistryAdapter}.
   *
   * @param queryExecutor  the executor used to run SQL queries against the database
   * @param registryAdapter the adapter used to resolve Bukkit registry entries
   */
  public TraderNpcDao(QueryExecutor queryExecutor, BukkitRegistryAdapter registryAdapter) {
    this.queryExecutor = queryExecutor;
    this.registryAdapter = registryAdapter;
  }

  /**
   * Retrieves all {@link TraderNpcEntry} records from the database.
   *
   * @return a list of all trader NPC entries; never {@code null}, may be empty
   */
  public List<TraderNpcEntry> findAll() {
    return queryExecutor.queryList("getNPCs.sql",
        _ -> {},
        rs -> TraderNpcMapper.mapNpc(rs, registryAdapter::resolveProfession));
  }
}