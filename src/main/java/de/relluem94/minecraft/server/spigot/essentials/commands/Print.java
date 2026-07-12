package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.*;
import static de.relluem94.rellulib.utils.StringUtils.implode;
import static de.relluem94.rellulib.utils.StringUtils.replaceSymbols;

@CommandName("print")
public class Print implements CommandConstruct {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String @NotNull [] args) {
        Player targetedPlayerBySelector = null;

        if (args.length < 1) {
            sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PRINT_INFO));
            return true;
        }
   
        String name = null;

        if (isCMDBlock(sender)) {
            BlockCommandSender bcs = (BlockCommandSender) sender;
            name = PLUGIN_COLOR_COMMAND_BLOCK + bcs.getName();

            List<String> argsList = Arrays.asList(args);
            if(argsList.contains("@p")){
                CommandBlock cb = (CommandBlock) bcs.getBlock().getState();
                targetedPlayerBySelector = PlayerHelper.getTargetedPlayer(cb.getBlock().getLocation());
                if(targetedPlayerBySelector == null){
                    sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, languageHelper.get(MessageKey.COMMAND_NO_PLAYER_IN_REACH)));
                    return true;
                }
            }
        }

        if (isConsole(sender)) {
            ConsoleCommandSender ccs = (ConsoleCommandSender) sender;
            name = PLUGIN_COLOR_CONSOLE + StringHelper.firstCharToUpper(ccs.getName().toLowerCase());
        }

        if (isPlayer(sender)) {
            Player p = (Player) sender;

            if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
                return true;
            }

            name = p.getCustomName();
        }

        if(name == null){
            sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_INVALID));
            return true;
        }

        String message = implode(0, args);
        message = replaceSymbols(replaceColor(message));

        if(targetedPlayerBySelector != null){
            message = message.replace("@p", Objects.requireNonNull(targetedPlayerBySelector.getCustomName()));
        }

        Bukkit.broadcastMessage(name + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_COLOR_MESSAGE + message);
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