package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.getText;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_FLY;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Fly implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (isPlayer(sender)) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
                    return flyMode(command, p);
                } else {
                    p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                    return true;
                }
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                        p.sendMessage(String.format(PLUGIN_COMMAND_FLYMODE, target.getCustomName(), !target.getAllowFlight() ? PLUGIN_COMMAND_FLYMODE_ACTIVATED : PLUGIN_COMMAND_FLYMODE_DEACTIVATED));
                        return flyMode(command, target);
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean flyMode(Command command, Player p) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_FLY)) {
            PlayerEntry pe = PlayerAPI.getPlayerEntry(p.getUniqueId());
            pe.setFlying(!pe.isFlying());
            pe.setUpdatedBy(pe.getID());
            pe.setToBeUpdated(true);
            p.setAllowFlight(pe.isFlying());
            p.sendMessage(
                PLUGIN_FORMS_COMMAND_PREFIX + 
                String.format(
                    getText("PLUGIN_COMMAND_FLYMODE"), 
                    p.getCustomName() + PLUGIN_COLOR_COMMAND, 
                    PLUGIN_COLOR_COMMAND_ARG + 
                    (pe.isFlying() ? 
                        getText("PLUGIN_COMMAND_FLYMODE_ACTIVATED") : 
                        getText("PLUGIN_COMMAND_FLYMODE_DEACTIVATED")
                    ) + PLUGIN_COLOR_COMMAND
                )
            );
            return true;
        } else {
            return false;
        }
    }
}
