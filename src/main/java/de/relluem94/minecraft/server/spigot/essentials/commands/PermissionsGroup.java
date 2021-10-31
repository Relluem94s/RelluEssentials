package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NAME_SETGROUP;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.ConsoleCommandSender;

public class PermissionsGroup implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_SETGROUP)) {
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                            GroupEntry g = Groups.getGroup(args[1]);
                            User u = User.getUserByPlayerName(target.getName());

                            if (u != null) {
                                u.setGroup(g);
                            } else {
                                u = new User(target);
                                u.setGroup(g);
                            }
                            return true;
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    } else if (sender instanceof ConsoleCommandSender || sender instanceof CommandBlock) {
                        GroupEntry g = Groups.getGroup(args[1]);
                        User u = User.getUserByPlayerName(target.getName());

                        if (u != null) {
                            u.setGroup(g);
                        } else {
                            u = new User(target);
                            u.setGroup(g);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}