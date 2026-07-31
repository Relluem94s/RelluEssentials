package de.relluem94.minecraft.server.spigot.essentials.npc;

import java.util.Locale;

public class NpcMannequinCommandBuilder {

  private static final String SUMMON_COMMAND_TEMPLATE = "summon minecraft:mannequin %s %s %s {\"profile\":\"%s\"}";

  public String buildSummonCommand(double x, double y, double z, String profileName) {
    return String.format(SUMMON_COMMAND_TEMPLATE,
        formatCoordinate(x),
        formatCoordinate(y),
        formatCoordinate(z),
        profileName
    );
  }

  private String formatCoordinate(double coordinate) {
    return String.format(Locale.US, "%.4f", coordinate);
  }
}