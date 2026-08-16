package de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.loader.SqlResourceLoader;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QueryExecutorTest {

  @Mock
  private DataSource dataSource;

  @Mock
  private SqlResourceLoader sqlResourceLoader;

  @Mock
  private Connection connection;

  @Mock
  private PreparedStatement preparedStatement;

  @Mock
  private ResultSet resultSet;

  @Mock
  private StatementConfigurer statementConfigurer;

  private QueryExecutor queryExecutor;

  @BeforeEach
  void setUp() {
    Logger silentLogger = Logger.getLogger(QueryExecutor.class.getName());
    silentLogger.setLevel(Level.OFF);
    queryExecutor = new QueryExecutor(dataSource, sqlResourceLoader, silentLogger);
  }
  @Test
  void queryForEachConsumesAllRows() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenReturn("SELECT 1");
    when(connection.prepareStatement("SELECT 1")).thenReturn(preparedStatement);
    when(preparedStatement.getResultSet()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true, true, false);

    RowConsumer consumer = mock(RowConsumer.class);
    queryExecutor.queryForEach("test.sql", statementConfigurer, consumer);

    verify(statementConfigurer).configure(preparedStatement);
    verify(preparedStatement).execute();
    verify(consumer, times(2)).consume(resultSet);
  }

  @Test
  void queryForEachHandlesSqlException() throws Exception {
    when(dataSource.getConnection()).thenThrow(new SQLException("connection failed"));

    RowConsumer consumer = mock(RowConsumer.class);
    assertDoesNotThrow(() -> queryExecutor.queryForEach("test.sql", statementConfigurer, consumer));
    verifyNoInteractions(consumer);
  }

  @Test
  void queryForEachHandlesFileNotFoundException() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenThrow(new FileNotFoundException("not found"));

    RowConsumer consumer = mock(RowConsumer.class);
    assertDoesNotThrow(() -> queryExecutor.queryForEach("test.sql", statementConfigurer, consumer));
    verifyNoInteractions(consumer);
  }

  @Test
  void queryListReturnsMappedResults() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenReturn("SELECT 1");
    when(connection.prepareStatement("SELECT 1")).thenReturn(preparedStatement);
    when(preparedStatement.getResultSet()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true, true, false);

    RowMapper<String> mapper = mock(RowMapper.class);
    when(mapper.map(resultSet)).thenReturn("row1", "row2");

    List<String> results = queryExecutor.queryList("test.sql", statementConfigurer, mapper);

    assertAll(
        () -> assertEquals(2, results.size()),
        () -> assertEquals("row1", results.get(0)),
        () -> assertEquals("row2", results.get(1))
    );
  }

  @Test
  void queryListReturnsEmptyListOnSqlException() throws Exception {
    when(dataSource.getConnection()).thenThrow(new SQLException("connection failed"));

    RowMapper<String> mapper = mock(RowMapper.class);
    List<String> results = queryExecutor.queryList("test.sql", statementConfigurer, mapper);

    assertAll(
        () -> assertNotNull(results),
        () -> assertTrue(results.isEmpty())
    );
  }

  @Test
  void queryListReturnsEmptyListOnFileNotFoundException() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenThrow(new FileNotFoundException("not found"));

    RowMapper<String> mapper = mock(RowMapper.class);
    List<String> results = queryExecutor.queryList("test.sql", statementConfigurer, mapper);

    assertAll(
        () -> assertNotNull(results),
        () -> assertTrue(results.isEmpty())
    );
  }

  @Test
  void querySingleReturnsMappedResult() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenReturn("SELECT 1");
    when(connection.prepareStatement("SELECT 1")).thenReturn(preparedStatement);
    when(preparedStatement.getResultSet()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);

    RowMapper<String> mapper = mock(RowMapper.class);
    when(mapper.map(resultSet)).thenReturn("singleRow");

    String result = queryExecutor.querySingle("test.sql", statementConfigurer, mapper);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals("singleRow", result)
    );
  }

  @Test
  void querySingleReturnsNullWhenNoRows() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenReturn("SELECT 1");
    when(connection.prepareStatement("SELECT 1")).thenReturn(preparedStatement);
    when(preparedStatement.getResultSet()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    RowMapper<String> mapper = mock(RowMapper.class);
    String result = queryExecutor.querySingle("test.sql", statementConfigurer, mapper);

    assertNull(result);
  }

  @Test
  void querySingleReturnsNullOnSqlException() throws Exception {
    when(dataSource.getConnection()).thenThrow(new SQLException("connection failed"));

    RowMapper<String> mapper = mock(RowMapper.class);
    String result = queryExecutor.querySingle("test.sql", statementConfigurer, mapper);

    assertNull(result);
  }

  @Test
  void querySingleReturnsNullOnFileNotFoundException() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenThrow(new FileNotFoundException("not found"));

    RowMapper<String> mapper = mock(RowMapper.class);
    String result = queryExecutor.querySingle("test.sql", statementConfigurer, mapper);

    assertNull(result);
  }

  @Test
  void executeUpdateExecutesPreparedStatement() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenReturn("UPDATE t SET x=1");
    when(connection.prepareStatement("UPDATE t SET x=1")).thenReturn(preparedStatement);

    queryExecutor.executeUpdate("test.sql", statementConfigurer);

    verify(statementConfigurer).configure(preparedStatement);
    verify(preparedStatement).execute();
  }

  @Test
  void executeUpdateHandlesSqlException() throws Exception {
    when(dataSource.getConnection()).thenThrow(new SQLException("connection failed"));

    assertDoesNotThrow(() -> queryExecutor.executeUpdate("test.sql", statementConfigurer));
  }

  @Test
  void executeUpdateHandlesFileNotFoundException() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenThrow(new FileNotFoundException("not found"));

    assertDoesNotThrow(() -> queryExecutor.executeUpdate("test.sql", statementConfigurer));
  }

  @Test
  void executeUpdateWithCountReturnsAffectedRowCount() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenReturn("UPDATE t SET x=1");
    when(connection.prepareStatement("UPDATE t SET x=1")).thenReturn(preparedStatement);
    when(preparedStatement.executeUpdate()).thenReturn(3);

    int count = queryExecutor.executeUpdateWithCount("test.sql", statementConfigurer);

    assertAll(
        () -> assertEquals(3, count),
        () -> verify(statementConfigurer).configure(preparedStatement),
        () -> verify(preparedStatement).executeUpdate()
    );
  }

  @Test
  void executeUpdateWithCountReturnsZeroOnSqlException() throws Exception {
    when(dataSource.getConnection()).thenThrow(new SQLException("connection failed"));

    int count = queryExecutor.executeUpdateWithCount("test.sql", statementConfigurer);

    assertEquals(0, count);
  }

  @Test
  void executeUpdateWithCountReturnsZeroOnFileNotFoundException() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenThrow(new FileNotFoundException("not found"));

    int count = queryExecutor.executeUpdateWithCount("test.sql", statementConfigurer);

    assertEquals(0, count);
  }

  @Test
  void executeInsertWithGeneratedKeyReturnsGeneratedKey() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenReturn("INSERT INTO t VALUES (?)");
    when(connection.prepareStatement("INSERT INTO t VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS))
        .thenReturn(preparedStatement);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getInt(1)).thenReturn(42);

    int key = queryExecutor.executeInsertWithGeneratedKey("test.sql", statementConfigurer);

    assertAll(
        () -> assertEquals(42, key),
        () -> verify(statementConfigurer).configure(preparedStatement),
        () -> verify(preparedStatement).executeUpdate()
    );
  }

  @Test
  void executeInsertWithGeneratedKeyReturnsNegativeOneWhenNoKeyGenerated() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenReturn("INSERT INTO t VALUES (?)");
    when(connection.prepareStatement("INSERT INTO t VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS))
        .thenReturn(preparedStatement);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    int key = queryExecutor.executeInsertWithGeneratedKey("test.sql", statementConfigurer);

    assertEquals(-1, key);
  }

  @Test
  void executeInsertWithGeneratedKeyReturnsNegativeOneOnSqlException() throws Exception {
    when(dataSource.getConnection()).thenThrow(new SQLException("connection failed"));

    int key = queryExecutor.executeInsertWithGeneratedKey("test.sql", statementConfigurer);

    assertEquals(-1, key);
  }

  @Test
  void executeInsertWithGeneratedKeyReturnsNegativeOneOnFileNotFoundException() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenThrow(new FileNotFoundException("not found"));

    int key = queryExecutor.executeInsertWithGeneratedKey("test.sql", statementConfigurer);

    assertEquals(-1, key);
  }

  @Test
  void executeScriptDelegatesToExecuteUpdate() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/script.sql")).thenReturn("CREATE TABLE t (id INT)");
    when(connection.prepareStatement("CREATE TABLE t (id INT)")).thenReturn(preparedStatement);

    queryExecutor.executeScript("script.sql");

    verify(preparedStatement).execute();
  }

  @Test
  void executeScriptHandlesSqlException() throws Exception {
    when(dataSource.getConnection()).thenThrow(new SQLException("connection failed"));

    assertDoesNotThrow(() -> queryExecutor.executeScript("script.sql"));
  }

  @Test
  void querySingleHandlesExceptionOnResultSetClose() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenReturn("SELECT 1");
    when(connection.prepareStatement("SELECT 1")).thenReturn(preparedStatement);
    when(preparedStatement.getResultSet()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);

    RowMapper<String> mapper = mock(RowMapper.class);
    when(mapper.map(resultSet)).thenReturn("singleRow");

    assertDoesNotThrow(() -> queryExecutor.querySingle("test.sql", statementConfigurer, mapper));
  }

  @Test
  void executeInsertWithGeneratedKeyHandlesExceptionOnResultSetClose() throws Exception {
    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenReturn("INSERT INTO t VALUES (?)");
    when(connection.prepareStatement("INSERT INTO t VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS))
        .thenReturn(preparedStatement);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getInt(1)).thenReturn(42);

    assertDoesNotThrow(() -> queryExecutor.executeInsertWithGeneratedKey("test.sql", statementConfigurer));
  }

  @Test
  void defaultConstructorCreatesUsableInstance() throws Exception {
    QueryExecutor defaultQueryExecutor = new QueryExecutor(dataSource, sqlResourceLoader);

    when(dataSource.getConnection()).thenReturn(connection);
    when(sqlResourceLoader.load("sqls/test.sql")).thenReturn("SELECT 1");
    when(connection.prepareStatement("SELECT 1")).thenReturn(preparedStatement);
    when(preparedStatement.getResultSet()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    RowMapper<String> mapper = mock(RowMapper.class);
    String result = defaultQueryExecutor.querySingle("test.sql", statementConfigurer, mapper);

    assertNull(result);
  }
}