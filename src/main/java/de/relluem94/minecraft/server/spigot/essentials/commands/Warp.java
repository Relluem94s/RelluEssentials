package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
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

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
      @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "user")) {
      return tabList;
    }

    if (!isPlayer(commandSender)) {
      return tabList;
    }

    Player p = (Player) commandSender;

    switch (strings.length) {
      case 1:
        if (serviceContext.getGroupService().isSenderAuthorized(p, "admin")) {
          tabList.addAll(TabCompleterHelper.getCommands(getCommands()));
        }
        tabList.addAll(serviceContext.getWarpService().getWarpNamesByWorld(p.getWorld()));
        break;
      case 2:
        if (!serviceContext.getGroupService().isSenderAuthorized(p, "admin")) {
          return tabList;
        }
        if (!Commands.REMOVE.getName().equalsIgnoreCase(strings[0])) {
          return tabList;
        }
        tabList.addAll(serviceContext.getWarpService().getWarpNamesByWorld(p.getWorld()));
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
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!serviceContext.getGroupService().isSenderAuthorized(sender, "user")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_WARP_LIST_INFO));
      for (LocationEntry le : serviceContext.getWarpService()
          .findWarpsByWorld(p.getWorld())) {
        p.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.COMMAND_WARP_LIST, le.getLocationName()));
      }
      return true;
    } else if (args.length == 1) {
      warp(args[0], p);
      return true;
    } else if (args.length == 2) {
      if (!serviceContext.getGroupService().isSenderAuthorized(p, "admin")) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
        return true;
      }

      if (args[0].equalsIgnoreCase(Commands.ADD.getName())) {
        int playerId = serviceContext.getPlayerService().getPlayerEntry(p).getId();
        boolean added = serviceContext.getWarpService().addWarp(args[1], p, playerId);
        if (added) {
          p.sendMessage(
              serviceContext.getTranslationService()
                  .getWithPrefix(MessageKey.COMMAND_WARP_ADD, args[1]));
        } else {
          p.sendMessage(
              serviceContext.getTranslationService()
                  .getWithPrefix(MessageKey.COMMAND_WARP_ERROR_ALREADY_EXISTS, args[1]));
        }
        return true;
      } else if (args[0].equalsIgnoreCase(Commands.REMOVE.getName())) {
        boolean removed = serviceContext.getWarpService().removeWarp(args[1]);
        if(removed){
          p.sendMessage(serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_WARP_REMOVE, args[1]));
        } else{
          p.sendMessage(serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_WARP_ERROR_WARP_NOT_DELETED_NOT_FOUND, args[1]));
        }

        return true;
      } else {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
        return true;
      }
    }
    return false;
  }

  private void warp(String name, @NotNull Player p) {
    Optional<LocationEntry> result = serviceContext.getWarpService()
        .findWarpByNameAndWorld(name, p.getWorld());

    if (result.isEmpty()) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WARP_ERROR_NO_WARP_FOUND));
      return;
    }

    LocationEntry le = result.get();

    if (le.getLocation() == null) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WARP_ERROR_WORLD_UNLOADED));
      return;
    }

    if (le.getLocation().getWorld() == null) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WARP_ERROR_WORLD_UNLOADED));
      return;
    }

    serviceContext.getTeleportService().teleportWarp(p, le.getLocation());
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