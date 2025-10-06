package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@CommandName("sudo")
public class Sudo implements CommandConstruct {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (!Permission.isAuthorized(sender, Groups.getGroup("admin").getId())) {
            sender.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (!isPlayer(sender)){
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if(args.length == 0){
            p.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
            return true;
        }

        if(RelluEssentials.getInstance().getCommand(args[0]) != null){
            dispatchCommand(args);
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
            return true;
        }

        if(SudoManager.sudoers.containsKey(p.getUniqueId())){
            exitSudo(p);
            return true;
        }

        OfflinePlayerEntry target = PlayerHelper.getOfflinePlayerByName((args[0]));
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);

        if (target == null) {
            p.sendMessage(String.format(Constants.PLUGIN_COMMAND_SUDO_PLAYER_NOT_FOUND, args[0]));
            return true;
        }

        if(RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(target.getId()) == null){
            p.sendMessage(String.format(Constants.PLUGIN_COMMAND_SUDO_PLAYER_NOT_FOUND, args[0]));
            return true;
        }

        PlayerEntry tpe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(target.getId());
        SudoManager.sudoers.put(p.getUniqueId(), new PlayerEntry(pe));
        WorldHelper.saveWorldGroupInventory(p, true);
        pe.setId(tpe.getId());
        pe.setCustomName(tpe.getCustomName());
        pe.setGroup(tpe.getGroup());
        pe.setHomes(tpe.getHomes());
        pe.setPurse(tpe.getPurse());
        p.setCustomName(tpe.getGroup().getPrefix() + target.getName());
        if (tpe.getCustomName() != null) {
            p.setCustomName(tpe.getGroup().getPrefix() + tpe.getCustomName());
        }
        WorldHelper.loadWorldGroupInventory(p);
        p.sendMessage(String.format(Constants.PLUGIN_COMMAND_SUDO_ACTIVATED, tpe.getGroup().getPrefix() + target.getName()));

        return true;
    }

    private void dispatchCommand(String[] args){
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.getServer().dispatchCommand(console, StringUtils.toString(args));
    }

    public static void exitSudo(@NotNull Player p){
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

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("admin").getId())) {
            return tabList;
        }

        if (!isPlayer(commandSender)){
            return tabList;
        }

        if(strings.length == 1){
            tabList.addAll(TabCompleterHelper.getPluginCommands());
            tabList.addAll(TabCompleterHelper.getOnlinePlayers());
            return tabList;
        }

        if(strings.length == 2){
            tabList.addAll(TabCompleterHelper.getOnlinePlayers());
            return tabList;
        }


        return tabList;
    }
}