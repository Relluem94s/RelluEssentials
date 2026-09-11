package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static de.relluem94.minecraft.server.spigot.essentials.constants.InventoryConstants.BAG_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.RowMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BagDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private PreparedStatement preparedStatement;

  @Mock
  private ResultSet resultSet;

  private BagDao bagDao;

  @BeforeEach
  void setUp() {
    bagDao = new BagDao(queryExecutor);
  }

  @Test
  void findAllBagTypesReturnsMappedList() {
    BagTypeEntry bagTypeEntry = new BagTypeEntry();
    bagTypeEntry.setId(1);
    bagTypeEntry.setName("Small");

    when(queryExecutor.queryList(eq("getBagTypes.sql"), any(), any()))
        .thenReturn(List.of(bagTypeEntry));

    List<BagTypeEntry> result = bagDao.findAllBagTypes();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(1, result.getFirst().getId());
    assertEquals("Small", result.getFirst().getName());
  }

  @Test
  void findAllBagTypesReturnsEmptyListWhenNoneExist() {
    when(queryExecutor.queryList(eq("getBagTypes.sql"), any(), any()))
        .thenReturn(List.of());

    List<BagTypeEntry> result = bagDao.findAllBagTypes();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void findBagTypeByIdReturnsPresentOptionalWhenFound() {
    BagTypeEntry bagTypeEntry = new BagTypeEntry();
    bagTypeEntry.setId(5);
    bagTypeEntry.setName("Large");

    when(queryExecutor.querySingle(eq("getBagTypeById.sql"), any(), any()))
        .thenReturn(bagTypeEntry);

    Optional<BagTypeEntry> result = bagDao.findBagTypeById(5);

    assertTrue(result.isPresent());
    assertEquals(5, result.get().getId());
    assertEquals("Large", result.get().getName());
  }

  @Test
  void findBagTypeByIdSetsIntParameterCorrectly() throws SQLException {
    BagTypeEntry bagTypeEntry = new BagTypeEntry();

    doAnswer(invocation -> {
      StatementConfigurer configurer = invocation.getArgument(1);
      configurer.configure(preparedStatement);
      return bagTypeEntry;
    }).when(queryExecutor).querySingle(eq("getBagTypeById.sql"), any(), any());

    bagDao.findBagTypeById(7);

    verify(preparedStatement).setInt(1, 7);
  }

  @Test
  void findBagTypeByIdWrapsNullInOptional() {
    when(queryExecutor.querySingle(eq("getBagTypeById.sql"), any(), any()))
        .thenReturn(null);

    assertThrows(NullPointerException.class, () -> bagDao.findBagTypeById(99));
  }

  @Test
  void findAllBagsReturnsMappedListWithBagTypes() {
    BagTypeEntry bagTypeEntry = new BagTypeEntry();
    bagTypeEntry.setId(2);

    BagEntry bagEntry = new BagEntry();
    bagEntry.setId(10);
    bagEntry.setPlayerId(1);
    bagEntry.setBagType(bagTypeEntry);

    when(queryExecutor.queryList(eq("getBags.sql"), any(), any()))
        .thenReturn(List.of(bagEntry));

    List<BagEntry> result = bagDao.findAllBags();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(10, result.getFirst().getId());
    assertEquals(1, result.getFirst().getPlayerId());
    assertNotNull(result.getFirst().getBagType());
    assertEquals(2, result.getFirst().getBagType().getId());
  }

  @Test
  void findAllBagsReturnsEmptyListWhenNoneExist() {
    when(queryExecutor.queryList(eq("getBags.sql"), any(), any()))
        .thenReturn(List.of());

    List<BagEntry> result = bagDao.findAllBags();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void findBagByPlayerIdAndBagTypeIdReturnsPresentOptionalWhenFound() {
    BagTypeEntry bagTypeEntry = new BagTypeEntry();
    bagTypeEntry.setId(3);

    BagEntry bagEntry = new BagEntry();
    bagEntry.setId(20);
    bagEntry.setPlayerId(4);
    bagEntry.setBagType(bagTypeEntry);

    when(queryExecutor.querySingle(eq("getBagByPlayerAndType.sql"), any(), any()))
        .thenReturn(bagEntry);

    Optional<BagEntry> result = bagDao.findBagByPlayerIdAndBagTypeId(4, 3);

    assertTrue(result.isPresent());
    assertEquals(20, result.get().getId());
    assertEquals(4, result.get().getPlayerId());
    assertNotNull(result.get().getBagType());
    assertEquals(3, result.get().getBagType().getId());
  }

  @Test
  void findBagByPlayerIdAndBagTypeIdReturnsEmptyOptionalWhenNotFound() {
    when(queryExecutor.querySingle(eq("getBagByPlayerAndType.sql"), any(), any()))
        .thenReturn(null);

    Optional<BagEntry> result = bagDao.findBagByPlayerIdAndBagTypeId(99, 99);

    assertFalse(result.isPresent());
  }

  @Test
  void findBagByPlayerIdAndBagTypeIdSetsParametersCorrectly() throws SQLException {
    BagEntry bagEntry = new BagEntry();

    doAnswer(invocation -> {
      StatementConfigurer configurer = invocation.getArgument(1);
      configurer.configure(preparedStatement);
      return bagEntry;
    }).when(queryExecutor).querySingle(eq("getBagByPlayerAndType.sql"), any(), any());

    bagDao.findBagByPlayerIdAndBagTypeId(6, 3);

    verify(preparedStatement).setInt(1, 3);
    verify(preparedStatement).setInt(2, 6);
  }

  @Test
  void insertBagSetsParametersCorrectly() throws SQLException {
    doAnswer(invocation -> {
      StatementConfigurer configurer = invocation.getArgument(1);
      configurer.configure(preparedStatement);
      return null;
    }).when(queryExecutor).executeUpdate(eq("insertBag.sql"), any());

    bagDao.insertBag(8, 2);

    verify(preparedStatement).setInt(1, 8);
    verify(preparedStatement).setInt(2, 8);
    verify(preparedStatement).setInt(3, 2);
  }

  @Test
  void updateBagSetsAllSlotParametersCorrectly() throws SQLException {
    BagEntry bagEntry = new BagEntry();
    bagEntry.setId(15);
    bagEntry.setPlayerId(3);
    for (int slotIndex = 0; slotIndex < BAG_SIZE; slotIndex++) {
      bagEntry.setSlotValue(slotIndex, slotIndex + 10);
    }

    doAnswer(invocation -> {
      StatementConfigurer configurer = invocation.getArgument(1);
      configurer.configure(preparedStatement);
      return null;
    }).when(queryExecutor).executeUpdate(eq("updateBag.sql"), any());

    bagDao.updateBag(bagEntry);

    verify(preparedStatement).setInt(1, 3);
    for (int slotIndex = 0; slotIndex < BAG_SIZE; slotIndex++) {
      verify(preparedStatement).setInt(slotIndex + 2, slotIndex + 10);
    }
    verify(preparedStatement).setInt(BAG_SIZE + 2, 15);
  }

  @Test
  void updateBagCallsExecuteUpdateWithCorrectSqlFile() {
    BagEntry bagEntry = new BagEntry();
    bagEntry.setId(1);
    bagEntry.setPlayerId(1);

    bagDao.updateBag(bagEntry);

    verify(queryExecutor).executeUpdate(eq("updateBag.sql"), any());
  }

  @Test
  void insertBagCallsExecuteUpdateWithCorrectSqlFile() {
    bagDao.insertBag(1, 1);

    verify(queryExecutor).executeUpdate(eq("insertBag.sql"), any());
  }

  @Test
  void findAllBagTypesCallsQueryListWithCorrectSqlFile() {
    when(queryExecutor.queryList(anyString(), any(), any())).thenReturn(List.of());

    bagDao.findAllBagTypes();

    verify(queryExecutor).queryList(eq("getBagTypes.sql"), any(), any());
  }

  @Test
  void findAllBagsCallsQueryListWithCorrectSqlFile() {
    when(queryExecutor.queryList(anyString(), any(), any())).thenReturn(List.of());

    bagDao.findAllBags();

    verify(queryExecutor).queryList(eq("getBags.sql"), any(), any());
  }

  @Test
  void findBagByPlayerIdAndBagTypeIdCallsQuerySingleWithCorrectSqlFile() {
    when(queryExecutor.querySingle(anyString(), any(), any())).thenReturn(null);

    bagDao.findBagByPlayerIdAndBagTypeId(1, 1);

    verify(queryExecutor).querySingle(eq("getBagByPlayerAndType.sql"), any(), any());
  }

  @Test
  void findBagTypeByIdCallsQuerySingleWithCorrectSqlFile() {
    BagTypeEntry bagTypeEntry = new BagTypeEntry();
    when(queryExecutor.querySingle(anyString(), any(), any())).thenReturn(bagTypeEntry);

    bagDao.findBagTypeById(1);

    verify(queryExecutor).querySingle(eq("getBagTypeById.sql"), any(), any());
  }

  @Test
  void updateBagWithZeroSlotValuesDoesNotThrow() throws SQLException {
    BagEntry bagEntry = new BagEntry();
    bagEntry.setId(1);
    bagEntry.setPlayerId(1);

    doAnswer(invocation -> {
      StatementConfigurer configurer = invocation.getArgument(1);
      configurer.configure(preparedStatement);
      return null;
    }).when(queryExecutor).executeUpdate(eq("updateBag.sql"), any());

    bagDao.updateBag(bagEntry);

    for (int slotIndex = 0; slotIndex < BAG_SIZE; slotIndex++) {
      verify(preparedStatement).setInt(slotIndex + 2, 0);
    }
  }


  @Test
  void findAllBagsMapperPopulatesBagEntryFromResultSet() throws SQLException {
    when(resultSet.getInt(DatabaseMappings.FIELD_ID)).thenReturn(10);
    when(resultSet.getInt(DatabaseMappings.FIELD_PLAYER_FK)).thenReturn(1);
    when(resultSet.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK)).thenReturn(2);
    when(resultSet.getInt(DatabaseMappings.FIELD_CREATEDBY)).thenReturn(0);
    when(resultSet.getInt(DatabaseMappings.FIELD_UPDATEDBY)).thenReturn(0);
    when(resultSet.getInt(DatabaseMappings.FIELD_DELETEDBY)).thenReturn(0);

    BagTypeEntry bagTypeEntry = new BagTypeEntry();
    bagTypeEntry.setId(2);

    when(queryExecutor.querySingle(eq("getBagTypeById.sql"), any(), any()))
        .thenReturn(bagTypeEntry);

    when(queryExecutor.queryList(eq("getBags.sql"), any(), any()))
        .thenAnswer(invocation -> {
          RowMapper<BagEntry> mapper = invocation.getArgument(2);
          return List.of(mapper.map(resultSet));
        });

    List<BagEntry> result = bagDao.findAllBags();

    assertEquals(1, result.size());
    assertEquals(10, result.getFirst().getId());
    assertEquals(1, result.getFirst().getPlayerId());
    assertNotNull(result.getFirst().getBagType());
    assertEquals(2, result.getFirst().getBagType().getId());
  }

  @Test
  void findAllBagsMapperFetchesBagTypeByIdFromResultSet() throws SQLException {
    when(resultSet.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK)).thenReturn(5);
    when(resultSet.getInt(DatabaseMappings.FIELD_ID)).thenReturn(0);
    when(resultSet.getInt(DatabaseMappings.FIELD_PLAYER_FK)).thenReturn(0);
    when(resultSet.getInt(DatabaseMappings.FIELD_CREATEDBY)).thenReturn(0);
    when(resultSet.getInt(DatabaseMappings.FIELD_UPDATEDBY)).thenReturn(0);
    when(resultSet.getInt(DatabaseMappings.FIELD_DELETEDBY)).thenReturn(0);

    BagTypeEntry bagTypeEntry = new BagTypeEntry();
    bagTypeEntry.setId(5);

    when(queryExecutor.querySingle(eq("getBagTypeById.sql"), any(), any()))
        .thenReturn(bagTypeEntry);

    when(queryExecutor.queryList(eq("getBags.sql"), any(), any()))
        .thenAnswer(invocation -> {
          RowMapper<BagEntry> mapper = invocation.getArgument(2);
          return List.of(mapper.map(resultSet));
        });

    bagDao.findAllBags();

    verify(queryExecutor).querySingle(eq("getBagTypeById.sql"), any(), any());
  }

  @Test
  void findBagByPlayerIdAndBagTypeIdMapperPopulatesBagEntryFromResultSet() throws SQLException {
    when(resultSet.getInt(DatabaseMappings.FIELD_ID)).thenReturn(20);
    when(resultSet.getInt(DatabaseMappings.FIELD_PLAYER_FK)).thenReturn(4);
    when(resultSet.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK)).thenReturn(3);
    when(resultSet.getInt(DatabaseMappings.FIELD_CREATEDBY)).thenReturn(0);
    when(resultSet.getInt(DatabaseMappings.FIELD_UPDATEDBY)).thenReturn(0);
    when(resultSet.getInt(DatabaseMappings.FIELD_DELETEDBY)).thenReturn(0);

    BagTypeEntry bagTypeEntry = new BagTypeEntry();
    bagTypeEntry.setId(3);

    when(queryExecutor.querySingle(eq("getBagTypeById.sql"), any(), any()))
        .thenReturn(bagTypeEntry);

    when(queryExecutor.querySingle(eq("getBagByPlayerAndType.sql"), any(), any()))
        .thenAnswer(invocation -> {
          RowMapper<BagEntry> mapper = invocation.getArgument(2);
          return mapper.map(resultSet);
        });

    Optional<BagEntry> result = bagDao.findBagByPlayerIdAndBagTypeId(4, 3);

    assertTrue(result.isPresent());
    assertEquals(20, result.get().getId());
    assertEquals(4, result.get().getPlayerId());
    assertNotNull(result.get().getBagType());
    assertEquals(3, result.get().getBagType().getId());
  }

  @Test
  void findBagByPlayerIdAndBagTypeIdMapperFetchesBagTypeByIdFromResultSet() throws SQLException {
    when(resultSet.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK)).thenReturn(7);
    when(resultSet.getInt(DatabaseMappings.FIELD_ID)).thenReturn(0);
    when(resultSet.getInt(DatabaseMappings.FIELD_PLAYER_FK)).thenReturn(0);
    when(resultSet.getInt(DatabaseMappings.FIELD_CREATEDBY)).thenReturn(0);
    when(resultSet.getInt(DatabaseMappings.FIELD_UPDATEDBY)).thenReturn(0);
    when(resultSet.getInt(DatabaseMappings.FIELD_DELETEDBY)).thenReturn(0);

    BagTypeEntry bagTypeEntry = new BagTypeEntry();
    bagTypeEntry.setId(7);

    when(queryExecutor.querySingle(eq("getBagTypeById.sql"), any(), any()))
        .thenReturn(bagTypeEntry);

    when(queryExecutor.querySingle(eq("getBagByPlayerAndType.sql"), any(), any()))
        .thenAnswer(invocation -> {
          RowMapper<BagEntry> mapper = invocation.getArgument(2);
          return mapper.map(resultSet);
        });

    bagDao.findBagByPlayerIdAndBagTypeId(4, 7);

    verify(queryExecutor).querySingle(eq("getBagTypeById.sql"), any(), any());
  }

  @Test
  void findAllBagTypesStatementConfigurerDoesNothing() throws SQLException {
    when(queryExecutor.queryList(eq("getBagTypes.sql"), any(), any()))
        .thenAnswer(invocation -> {
          StatementConfigurer configurer = invocation.getArgument(1);
          configurer.configure(preparedStatement);
          return List.of();
        });

    bagDao.findAllBagTypes();

    verify(preparedStatement, org.mockito.Mockito.never()).setInt(any(int.class), any(int.class));
  }

  @Test
  void findAllBagTypesMapperPopulatesBagTypeEntryFromResultSet() throws SQLException {
    when(resultSet.getInt(DatabaseMappings.FIELD_ID)).thenReturn(1);
    when(resultSet.getString(DatabaseMappings.FIELD_NAME)).thenReturn("Small");
    when(resultSet.getString(DatabaseMappings.FIELD_DISPLAY_NAME)).thenReturn("Small Bag");
    when(resultSet.getInt(DatabaseMappings.FIELD_COST)).thenReturn(100);

    when(queryExecutor.queryList(eq("getBagTypes.sql"), any(), any()))
        .thenAnswer(invocation -> {
          RowMapper<BagTypeEntry> mapper = invocation.getArgument(2);
          return List.of(mapper.map(resultSet));
        });

    List<BagTypeEntry> result = bagDao.findAllBagTypes();

    assertEquals(1, result.size());
    assertEquals(1, result.getFirst().getId());
    assertEquals("Small", result.getFirst().getName());
    assertEquals("Small Bag", result.getFirst().getDisplayName());
    assertEquals(100, result.getFirst().getCost());
  }

  @Test
  void findAllBagsStatementConfigurerDoesNothing() throws SQLException {
    when(queryExecutor.queryList(eq("getBags.sql"), any(), any()))
        .thenAnswer(invocation -> {
          StatementConfigurer configurer = invocation.getArgument(1);
          configurer.configure(preparedStatement);
          return List.of();
        });

    bagDao.findAllBags();

    verify(preparedStatement, org.mockito.Mockito.never()).setInt(any(int.class), any(int.class));
  }
}