package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.commands.Back;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;

public class TeleportHelper {

    public static void teleportWorld(Player p, String name){
       teleportWorld(p, name, false);
    }

    public static void teleportWorld(Player p, String name, boolean silent){
        World w = getWorld(p, silent, name);
        if (w == null){
            return;
        }

        Location spawn = w.getSpawnLocation();

        addBackPointAndTeleport(PLUGIN_COMMAND_SPAWN, p, Bukkit.getWorld(name), silent, spawn);
    }

    public static void teleportBed(Player p){
        teleportBed(p,false);
    }

    public static void teleportBed(@NotNull Player p, boolean silent){
        if (p.getRespawnLocation() == null) {
            p.sendMessage(String.format(PLUGIN_COMMAND_HOME_NO_BED, p.getWorld().getName()));
        }

        Location spawn = p.getRespawnLocation();

        World w = getWorld(p, silent, spawn, "world_of_bed_from_" + p.getName().toLowerCase());
        if (w == null){
            return;
        }

        addBackPointAndTeleport(PLUGIN_COMMAND_HOME, p, w, silent, spawn);
    }


    public static void teleportHome(@NotNull Player p, LocationEntry locationEntry){
        if (p.getRespawnLocation() == null) {
            p.sendMessage(String.format(PLUGIN_COMMAND_HOME_NO_BED, p.getWorld().getName()));
        }

        Location home = locationEntry.getLocation();

        World w = getWorld(p, false, home, "world_of_home_from_" + p.getName().toLowerCase());
        if (w == null){
            return;
        }

        addBackPointAndTeleport("", p, w, true, home);
        p.sendMessage(String.format(PLUGIN_COMMAND_HOME_TP, locationEntry.getLocationName()));
    }

    public static void teleportBack(Player p, @NotNull Location location){
        World w = location.getWorld();
        if (w == null){
            return;
        }

        addBackPointAndTeleport("", p, w, true, location, false);
        p.sendMessage(PLUGIN_COMMAND_BACK);
    }

    public static void teleportWarp(Player p, @NotNull Location location){
        World w = location.getWorld();
        if (w == null){
            return;
        }

        addBackPointAndTeleport("", p, w, true, location, true);
        p.sendMessage(PLUGIN_COMMAND_WARP);
    }



    private static @Nullable World getWorld(Player p, boolean silent, @NotNull Location location, String worldName) {
        World w = location.getWorld();
        if (w == null) {
            if(!silent){
                p.sendMessage(String.format(PLUGIN_COMMAND_WORLD_NOT_FOUND, worldName));
            }

            return null;
        }
        return w;
    }

    private static @Nullable World getWorld(Player p, boolean silent, String worldName) {
        World w = Bukkit.getWorld(worldName);
        if (w == null) {
            if(!silent){
                p.sendMessage(String.format(PLUGIN_COMMAND_WORLD_NOT_FOUND, worldName));
            }

            return null;
        }
        return w;
    }

    private static void addBackPointAndTeleport(String message, Player p, World world, boolean silent, @NotNull Location location) {
        addBackPointAndTeleport(message, p, world, silent, location, true);
    }

    private static void addBackPointAndTeleport(String message, Player p, World world, boolean silent, @NotNull Location location, boolean saveBackPoint){
        if(saveBackPoint){
            Back.addBackPoint(p);
        }

        Location locationWorld = new Location(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        p.teleport(locationWorld);
        if(!silent){
            p.sendMessage(String.format(message, p.getWorld().getName()));
        }
    }
}
