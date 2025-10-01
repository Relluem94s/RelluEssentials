package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_COMMANDS_REGISTERED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_REGISTER_COMMANDS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import java.util.Objects;

import de.relluem94.minecraft.server.spigot.essentials.commands.*;
import de.relluem94.minecraft.server.spigot.essentials.constants.commands.*;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AnnotationHelper;
import org.bukkit.command.PluginCommandYamlParser;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

public class CommandManager implements IEnable {

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + PLUGIN_MANAGER_REGISTER_COMMANDS);

        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_GAMEMODE_0)).setExecutor(new GameMode());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_GAMEMODE_1)).setExecutor(new GameMode());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_GAMEMODE_2)).setExecutor(new GameMode());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_GAMEMODE_3)).setExecutor(new GameMode());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_FLY)).setExecutor(new Fly());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_PURSE)).setExecutor(new Purse());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(Objects.requireNonNull(AnnotationHelper.getCommandName(CookieCommand.class)))).setExecutor(new Cookies());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_CRAFT)).setExecutor(new PortableCraftingBench());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_SUN)).setExecutor(new Sun());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_RAIN)).setExecutor(new Rain());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_STORM)).setExecutor(new Storm());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_SPAWN)).setExecutor(new Spawn());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_HOME)).setExecutor(new Home());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(Objects.requireNonNull(AnnotationHelper.getCommandName(DayCommand.class)))).setExecutor(new Day());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_NIGHT)).setExecutor(new Night());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_MORE)).setExecutor(new More());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_REPAIR)).setExecutor(new Repair());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(Objects.requireNonNull(AnnotationHelper.getCommandName(EnderchestCommand.class)))).setExecutor(new Enderchest());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_INVENTORY)).setExecutor(new Inventory());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_SETGROUP)).setExecutor(new PermissionsGroup());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_NICK)).setExecutor(new Nick());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_SUICIDE)).setExecutor(new Suicide());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_HEAL)).setExecutor(new Heal());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_GOD)).setExecutor(new God());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(Objects.requireNonNull(AnnotationHelper.getCommandName(AdminCommand.class)))).setExecutor(new Admin());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_GAMERULES)).setExecutor(new GameRules());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_HEAD)).setExecutor(new Head());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_VANISH)).setExecutor(new Vanish());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(Objects.requireNonNull(AnnotationHelper.getCommandName(AfkCommand.class)))).setExecutor(new AFK());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_MSG)).setExecutor(new Message());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_REPLY)).setExecutor(new Message());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_TITLE)).setExecutor(new Title());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_WHERE)).setExecutor(new Where());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_PRINT)).setExecutor(new Print());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(Objects.requireNonNull(AnnotationHelper.getCommandName(BroadcastCommand.class)))).setExecutor(new Broadcast());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_RENAME)).setExecutor(new Rename());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_SPEED)).setExecutor(new Speed());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_POKE)).setExecutor(new Poke());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_WORLD)).setExecutor(new Worlds());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_ROLLBACK)).setExecutor(new Rollback());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_TEST_COMMAND)).setExecutor(new TestCommand());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_PROTECT)).setExecutor(new Protect());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_SIGN)).setExecutor(new Sign());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand((Objects.requireNonNull(AnnotationHelper.getCommandName(BagsCommand.class))))).setExecutor(new Bags());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_SUDO)).setExecutor(new Sudo());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_EXIT)).setExecutor(new Exit());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_TELEPORT)).setExecutor(new Teleport());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(Objects.requireNonNull(AnnotationHelper.getCommandName(BackCommand.class)))).setExecutor(new Back());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_WARP)).setExecutor(new Warp());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_MARRY)).setExecutor(new Marry());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(Objects.requireNonNull(AnnotationHelper.getCommandName(CustomHeadsCommand.class)))).setExecutor(new CustomHead());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_PLAYERINFO)).setExecutor(new PlayerInfo());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_TEAM)).setExecutor(new Team());

        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_PROTECT)).setTabCompleter(new ProtectTabCompleter());

        int commands = PluginCommandYamlParser.parse(RelluEssentials.getInstance()).size();
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + String.format(PLUGIN_MANAGER_COMMANDS_REGISTERED, commands));
    }
}