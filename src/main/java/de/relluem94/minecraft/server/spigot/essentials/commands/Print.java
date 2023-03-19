package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.BlockCommandSender;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_PRINT;
import static de.relluem94.rellulib.utils.StringUtils.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer; 
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;

public class Print implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_PRINT)) {
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(PLUGIN_COMMAND_PRINT_INFO);
            return true;
        }
   
        String name = null;

        if (isCMDBlock(sender)) {
            BlockCommandSender bcs = (BlockCommandSender) sender;
            name = bcs.getName();
        }

        if (isConsole(sender)) {
            ConsoleCommandSender ccs = (ConsoleCommandSender) sender;
            name = ccs.getName();
        }

        if (isPlayer(sender)) {
            Player p = (Player) sender;

            if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }

            name = p.getCustomName();
        }

        if(name == null){
            sender.sendMessage(PLUGIN_COMMAND_INVALID);
            return true;
        }

        String message = implode(0, args);
        message = replaceSymbols(replaceColor(message));

        Bukkit.broadcastMessage(name + PLUGIN_SPACER + PLUGIN_MESSAGE_COLOR + message);
        return true;

    }
}