package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import org.jetbrains.annotations.NotNull;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("afk")
public class AFK implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 0) {
            PlayerHelper.setAFK(p, false);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            return false;
        }

        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 1) {
            PlayerHelper.setAFK(target, false);
            return true;
        }

        return false;
    }
}