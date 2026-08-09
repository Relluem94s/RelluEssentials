package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getPlayerDirection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("position")
public class Position implements CommandConstruct {

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
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
      @NotNull String s, @NotNull String[] strings) {
    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
      commandSender.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (!isPlayer(commandSender)) {
      commandSender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) commandSender;

    if (strings.length == 0) {
      if (!serviceContext.getPositionService().hasPositions(p)) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_POSITION_NO_POSITIONS));
        return true;
      }

      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_POSITION_INFO_1));

      DoubleStore<Location, Location> positions = serviceContext.getPositionService()
          .getPositions(p);
      Location first = positions.getValue();
      Location second = positions.getSecondValue();

      String notAvailable = serviceContext.getTranslationService()
          .get(MessageKey.COMMAND_POSITION_NO_POSITIONS);
      String firstLocationString =
          first == null ? notAvailable : serviceContext.getMessageService().locationToString(first);
      String secondLocationString =
          second == null ? notAvailable
              : serviceContext.getMessageService().locationToString(second);

      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_POSITION_INFO_2,
              firstLocationString));
      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_POSITION_INFO_3,
              secondLocationString));
      return true;
    }

    String cmd = strings[0].toLowerCase();
    serviceContext.getPositionService().ensurePositionsExist(p);

    if (cmd.equals(Commands.CLEAR.getName())) {
      if (strings.length != 1) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
        return true;
      }
      serviceContext.getPositionService().clearPositions(p);
      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_POSITION_CLEAR));
      return true;
    }

    if (cmd.equals(Commands.SET.getName())) {
      if (strings.length != 2) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
        return true;
      }
      String sub = strings[1].toLowerCase();
      Location location = PlayerHelper.getLookingLocation(p, 100);
      if (sub.equals(Commands.SET.getSubCommands()[0])) {
        serviceContext.getPositionService().setFirstPosition(p, location);
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_POSITION_SET_FIRST,
                serviceContext.getMessageService().locationToString(location)));
      } else if (sub.equals(Commands.SET.getSubCommands()[1])) {
        serviceContext.getPositionService().setSecondPosition(p, location);
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_POSITION_SET_SECOND,
                serviceContext.getMessageService().locationToString(location)));
      } else {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
      }
      return true;
    }

    if (cmd.equals(Commands.REMOVE.getName())) {
      if (strings.length != 2) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
        return true;
      }
      String sub = strings[1].toLowerCase();
      if (sub.equals(Commands.REMOVE.getSubCommands()[0])) {
        serviceContext.getPositionService().removeFirstPosition(p);
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_POSITION_REMOVE_FIRST));
      } else if (sub.equals(Commands.REMOVE.getSubCommands()[1])) {
        serviceContext.getPositionService().removeSecondPosition(p);
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_POSITION_REMOVE_SECOND));
      } else {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
      }
      return true;
    }

    if (strings.length != 2) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
      return true;
    }

    int amount;
    try {
      amount = Integer.parseInt(strings[1]);
    } catch (NumberFormatException e) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_POSITION_INVALID_AMOUNT));
      return true;
    }

    Vector direction = getPlayerDirection(p);
    if (cmd.equals(Commands.SHIFT.getName())) {
      DoubleStore<Location, Location> positions = serviceContext.getPositionService()
          .getPositions(p);
      if (positions.getValue() == null && positions.getSecondValue() == null) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_POSITION_NO_POSITIONS));
        return true;
      }

      serviceContext.getPositionService().shiftPositions(p, direction, amount);
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_POSITION_SHIFT, amount));
      return true;
    }

    if (cmd.equals(Commands.EXPAND.getName()) || cmd.equals(Commands.DECREASE.getName())) {
      DoubleStore<Location, Location> positions = serviceContext.getPositionService()
          .getPositions(p);
      if (positions.getValue() == null || positions.getSecondValue() == null) {
        p.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.COMMAND_POSITION_NEED_BOTH_POSITIONS));
        return true;
      }

      serviceContext.getPositionService().expandOrDecreasePositions(p, direction, amount,
          cmd.equals(Commands.EXPAND.getName()));

      MessageKey actionKey =
          cmd.equals(Commands.EXPAND.getName()) ? MessageKey.COMMAND_POSITION_EXPAND
              : MessageKey.COMMAND_POSITION_DECREASE;
      p.sendMessage(serviceContext.getTranslationService().getWithPrefix(actionKey, amount));
      return true;
    }

    p.sendMessage(
        serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
      return tabList;
    }

    if (strings.length == 1) {
      tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
      return tabList;
    }

    if (strings.length == 2) {
      if (strings[0].equalsIgnoreCase(Commands.SET.getName())) {
        tabList.addAll(List.of(Commands.SET.getSubCommands()));
      } else if (strings[0].equalsIgnoreCase(Commands.REMOVE.getName())) {
        tabList.addAll(List.of(Commands.REMOVE.getSubCommands()));
      }
      return tabList;
    }

    return tabList;
  }

  @Getter
  public enum Commands implements CommandsEnum {

    SET("set", "first", "second"),
    REMOVE("remove", "first", "second"),
    SHIFT("shift"),
    EXPAND("expand"),
    DECREASE("decrease"),
    CLEAR("clear");

    private final String name;
    private final String[] subCommands;

    Commands(String name, String... subCommands) {
      this.name = name;
      this.subCommands = subCommands;
    }
  }
}