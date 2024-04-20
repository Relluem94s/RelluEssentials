package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class ProtectTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("user").getId())) {
            return tabList;
        }

        if (!isPlayer(commandSender)) {
            return tabList;
        }

        switch (strings.length){
            case 1:
                tabList.add(PLUGIN_COMMAND_NAME_PROTECT_ADD);
                tabList.add(PLUGIN_COMMAND_NAME_PROTECT_REMOVE);
                tabList.add(PLUGIN_COMMAND_NAME_PROTECT_FLAG);
                tabList.add(PLUGIN_COMMAND_NAME_PROTECT_RIGHT);
                tabList.add(PLUGIN_COMMAND_NAME_PROTECT_INFO);
                break;
            case 2:
                if (strings[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_PROTECT_FLAG)) {
                    tabList.add(PLUGIN_COMMAND_NAME_PROTECT_FLAG_ADD);
                    tabList.add(PLUGIN_COMMAND_NAME_PROTECT_FLAG_REMOVE);
                }
                else if (strings[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_PROTECT_RIGHT)) {
                    tabList.add(PLUGIN_COMMAND_NAME_PROTECT_RIGHT_ADD);
                    tabList.add(PLUGIN_COMMAND_NAME_PROTECT_RIGHT_REMOVE);
                }
                break;
            case 3:
                if (strings[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_PROTECT_FLAG) && (strings[1].equalsIgnoreCase(PLUGIN_COMMAND_NAME_PROTECT_FLAG_ADD) || strings[1].equalsIgnoreCase(PLUGIN_COMMAND_NAME_PROTECT_FLAG_REMOVE))) {
                    tabList.addAll(TabCompleterHelper.getProtectionFlags());
                }
                else if (strings[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_PROTECT_RIGHT) && (strings[1].equalsIgnoreCase(PLUGIN_COMMAND_NAME_PROTECT_RIGHT_ADD) || strings[1].equalsIgnoreCase(PLUGIN_COMMAND_NAME_PROTECT_RIGHT_REMOVE))) {
                    tabList.addAll(TabCompleterHelper.getOnlinePlayers());
                }
                break;
            default:
                break;
        }

        return tabList;
    }
}
