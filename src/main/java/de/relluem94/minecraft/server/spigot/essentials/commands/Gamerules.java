package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.GameRule;
import org.bukkit.World;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMERULES;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Gamerules implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMERULES)) {
            if (args.length == 0) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                        World world = p.getWorld();
                        String[] gamerules = world.getGameRules();
                        sendMessage(p, String.format(PLUGIN_COMMAND_GAMERULES, world.getName()));
                        for (String gamerule : gamerules) {
                            Object value = world.getGameRuleValue(GameRule.getByName(gamerule));
                            String color;
                            if (value instanceof Boolean) {
                                if ((boolean) value == true) {
                                    color = "§a";
                                } else {
                                    color = "§c";
                                }
                            } else {
                                color = "§7";
                            }

                            sendMessage(p, "        §d" + gamerule + "§f = " + color + value);
                        }

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
