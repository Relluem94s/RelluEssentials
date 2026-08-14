package de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SchemaBootstrap {

  private final String jdbcBaseUrl;
  private final String username;
  private final String password;
  private final String schemaName;

  public SchemaBootstrap(String jdbcBaseUrl, String username, String password, String schemaName) {
    this.jdbcBaseUrl = jdbcBaseUrl;
    this.username = username;
    this.password = password;
    this.schemaName = schemaName;
  }

  public boolean schemaExists() {
    try (Connection connection = DriverManager.getConnection(jdbcBaseUrl, username, password);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + schemaName + "'")) {
      return rs.next();
    } catch (Exception ex) {
      return false;
    }
  }

  public void createSchema() {
    try (Connection connection = DriverManager.getConnection(jdbcBaseUrl, username, password);
        Statement stmt = connection.createStatement()) {
      stmt.execute("CREATE DATABASE IF NOT EXISTS `" + schemaName + "`");
    } catch (Exception ex) {
      throw new RuntimeException("Failed to create schema: " + schemaName, ex);
    }
  }

  public void ensureSchemaExists() {
    if (!schemaExists()) {
      createSchema();
    }
  }
}