package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.reply;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.msg;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("r")
public class Reply implements CommandConstruct {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
            return true;
        }

        if (!reply.containsKey(p)) {
            p.sendMessage(PLUGIN_COMMAND_MSG_NO_ONE_TO_REPLY);
            return true;
        }

        Player target = reply.get(p);

        if (target == null) {
            p.sendMessage(PLUGIN_COMMAND_MSG_NO_ONE_TO_REPLY);
            return true;
        }

        if (!target.isOnline()) {
            p.sendMessage(PLUGIN_COMMAND_MSG_PLAYER_OFFLINE);
            return true;
        }

        msg(sender, target, args, 0);
        return true;
    }


    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>();
    }
}
