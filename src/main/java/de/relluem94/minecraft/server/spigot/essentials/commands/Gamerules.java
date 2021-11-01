package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.bukkit.GameRule;
import org.bukkit.World;

public class Gamerules implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMERULES)) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                        World world = p.getWorld();
                        String[] gamerules = world.getGameRules();
                        sendMessage(p, String.format(PLUGIN_COMMAND_GAMERULES, world.getName()));
                        for(String gamerule : gamerules){
                            Object value = world.getGameRuleValue(GameRule.getByName(gamerule));
                            String color;
                            if(value instanceof Boolean){
                                if((boolean)value == true){
                                    color = "§a";
                                }
                                else{
                                    color = "§c";
                                }
                            }
                            else{
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
