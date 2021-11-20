package de.relluem94.minecraft.server.spigot.essentials;

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

    public static SignHelper spawn;

    static {
        SignHelper temp = null;
        try {
            temp = new SignHelper(ActionType.SPAWN);
        } catch (SignMissingCustomInputException ex) {
            Logger.getLogger(CustomSigns.class.getName()).log(Level.SEVERE, null, ex);
        }
        spawn = temp;
    }

    public static SignHelper up;

    static {
        SignHelper temp = null;
        try {
            temp = new SignHelper(ActionType.UP);
        } catch (SignMissingCustomInputException ex) {
            Logger.getLogger(CustomSigns.class.getName()).log(Level.SEVERE, null, ex);
        }
        up = temp;
    }

    public static SignHelper down;

    static {
        SignHelper temp = null;
        try {
            temp = new SignHelper(ActionType.DOWN);
        } catch (SignMissingCustomInputException ex) {
            Logger.getLogger(CustomSigns.class.getName()).log(Level.SEVERE, null, ex);
        }
        down = temp;
    }

    public static SignHelper command = new SignHelper(ActionType.COMMAND, "");
    public static SignHelper teleport = new SignHelper(ActionType.TELEPORT, "");
    public static SignHelper home = new SignHelper(ActionType.HOME, "");
}
