package de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc;

import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.helpers.db.loader.SqlResourceLoader;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class QueryExecutor {

  private final DataSource dataSource;
  private final DataSource dataSourceNoSchema;
  private final SqlResourceLoader sqlResourceLoader;

  public QueryExecutor(DataSource dataSource, SqlResourceLoader sqlResourceLoader) {
    this.dataSource = dataSource;
    this.dataSourceNoSchema = null;
    this.sqlResourceLoader = sqlResourceLoader;
  }

  public QueryExecutor(DataSource dataSource, DataSource dataSourceNoSchema,
      SqlResourceLoader sqlResourceLoader) {
    this.dataSource = dataSource;
    this.dataSourceNoSchema = dataSourceNoSchema;
    this.sqlResourceLoader = sqlResourceLoader;
  }

  public void queryForEach(String sqlFile, StatementConfigurer configurer, RowConsumer consumer) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          consumer.consume(rs);
        }
      }
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
  }

  public <T> List<T> queryList(String sqlFile, StatementConfigurer configurer,
      RowMapper<T> mapper) {
    List<T> results = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          results.add(mapper.map(rs));
        }
      }
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
    return results;
  }

  public <T> T querySingle(String sqlFile, StatementConfigurer configurer, RowMapper<T> mapper) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          return mapper.map(rs);
        }
      }
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
    return null;
  }

  private <T> T querySingleNoSchema(String sqlFile, StatementConfigurer configurer,
      RowMapper<T> mapper) {
    try (Connection connection = dataSourceNoSchema.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          return mapper.map(rs);
        }
      }
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
      throw new RuntimeException(ex);
    }
    return null;
  }

  public void executeUpdate(String sqlFile, StatementConfigurer configurer) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      ps.execute();
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
  }

  private int executeUpdateWithCount(String sqlFile) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      return ps.executeUpdate();
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
      return 0;
    }
  }

  public int executeUpdateWithCount(String sqlFile, StatementConfigurer configurer) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      return ps.executeUpdate();
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
      return 0;
    }
  }

  private void executeUpdateNoSchema(String sqlFile) {
    try (Connection connection = dataSourceNoSchema.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      ps.execute();
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
  }

  public int executeInsertWithGeneratedKey(String sqlFile, StatementConfigurer configurer) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile),
            PreparedStatement.RETURN_GENERATED_KEYS)) {
      configurer.configure(ps);
      ps.executeUpdate();
      try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1);
        }
      }
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
    return -1;
  }

  void executeScript(String script) {
    executeUpdate(script, _ -> {
    });
  }

  void executeScriptNoSchema(String script) {
    executeUpdateNoSchema(script);
  }


  @FunctionalInterface
  public interface StatementConfigurer {

    void configure(PreparedStatement ps) throws SQLException;
  }

  @FunctionalInterface
  public interface RowMapper<T> {

    T map(ResultSet rs) throws SQLException;
  }

  @FunctionalInterface
  public interface RowConsumer {

    void consume(ResultSet rs) throws SQLException;
  }
}
