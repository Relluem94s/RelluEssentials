/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.relluem94.minecraft.server.spigot.essentials.helpers;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.players;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author rellu
 */
public class PlayerHelper {
    public static void setFlying(Player p){
        if (User.getGroup(p).getId() >= Groups.VIP.getId()) {
            if (players.getConfig().getBoolean("player." + p.getUniqueId() + ".fly")) {
                p.setAllowFlight(true);
                if (p.getLocation().getBlock().getRelative(0, -1, 0).getType().equals(Material.AIR)) {
                    p.setFlying(true);
                }
            }
        }
    }
}
