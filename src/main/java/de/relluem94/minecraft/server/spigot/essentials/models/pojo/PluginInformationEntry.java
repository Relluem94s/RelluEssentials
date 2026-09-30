package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents metadata and configuration information for the essentials plugin, including MOTD content, tab header and
 * footer, and audit attributes.
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class PluginInformationEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private int updatedBy;
  private String deleted;
  private int deletedBy;
  private String tabHeader;
  private String tabFooter;
  private String motdMessage;
  private int motdPlayers;
  private int dbVersion;
}