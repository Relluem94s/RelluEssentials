package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Effect;
import org.bukkit.Sound;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_POKE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Poke implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_POKE)) {
            if (args.length == 0) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
                        sendMessage(p, PLUGIN_COMMAND_POKE);
                        return true;
                    } else {
                        sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (isPlayer(sender)) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
                            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10F, 0F);
                            target.getWorld().playEffect(target.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
                            target.getWorld().playEffect(target.getLocation(), Effect.EXTINGUISH, 5);
                            target.getWorld().playEffect(target.getLocation(), Effect.ENDERDRAGON_GROWL, 5);
                            target.sendTitle(PLUGIN_COMMAND_POKE_TITLE, PLUGIN_COMMAND_POKE_SUBTITLE, 5, 80, 5);
                            sendMessage(target, String.format(PLUGIN_COMMAND_POKE_MESSAGE_TARGET, p.getDisplayName()));
                            sendMessage(p, String.format(PLUGIN_COMMAND_POKE_MESSAGE_SENDER, target.getDisplayName()));
                            return true;
                        } else {
                            sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
