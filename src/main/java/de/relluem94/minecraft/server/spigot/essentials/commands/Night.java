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
 * Command implementation that sets the time of a world to night.
 *
 * <p>If no arguments are provided, the time is set in the player's current world.</p>
 *
 * <p>Optionally, a world name can be passed as an argument to target a specific world.</p>
 *
 * <p>Requires the sender to be a player with at least the "mod" group authorization.</p>
 */
@CommandName("night")
public class Night implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the {@link ServiceContext} into this command, providing access to
   * required services such as translation, group authorization, and plugin metadata.
   *
   * @param context the service context to inject
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Executes the night command, setting the time to 18000 ticks in the target world.
   *
   * <p>If no arguments are provided, the player's current world is used.</p>
   *
   * <p>If a world name is provided as the first argument, that world is targeted instead.</p>
   *
   * <p>Sends appropriate feedback messages for missing permissions, invalid world names,
   * or successful time changes.</p>
   *
   * @param sender  the command sender, must be a player
   * @param command the executed command
   * @param label   the alias used
   * @param args    optional arguments; the first argument may be a world name
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
      p.getWorld().setTime(18000L);
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TIME_NIGHT, p.getWorld().getName()));
      return true;
    }

    World world = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getWorld(args[0]);

    if (world == null) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WORLD_NOT_LOADED, args[0]));
      return true;
    }

    world.setTime(18000L);
    p.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_TIME_NIGHT, world.getName()));
    return true;
  }

  /**
   * Returns the sub-commands associated with this command.
   *
   * @return an empty array, as this command has no sub-commands
   */
  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  /**
   * Provides tab completion for the night command.
   *
   * <p>Returns an empty list if the sender lacks "mod" group authorization or if
   * more than one argument has already been entered.</p>
   * Otherwise, returns a list of available world names.
   *
   * @param commandSender the sender requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias used
   * @param strings       the current arguments entered by the sender
   * @return a list of matching world names, or an empty list if not applicable
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