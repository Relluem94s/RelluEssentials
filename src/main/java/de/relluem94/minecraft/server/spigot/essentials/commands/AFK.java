package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.players;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class AFK implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.USER.getId())) {
                    return setAFK(command, p);
                } else {
                    p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                    return true;
                }
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.MOD.getId())) {
                        return setAFK(command, target);
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean setAFK(Command command, Player p) {
        if (command.getName().equalsIgnoreCase("afk")) {
            boolean isAFK = true;
            
            if(players.getConfig().get("player." + p.getUniqueId() + ".afk") != null){
                isAFK = !players.getConfig().getBoolean("player." + p.getUniqueId() + ".afk");
            }
            players.getConfig().set("player." + p.getUniqueId() + ".afk", !isAFK);
            p.setInvulnerable(isAFK);
            Bukkit.broadcastMessage(String.format(PLUGIN_COMMAND_AFK, p.getCustomName(), isAFK ? PLUGIN_COMMAND_AFK_ACTIVATED : PLUGIN_COMMAND_AFK_DEACTIVATED));
            return true;
        } else {
            return false;
        }
    }

}
