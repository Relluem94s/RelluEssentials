package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import java.sql.PreparedStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PluginInformationDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private PreparedStatement preparedStatement;

  private PluginInformationDao pluginInformationDao;

  @BeforeEach
  void setUp() {
    pluginInformationDao = new PluginInformationDao(queryExecutor);
  }

  @Test
  void findReturnsPluginInformationEntry() {
    PluginInformationEntry expected = new PluginInformationEntry();
    when(queryExecutor.querySingle(eq("getPluginInformation.sql"), any(StatementConfigurer.class),
        any())).thenReturn(expected);

    PluginInformationEntry result = pluginInformationDao.find();

    assertEquals(expected, result);
  }

  @Test
  void findReturnsNullWhenQueryExecutorReturnsNull() {
    when(queryExecutor.querySingle(eq("getPluginInformation.sql"), any(StatementConfigurer.class),
        any())).thenReturn(null);

    PluginInformationEntry result = pluginInformationDao.find();

    assertNull(result);
  }

  @Test
  void findPropagatesExceptionFromQueryExecutor() {
    when(queryExecutor.querySingle(eq("getPluginInformation.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> pluginInformationDao.find());
  }

  @Test
  void findDoesNotConfigurePreparedStatement() throws Exception {
    ArgumentCaptor<StatementConfigurer> configurerCaptor =
        ArgumentCaptor.forClass(StatementConfigurer.class);

    when(queryExecutor.querySingle(eq("getPluginInformation.sql"), configurerCaptor.capture(),
        any())).thenReturn(null);

    pluginInformationDao.find();

    configurerCaptor.getValue().configure(preparedStatement);
    verify(preparedStatement, org.mockito.Mockito.never())
        .setObject(any(Integer.class), any());
  }
}