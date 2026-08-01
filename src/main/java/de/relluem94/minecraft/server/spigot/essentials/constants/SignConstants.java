package de.relluem94.minecraft.server.spigot.essentials.constants;

public class SignConstants {

  private static final String PLUGIN_NAMESPACE = "relluessentials:";

  public static final String PLUGIN_SIGN_ACTION_SPAWN = PLUGIN_NAMESPACE + "spawn";
  public static final String PLUGIN_SIGN_ACTION_UP = PLUGIN_NAMESPACE + "up";
  public static final String PLUGIN_SIGN_ACTION_DOWN = PLUGIN_NAMESPACE + "down";
  public static final String PLUGIN_SIGN_ACTION_COMMAND = PLUGIN_NAMESPACE + "command";
  public static final String PLUGIN_SIGN_ACTION_TELEPORT = PLUGIN_NAMESPACE + "teleport";
  public static final String PLUGIN_SIGN_ACTION_HOME = PLUGIN_NAMESPACE + "home";


  private SignConstants() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }
}