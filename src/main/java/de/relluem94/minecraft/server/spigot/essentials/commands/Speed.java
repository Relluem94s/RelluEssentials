package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_INVALID;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_SPEED;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_SPEED_INFO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SPEED;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class Speed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_SPEED)) {
            return false;
        }

        if (args.length != 1) {
            sender.sendMessage(PLUGIN_COMMAND_SPEED_INFO);
            return true;
        } 

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;
        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (!args[0].matches("^\\d+$")) {
            p.sendMessage(PLUGIN_COMMAND_INVALID);
        } 

        float speed = parseSpeed(args[0]);
        if (p.isFlying()) {
            p.setFlySpeed(speed);
        } else {
            p.setWalkSpeed(speed);
        }
        p.sendMessage(String.format(PLUGIN_COMMAND_SPEED, args[0]));
        return true;
    }

    private float parseSpeed(String arg) {
        return (float) Integer.parseInt(arg) / 10;
    }
}
