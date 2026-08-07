package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandName("warp")
public class Warp implements CommandConstruct {

  private GroupService groupService;
  private TranslationService translationService;
  private SchedulerService schedulerService;
  private TeleportService teleportService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
    this.translationService = context.getTranslationService();
    this.schedulerService = context.getSchedulerService();
    this.teleportService = context.getTeleportService();
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
      @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!groupService.isSenderAuthorized(commandSender, "user")) {
      return tabList;
    }

    if (!isPlayer(commandSender)) {
      return tabList;
    }

    Player p = (Player) commandSender;

    switch (strings.length) {
      case 1:
        if (groupService.isSenderAuthorized(p, "admin")) {
          tabList.addAll(TabCompleterHelper.getCommands(getCommands()));
        }
        tabList.addAll(TabCompleterHelper.getWarps(p.getWorld()));
        break;
      case 2:
        if (!groupService.isSenderAuthorized(p, "admin")) {
          return tabList;
        }
        if (Commands.ADD.getName().equalsIgnoreCase(strings[1])) {
          return tabList;
        }
        tabList.addAll(TabCompleterHelper.getWarps(p.getWorld()));
        return tabList;
      default:
        break;
    }

    return tabList;
  }

  @Override
  public CommandsEnum[] getCommands() {
    return Commands.values();
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {

    if (!isPlayer(sender)) {
      sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!groupService.isSenderAuthorized(sender, "user")) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_WARP_LIST_INFO));
      for (LocationEntry le : RelluEssentials.getInstance().getWarpRepository()
          .findByWorld(p.getWorld())) {
        p.sendMessage(
            translationService.getWithPrefix(MessageKey.COMMAND_WARP_LIST, le.getLocationName()));
      }
      return true;
    } else if (args.length == 1) {
      warp(args[0], p);
      return true;
    } else if (args.length == 2) {
      if (!groupService.isSenderAuthorized(p, "admin")) {
        p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
        return true;
      }

      if (args[0].equalsIgnoreCase(Commands.ADD.getName())) {
        addWarp(args[1], p);
        return true;
      } else if (args[0].equalsIgnoreCase(Commands.REMOVE.getName())) {
        removeWarp(args[1], p);
        return true;
      } else {
        p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
        return true;
      }
    }
    return false;
  }

  private void addWarp(String name, Player p) {
    Optional<LocationEntry> existing = RelluEssentials.getInstance().getWarpRepository()
        .findByName(name);
    if (existing.isPresent()) {
      return;
    }

    int typeId = 3;
    LocationEntry le = new LocationEntry();
    le.setLocation(p.getLocation());
    le.setLocationName(name);
    le.setLocationType(RelluEssentials.getInstance().locationTypeEntryList.get(typeId - 1));
    le.setPlayerId(RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p).getId());
    RelluEssentials.getInstance().getDatabaseHelper().insertLocation(le);

    LocationEntry persisted = RelluEssentials.getInstance().getDatabaseHelper()
        .getLocation(p.getLocation(), typeId);
    if (persisted != null) {
      le = persisted;
    }

    RelluEssentials.getInstance().getWarpRepository().save(le);
    p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_WARP_ADD, name));
  }

  private void removeWarp(String name, Player p) {
    RelluEssentials.getInstance().getWarpRepository().findByName(name).ifPresent(le -> {
      RelluEssentials.getInstance().getDatabaseHelper().deleteLocation(le);
      RelluEssentials.getInstance().getWarpRepository().delete(le);
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_WARP_REMOVE, name));
    });
  }

  private void warp(String name, @NotNull Player p) {
    Optional<LocationEntry> result = RelluEssentials.getInstance().getWarpRepository()
        .findByNameAndWorld(name, p.getWorld());

    if (result.isEmpty()) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_WARP_ERROR_NO_WARP_FOUND));
      return;
    }

    LocationEntry le = result.get();

    if (le.getLocation() == null) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_WARP_ERROR_WORLD_UNLOADED));
      return;
    }

    if (le.getLocation().getWorld() == null) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_WARP_ERROR_WORLD_UNLOADED));
      return;
    }

    teleportService.teleportWarp(p, le.getLocation());
  }

  @Getter
  public enum Commands implements CommandsEnum {

    ADD("add"),
    REMOVE("remove");

    private final String name;
    private final String[] subCommands;

    Commands(String name, String... subCommands) {
      this.name = name;
      this.subCommands = subCommands;
    }
  }
}