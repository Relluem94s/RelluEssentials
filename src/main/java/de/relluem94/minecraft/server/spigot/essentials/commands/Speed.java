package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SPEED;

public class Speed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_SPEED)) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                        if (args[0].matches("^\\d+$")) {
                            float speed = parseSpeed(args[0]);
                            if (p.isFlying()) {
                                p.setFlySpeed(speed);
                            } else {
                                p.setWalkSpeed(speed);
                            }
                            p.sendMessage(String.format(PLUGIN_COMMAND_SPEED, args[0]));
                            return true;
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }
                }
            } else {
                sender.sendMessage(PLUGIN_COMMAND_SPEED_INFO);
            }
        }
        return false;
    }

    private float parseSpeed(String arg) {
        return Integer.parseInt(arg)/10;
    }
}