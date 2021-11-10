package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static java.lang.Math.round;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_WHERE_STRING;
import org.bukkit.Location;

/**
 *
 * @author rellu
 */
public class StringHelper {

    /**
     *
     * @param message String
     * @return String replaces & with § to trigger the ChatColor codes
     */
    public static String replaceColor(String message) {
        return message.replaceAll("&", "§");
    }

    /**
     *
     * @param l Location
     * @return String with Location
     */
    public static String locationToString(Location l) {
        return String.format(PLUGIN_COMMAND_WHERE_STRING, round(l.getX()), round(l.getY()), round(l.getZ()), l.getWorld().getName());
    }
}
