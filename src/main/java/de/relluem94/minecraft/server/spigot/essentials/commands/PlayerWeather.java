package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("playerweather")
public class PlayerWeather implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!isPlayer(commandSender)) {
            commandSender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) commandSender;

        if (!Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
            sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (strings.length == 0) {
            p.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
            return true;
        }

        if(strings.length > 1){
            p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;
        }

        if(isValidWeatherType(strings[0])){
            p.setPlayerWeather(WeatherType.valueOf(strings[0].toUpperCase()));
            return true;
        }

        p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!isPlayer(commandSender)) {
            return tabList;
        }

        Player p = (Player) commandSender;

        if (!Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
            return tabList;
        }

        if (strings.length == 1){
            tabList.addAll(TabCompleterHelper.getWeatherTypes());
            return tabList;
        }

        return tabList;
    }

    private boolean isValidWeatherType(String input){
        return Arrays.stream(WeatherType.values())
                .filter(env -> env.name().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null) != null;
    }
}
