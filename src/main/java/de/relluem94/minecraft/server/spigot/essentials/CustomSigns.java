package de.relluem94.minecraft.server.spigot.essentials;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.SignMissingCustomInputException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper.ActionType;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rellu
 */
public class CustomSigns {

  public static final SignHelper spawn;
  public static final SignHelper up;
  public static final SignHelper down;
  public static final SignHelper command = new SignHelper(ActionType.COMMAND, "");
  public static final SignHelper teleport = new SignHelper(ActionType.TELEPORT, "");
  public static final SignHelper home = new SignHelper(ActionType.HOME, "");

  static {
    SignHelper temp = null;
    try {
      temp = new SignHelper(ActionType.SPAWN);
    } catch (SignMissingCustomInputException ex) {
      Logger.getLogger(CustomSigns.class.getName()).log(Level.SEVERE, null, ex);
    }
    spawn = temp;
  }

  static {
    SignHelper temp = null;
    try {
      temp = new SignHelper(ActionType.UP);
    } catch (SignMissingCustomInputException ex) {
      Logger.getLogger(CustomSigns.class.getName()).log(Level.SEVERE, null, ex);
    }
    up = temp;
  }

  static {
    SignHelper temp = null;
    try {
      temp = new SignHelper(ActionType.DOWN);
    } catch (SignMissingCustomInputException ex) {
      Logger.getLogger(CustomSigns.class.getName()).log(Level.SEVERE, null, ex);
    }
    down = temp;
  }

  private CustomSigns() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }
}
