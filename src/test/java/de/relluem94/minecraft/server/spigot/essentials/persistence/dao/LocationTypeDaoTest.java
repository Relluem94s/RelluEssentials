package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.RowMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationTypeDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  private LocationTypeDao locationTypeDao;

  @BeforeEach
  void setUp() {
    locationTypeDao = new LocationTypeDao(queryExecutor);
  }

  @Test
  void findAllReturnsListFromQueryExecutor() {
    List<LocationTypeEntry> expectedEntries = List.of(new LocationTypeEntry(), new LocationTypeEntry());
    doReturn(expectedEntries).when(queryExecutor).queryList(
        eq("getLocationTypes.sql"),
        ArgumentMatchers.any(),
        ArgumentMatchers.<RowMapper<LocationTypeEntry>>any()
    );

    List<LocationTypeEntry> result = locationTypeDao.findAll();

    assertEquals(expectedEntries, result);
  }

  @Test
  void findAllReturnsEmptyListWhenQueryExecutorReturnsEmpty() {
    doReturn(List.of()).when(queryExecutor).queryList(
        eq("getLocationTypes.sql"),
        ArgumentMatchers.any(),
        ArgumentMatchers.<RowMapper<LocationTypeEntry>>any()
    );

    List<LocationTypeEntry> result = locationTypeDao.findAll();

    assertTrue(result.isEmpty());
  }

  @Test
  void findAllPropagatesRuntimeExceptionFromQueryExecutor() {
    when(queryExecutor.queryList(
        eq("getLocationTypes.sql"),
        ArgumentMatchers.any(),
        ArgumentMatchers.<RowMapper<LocationTypeEntry>>any()
    )).thenThrow(new RuntimeException("database error"));

    assertThrows(RuntimeException.class, () -> locationTypeDao.findAll());
  }

  @Test
  void findAllPassesNoOpStatementConfigurerThatDoesNothing() throws SQLException {
    ArgumentCaptor<StatementConfigurer> configurerCaptor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doReturn(List.of()).when(queryExecutor).queryList(
        eq("getLocationTypes.sql"),
        configurerCaptor.capture(),
        ArgumentMatchers.<RowMapper<LocationTypeEntry>>any()
    );

    locationTypeDao.findAll();

    PreparedStatement mockStatement = mock(PreparedStatement.class);
    configurerCaptor.getValue().configure(mockStatement);
    verifyNoInteractions(mockStatement);
  }
}