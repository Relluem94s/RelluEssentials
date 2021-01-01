package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class PortableCraftingBench implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("craft")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.VIP.getId())) {
                    p.openWorkbench(p.getLocation(), true);
                    p.sendMessage(String.format(PLUGIN_COMMAND_CRAFTINGBENCH, p.getCustomName()));
                    return true;
                } else {
                    p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                    return true;
                }
            }
        }
        return false;
    }
}
