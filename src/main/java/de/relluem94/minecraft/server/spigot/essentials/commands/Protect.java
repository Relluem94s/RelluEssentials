package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

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

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.constants.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.OfflinePlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@CommandName("protect")
public class Protect implements CommandConstruct {

    private @NotNull String getFlags() {
        ProtectionFlags[] flags = ProtectionFlags.values();

        StringBuilder sb = new StringBuilder();
        sb.append("Available Flags: ");
        for (ProtectionFlags flag : flags) {
            sb.append(flag.name()).append(" ");
        }
        return sb.toString();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(sender, Groups.getGroup("user").getId())) {
            sender.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());

        if (args.length == 0) {
            p.sendMessage(String.format(PLUGIN_COMMAND_PROTECT_COMMAND_INFO, AnnotationHelper.getCommandName(this.getClass()), Commands.ADD.getName(), Commands.REMOVE.getName(), Commands.FLAG.getName(), Commands.RIGHT.getName(), Commands.INFO.getName()));
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase(Commands.ADD.getName())) {
                p.sendMessage(PLUGIN_COMMAND_PROTECT_ADD);
                pe.setPlayerState(PlayerState.PROTECTION_ADD);
            } else if (args[0].equalsIgnoreCase(Commands.REMOVE.getName())) {
                p.sendMessage(PLUGIN_COMMAND_PROTECT_REMOVE);
                pe.setPlayerState(PlayerState.PROTECTION_REMOVE);
            } else if (args[0].equalsIgnoreCase(Commands.FLAG.getName())) {
                p.sendMessage(String.format(PLUGIN_COMMAND_PROTECT_FLAG, AnnotationHelper.getCommandName(this.getClass()), Commands.FLAG.getName(), Commands.FLAG.getSubCommands()[1], Commands.FLAG.getSubCommands()[0]));
            } else if (args[0].equalsIgnoreCase(Commands.RIGHT.getName())) {
                p.sendMessage(String.format(PLUGIN_COMMAND_PROTECT_RIGHT, AnnotationHelper.getCommandName(this.getClass()), Commands.RIGHT.getName(), Commands.RIGHT.getSubCommands()[1], Commands.RIGHT.getSubCommands()[0]));
            } else if (args[0].equalsIgnoreCase(Commands.INFO.getName())) {
                pe.setPlayerState(PlayerState.PROTECTION_INFO);
                p.sendMessage(PLUGIN_COMMAND_PROTECT_INFO);
            } else {
                p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase(Commands.FLAG.getName())) {
                if (args[1].equalsIgnoreCase(Commands.FLAG.getSubCommands()[0])) {
                    try {
                        if (ProtectionFlags.valueOf(args[2].toUpperCase()) != null) {
                            p.sendMessage(PLUGIN_COMMAND_PROTECT_FLAG_ADD);
                            pe.setPlayerState(PlayerState.PROTECTION_FLAG_ADD);
                            pe.setPlayerStateParameter(ProtectionFlags.valueOf(args[2].toUpperCase()).name());
                        }
                    } catch (IllegalArgumentException ex) {
                        p.sendMessage(PLUGIN_COMMAND_PROTECT_FLAG_NOT_FOUND);
                        p.sendMessage(getFlags());
                    }
                } else if (args[1].equalsIgnoreCase(Commands.FLAG.getSubCommands()[1])) {
                    try {
                        if (ProtectionFlags.valueOf(args[2].toUpperCase()) != null) {
                            p.sendMessage(PLUGIN_COMMAND_PROTECT_FLAG_REMOVE);
                            pe.setPlayerState(PlayerState.PROTECTION_FLAG_REMOVE);
                            pe.setPlayerStateParameter(ProtectionFlags.valueOf(args[2].toUpperCase()).name());
                        }
                    } catch (IllegalArgumentException ex) {
                        p.sendMessage(PLUGIN_COMMAND_PROTECT_FLAG_NOT_FOUND);
                        p.sendMessage(getFlags());
                    }
                } else {
                    p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
                }
            } else if (args[0].equalsIgnoreCase(Commands.RIGHT.getName())) {
                if (args[1].equalsIgnoreCase(Commands.RIGHT.getSubCommands()[0])) {
                    OfflinePlayerEntry player = PlayerHelper.getOfflinePlayerByName(args[2]);

                    if (player != null) {
                        p.sendMessage(PLUGIN_COMMAND_PROTECT_RIGHT_ADD);
                        pe.setPlayerState(PlayerState.PROTECTION_RIGHT_ADD);
                        pe.setPlayerStateParameter(player.getId().toString());
                    } else {
                        p.sendMessage(String.format(PLUGIN_COMMAND_PROTECT_RIGHT_PLAYER_NOTFOUND, args[2]));
                    }
                } else if (args[1].equalsIgnoreCase(Commands.RIGHT.getSubCommands()[1])) {
                    OfflinePlayerEntry player = PlayerHelper.getOfflinePlayerByName(args[2]);

                    if (player != null) {
                        p.sendMessage(PLUGIN_COMMAND_PROTECT_RIGHT_REMOVE);
                        pe.setPlayerState(PlayerState.PROTECTION_RIGHT_REMOVE);
                        pe.setPlayerStateParameter(player.getId().toString());
                    } else {
                        p.sendMessage(String.format(PLUGIN_COMMAND_PROTECT_RIGHT_PLAYER_NOTFOUND, args[2]));
                    }
                } else {
                    p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
                }
            } else {
                p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
            }
        } else {
            p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
        }
        return true;

    }

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
                tabList.addAll(TabCompleterHelper.getCommands(getCommands()));
                break;
            case 2:
                if (strings[0].equalsIgnoreCase(Commands.FLAG.getName())) {
                    tabList.addAll(List.of(Commands.FLAG.subCommands));
                }
                else if (strings[0].equalsIgnoreCase(Commands.RIGHT.getName())) {
                    tabList.addAll(List.of(Commands.RIGHT.subCommands));
                }
                break;
            case 3:
                if (strings[0].equalsIgnoreCase(Commands.FLAG.getName()) && (strings[1].equalsIgnoreCase(Commands.FLAG.getSubCommands()[0]) || strings[1].equalsIgnoreCase(Commands.FLAG.getSubCommands()[1]))) {
                    tabList.addAll(TabCompleterHelper.getProtectionFlags());
                }
                else if (strings[0].equalsIgnoreCase(Commands.RIGHT.getName()) && (strings[1].equalsIgnoreCase(Commands.RIGHT.getSubCommands()[0]) || strings[1].equalsIgnoreCase(Commands.RIGHT.getSubCommands()[1]))) {
                    tabList.addAll(TabCompleterHelper.getOnlinePlayers());
                }
                break;
            default:
                break;
        }

        return tabList;
    }

    @Getter
    public enum Commands implements CommandsEnum {

        ADD("add"),
        REMOVE("remove"),
        INFO("info"),
        FLAG("flag", "add", "remove"),
        RIGHT("right", "add", "remove");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }
}