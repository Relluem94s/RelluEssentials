/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 *
 * @author rellu
 */
public class WorldHelper {
    public static boolean isInWorld(CommandSender sender, String m) {
        if (TypeHelper.isPlayer(sender)) {
            Player p = (Player) sender;
            return p.getWorld().getName().equalsIgnoreCase(m);
        } else {
            return true;
        }
    }
    
    public static boolean isInWorld(Player p, String m) {
        return p.getWorld().getName().equalsIgnoreCase(m);
    }

    public static boolean isInWorld(CommandSender sender, World w) {
        if (TypeHelper.isPlayer(sender)) {
            Player p = (Player) sender;
            return p.getWorld().equals(w);
        } else {
            return true;
        }
    }

    public static boolean isInWorld(Block b, World m) {
        return b.getWorld().equals(m);
    }

    public static boolean isInWorld(Entity e, World m) {
        return e.getWorld().equals(m);
    }
}
