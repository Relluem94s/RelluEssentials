package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_BACK_NO_LOCATION;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TeleportHelper.teleportBack;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("back")
public class Back implements CommandConstruct {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>();
    }

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    private static final Map<Player, Location> backPlayerLocation = new HashMap<>();

    public static void addBackPoint(Player p){
        removeBackPoint(p);
        backPlayerLocation.put(p, p.getLocation());
    }

    public static void removeBackPoint(Player p){
        backPlayerLocation.remove(p);
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }
           
        if(!backPlayerLocation.containsKey(p)){
            p.sendMessage(PLUGIN_COMMAND_BACK_NO_LOCATION);
            return true;
        }
        
        teleportBack(p, backPlayerLocation.get(p));
        backPlayerLocation.remove(p);
        return true;
    }
}