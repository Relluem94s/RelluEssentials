package de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {
  T map(ResultSet rs) throws SQLException;
}