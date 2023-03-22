package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static java.lang.Math.round;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_WHERE_STRING;
import org.bukkit.Location;

import de.relluem94.minecraft.server.spigot.essentials.Strings;

/**
 *
 * @author rellu
 */
public class StringHelper {

    private StringHelper() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_CLASS_PRIVATE_CONSTRUCTOR);
    }

    /**
     *
     * @param message String
     * @return String replaces & with § to trigger the ChatColor codes
     */
    public static String replaceColor(String message) {
        return message.replace("&", "§");
    }

    /**
     *
     * @param l Location
     * @return String with Location
     */
    public static String locationToString(Location l) {
        return String.format(PLUGIN_COMMAND_WHERE_STRING, round(l.getX()), round(l.getY()), round(l.getZ()), l.getWorld().getName());
    }

    public static String firstCharToUpper(String s){
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }


    public static String formatLong(long l) {
        if(l >= 1000000000){
            return String.format("%sB", l / 1000000000.0);
        }
        else if(l >= 1000000){
            return String.format("%sM", l / 1000000.0);
        }
        else if(l >= 1000){
            return String.format("%sK", l / 1000.0);
        }
        else{
            return String.format("%s", l);
        }
    }

    public static String formatInt(int i) {
        if(i >= 1000000000){
            return String.format("%sB", i / 1000000000);
        }
        else if(i >= 1000000){
            return String.format("%sM", i / 1000000);
        }
        else if(i >= 1000){
            return String.format("%sK", i / 1000);
        }
        else{
            return String.format("%s", i);
        }
    }

    public static String formatDouble(double d) {
        if(d >= 1000000000){
            return String.format("%.2fB", d / 1000000000.0);
        }
        else if(d >= 1000000){
            return String.format("%.2fM", d / 1000000.0);
        }
        else if(d >= 1000){
            return String.format("%.2fK", d / 1000.0);
        }
        else if(d < 1000 && d > -1000){
            return String.format("%.2f", d);
        }
        else if(d <= -1000000000){
            return String.format("%.2fB", d / 1000000000.0);
        }
        else if(d <= -1000000){
            return String.format("%.2fM", d / 1000000.0);
        }
        else if(d <= -1000){
            return String.format("%.2fK", d / 1000.0);
        }
        else{
            return String.format("%.2f", d);
        }
    }
}