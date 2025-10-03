package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Bags;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.constants.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Relluem94
 */
public class TabCompleterHelper {
    private TabCompleterHelper() {
        throw new IllegalStateException(
                Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static @NotNull List<String> getOnlinePlayers(){
        List<String> playerList = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            playerList.add(p.getName());
        }

        return playerList;
    }

    public static @NotNull List<String> getProtectionFlags(){
        List<String> protectionFlagList = new ArrayList<>();
        for(ProtectionFlags protectionFlag : ProtectionFlags.values()){
            protectionFlagList.add(protectionFlag.toString());
        }

        return protectionFlagList;
    }

    public static @NotNull List<String> getCommands(CommandsEnum @NotNull [] commandsEnums){
        List<String> commands = new ArrayList<>();
        for(CommandsEnum command : commandsEnums){
            commands.add(command.getName());
        }

        return commands;
    }

    public static @NotNull List<String> getBags(Player p){
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);

        Collection<BagEntry> bags = BagHelper.getBags(pe.getId());
        List<String> bagsList = new ArrayList<>();
        for(BagEntry bag : bags){
            bagsList.add(bag.getBagType().getName().toLowerCase());
        }

        return bagsList;
    }

    public static @NotNull List<String> getWorlds(){
        List<String> worldNames = new ArrayList<>();

        for(World world : Bukkit.getWorlds()){
            worldNames.add(world.getName());
        }

        return worldNames;
    }

    public static @NotNull List<String> getHomes(Player p) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        List<String> homes = new ArrayList<>();

        for(LocationEntry le : pe.getHomes()){
            homes.add(le.getLocationName());
        }

        for(LocationEntry le : pe.getDeaths()){
            homes.add(le.getLocationName());
        }

        return homes;
    }
}
