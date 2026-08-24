package de.relluem94.minecraft.server.spigot.essentials.constants;

public class SignConstants {


  public static final String PLUGIN_SIGN_ACTION_SPAWN = "spawn";
  public static final String PLUGIN_SIGN_ACTION_UP = "up";
  public static final String PLUGIN_SIGN_ACTION_DOWN = "down";
  public static final String PLUGIN_SIGN_ACTION_COMMAND = "command";
  public static final String PLUGIN_SIGN_ACTION_TELEPORT = "teleport";
  public static final String PLUGIN_SIGN_ACTION_HOME = "home";


  private SignConstants() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }
}