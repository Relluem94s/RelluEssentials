package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.*;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.NPC;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.NPCDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.registry.SubCommandRegistry;
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
                new NPCEquipCommand(),
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
        Player player = (Player) commandSender;
        if (strings.length == 1) {
            tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
            return tabList;
        }
        if (strings.length == 2) {
            if (Commands.PING.getName().equalsIgnoreCase(strings[0])) {
                tabList.addAll(TabCompleterHelper.getOnlinePlayers());
            }
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0])) {
                tabList.addAll(List.of("create", "update", "delete", "dialogue", "equip"));
            }
            return tabList;
        }
        if (strings.length == 3) {
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0])) {
                if ("create".equalsIgnoreCase(strings[1])) {
                    tabList.add("<profileName>");
                } else if ("dialogue".equalsIgnoreCase(strings[1])) {
                    tabList.addAll(List.of("add", "update", "delete"));
                } else if ("update".equalsIgnoreCase(strings[1]) || "equip".equalsIgnoreCase(strings[1]) || "delete".equalsIgnoreCase(strings[1])) {
                    resolveNearestNpcId(player).ifPresentOrElse(
                            tabList::add,
                            () -> tabList.add("<NPC-ID>")
                    );
                }
            }
            return tabList;
        }
        if (strings.length == 4) {
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0])) {
                if ("create".equalsIgnoreCase(strings[1])) {
                    tabList.add(resolvePlayerCoordinate(player, "x"));
                } else if ("update".equalsIgnoreCase(strings[1])) {
                    tabList.addAll(List.of("profile", "position"));
                } else if ("dialogue".equalsIgnoreCase(strings[1])) {
                    resolveNearestNpcId(player).ifPresentOrElse(
                            tabList::add,
                            () -> tabList.add("<NPC-ID>")
                    );
                }
            }
            return tabList;
        }
        if (strings.length == 5) {
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0])) {
                if ("create".equalsIgnoreCase(strings[1])) {
                    tabList.add(resolvePlayerCoordinate(player, "y"));
                } else if ("update".equalsIgnoreCase(strings[1])) {
                    if ("profile".equalsIgnoreCase(strings[3])) {
                        tabList.add("<profileName>");
                    } else if ("position".equalsIgnoreCase(strings[3])) {
                        tabList.add(resolvePlayerCoordinate(player, "x"));
                    }
                } else if ("dialogue".equalsIgnoreCase(strings[1])) {
                    resolveNpcFromArg(strings[3]).ifPresent(npc -> {
                        List<NPCDialogueEntry> dialogueEntries = RelluEssentials.getInstance().getDatabaseHelper().getNPCDialogues(npc.getDbid());
                        List<Integer> usedPositions = dialogueEntries.stream()
                                .map(NPCDialogueEntry::getListPosition)
                                .sorted()
                                .toList();

                        if ("add".equalsIgnoreCase(strings[2])) {
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
            return tabList;
        }
        if (strings.length == 6) {
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0])) {
                if ("create".equalsIgnoreCase(strings[1])) {
                    tabList.add(resolvePlayerCoordinate(player, "z"));
                } else if ("update".equalsIgnoreCase(strings[1]) && "position".equalsIgnoreCase(strings[3])) {
                    tabList.add(resolvePlayerCoordinate(player, "y"));
                } else if ("dialogue".equalsIgnoreCase(strings[1])) {
                    if ("add".equalsIgnoreCase(strings[2])) {
                        tabList.add("<text...>");
                    } else if ("update".equalsIgnoreCase(strings[2])) {
                        resolveCurrentDialogueText(strings[3], strings[4]).ifPresentOrElse(
                                tabList::add,
                                () -> tabList.add("<text...>")
                        );
                    }
                }
            }
            return tabList;
        }
        if (strings.length == 7) {
            if (Commands.NPC.getName().equalsIgnoreCase(strings[0])
                    && "update".equalsIgnoreCase(strings[1])
                    && "position".equalsIgnoreCase(strings[3])) {
                tabList.add(resolvePlayerCoordinate(player, "z"));
            }
        }

        return tabList;
    }

    private Optional<String> resolveNearestNpcId(Player player) {
        return RelluEssentials.getInstance().getNpcService()
                .getNearestNPC(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getWorld().getName())
                .map(npc -> String.valueOf(npc.getId()));
    }

    private Optional<String> resolveCurrentDialogueText(String npcIdArg, String listPositionArg) {
        try {
            UUID npcId = UUID.fromString(npcIdArg);
            int listPosition = Integer.parseInt(listPositionArg);
            return RelluEssentials.getInstance().getNpcService().getNPCById(npcId)
                    .flatMap(npc -> RelluEssentials.getInstance().getDatabaseHelper()
                            .getNPCDialogues(npc.getDbid())
                            .stream()
                            .filter(entry -> entry.getListPosition() == listPosition)
                            .map(NPCDialogueEntry::getText)
                            .map(text -> text.replace('§', '&'))
                            .findFirst());
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private Optional<NPC> resolveNpcFromArg(String npcIdArg) {
        try {
            UUID npcId = UUID.fromString(npcIdArg);
            return RelluEssentials.getInstance().getNpcService().getNPCById(npcId);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private @NonNull String resolvePlayerCoordinate(Player player, String axis) {
        return switch (axis) {
            case "x" -> String.valueOf((int) player.getLocation().getX());
            case "y" -> String.valueOf((int) player.getLocation().getY());
            case "z" -> String.valueOf((int) player.getLocation().getZ());
            default -> "<" + axis + ">";
        };
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