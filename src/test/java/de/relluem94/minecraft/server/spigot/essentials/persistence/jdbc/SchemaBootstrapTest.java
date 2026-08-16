package de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SchemaBootstrapTest {

  private static final String JDBC_BASE_URL = "jdbc:mysql://localhost:3306";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "password";
  private static final String SCHEMA_NAME = "test_schema";
  private static final String CONNECTION_PARAMS = "?useSSL=false&allowPublicKeyRetrieval=true";

  private SchemaBootstrap schemaBootstrap;

  @BeforeEach
  void setUp() {
    schemaBootstrap = new SchemaBootstrap(JDBC_BASE_URL, USERNAME, PASSWORD, SCHEMA_NAME);
  }

  @Test
  void schemaExists_returnsTrue_whenSchemaIsFound() throws Exception {
    try (MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class)) {
      Connection connection = mock(Connection.class);
      Statement statement = mock(Statement.class);
      ResultSet resultSet = mock(ResultSet.class);

      driverManager.when(() -> DriverManager.getConnection(JDBC_BASE_URL + CONNECTION_PARAMS, USERNAME, PASSWORD))
          .thenReturn(connection);
      when(connection.createStatement()).thenReturn(statement);
      when(statement.executeQuery(anyString())).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(true);

      assertTrue(schemaBootstrap.schemaExists());
    }
  }

  @Test
  void schemaExists_returnsFalse_whenSchemaIsNotFound() throws Exception {
    try (MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class)) {
      Connection connection = mock(Connection.class);
      Statement statement = mock(Statement.class);
      ResultSet resultSet = mock(ResultSet.class);

      driverManager.when(() -> DriverManager.getConnection(JDBC_BASE_URL + CONNECTION_PARAMS, USERNAME, PASSWORD))
          .thenReturn(connection);
      when(connection.createStatement()).thenReturn(statement);
      when(statement.executeQuery(anyString())).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(false);

      assertFalse(schemaBootstrap.schemaExists());
    }
  }

  @Test
  void schemaExists_returnsFalse_whenConnectionFails() {
    try (MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class)) {
      driverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
          .thenThrow(new RuntimeException("Connection refused"));

      assertFalse(schemaBootstrap.schemaExists());
    }
  }

  @Test
  void createSchema_executesCreateDatabaseStatement() throws Exception {
    try (MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class)) {
      Connection connection = mock(Connection.class);
      Statement statement = mock(Statement.class);

      driverManager.when(() -> DriverManager.getConnection(JDBC_BASE_URL, USERNAME, PASSWORD))
          .thenReturn(connection);
      when(connection.createStatement()).thenReturn(statement);

      schemaBootstrap.createSchema();

      verify(statement).execute("CREATE DATABASE IF NOT EXISTS `" + SCHEMA_NAME + "`");
    }
  }

  @Test
  void createSchema_throwsRuntimeException_whenExecuteFails() throws Exception {
    try (MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class)) {
      Connection connection = mock(Connection.class);
      Statement statement = mock(Statement.class);

      driverManager.when(() -> DriverManager.getConnection(JDBC_BASE_URL, USERNAME, PASSWORD))
          .thenReturn(connection);
      when(connection.createStatement()).thenReturn(statement);
      when(statement.execute(anyString())).thenThrow(new RuntimeException("Execute failed"));

      RuntimeException thrown = assertThrows(RuntimeException.class, () -> schemaBootstrap.createSchema());

      assertEquals("Failed to create schema: " + SCHEMA_NAME, thrown.getMessage());
    }
  }

  @Test
  void ensureSchemaExists_createsSchema_whenSchemaDoesNotExist() throws Exception {
    try (MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class)) {
      Connection checkConnection = mock(Connection.class);
      Statement checkStatement = mock(Statement.class);
      ResultSet resultSet = mock(ResultSet.class);

      Connection createConnection = mock(Connection.class);
      Statement createStatement = mock(Statement.class);

      driverManager.when(() -> DriverManager.getConnection(JDBC_BASE_URL + CONNECTION_PARAMS, USERNAME, PASSWORD))
          .thenReturn(checkConnection);
      when(checkConnection.createStatement()).thenReturn(checkStatement);
      when(checkStatement.executeQuery(anyString())).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(false);

      driverManager.when(() -> DriverManager.getConnection(JDBC_BASE_URL, USERNAME, PASSWORD))
          .thenReturn(createConnection);
      when(createConnection.createStatement()).thenReturn(createStatement);

      schemaBootstrap.ensureSchemaExists();

      verify(createStatement).execute("CREATE DATABASE IF NOT EXISTS `" + SCHEMA_NAME + "`");
    }
  }

  @Test
  void ensureSchemaExists_skipsCreation_whenSchemaAlreadyExists() throws Exception {
    try (MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class)) {
      Connection connection = mock(Connection.class);
      Statement statement = mock(Statement.class);
      ResultSet resultSet = mock(ResultSet.class);

      driverManager.when(() -> DriverManager.getConnection(JDBC_BASE_URL + CONNECTION_PARAMS, USERNAME, PASSWORD))
          .thenReturn(connection);
      when(connection.createStatement()).thenReturn(statement);
      when(statement.executeQuery(anyString())).thenReturn(resultSet);
      when(resultSet.next()).thenReturn(true);

      schemaBootstrap.ensureSchemaExists();

      driverManager.verify(() -> DriverManager.getConnection(eq(JDBC_BASE_URL), anyString(), anyString()), never());
    }
  }
}