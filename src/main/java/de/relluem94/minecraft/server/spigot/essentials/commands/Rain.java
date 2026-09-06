package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for changing the weather to rain in a specific world. If no world argument
 * is provided, the player's current world is used. Requires the sender to be a player with at least
 * "mod" group authorization.
 */
@CommandName("rain")
public class Rain implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for translation, group, and plugin metadata services.
   *
   * @param context the service context to inject
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Executes the rain command, setting the weather of the target world to rain. If no arguments are
   * provided, the player's current world is used. If a world name is provided as the first
   * argument, that world is targeted.
   *
   * @param sender  the entity that sent the command, must be a player
   * @param command the command that was executed
   * @param label   the alias used to trigger the command
   * @param args    optional arguments; the first argument may specify a target world name
   * @return {@code true} in all cases to indicate the command was handled
   */
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
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      p.getWorld().setStorm(true);
      p.getWorld().setThundering(false);
      p.getWorld().setWeatherDuration(1000000);
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WEATHER_RAIN, p.getWorld().getName()));
      return true;
    }

    World world = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getWorld(args[0]);

    if (world == null) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WORLD_NOT_LOADED, args[0]));
      return true;
    }

    world.setStorm(true);
    world.setThundering(false);
    world.setWeatherDuration(1000000);
    p.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_WEATHER_RAIN, world.getName()));
    return true;
  }

  /**
   * Returns the sub-commands associated with this command. This command has no sub-commands.
   *
   * @return an empty array of {@link CommandsEnum}
   */
  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  /**
   * Provides tab completion suggestions for the rain command. Returns available world names as
   * suggestions for the first argument. Returns an empty list if the sender lacks "mod"
   * authorization or if more than one argument is present.
   *
   * @param commandSender the entity requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias used
   * @param strings       the current arguments typed by the sender
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
