package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_EXIT;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;;

public class Exit implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_EXIT)) {
            if (args.length == 0) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
                        p.kickPlayer(Strings.PLUGIN_COMAND_EXIT_KICK_MESSAGE);
                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
                else if (isConsole(sender)) {
                    Bukkit.broadcastMessage(Strings.PLUGIN_COMMAND_EXIT_SERVER_SHUTTING_DOWN);
 
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            
                            Bukkit.getOnlinePlayers().forEach(op -> {
                                op.kickPlayer(Strings.PLUGIN_COMMAND_EXIT_SERVER_SHUTTING_DOWN);
                            });
            
                        }
                    }.runTaskLater(RelluEssentials.getInstance(),  10l);

                    Bukkit.getServer().getScheduler().runTaskLater(RelluEssentials.getInstance(), Bukkit.getServer()::shutdown, 20L);
                }
            }
        }
        return false;
    }
}
