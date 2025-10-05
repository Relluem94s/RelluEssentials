package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.*;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@CommandName("spawn")
public class Spawn implements CommandConstruct {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            return tabList;
        }

        if(strings.length > 1){
            return tabList;
        }

        if (isPlayer(commandSender) || isConsole(commandSender)) {
            tabList.addAll(TabCompleterHelper.getOnlinePlayers());
            return tabList;
        }

        return tabList;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String @NotNull [] args) {
        if (isCMDBlock(sender) && args.length == 1 && args[0].equals("@p")) {
            BlockCommandSender bcs = (BlockCommandSender) sender;
            CommandBlock cb = (CommandBlock) bcs.getBlock().getState();
            Player p = PlayerHelper.getTargetedPlayer(cb.getBlock().getLocation());
            if(p == null){
                sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, "No Player in Reach"));
                return true;
            }

            spawn(p);
            return true;
        }

        if(args.length > 1){
            sender.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (!Permission.isAuthorized(sender, Groups.getGroup("mod").getId())) {
                sender.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }

            if (target == null) {
                sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
                return true;
            }

            spawn(target);
            return true;
        } 

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;
            
        if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        spawn(p);
        return true;
    }

    public void spawn(Player p){
        Back.addBackPoint(p);
        p.teleport(p.getWorld().getSpawnLocation());
        p.sendMessage(String.format(PLUGIN_COMMAND_SPAWN, p.getWorld().getName()));
    }

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }
}
