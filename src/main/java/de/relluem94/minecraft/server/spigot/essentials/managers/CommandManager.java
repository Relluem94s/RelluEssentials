package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_COMMANDS_REGISTERED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_REGISTER_COMMANDS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_AFK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_BACK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_BAGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_BROADCAST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_COOCKIE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_CRAFT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_CUSTOMHEADS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_DAY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_ENDERCHEST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_EXIT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_FLY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_0;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_2;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_3;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMERULES;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GOD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HEAD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HEAL;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HOME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_INVENTORY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_MARRY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_MORE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_MSG;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_NICK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_NIGHT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_PLAYERINFO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_POKE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_PRINT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_PROTECT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_PURSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_RAIN;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_RENAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_REPAIR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_REPLY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_ROLLBACK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SETGROUP;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SIGN;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SPAWN;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SPEED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_STORM;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SUDO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SUICIDE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SUN;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEAM;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TELEPORT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TEST_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_TITLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_VANISH;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WARP;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WHERE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WORLD;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import java.util.Objects;

import de.relluem94.minecraft.server.spigot.essentials.commands.*;
import de.relluem94.minecraft.server.spigot.essentials.constants.commands.AdminCommand;
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
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_COOCKIE)).setExecutor(new Cookies());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_CRAFT)).setExecutor(new PortableCraftingBench());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_SUN)).setExecutor(new Sun());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_RAIN)).setExecutor(new Rain());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_STORM)).setExecutor(new Storm());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_SPAWN)).setExecutor(new Spawn());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_HOME)).setExecutor(new Home());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_DAY)).setExecutor(new Day());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_NIGHT)).setExecutor(new Night());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_MORE)).setExecutor(new More());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_REPAIR)).setExecutor(new Repair());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_ENDERCHEST)).setExecutor(new Enderchest());
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
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_AFK)).setExecutor(new AFK());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_MSG)).setExecutor(new Message());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_REPLY)).setExecutor(new Message());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_TITLE)).setExecutor(new Title());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_WHERE)).setExecutor(new Where());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_PRINT)).setExecutor(new Print());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_BROADCAST)).setExecutor(new Broadcast());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_RENAME)).setExecutor(new Rename());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_SPEED)).setExecutor(new Speed());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_POKE)).setExecutor(new Poke());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_WORLD)).setExecutor(new Worlds());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_ROLLBACK)).setExecutor(new Rollback());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_TEST_COMMAND)).setExecutor(new TestCommand());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_PROTECT)).setExecutor(new Protect());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_SIGN)).setExecutor(new Sign());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_BAGS)).setExecutor(new Bags());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_SUDO)).setExecutor(new Sudo());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_EXIT)).setExecutor(new Exit());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_TELEPORT)).setExecutor(new Teleport());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_BACK)).setExecutor(new Back());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_WARP)).setExecutor(new Warp());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_MARRY)).setExecutor(new Marry());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_CUSTOMHEADS)).setExecutor(new CustomHead());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_PLAYERINFO)).setExecutor(new PlayerInfo());
        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_TEAM)).setExecutor(new Team());

        Objects.requireNonNull(RelluEssentials.getInstance().getCommand(PLUGIN_COMMAND_NAME_PROTECT)).setTabCompleter(new ProtectTabCompleter());

        int commands = PluginCommandYamlParser.parse(RelluEssentials.getInstance()).size();
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + String.format(PLUGIN_MANAGER_COMMANDS_REGISTERED, commands));
    }
}