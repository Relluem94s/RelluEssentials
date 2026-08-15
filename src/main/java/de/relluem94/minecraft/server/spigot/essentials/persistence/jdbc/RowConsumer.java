package de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowConsumer {
  void consume(ResultSet rs) throws SQLException;
}