package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.commands.admin.AdminToolsGUI.adminToolsGUI;
import static de.relluem94.minecraft.server.spigot.essentials.commands.admin.CleanUpChat.cleanChat;
import static de.relluem94.minecraft.server.spigot.essentials.commands.admin.CleanUpLocations.cleanUpLocations;
import static de.relluem94.minecraft.server.spigot.essentials.commands.admin.CleanUpProtections.cleanUpProtections;
import static de.relluem94.minecraft.server.spigot.essentials.commands.admin.FakeAFK.fakeAFK;
import static de.relluem94.minecraft.server.spigot.essentials.commands.admin.LightToggle.lightToggle;
import static de.relluem94.minecraft.server.spigot.essentials.commands.admin.NPCGUI.npcGUI;
import static de.relluem94.minecraft.server.spigot.essentials.commands.admin.Ping.ping;
import static de.relluem94.minecraft.server.spigot.essentials.commands.admin.PluginInfo.pluginInfo;
import static de.relluem94.minecraft.server.spigot.essentials.commands.admin.Top.top;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("admin")
public class Admin implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();
        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            return tabList;
        }
        if (!isPlayer(commandSender)) {
            return tabList;
        }
        if (strings.length == 1) {
            tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
            return tabList;
        }
        if (strings.length == 2) {
            if (Commands.PING.getName().equalsIgnoreCase(strings[0])) {
                tabList.addAll(TabCompleterHelper.getOnlinePlayers());
            }
            return tabList;
        }
        return tabList;
    }

    @Getter
    public enum Commands implements CommandsEnum {

        AFK("afk"),
        CLEAN_PROTECTIONS("cleanProtections"),
        CLEAN_LOCATIONS("cleanLocations"),
        CHAT("chat"),
        INFO("info"),
        LIGHT("light"),
        NPC("npc"),
        PING("ping"),
        TOP("top"),
        ADMIN_TOOLS("adminTools");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
            return true;
        }
        Player p = (Player) sender;
        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_INFO));
            return true;
        } else if (args.length == 1) {
            if (Commands.NPC.getName().equalsIgnoreCase(args[0])) {
                if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                    p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
                    return true;
                }
                npcGUI(sender);
                return true;
            } else if (Commands.INFO.getName().equalsIgnoreCase(args[0])) {
                pluginInfo(p);
                return true;
            } else if (Commands.ADMIN_TOOLS.getName().equalsIgnoreCase(args[0])) {
                if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                    p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
                    return true;
                }
                adminToolsGUI(sender);
                return true;
            } else if (Commands.PING.getName().equalsIgnoreCase(args[0])) {
                p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_PING, p.getPing()));
                return true;
            } else if (Commands.CHAT.getName().equalsIgnoreCase(args[0])) {
                cleanChat(p);
                return true;
            } else if (Commands.LIGHT.getName().equalsIgnoreCase(args[0])) {
                lightToggle(p);
                return true;
            } else if (Commands.CLEAN_PROTECTIONS.getName().equalsIgnoreCase(args[0])) {
                if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                    p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
                    return true;
                }
                cleanUpProtections(p);
                return true;
            } else if (Commands.CLEAN_LOCATIONS.getName().equalsIgnoreCase(args[0])) {
                if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                    p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
                    return true;
                }
                cleanUpLocations(p);
                return true;
            } else if (Commands.AFK.getName().equalsIgnoreCase(args[0])) {
                fakeAFK(p);
                return true;
            } else if (Commands.TOP.getName().equalsIgnoreCase(args[0])) {
                top(p);
                return true;
            } else {
                p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
                return true;
            }
        } else if (args.length == 2) {
            if (Commands.PING.getName().equalsIgnoreCase(args[0])) {
                ping(args, p);
            }
            return true;
        } else {
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
            return true;
        }
    }
}