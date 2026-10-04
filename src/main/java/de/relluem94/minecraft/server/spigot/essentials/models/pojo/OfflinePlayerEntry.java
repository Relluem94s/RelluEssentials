package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import java.util.Properties;
import java.util.UUID;
import lombok.Data;

/**
 * Represents an offline player entry that stores the player's unique identifier, display name, and additional
 * properties related to their state or configuration.
 *
 * @author rellu
 */
@Data
public class OfflinePlayerEntry {

  private UUID id;
  private String name;
  private Properties properties;
}