package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.AdminToolsGuiCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.CleanUpChatCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.CleanUpLocationsCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.CleanUpProtectionsCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.FakeAfkCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.LightToggleCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.NpcCreateCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.NpcDeleteCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.NpcDialogueAddCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.NpcDialogueDeleteCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.NpcDialogueUpdateCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.NpcEquipCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.NpcGuiCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.NpcUpdateCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.PingCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.PluginInfoCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.admin.TopCommand;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.SubCommandRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("admin")
public class Admin implements CommandConstruct {

  private SubCommandRegistry<SubCommand> subCommandRegistry;

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;

    this.subCommandRegistry = new SubCommandRegistry<>(List.of(
        new AdminToolsGuiCommand(context),
        new CleanUpChatCommand(context),
        new CleanUpLocationsCommand(context),
        new CleanUpProtectionsCommand(context),
        new FakeAfkCommand(context),
        new LightToggleCommand(context),
        new NpcGuiCommand(context),
        new NpcCreateCommand(context),
        new NpcDeleteCommand(context),
        new NpcUpdateCommand(context),
        new NpcEquipCommand(context),
        new NpcDialogueAddCommand(context),
        new NpcDialogueUpdateCommand(context),
        new NpcDialogueDeleteCommand(context),
        new PingCommand(context),
        new PluginInfoCommand(context),
        new TopCommand(context)
    ));
  }

  @Override
  public CommandsEnum[] getCommands() {
    return Commands.values();
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();
    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
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
        } else if ("update".equalsIgnoreCase(strings[1]) || "equip".equalsIgnoreCase(strings[1])
            || "delete".equalsIgnoreCase(strings[1])) {
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
            List<NpcDialogueEntry> dialogueEntries = serviceContext
                .getNpcService().getNPCDialogues(npc.getDbid());
            List<Integer> usedPositions = dialogueEntries.stream()
                .map(NpcDialogueEntry::getListPosition)
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
        } else if ("update".equalsIgnoreCase(strings[1]) && "position".equalsIgnoreCase(
            strings[3])) {
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
    return serviceContext.getNpcService()
        .getNearestNPC(player.getLocation().getX(), player.getLocation().getY(),
            player.getLocation().getZ(), player.getWorld().getName())
        .map(npc -> String.valueOf(npc.getId()));
  }

  private Optional<String> resolveCurrentDialogueText(String npcIdArg, String listPositionArg) {
    try {
      UUID npcId = UUID.fromString(npcIdArg);
      int listPosition = Integer.parseInt(listPositionArg);
      return serviceContext.getNpcService().getNPCById(npcId)
          .flatMap(npc -> serviceContext.getNpcService()
              .getNPCDialogues(npc.getDbid())
              .stream()
              .filter(entry -> entry.getListPosition() == listPosition)
              .map(NpcDialogueEntry::getText)
              .map(text -> text.replace('§', '&'))
              .findFirst());
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  private Optional<Npc> resolveNpcFromArg(String npcIdArg) {
    try {
      UUID npcId = UUID.fromString(npcIdArg);
      return serviceContext.getNpcService().getNPCById(npcId);
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

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }
    Player p = (Player) sender;
    if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }
    if (args.length == 0) {
      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_ADMIN_INFO));
      return true;
    }

    SubCommand subCommand = subCommandRegistry.find(args);
    if (subCommand == null) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
      return true;
    }

    subCommand.execute(p, args);
    return true;
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
}