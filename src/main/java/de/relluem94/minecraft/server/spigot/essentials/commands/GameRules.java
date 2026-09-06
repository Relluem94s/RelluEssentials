package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_NEGATIVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_POSITIVE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation that displays all game rules and their current values for a specified or
 * current world.
 *
 * <p>Players with admin permissions can view game rules for their current world or specify a world
 * by name. Console and command block senders must provide a world name explicitly.
 */
@CommandName("gamerules")
public class GameRules implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for translation, group and plugin metadata services.
   *
   * @param context the service context to inject
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Returns an empty array as this command does not define any sub-commands.
   *
   * @return an empty {@link CommandsEnum} array
   */
  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  /**
   * Handles the gamerules command execution.
   *
   * <p>Console and command block senders must provide exactly one argument containing the world
   * name. Players with admin permissions may omit the world name to view rules for their current
   * world, or provide one argument to specify a different world by name.
   *
   * @param sender  the entity that executed the command
   * @param command the command that was executed
   * @param label   the alias used to execute the command
   * @param args    the arguments provided with the command
   * @return {@code true} in all cases to suppress Bukkit's default usage message
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {

    if (isCMDBlock(sender) || isConsole(sender)) {
      if (args.length < 1) {
        sender.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
        return true;
      }

      showGameRulesForWorld(sender, args[0]);

      return true;
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "admin")) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      showGameRule(sender, p.getWorld());
      return true;
    }

    if (args.length > 1) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    showGameRulesForWorld(sender, args[0]);
    return true;
  }

  private void showGameRule(CommandSender sender, @NotNull World world) {
    String[] gameRules = world.getGameRules();
    sender.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_GAMERULES, world.getName()));
    for (String gameRule : gameRules) {
      Object value = world.getGameRuleValue(Objects.requireNonNull(GameRule.getByName(gameRule)));
      String color;
      if (value instanceof Boolean) {
        color = (boolean) value ? PLUGIN_COLOR_POSITIVE : PLUGIN_COLOR_NEGATIVE;
      } else {
        color = "§7";
      }

      sender.sendMessage("        §d" + gameRule + "§f = " + color + value);
    }
  }

  private void showGameRulesForWorld(CommandSender sender, String name) {
    World world = serviceContext.getPluginMetadataService().getPlugin().getServer().getWorld(name);
    if (world == null) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WORLD_NOT_LOADED, name));
      return;
    }

    showGameRule(sender, world);
  }

  /**
   * Provides tab completion suggestions for the gamerules command. Returns an empty list if the
   * sender lacks mod permissions or if more than one argument is already present. Otherwise,
   * returns a list of all available world names.
   *
   * @param commandSender the entity requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias used
   * @param strings       the current arguments
   * @return a list of world name suggestions, or an empty list if not applicable
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
      return new ArrayList<>();
    }

    if (strings.length > 1) {
      return new ArrayList<>();
    }

    return TabCompleterHelper.getWorlds();
  }
}
