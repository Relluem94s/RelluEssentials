package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.rellulib.stores.DoubleStore;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.locationToString;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("position")
public class Position implements CommandConstruct {
    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum {

        SET("set", "first", "second"),
        REMOVE("remove", "first", "second"),
        SHIFT("shift"),
        EXPAND("expand"),
        DECREASE("decrease"),
        CLEAR("clear");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())){
            commandSender.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if(!isPlayer(commandSender)){
            commandSender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) commandSender;

        if(strings.length == 0){
            if(!RelluEssentials.getInstance().postion.containsKey(p)){
                p.sendMessage(PLUGIN_COMMAND_POSITION_INFO_NO_POSITIONS);
                return true;
            }

            p.sendMessage(PLUGIN_COMMAND_POSITION_INFO_1);

            Location first = RelluEssentials.getInstance().postion.get(p).getValue();
            Location second = RelluEssentials.getInstance().postion.get(p).getSecondValue();

            String firstLocationString = first == null ? PLUGIN_COMMAND_POSITION_LOCATION_NOT_AVAILIBLE : StringHelper.locationToString(first);
            String secondLocationString = second == null ? PLUGIN_COMMAND_POSITION_LOCATION_NOT_AVAILIBLE : StringHelper.locationToString(second);

            p.sendMessage(String.format(PLUGIN_COMMAND_POSITION_INFO_2, firstLocationString));
            p.sendMessage(String.format(PLUGIN_COMMAND_POSITION_INFO_3, secondLocationString));
            return true;
        }

        if(Commands.CLEAR.getName().equalsIgnoreCase(strings[0])){
            RelluEssentials.getInstance().postion.put(p, new DoubleStore<>(null, null));
            p.sendMessage(PLUGIN_COMMAND_POSITION_CLEAR);
            return true;
        }

        if(strings.length != 2){
            p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
            return true;
        }

        if(strings[0].equalsIgnoreCase(Commands.SET.getName())){
            if(!RelluEssentials.getInstance().postion.containsKey(p)){
                RelluEssentials.getInstance().postion.put(p, new DoubleStore<>(null, null));
            }

            Location location = PlayerHelper.getLookingLocation(p, 100);

            if(strings[1].equalsIgnoreCase(Commands.SET.getSubCommands()[0])){
                RelluEssentials.getInstance().postion.get(p).setValue(location);
                p.sendMessage(String.format(PLUGIN_COMMAND_POSITION_SET_FIRST, locationToString(location)));
            }
            else{
                RelluEssentials.getInstance().postion.get(p).setSecondValue(location);
                p.sendMessage(String.format(PLUGIN_COMMAND_POSITION_SET_SECOND, locationToString(location)));
            }
        }



        if(strings[0].equalsIgnoreCase(Commands.REMOVE.getName())){
            if(!RelluEssentials.getInstance().postion.containsKey(p)){
                RelluEssentials.getInstance().postion.put(p, new DoubleStore<>(null, null));
                return true;
            }

            if(strings[1].equalsIgnoreCase(Commands.REMOVE.getSubCommands()[0])){
                RelluEssentials.getInstance().postion.get(p).setValue(null);
                p.sendMessage(PLUGIN_COMMAND_POSITION_REMOVE_FIRST);
            }
            else{
                RelluEssentials.getInstance().postion.get(p).setSecondValue(null);
                p.sendMessage(PLUGIN_COMMAND_POSITION_REMOVE_SECOND);
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if(!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())){
            return tabList;
        }

        if(strings.length == 1){
            tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
            return tabList;
        }

        if(strings.length == 2){
            if(strings[0].equalsIgnoreCase(Commands.SET.getName())){
                tabList.addAll(List.of(Commands.SET.getSubCommands()));
            } else if(strings[0].equalsIgnoreCase(Commands.REMOVE.getName())){
                tabList.addAll(List.of(Commands.REMOVE.getSubCommands()));
            }
            return tabList;
        }

        return tabList;
    }
}
