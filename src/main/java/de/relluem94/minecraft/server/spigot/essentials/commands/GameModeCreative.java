package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.getText;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("1")
public class GameModeCreative implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    private static final String LANG_KEY = "PLUGIN_COMMAND_GAMEMODE";

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (!Permission.isAuthorized(sender, Groups.getGroup("mod").getId())) {
            sender.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
                return true;
            }

            gameMode(target);
            return true;
        }

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        gameMode((Player) sender);
        return true;
    }

    private void gameMode(@NotNull Player p) {
        p.setGameMode(org.bukkit.GameMode.CREATIVE);
        p.sendMessage(PLUGIN_FORMS_COMMAND_PREFIX + String.format(getText(p.getLocale(), LANG_KEY), p.getCustomName() + PLUGIN_COLOR_COMMAND, PLUGIN_COLOR_COMMAND_NAME + PLUGIN_COMMAND_NAME_GAME_MODE_1_NAME + PLUGIN_COLOR_COMMAND));
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
