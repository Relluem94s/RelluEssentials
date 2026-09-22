package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PROFESSION;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_SLOT_VAR_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_TYPE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.TraderNpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc.Type;
import de.relluem94.minecraft.server.spigot.essentials.persistence.bukkit.BukkitRegistryAdapter;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.RowMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraderNpcDaoTest {

  @Mock
  private ResultSet resultSet;

  @Mock
  private PreparedStatement preparedStatement;

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private BukkitRegistryAdapter registryAdapter;

  private TraderNpcDao traderNpcDao;

  @BeforeEach
  void setUp() {
    traderNpcDao = new TraderNpcDao(queryExecutor, registryAdapter);
  }


  @Test
  void findAllReturnsListFromQueryExecutor() {
    TraderNpcEntry entry = new TraderNpcEntry();
    when(queryExecutor.queryList(eq("getNPCs.sql"), any(), any()))
        .thenReturn(List.of(entry));

    List<TraderNpcEntry> result = traderNpcDao.findAll();

    assertEquals(1, result.size());
    assertEquals(entry, result.getFirst());
  }

  @Test
  void findAllReturnsEmptyListWhenNoneFound() {
    when(queryExecutor.queryList(eq("getNPCs.sql"), any(), any()))
        .thenReturn(List.of());

    List<TraderNpcEntry> result = traderNpcDao.findAll();

    assertEquals(0, result.size());
  }

  @Test
  void findAllPropagatesException() {
    when(queryExecutor.queryList(eq("getNPCs.sql"), any(), any()))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> traderNpcDao.findAll());
  }

  @Test
  void findAllInvokesMapperWithResultSet() throws SQLException {
    lenient().when(resultSet.getInt(FIELD_ID)).thenReturn(1);
    lenient().when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
    lenient().when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(1);
    lenient().when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-01-02");
    lenient().when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(2);
    lenient().when(resultSet.getString(FIELD_DELETED)).thenReturn(null);
    lenient().when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(0);
    lenient().when(resultSet.getString(FIELD_NAME)).thenReturn("TestTrader");
    lenient().when(resultSet.getString(FIELD_PROFESSION)).thenReturn("farmer");
    lenient().when(resultSet.getString(FIELD_TYPE)).thenReturn(TraderNpc.Type.TRADER.name());
    for (int i = 0; i <= 27; i++) {
      lenient().when(resultSet.getString(String.format(FIELD_SLOT_VAR_NAME, i + 1)))
          .thenReturn("slot_" + (i + 1));
    }

    when(registryAdapter.resolveProfession("farmer")).thenReturn(null);

    when(queryExecutor.queryList(eq("getNPCs.sql"), any(), any()))
        .thenAnswer(invocation -> {
          StatementConfigurer configurer = invocation.getArgument(1);
          configurer.configure(preparedStatement);

          RowMapper<TraderNpcEntry> mapper = invocation.getArgument(2);
          TraderNpcEntry mapped = mapper.map(resultSet);
          return List.of(mapped);
        });

    List<TraderNpcEntry> result = traderNpcDao.findAll();

    assertEquals(1, result.size());
    TraderNpcEntry mapped = result.getFirst();
    assertEquals(1, mapped.getId());
    assertEquals("2024-01-01", mapped.getCreated());
    assertEquals(1, mapped.getCreatedBy());
    assertEquals("2024-01-02", mapped.getUpdated());
    assertEquals(2, mapped.getUpdatedBy());
    assertNull(mapped.getDeleted());
    assertEquals(0, mapped.getDeletedBy());
    assertEquals("TestTrader", mapped.getName());
    assertNull(mapped.getProfession());
    assertEquals(Type.TRADER, mapped.getType());
    for (int i = 0; i <= 27; i++) {
      assertEquals("slot_" + (i + 1), mapped.getSlotName(i));
    }
  }
}