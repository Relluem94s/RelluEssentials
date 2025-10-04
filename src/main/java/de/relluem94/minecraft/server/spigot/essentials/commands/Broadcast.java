package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_BROADCAST_INFO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_SPACER_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_BROADCAST;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.rellulib.utils.StringUtils.implode;
import static de.relluem94.rellulib.utils.StringUtils.replaceSymbols;

import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@CommandName("broadcast")
public class Broadcast implements CommandConstruct {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            return tabList;
        }

        if(strings.length > 1){
            return tabList;
        }

        tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));

        return tabList;
    }

    @Override
    public CommandsEnum[] getCommands() {
        return Admin.Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum {

        TITLE("title"),
        CHAT("chat");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String @NotNull [] args) {
        if (args.length < 1) {
            sendMessage(sender, PLUGIN_COMMAND_BROADCAST_INFO);
            return true;
        }

        if (!Permission.isAuthorized(sender, Groups.getGroup("mod").getId())) {
            sendMessage(sender, PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args[0].equalsIgnoreCase(Commands.TITLE.getName())) {
            broadcast(args, 1, false);
            return true;
        }
        else if(args[0].equalsIgnoreCase(Commands.CHAT.getName())) {
            broadcast(args, 1, true);
            return true;
        }

        broadcast(args, 0, true);
        return true;
    }

    private void broadcast(String[] args, int start, boolean chat) {
        String message = implode(start, args);
        message = replaceSymbols(replaceColor(message));

        if (chat) {
            Bukkit.broadcastMessage(PLUGIN_NAME_BROADCAST + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_COLOR_MESSAGE + message);
            return;
        }

        for (Player op : Bukkit.getOnlinePlayers()) {
            op.sendTitle(PLUGIN_NAME_BROADCAST, message, 5, 80, 5);
        }
    }
}