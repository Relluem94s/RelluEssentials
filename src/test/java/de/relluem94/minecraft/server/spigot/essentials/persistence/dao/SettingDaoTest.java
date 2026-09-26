package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private PreparedStatement preparedStatement;

  private SettingDao settingDao;

  @BeforeEach
  void setUp() {
    settingDao = new SettingDao(queryExecutor);
  }

  @Test
  void findAllReturnsListFromQueryExecutor() {
    List<SettingEntry> expectedList = List.of(new SettingEntry());
    when(queryExecutor.queryList(eq("getAllSettings.sql"), any(StatementConfigurer.class),
        any())).thenReturn(Collections.singletonList(expectedList.getFirst()));

    List<SettingEntry> result = settingDao.findAll();

    assertEquals(expectedList, result);
  }

  @Test
  void findAllReturnsEmptyListWhenQueryExecutorReturnsEmpty() {
    when(queryExecutor.queryList(eq("getAllSettings.sql"), any(StatementConfigurer.class),
        any())).thenReturn(Collections.emptyList());

    List<SettingEntry> result = settingDao.findAll();

    assertEquals(Collections.emptyList(), result);
  }

  @Test
  void findAllPropagatesExceptionFromQueryExecutor() {
    when(queryExecutor.queryList(eq("getAllSettings.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> settingDao.findAll());
  }

  @Test
  void findAllDoesNotConfigurePreparedStatement() throws Exception {
    ArgumentCaptor<StatementConfigurer> configurerCaptor =
        ArgumentCaptor.forClass(StatementConfigurer.class);

    when(queryExecutor.queryList(eq("getAllSettings.sql"), configurerCaptor.capture(),
        any())).thenReturn(Collections.emptyList());

    settingDao.findAll();

    configurerCaptor.getValue().configure(preparedStatement);
    verify(preparedStatement, org.mockito.Mockito.never())
        .setObject(any(Integer.class), any());
  }

  @Test
  void findAllInvokesQueryExecutorWithCorrectSqlFile() {
    when(queryExecutor.queryList(eq("getAllSettings.sql"), any(StatementConfigurer.class),
        any())).thenReturn(Collections.emptyList());

    settingDao.findAll();

    verify(queryExecutor).queryList(eq("getAllSettings.sql"), any(StatementConfigurer.class), any());
  }
}