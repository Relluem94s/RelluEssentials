package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("more")
public class More implements CommandConstruct {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 0) {
            p.getInventory().getItemInMainHand().setAmount(64);
            p.sendMessage(String.format(PLUGIN_COMMAND_MORE, p.getInventory().getItemInMainHand().getType()));
            return true;
        }

        if (args.length > 1) {
            p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;
        }


        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            p.sendMessage(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER);
            return true;
        }

        target.getInventory().getItemInMainHand().setAmount(64);
        p.sendMessage(String.format(PLUGIN_COMMAND_MORE, target.getInventory().getItemInMainHand().getType()));
        target.sendMessage(String.format(PLUGIN_COMMAND_MORE_PLAYER, target.getInventory().getItemInMainHand().getType()));
        return true;
    }

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            return new ArrayList<>();
        }

        if(strings.length > 1){
            return new ArrayList<>();
        }

        return TabCompleterHelper.getOnlinePlayers();
    }
}
