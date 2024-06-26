package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WRONG_SUB_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SUDO;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.OfflinePlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.rellulib.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

public class Sudo implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_SUDO)) {
            
            if(RelluEssentials.getInstance().getCommand(args[0]) != null){
                dispatchCommand(args);
                return true;
            }

            if (args.length == 1) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if(SudoManager.sudoers.containsKey(p.getUniqueId())){
                        exitSudo(p);
                        return true;
                    }
                    else if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                        OfflinePlayerEntry target = PlayerHelper.getOfflinePlayerByName((args[0]));
                        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
                        if (target != null && RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(target.getId()) != null) {
                            PlayerEntry tpe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(target.getId());
                            SudoManager.sudoers.put(p.getUniqueId(), new PlayerEntry(pe));
                            WorldHelper.saveWorldGroupInventory(p, true);
                            pe.setId(tpe.getId());
                            pe.setCustomName(tpe.getCustomName());
                            pe.setGroup(tpe.getGroup());
                            pe.setHomes(tpe.getHomes());
                            pe.setPurse(tpe.getPurse());
                            p.setCustomName(tpe.getGroup().getPrefix() + target.getName());
                            if(tpe.getCustomName() != null){
                                p.setCustomName(tpe.getGroup().getPrefix() + tpe.getCustomName());
                            }
                            WorldHelper.loadWorldGroupInventory(p);
                            p.sendMessage(String.format(Constants.PLUGIN_COMMAND_SUDO_ACTIVATED, tpe.getGroup().getPrefix() + target.getName()));
                        }
                        else{
                            
                            p.sendMessage(String.format(Constants.PLUGIN_COMMAND_SUDO_PLAYER_NOT_FOUND, args[0]));
                        }
                        return true;
                    }
                    else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
            else{
                sender.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
                return true;
            }
        }
        return false;
    }

    private void dispatchCommand(String[] args){
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.getServer().dispatchCommand(console, StringUtils.toString(args));
    }

    public static void exitSudo(Player p){
        PlayerEntry tpe = SudoManager.sudoers.get(p.getUniqueId());
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        WorldHelper.saveWorldGroupInventory(p, true);
        pe.setId(tpe.getId());
        pe.setCustomName(tpe.getCustomName());
        pe.setGroup(tpe.getGroup());
        pe.setHomes(tpe.getHomes());
        pe.setPurse(tpe.getPurse());
        p.setCustomName(tpe.getGroup().getPrefix() + p.getName());
        if(tpe.getCustomName() != null){
            p.setCustomName(tpe.getGroup().getPrefix() + tpe.getCustomName());
        }
        WorldHelper.loadWorldGroupInventory(p);
        SudoManager.sudoers.remove(p.getUniqueId());
        p.sendMessage(Constants.PLUGIN_COMMAND_SUDO_DEACTIVATED);
    }
}