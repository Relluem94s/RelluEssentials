package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.SubCommandRegistry;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.*;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
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
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("admin")
public class Admin implements CommandConstruct {
    private final SubCommandRegistry<SubCommand> subCommandRegistry;

    public Admin() {
        this.subCommandRegistry = new SubCommandRegistry<>(List.of(
                new AdminToolsGUICommand(),
                new CleanUpChatCommand(),
                new CleanUpLocationsCommand(),
                new CleanUpProtectionsCommand(),
                new FakeAFKCommand(),
                new LightToggleCommand(),
                new NPCGUICommand(),
                new PingCommand(),
                new PluginInfoCommand(),
                new TopCommand()
        ));
    }

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
        }

        SubCommand subCommand = subCommandRegistry.find(args);
        if (subCommand == null) {
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
            return true;
        }

        subCommand.execute(p, args);
        return true;
    }
}