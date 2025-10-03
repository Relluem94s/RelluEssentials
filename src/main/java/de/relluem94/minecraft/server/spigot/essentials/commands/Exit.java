package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;

@CommandName("exit")
public class Exit implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>();
    }
}
