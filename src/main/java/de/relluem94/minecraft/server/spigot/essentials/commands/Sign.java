package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AnnotationHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("sign")
public class Sign implements CommandConstruct {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(String.format(PLUGIN_COMMAND_SIGN_INFO, AnnotationHelper.getCommandName(this.getClass()), Commands.COPY.getName(), Commands.EDIT.getName()));
        }

        if (args.length > 1) {
            p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;
        }

        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);

        if(args[0].equalsIgnoreCase(Commands.EDIT.getName())){
            pe.setPlayerState(PlayerState.SIGN_EDIT);
            p.sendMessage(PLUGIN_COMMAND_SIGN_EDIT);
        }
        else if(args[0].equalsIgnoreCase(Commands.COPY.getName())){
            pe.setPlayerState(PlayerState.SIGN_COPY);
            p.sendMessage(PLUGIN_COMMAND_SIGN_COPY);
        }

        return true;
    }

    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum {

        EDIT("edit"),
        COPY("copy");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("user").getId())) {
            return tabList;
        }

        if (!isPlayer(commandSender)) {
            return tabList;
        }

        if(strings.length == 1){
            tabList.addAll(TabCompleterHelper.getCommands(getCommands()));
        }

        return tabList;
    }
}
