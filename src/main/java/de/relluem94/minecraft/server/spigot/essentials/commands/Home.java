package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("home")
public class Home implements CommandConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public CommandsEnum[] getCommands() {
    return Commands.values();
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
      @NonNull String label, String @NotNull [] args) {
    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "user")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    PlayerEntry pe = serviceContext.getPlayerService()
        .getPlayerEntry(p);

    switch (args.length) {
      case 0:
        serviceContext.getTeleportService().teleportBed(p);
        return true;
      case 1:
        if (args[0].equalsIgnoreCase(Commands.LIST.getName())) {
          if (!pe.getHomes().isEmpty()) {
            p.sendMessage(
                serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_HOME_LIST));
            pe.getHomes().forEach(fle -> p.sendMessage(
                serviceContext.getTranslationService()
                    .getWithPrefix(MessageKey.COMMAND_HOME_LIST_NAME,
                        fle.getLocationName(),
                        serviceContext.getMessageService().locationToString(fle.getLocation()))));
          } else {
            p.sendMessage(
                serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_HOME_NONE));
          }

          if (!pe.getDeaths().isEmpty()) {
            p.sendMessage(
                serviceContext.getTranslationService()
                    .getWithPrefix(MessageKey.COMMAND_HOME_LIST_DEATHPOINTS));
            pe.getDeaths().forEach(fle -> p.sendMessage(
                serviceContext.getTranslationService()
                    .getWithPrefix(MessageKey.COMMAND_HOME_LIST_DEATHPOINTS_NAME,
                        fle.getLocationName(),
                        serviceContext.getMessageService().locationToString(fle.getLocation()))));
          }
        } else {
          p.sendMessage(serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
        }
        return true;
      case 2:
        LocationTypeEntry homeType = serviceContext.getLocationTypeService()
            .findByName(LocationType.HOME)
            .orElseThrow(() -> new IllegalStateException(PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND));

        LocationEntry le = new LocationEntry();
        le.setLocation(p.getLocation());
        le.setLocationName(args[1]);
        le.setLocationType(homeType);
        le.setPlayerId(pe.getId());

        if (args[0].equalsIgnoreCase(Commands.SET.getName())) {
          if (homeExists(pe, le)) {
            p.sendMessage(
                serviceContext.getTranslationService()
                    .getWithPrefix(MessageKey.COMMAND_HOME_EXISTS, args[1]));
          } else if (!args[1].startsWith("death_")) {
            serviceContext.getLocationService().save(le);
            pe.getHomes().add(le);
            p.sendMessage(serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.COMMAND_HOME_SET, args[1]));
          } else {
            p.sendMessage(
                serviceContext.getTranslationService()
                    .getWithPrefix(MessageKey.COMMAND_HOME_RESERVED, args[1]));
          }
          return true;
        } else if (args[0].equalsIgnoreCase(Commands.DELETE.getName())) {
          if (homeExists(pe, le)) {
            le = getLocationEntry(pe, le);
            pe.getHomes().remove(le);
            p.sendMessage(
                serviceContext.getTranslationService()
                    .getWithPrefix(MessageKey.COMMAND_HOME_DELETE, args[1]));

            if (le == null) {
              return true;
            }

            serviceContext.getLocationService().delete(le);
            return true;
          } else if (deathExists(pe, le)) {
            le = getLocationEntry(pe, le);
            pe.getDeaths().remove(le);
            p.sendMessage(
                serviceContext.getTranslationService()
                    .getWithPrefix(MessageKey.COMMAND_HOME_DEATH_DELETE, args[1]));

            if (le == null) {
              return true;
            }

            serviceContext.getLocationService().delete(le);
            return true;
          } else if (le.getLocationName().startsWith("death_") && le.getLocationName()
              .contains("*")) {
            for (LocationEntry dle : pe.getDeaths()) {
              p.sendMessage(serviceContext.getTranslationService()
                  .getWithPrefix(MessageKey.COMMAND_HOME_DEATH_DELETE,
                      dle.getLocationName()));
              serviceContext.getLocationService().delete(dle);
            }
            pe.getDeaths().clear();
            return true;
          } else {
            p.sendMessage(
                serviceContext.getTranslationService()
                    .getWithPrefix(MessageKey.COMMAND_HOME_NOT_FOUND, args[1]));
            return true;
          }
        } else if (args[0].equalsIgnoreCase(Commands.TP.getName())) {
          le.setLocationName(args[1]);
          le.setLocationType(homeType);
          le.setPlayerId(pe.getId());

          if (homeExists(pe, le) || deathExists(pe, le)) {
            serviceContext.getTeleportService().teleportHome(p, getLocationEntry(pe, le));
          } else {
            p.sendMessage(
                serviceContext.getTranslationService()
                    .getWithPrefix(MessageKey.COMMAND_HOME_NOT_FOUND, args[1]));
          }
          return true;
        }
        break;
      default:
        break;
    }
    return false;
  }

  private boolean homeExists(@NotNull PlayerEntry pe, LocationEntry le) {
    return pe.getHomes().stream()
        .anyMatch(fle -> fle.getLocationName().equals(le.getLocationName()));
  }

  private boolean deathExists(@NotNull PlayerEntry pe, LocationEntry le) {
    return pe.getDeaths().stream()
        .anyMatch(fle -> fle.getLocationName().equals(le.getLocationName()));
  }

  private @Nullable LocationEntry getLocationEntry(@NotNull PlayerEntry pe, LocationEntry le) {
    for (LocationEntry fle : pe.getHomes()) {
      if (fle.getLocationName().equals(le.getLocationName())) {
        return fle;
      }
    }

    for (LocationEntry fle : pe.getDeaths()) {
      if (fle.getLocationName().equals(le.getLocationName())) {
        return fle;
      }
    }
    return null;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "user")) {
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
      if (Commands.SET.getName().equalsIgnoreCase(strings[0])) {
        return tabList;
      }
      if (Commands.LIST.getName().equalsIgnoreCase(strings[0])) {
        return tabList;
      }
      tabList.addAll(TabCompleterHelper.getHomes((Player) commandSender));
      return tabList;
    }

    return tabList;
  }

  @Getter
  public enum Commands implements CommandsEnum {

    SET("set"),
    DELETE("delete"),
    LIST("list"),
    TP("tp");

    private final String name;
    private final String[] subCommands;

    Commands(String name, String... subCommands) {
      this.name = name;
      this.subCommands = subCommands;
    }
  }
}
