package de.relluem94.minecraft.server.spigot.essentials.commands;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_EXIT;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;

public class Exit implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_EXIT)) {
            return false;
        }

        if (isConsole(sender)){
            Bukkit.broadcastMessage(Constants.PLUGIN_COMMAND_EXIT_SERVER_SHUTTING_DOWN);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        
                        Bukkit.getOnlinePlayers().forEach(op ->  op.kickPlayer(Constants.PLUGIN_COMMAND_EXIT_SERVER_SHUTTING_DOWN));
        
                    }
                }.runTaskLater(RelluEssentials.getInstance(), 10L);

                Bukkit.getServer().getScheduler().runTaskLater(RelluEssentials.getInstance(), Bukkit.getServer()::shutdown, 20L);
                return true;
        }

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;
        if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        } 

        p.kickPlayer(Constants.PLUGIN_COMMAND_EXIT_KICK_MESSAGE);
        return true;
    }
}
