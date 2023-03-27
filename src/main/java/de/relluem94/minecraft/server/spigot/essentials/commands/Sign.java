package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Sign implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_SIGN)) {
            
            if (isPlayer(sender)) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                    if (args.length == 0) {
                        p.sendMessage(PLUGIN_COMMAND_SIGN_INFO);
                    }
                    else if (args.length == 1) {
                        if(args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_SIGN_EDIT)){
                            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
                            pe.setPlayerState(PlayerState.SIGNEDIT);
                            p.sendMessage(PLUGIN_COMMAND_SIGN_EDIT);
                        }
                        else 
                        if(args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_SIGN_COPY)){
                            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
                            pe.setPlayerState(PlayerState.SIGNCOPY);
                            p.sendMessage(PLUGIN_COMMAND_SIGN_COPY);
                        }
                    }

                    //
                    // Remeber to check for Permission (Protection if lower then mod)
                    //
                    return true;
                }
                else {
                    p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                    return true;
                }
            }
           
        }
        return false;
    }
}
