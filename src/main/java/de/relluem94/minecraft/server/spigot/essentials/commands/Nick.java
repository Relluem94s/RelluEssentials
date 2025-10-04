package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@CommandName("nick")
public class Nick implements CommandConstruct {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String @NotNull [] args) {

        if (args.length < 2) {
           sender.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
           return true;
        }

        if (args.length > 2) {
            sender.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;
        }

        if (!Permission.isAuthorized(sender, Groups.getGroup("admin").getId())) {
            sender.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
            return true;
        }

        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(target.getUniqueId());
        pe.setCustomName(args[1]);
        pe.setUpdatedBy(isPlayer(sender) ? RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(((Player)sender).getUniqueId()).getId() : 1);
        pe.setHasToBeUpdated(true);
        target.setCustomName(pe.getGroup().getPrefix() + args[1]);
        target.setPlayerListName(pe.getGroup().getPrefix() + args[1]);
        sender.sendMessage(String.format(PLUGIN_COMMAND_NICK, pe.getGroup().getPrefix() + target.getName()));
        return true;
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

        if (isPlayer(commandSender)) {
            return tabList;
        }

        if(strings.length > 1){
            return tabList;
        }

        tabList.addAll(TabCompleterHelper.getOnlinePlayers());

        return tabList;
    }
}