package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_NEGATIVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_POSITIVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_GAMERULES;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_LESS_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_MANY_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WORLD_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMERULES;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

import java.util.Objects;

public class GameRules implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMERULES)) {
            return false;
        }

        if (isCMDBlock(sender) || isConsole(sender)) {
            if (args.length < 1) {
                sender.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
                return true;   
            }


            World world = Bukkit.getWorld(args[0]);
            if (world == null) {
                sender.sendMessage(String.format(PLUGIN_COMMAND_WORLD_NOT_FOUND, args[0]));
                return true;
            }

            showGameRule(sender, world);
            return true;
        }


        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;   
        }

        if (args.length > 0) {
            p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;   
        }

        showGameRule(sender, p.getWorld());
        return true;
    }

    private void showGameRule(CommandSender sender, World world) {
        String[] gameRules = world.getGameRules();
        sendMessage(sender, String.format(PLUGIN_COMMAND_GAMERULES, world.getName()));
        for (String gameRule : gameRules) {
            Object value = world.getGameRuleValue(Objects.requireNonNull(GameRule.getByName(gameRule)));
            String color;
            if (value instanceof Boolean) {
                color = ((boolean)value ? PLUGIN_COLOR_POSITIVE : PLUGIN_COLOR_NEGATIVE);
            } else {
                color = "§7";
            }

            sendMessage(sender, "        §d" + gameRule + "§f = " + color + value);
        }
    }
}
