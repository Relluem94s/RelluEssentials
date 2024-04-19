package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.sga.extras.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Relluem94
 */
public class TabCompleterHelper {
    private TabCompleterHelper() {
        throw new IllegalStateException(
                de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static List<String> getOnlinePlayers(){
        List<String> playerList = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            playerList.add(p.getName());
        }

        return playerList;
    }
}
