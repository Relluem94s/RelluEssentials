package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
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
class GroupDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private PreparedStatement preparedStatement;

  private GroupDao groupDao;

  @BeforeEach
  void setUp() {
    groupDao = new GroupDao(queryExecutor);
  }

  @Test
  void findAllReturnsListFromQueryExecutor() {
    List<GroupEntry> expectedList = List.of(new GroupEntry());
    when(queryExecutor.queryList(eq("getGroups.sql"), any(StatementConfigurer.class),
        any())).thenReturn(Collections.singletonList(expectedList.getFirst()));

    List<GroupEntry> result = groupDao.findAll();

    assertEquals(expectedList, result);
  }

  @Test
  void findAllPropagatesExceptionFromQueryExecutor() {
    when(queryExecutor.queryList(eq("getGroups.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> groupDao.findAll());
  }

  @Test
  void insertSetsAllFieldsOnPreparedStatement() throws Exception {
    GroupEntry groupEntry = buildFullGroupEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("insertGroup.sql"), captor.capture());

    groupDao.insert(groupEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, groupEntry.getId());
    verify(preparedStatement).setString(2, groupEntry.getName());
    verify(preparedStatement).setString(3, groupEntry.getPrefix());
  }

  @Test
  void insertPropagatesExceptionFromQueryExecutor() {
    GroupEntry groupEntry = buildFullGroupEntry();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("insertGroup.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> groupDao.insert(groupEntry));
  }

  @Test
  void findAllDoesNotConfigurePreparedStatement() throws Exception {
    ArgumentCaptor<StatementConfigurer> configurerCaptor =
        ArgumentCaptor.forClass(StatementConfigurer.class);

    when(queryExecutor.queryList(eq("getGroups.sql"), configurerCaptor.capture(),
        any())).thenReturn(Collections.emptyList());

    groupDao.findAll();

    configurerCaptor.getValue().configure(preparedStatement);
    verify(preparedStatement, org.mockito.Mockito.never())
        .setObject(any(Integer.class), any());
  }

  private GroupEntry buildFullGroupEntry() {
    GroupEntry entry = new GroupEntry();
    entry.setId(1);
    entry.setName("Admin");
    entry.setPrefix("[A]");
    return entry;
  }
}