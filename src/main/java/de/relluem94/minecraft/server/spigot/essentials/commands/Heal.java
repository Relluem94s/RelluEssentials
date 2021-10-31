package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.bukkit.attribute.Attribute;

public class Heal implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_HEAL)) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                        p.setFoodLevel(20);
                        p.sendMessage(PLUGIN_COMMAND_HEAL);
                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
