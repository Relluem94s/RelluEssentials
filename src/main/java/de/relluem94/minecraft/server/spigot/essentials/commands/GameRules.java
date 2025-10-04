package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_NEGATIVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_POSITIVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_GAMERULES;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_LESS_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_MANY_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WORLD_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CommandName("gamerules")
public class GameRules implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {

        if (isCMDBlock(sender) || isConsole(sender)) {
            if (args.length < 1) {
                sender.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
                return true;   
            }

            showGameRulesForWorld(sender, args[0]);
            
            return true;
        }


        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;   
        }

        if(args.length == 0){
            showGameRule(sender, p.getWorld());
            return true;
        }

        if (args.length > 1) {
            p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;   
        }

        showGameRulesForWorld(sender, args[0]);
        return true;
    }

    private void showGameRule(CommandSender sender, @NotNull World world) {
        String[] gameRules = world.getGameRules();
        sendMessage(sender, String.format(PLUGIN_COMMAND_GAMERULES, world.getName()));
        for (String gameRule : gameRules) {
            Object value = world.getGameRuleValue(Objects.requireNonNull(GameRule.getByName(gameRule)));
            String color;
            if (value instanceof Boolean) {
                color = ((boolean)value ? PLUGIN_COLOR_POSITIVE : PLUGIN_COLOR_NEGATIVE);
            } else {
                color = "§7";
            }

            sendMessage(sender, "        §d" + gameRule + "§f = " + color + value);
        }
    }

    private void showGameRulesForWorld(CommandSender sender, String name){
        World world = Bukkit.getWorld(name);
        if (world == null) {
            sender.sendMessage(String.format(PLUGIN_COMMAND_WORLD_NOT_FOUND, name));
            return;
        }

        showGameRule(sender, world);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            return new ArrayList<>();
        }

        if(strings.length > 1){
            return new ArrayList<>();
        }

        return TabCompleterHelper.getWorlds();
    }
}
