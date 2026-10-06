package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

/**
 * Command implementation for adjusting a player's walk or fly speed.
 *
 * <p>Allows authorized players to set their movement speed to a value between 1 and 10.
 * The speed is normalized to Bukkit's expected range by dividing the input by 10.</p>
 *
 * @author rellu
 */
@CommandName("speed")
public class Speed implements CommandConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String @NotNull [] args) {
    if (args.length != 1) {
      sender.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_SPEED_INFO));
      return true;
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;
    if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
      p.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (!args[0].matches("^\\d+$")) {
      p.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_INVALID));
      return true;
    }

    float speed = parseSpeed(args[0]);
    if (p.isFlying()) {
      p.setFlySpeed(speed);
    } else {
      p.setWalkSpeed(speed);
    }
    p.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_SPEED, args[0]));
    return true;
  }

  private float parseSpeed(String arg) {
    return (float) Integer.parseInt(arg) / 10;
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

    if (strings.length > 1) {
      return tabList;
    }

    tabList.addAll(serviceContext.getTabCompleterService().getCommands(getCommands()));

    return tabList;
  }

  /**
   * Enum representing the valid speed values accepted by the speed command.
   *
   * <p>Each constant corresponds to a numeric input from 1 to 10.</p>
   */
  @Getter
  public enum Commands implements CommandsEnum {
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10");

    private final String name;
    private final String[] subCommands;

    Commands(String name, String... subCommands) {
      this.name = name;
      this.subCommands = subCommands;
    }
  }
}
