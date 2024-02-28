package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COLOR_COMMAND_BLOCK;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COLOR_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COLOR_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_INVALID;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PRINT_INFO;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_FORMS_SPACER_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_PRINT;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;
import static de.relluem94.rellulib.utils.StringUtils.implode;
import static de.relluem94.rellulib.utils.StringUtils.replaceSymbols;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class Print implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {
        Player targetedPlayerBySelector = null;

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
            name = PLUGIN_COLOR_COMMAND_BLOCK + bcs.getName();

            List<String> argsList = Arrays.asList(args);
            if(argsList.contains("@p")){
                CommandBlock cb = (CommandBlock) bcs.getBlock().getState();
                targetedPlayerBySelector = PlayerHelper.getTargetedPlayer(cb.getBlock().getLocation());
                if(targetedPlayerBySelector == null){
                    sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, "No Player in Reach"));
                    return true;
                }
            }
        }

        if (isConsole(sender)) {
            ConsoleCommandSender ccs = (ConsoleCommandSender) sender;
            name = PLUGIN_COLOR_CONSOLE + StringHelper.firstCharToUpper(ccs.getName().toLowerCase());
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

        if(targetedPlayerBySelector != null){
            message = message.replace("@p", Objects.requireNonNull(targetedPlayerBySelector.getCustomName()));
        }

        Bukkit.broadcastMessage(name + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_COLOR_MESSAGE + message);
        return true;
    }
}