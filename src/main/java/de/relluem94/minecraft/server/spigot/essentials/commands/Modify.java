package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.SubCommandRegistry;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.*;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.SelectionResolver;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("modify")
public class Modify implements CommandConstruct {

    public static final int BLOCKS_PER_TICK = 64;
    public static final int MAX_RADIUS = 128;
    public static final int MAX_ITERATIONS = 1048576;

    private final SubCommandRegistry<SubCommand> subCommandRegistry;

    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum {

        SET("set"),
        REPLACE("replace"),
        MOVE("move"),
        COPY("copy"),
        CUT("cut"),
        PASTE("paste"),
        CLIPBOARD("clipboard", "rotate"),
        UNDO("undo"),
        WALL("wall"),
        CYLINDER("cylinder"),
        FILL("fill"),
        FILLR("fillr"),
        PLANT("plant");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    public Modify() {
        SelectionResolver selectionResolver = new SelectionResolver();
        UndoHistoryManager undoHistoryManager = new UndoHistoryManager();

        this.subCommandRegistry = new SubCommandRegistry<>(List.of(
                new CopyCommand(false, BLOCKS_PER_TICK, selectionResolver, undoHistoryManager),
                new CopyCommand(true, BLOCKS_PER_TICK, selectionResolver, undoHistoryManager),
                new CylinderCommand(BLOCKS_PER_TICK, selectionResolver, undoHistoryManager),
                new FillCommand(false, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS, undoHistoryManager),
                new FillCommand(true, BLOCKS_PER_TICK, MAX_RADIUS, MAX_ITERATIONS, undoHistoryManager),
                new ClipboardCommand(),
                new MoveCommand(BLOCKS_PER_TICK, selectionResolver, undoHistoryManager),
                new PasteCommand(BLOCKS_PER_TICK, undoHistoryManager),
                new PlantCommand(BLOCKS_PER_TICK, selectionResolver, undoHistoryManager),
                new ReplaceCommand(BLOCKS_PER_TICK, selectionResolver, undoHistoryManager),
                new SetCommand(BLOCKS_PER_TICK, selectionResolver, undoHistoryManager),
                new UndoCommand(BLOCKS_PER_TICK, undoHistoryManager),
                new WallCommand(BLOCKS_PER_TICK, selectionResolver, undoHistoryManager)
        ));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] strings) {
        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            commandSender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
            return true;
        }

        if (!isPlayer(commandSender)) {
            commandSender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
            return true;
        }

        Player p = (Player) commandSender;

        if (strings.length == 0) {
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
            return true;
        }

        SubCommand subCommand = subCommandRegistry.find(strings);
        if (subCommand == null) {
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
            return true;
        }

        subCommand.execute(p, strings);
        return true;
    }



    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            return tabList;
        }

        if (strings.length == 1) {
            tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
            return tabList;
        }

        if (strings[0].equalsIgnoreCase(Commands.UNDO.getName())) {
            return tabList;
        }

        if (strings[0].equalsIgnoreCase(Commands.MOVE.getName())) {
            return tabList;
        }

        if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase(Commands.CLIPBOARD.getName())) {
                tabList.addAll(List.of(Commands.CLIPBOARD.getSubCommands()));
                return tabList;
            }

            if (strings[0].equalsIgnoreCase(Commands.PLANT.getName())) {
                String input = strings[1].isEmpty() ? null : strings[1].toUpperCase();
                tabList.addAll(Arrays.stream(Material.values())
                        .filter(ModifyHelper::isPlantMaterial)
                        .map(Material::name)
                        .filter(name -> input == null || name.startsWith(input))
                        .sorted()
                        .toList());
                return tabList;
            }

            tabList.addAll(TabCompleterHelper.getMaterials(strings[1].isEmpty() ? null : strings[1]));
            return tabList;
        }

        if (strings.length == 3) {
            if (!strings[0].equalsIgnoreCase(Commands.REPLACE.getName())) {
                return tabList;
            }

            tabList.addAll(TabCompleterHelper.getMaterials(strings[2].isEmpty() ? null : strings[2]));
            return tabList;
        }

        return tabList;
    }
}