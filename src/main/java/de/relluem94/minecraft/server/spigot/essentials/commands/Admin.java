package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.api.NPCAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_ADMIN;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_ADMIN_PING;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Admin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_ADMIN)) {
            if (isPlayer(sender)) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                    if (args.length == 0) {
                        p.sendMessage(Strings.PLUGIN_COMMAND_ADMIN_INFO);
                    }
                    else if (args.length == 1) {
                        if(args[0].equals("npc")){
                            if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                                org.bukkit.inventory.Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(18, Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER +"§dNPCs"), CustomItems.npc_gui_disabled.getCustomItem());
                            
                                for(int i = 0; i < NPCAPI.getNPCs().size(); i++){
                                    inv.setItem(i, NPCAPI.getNPCs().get(i).getItemHelper().getCustomItem());
                                }

                                InventoryHelper.openInventory(sender, inv);
                            }
                            else{
                                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            }
                           
                        }
                        else if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_ADMIN_PING)) {
                            int ping = p.getPing();
                            
                            p.sendMessage(String.format(PLUGIN_COMMAND_ADMIN_PING, ping));
                            return true;
                        }
                        else if(args[0].equals("chat")){
                            for(Player bp : Bukkit.getOnlinePlayers()){
                                for (int i = 0; i < 100; i++) {
                                    bp.sendMessage("");
                                }
                            }
                            p.sendMessage(PLUGIN_COMMAND_ADMIN_CHAT_CLEARED);
                        }
                        else if(args[0].equals("light")){
                            PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
                            
                            if(pe.getPlayerState().equals(PlayerState.LIGHT_TOOGLE)){
                                pe.setPlayerState(PlayerState.DEFAULT);
                                p.sendMessage(Strings.PLUGIN_COMMAND_ADMIN_LIGHT_TOOGLE_DISABLED);
                            }
                            else{
                                p.sendMessage(Strings.PLUGIN_COMMAND_ADMIN_LIGHT_TOOGLE);
                                pe.setPlayerState(PlayerState.LIGHT_TOOGLE);
                            }
                        }
                        else if(args[0].equals("afk")){
                            PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
                            
                            if(pe.getPlayerState().equals(PlayerState.FAKE_AFK_ACTIVE)){
                                PlayerHelper.setAFK(p, false);
                                pe.setPlayerState(PlayerState.DEFAULT);
                            }
                            else{
                                pe.setPlayerState(PlayerState.FAKE_AFK_ON);
                                PlayerHelper.setAFK(p, false);
                                pe.setPlayerState(PlayerState.FAKE_AFK_ACTIVE);
                            }
                        }
                        else if(args[0].equals("top")){
                            Location l = p.getWorld().getHighestBlockAt(p.getLocation()).getLocation().add(0,1,0);
                            p.sendMessage(PLUGIN_COMMAND_ADMIN_TOP);
                            p.teleport(l);
                        }
                        else{
                            p.sendMessage(PLUGIN_COMMAND_ADMIN_WRONG_SUBCOMMAND);
                        }
                    }
                    else if (args.length == 2) {
                        if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_ADMIN_PING)) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null) {
                                if (isPlayer(sender)) {
                                    int ping = target.getPing();
                                    p.sendMessage(String.format(PLUGIN_COMMAND_ADMIN_PING_OTHER, target.getCustomName(), ping));
                                    return true;
                                }
                            }
                        }
                    }
                    else{
                        p.sendMessage(PLUGIN_COMMAND_ADMIN_WRONG_SUBCOMMAND);
                    }
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
