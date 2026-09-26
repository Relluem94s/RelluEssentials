package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.DropEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.RowMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DropDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private PreparedStatement preparedStatement;

  @Mock
  private ResultSet resultSet;

  private DropDao dropDao;

  @BeforeEach
  void setUp() {
    dropDao = new DropDao(queryExecutor);
  }

  @Test
  @SuppressWarnings("unchecked")
  void findAllReturnsListFromQueryExecutor() {
    when(queryExecutor.queryList(eq("getDrops.sql"), any(StatementConfigurer.class),
        any(RowMapper.class)))
        .thenAnswer(invocation -> {
          StatementConfigurer configurer = invocation.getArgument(1);
          configurer.configure(preparedStatement);
          RowMapper<DropEntry> mapper = invocation.getArgument(2);
          return List.of(mapper.map(resultSet));
        });

    List<DropEntry> result = dropDao.findAll();

    assertEquals(1, result.size());
  }
  @Test
  void findAllReturnsEmptyListWhenQueryExecutorReturnsEmpty() {
    when(queryExecutor.queryList(eq("getDrops.sql"), any(StatementConfigurer.class),
        any())).thenReturn(Collections.emptyList());

    List<DropEntry> result = dropDao.findAll();

    assertEquals(Collections.emptyList(), result);
  }

  @Test
  void findAllPropagatesExceptionFromQueryExecutor() {
    when(queryExecutor.queryList(eq("getDrops.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> dropDao.findAll());
  }
}