package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.BagMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import java.util.Optional;

public class BagDao {

  private final QueryExecutor queryExecutor;

  public BagDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  public List<BagTypeEntry> findAllBagTypes() {
    return queryExecutor.queryList("getBagTypes.sql", _ -> {
    }, BagMapper::mapBagType);
  }

  public Optional<BagTypeEntry> findBagTypeById(int bagTypeId) {
    return Optional.ofNullable(
        queryExecutor.querySingle("getBagTypeById.sql",
            ps -> ps.setInt(1, bagTypeId),
            BagMapper::mapBagType
        )
    );
  }

  public List<BagEntry> findAllBags() {
    return queryExecutor.queryList("getBags.sql", _ -> {
    }, rs -> {
      BagEntry bagEntry = BagMapper.mapBag(rs);
      bagEntry.setBagType(findBagTypeById(rs.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK))
          .orElseThrow());
      return bagEntry;
    });
  }

  public Optional<BagEntry> findBagByPlayerIdAndBagTypeId(int playerId, int bagTypeId) {
    return Optional.ofNullable(
        queryExecutor.querySingle("getBagByPlayerAndType.sql", ps -> {
          ps.setInt(1, bagTypeId);
          ps.setInt(2, playerId);
        }, rs -> {
          BagEntry bagEntry = BagMapper.mapBag(rs);
          bagEntry.setBagType(findBagTypeById(rs.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK))
              .orElseThrow());
          return bagEntry;
        })
    );
  }

  public void insertBag(int playerId, int bagTypeId) {
    queryExecutor.executeUpdate("insertBag.sql", ps -> {
      ps.setInt(1, playerId);
      ps.setInt(2, playerId);
      ps.setInt(3, bagTypeId);
    });
  }

  public void updateBag(BagEntry bagEntry) {
    queryExecutor.executeUpdate("updateBag.sql", ps -> {
      ps.setInt(1, bagEntry.getPlayerId());
      for (int slotIndex = 0; slotIndex < BagHelper.BAG_SIZE; slotIndex++) {
        ps.setInt(slotIndex + 2, bagEntry.getSlotValue(slotIndex));
      }
      ps.setInt(BagHelper.BAG_SIZE + 2, bagEntry.getId());
    });
  }
}