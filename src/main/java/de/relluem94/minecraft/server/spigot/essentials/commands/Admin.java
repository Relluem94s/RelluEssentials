package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.SubCommandRegistry;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;

import de.relluem94.minecraft.server.spigot.essentials.commands.admin.*;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC;
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
import java.util.Optional;
import java.util.UUID;

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
                new NPCCreateCommand(),
                new NPCDeleteCommand(),
                new NPCUpdateCommand(),
                new NPCDialogueAddCommand(),
                new NPCDialogueUpdateCommand(),
                new NPCDialogueDeleteCommand(),
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
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0])) {
                tabList.addAll(List.of("create", "update", "delete", "dialogue"));
            }
            return tabList;
        }
        if (strings.length == 3) {
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0]) && "dialogue".equalsIgnoreCase(strings[1])) {
                tabList.addAll(List.of("add", "update", "delete"));
            }
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0]) && "create".equalsIgnoreCase(strings[1])) {
                tabList.add("<profileName>");
            }
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0])
                    && ("update".equalsIgnoreCase(strings[1]) || "delete".equalsIgnoreCase(strings[1]))) {
                Player player = (Player) commandSender;
                RelluEssentials.getInstance().getNpcService()
                        .getNearestNPC(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getWorld().getName())
                        .ifPresentOrElse(
                                npc -> tabList.add(String.valueOf(npc.getId())),
                                () -> tabList.add("<NPC ID>")
                        );
            }
            return tabList;
        }
        if (strings.length == 4) {
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0])) {
                if ("create".equalsIgnoreCase(strings[1])) {
                    tabList.add(resolvePlayerCoordinate(commandSender, "x"));
                } else if ("update".equalsIgnoreCase(strings[1])) {
                    tabList.addAll(List.of("profile", "position"));
                }
            }
            return tabList;
        }
        if (strings.length == 5) {
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0]) && "dialogue".equalsIgnoreCase(strings[1])) {
                String dialogueAction = strings[2];
                boolean isAddAction = "add".equalsIgnoreCase(dialogueAction);
                boolean isUpdateOrDeleteAction = "update".equalsIgnoreCase(dialogueAction) || "delete".equalsIgnoreCase(dialogueAction);

                if (isAddAction || isUpdateOrDeleteAction) {
                    resolveNpcFromArg(strings[3]).ifPresent(npc -> {
                        List<NPCDialogueEntry> dialogueEntries = RelluEssentials.getInstance().getDatabaseHelper().getNPCDialogues(npc.getDbid());
                        List<Integer> usedPositions = dialogueEntries.stream()
                                .map(NPCDialogueEntry::getListPosition)
                                .sorted()
                                .toList();

                        if (isAddAction) {
                            tabList.addAll(findGapsAndNextPosition(usedPositions).stream()
                                    .map(String::valueOf)
                                    .toList());
                        } else {
                            tabList.addAll(usedPositions.stream()
                                    .map(String::valueOf)
                                    .toList());
                        }
                    });
                }
            }
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0]) && "update".equalsIgnoreCase(strings[1])) {
                if ("profile".equalsIgnoreCase(strings[3])) {
                    tabList.add("<profileName>");
                } else if ("position".equalsIgnoreCase(strings[3])) {
                    tabList.add(resolvePlayerCoordinate(commandSender, "x"));
                }
            }
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0]) && "create".equalsIgnoreCase(strings[1])) {
                tabList.add(resolvePlayerCoordinate(commandSender, "y"));
            }
            return tabList;
        }
        if (strings.length == 6) {
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0]) && "dialogue".equalsIgnoreCase(strings[1])
                    && ("add".equalsIgnoreCase(strings[2]) || "update".equalsIgnoreCase(strings[2]))) {
                tabList.add("<text...>");
            }
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0]) && "create".equalsIgnoreCase(strings[1])) {
                tabList.add(resolvePlayerCoordinate(commandSender, "z"));
            }
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0]) && "update".equalsIgnoreCase(strings[1])
                    && "position".equalsIgnoreCase(strings[3])) {
                tabList.add(resolvePlayerCoordinate(commandSender, "y"));
            }
            return tabList;
        }
        if (strings.length == 7) {
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0]) && "update".equalsIgnoreCase(strings[1])
                    && "position".equalsIgnoreCase(strings[3])) {
                tabList.add(resolvePlayerCoordinate(commandSender, "z"));
            }
        }
        return tabList;
    }

    private @NonNull String resolvePlayerCoordinate(CommandSender commandSender, String axis) {
        if (!isPlayer(commandSender)) {
            return "<" + axis + ">";
        }
        Player player = (Player) commandSender;
        return switch (axis) {
            case "x" -> String.valueOf((int) player.getLocation().getX());
            case "y" -> String.valueOf((int) player.getLocation().getY());
            case "z" -> String.valueOf((int) player.getLocation().getZ());
            default -> "<" + axis + ">";
        };
    }

    private Optional<NPC> resolveNpcFromArg(String npcIdArg) {
        try {
            UUID npcId = UUID.fromString(npcIdArg);
            return RelluEssentials.getInstance().getNpcService().getNPCById(npcId);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private @NonNull List<Integer> findGapsAndNextPosition(@NonNull List<Integer> usedPositions) {
        if (usedPositions.isEmpty()) {
            return List.of(1);
        }
        List<Integer> availablePositions = new ArrayList<>();
        int maxPosition = usedPositions.getLast();
        for (int position = 1; position <= maxPosition; position++) {
            if (!usedPositions.contains(position)) {
                availablePositions.add(position);
            }
        }
        availablePositions.add(maxPosition + 1);
        return availablePositions;
    }

    @Getter
    public enum Commands implements CommandsEnum {

        AFK("afk"),
        CLEAN_PROTECTIONS("cleanProtections"),
        CLEAN_LOCATIONS("cleanLocations"),
        CHAT("chat"),
        INFO("info"),
        LIGHT("light"),
        NPC("npc", "create", "update", "delete", "dialogue"),
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