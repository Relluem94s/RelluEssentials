package de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc;

import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.loader.SqlResourceLoader;
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
import org.jspecify.annotations.Nullable;

/**
 * Executes SQL queries and updates against a relational database using JDBC.
 *
 * <p>SQL statements are loaded from classpath resources via a {@link SqlResourceLoader}.
 * All database operations are performed using a {@link javax.sql.DataSource} for connection management.</p>
 *
 * @author rellu
 */
public class QueryExecutor {

  private final DataSource dataSource;
  private final SqlResourceLoader sqlResourceLoader;
  private final Logger logger;

  /**
   * Creates a new {@code QueryExecutor} with a default {@link Logger}.
   *
   * @param dataSource        the data source used to obtain database connections
   * @param sqlResourceLoader the loader used to resolve SQL files from classpath resources
   */
  public QueryExecutor(DataSource dataSource, SqlResourceLoader sqlResourceLoader) {
    this(dataSource, sqlResourceLoader, Logger.getLogger(QueryExecutor.class.getName()));
  }

  QueryExecutor(DataSource dataSource, SqlResourceLoader sqlResourceLoader, Logger logger) {
    this.dataSource = dataSource;
    this.sqlResourceLoader = sqlResourceLoader;
    this.logger = logger;
  }

  /**
   * Executes a SQL query and passes each resulting row to the given {@link RowConsumer}.
   *
   * <p>The SQL statement is loaded from the given file name relative to the {@code sqls/} resource directory.</p>
   *
   * @param sqlFile    the name of the SQL file to load
   * @param configurer the configurer used to bind parameters to the {@link PreparedStatement}
   * @param consumer   the consumer invoked for each row in the result set
   */
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
      logger.log(Level.SEVERE, ex.getMessage(), ex);
    }
  }

  /**
   * Executes a SQL query and maps each resulting row to an object of type {@code T}.
   *
   * <p>The SQL statement is loaded from the given file name relative to the {@code sqls/} resource directory.
   * If an error occurs, an empty list is returned.</p>
   *
   * @param <T>        the type of objects in the returned list
   * @param sqlFile    the name of the SQL file to load
   * @param configurer the configurer used to bind parameters to the {@link PreparedStatement}
   * @param mapper     the mapper used to convert each row into an object of type {@code T}
   * @return a list of mapped results, or an empty list if no rows were found or an error occurred
   */
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
      logger.log(Level.SEVERE, ex.getMessage(), ex);
    }
    return results;
  }

  /**
   * Executes a SQL query and maps the first resulting row to an object of type {@code T}.
   *
   * <p>The SQL statement is loaded from the given file name relative to the {@code sqls/} resource directory.
   * If no row is found or an error occurs, {@code null} is returned.</p>
   *
   * @param <T>        the type of the returned object
   * @param sqlFile    the name of the SQL file to load
   * @param configurer the configurer used to bind parameters to the {@link PreparedStatement}
   * @param mapper     the mapper used to convert the first row into an object of type {@code T}
   * @return the mapped result, or {@code null} if no row was found or an error occurred
   */
  public @Nullable <T> T querySingle(String sqlFile, StatementConfigurer configurer, RowMapper<T> mapper) {
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
      logger.log(Level.SEVERE, ex.getMessage(), ex);
    }
    return null;
  }

  /**
   * Executes a SQL update statement without returning a result.
   *
   * <p>The SQL statement is loaded from the given file name relative to the {@code sqls/} resource directory.</p>
   *
   * @param sqlFile    the name of the SQL file to load
   * @param configurer the configurer used to bind parameters to the {@link PreparedStatement}
   */
  public void executeUpdate(String sqlFile, StatementConfigurer configurer) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      ps.execute();
    } catch (SQLException | FileNotFoundException ex) {
      logger.log(Level.SEVERE, ex.getMessage(), ex);
    }
  }

  /**
   * Executes a SQL update statement and returns the number of affected rows.
   *
   * <p>The SQL statement is loaded from the given file name relative to the {@code sqls/} resource directory.
   * If an error occurs, {@code 0} is returned.</p>
   *
   * @param sqlFile    the name of the SQL file to load
   * @param configurer the configurer used to bind parameters to the {@link PreparedStatement}
   * @return the number of affected rows, or {@code 0} if an error occurred
   */
  public int executeUpdateWithCount(String sqlFile, StatementConfigurer configurer) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      return ps.executeUpdate();
    } catch (SQLException | FileNotFoundException ex) {
      logger.log(Level.SEVERE, ex.getMessage(), ex);
      return 0;
    }
  }

  /**
   * Executes a SQL insert statement and returns the generated primary key.
   *
   * <p>The SQL statement is loaded from the given file name relative to the {@code sqls/} resource directory.
   * If no key was generated or an error occurs, {@code -1} is returned.</p>
   *
   * @param sqlFile    the name of the SQL file to load
   * @param configurer the configurer used to bind parameters to the {@link PreparedStatement}
   * @return the generated primary key, or {@code -1} if no key was generated or an error occurred
   */
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
      logger.log(Level.SEVERE, ex.getMessage(), ex);
    }
    return -1;
  }

  /**
   * Executes a SQL script file without binding any parameters.
   *
   * <p>This is a convenience method that delegates to {@link #executeUpdate(String, StatementConfigurer)}
   * with an empty {@link StatementConfigurer}.</p>
   *
   * @param script the name of the SQL script file to execute
   */
  public void executeScript(String script) {
    executeUpdate(script, _ -> {});
  }
}