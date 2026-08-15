package de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementConfigurer {
  void configure(PreparedStatement ps) throws SQLException;
}