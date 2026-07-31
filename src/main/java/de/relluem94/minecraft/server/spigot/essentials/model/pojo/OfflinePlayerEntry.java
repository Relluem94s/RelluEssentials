package de.relluem94.minecraft.server.spigot.essentials.model.pojo;

import java.util.Properties;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class OfflinePlayerEntry {

  private UUID id;
  private String name;
  private Properties properties;
}