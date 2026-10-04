package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.CropEntry;
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
class CropDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  private CropDao cropDao;

  @Mock
  private PreparedStatement preparedStatement;

  @BeforeEach
  void setUp() {
    cropDao = new CropDao(queryExecutor);
  }

  @Test
  void findAllReturnsListFromQueryExecutor() {
    List<CropEntry> expectedList = List.of(new CropEntry());
    when(queryExecutor.queryList(eq("getCrops.sql"), any(StatementConfigurer.class),
        any())).thenReturn(Collections.singletonList(expectedList.getFirst()));

    List<CropEntry> result = cropDao.findAll();

    assertEquals(expectedList, result);
  }

  @Test
  void findAllReturnsEmptyListWhenQueryExecutorReturnsEmpty() {
    when(queryExecutor.queryList(eq("getCrops.sql"), any(StatementConfigurer.class),
        any())).thenReturn(Collections.emptyList());

    List<CropEntry> result = cropDao.findAll();

    assertEquals(Collections.emptyList(), result);
  }

  @Test
  void findAllPropagatesExceptionFromQueryExecutor() {
    when(queryExecutor.queryList(eq("getCrops.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> cropDao.findAll());
  }

  @Test
  void findAllDoesNotConfigurePreparedStatement() throws Exception {
    ArgumentCaptor<StatementConfigurer> configurerCaptor =
        ArgumentCaptor.forClass(StatementConfigurer.class);

    when(queryExecutor.queryList(eq("getCrops.sql"), configurerCaptor.capture(),
        any())).thenReturn(Collections.emptyList());

    cropDao.findAll();

    configurerCaptor.getValue().configure(preparedStatement);
    verify(preparedStatement, org.mockito.Mockito.never())
        .setObject(any(Integer.class), any());
  }

}