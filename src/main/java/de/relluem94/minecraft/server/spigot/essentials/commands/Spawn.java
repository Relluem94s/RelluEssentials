package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for teleporting players to the world spawn location.
 *
 * <p>Supports execution by players, console, and command blocks. Players with mod permissions
 * may teleport other online players to spawn. Command blocks support the {@code @p} argument
 * to target the nearest player.</p>
 */
@CommandName("spawn")
public class Spawn implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the {@link ServiceContext} into this command instance.
   *
   * @param context the service context providing access to all required services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Provides tab completion suggestions for the spawn command.
   *
   * <p>Returns a list of online player names when the sender has mod-level authorization
   * and only one argument is being completed. Returns an empty list otherwise.</p>
   *
   * @param commandSender the sender requesting tab completion
   * @param command the command being tab-completed
   * @param s the command alias used
   * @param strings the current command arguments
   * @return a list of matching player name suggestions, or an empty list if not applicable
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
      return tabList;
    }

    if (strings.length > 1) {
      return tabList;
    }

    if (isPlayer(commandSender) || isConsole(commandSender)) {
      tabList.addAll(TabCompleterHelper.getOnlinePlayers());
      return tabList;
    }

    return tabList;
  }

  /**
   * Executes the spawn command for the given sender and arguments.
   *
   * <p>Handles the following cases:</p>
   * <ul>
   *   <li>Command block with {@code @p}: teleports the nearest player to spawn.</li>
   *   <li>One argument: teleports the named online player to spawn, requires mod permission.</li>
   *   <li>No arguments: teleports the sending player to spawn, requires user permission.</li>
   * </ul>
   *
   * @param sender the entity executing the command
   * @param command the command being executed
   * @param label the alias used to trigger the command
   * @param args the arguments provided with the command
   * @return {@code true} in all cases to indicate the command was handled
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String @NotNull [] args) {
    if (isCMDBlock(sender) && args.length == 1 && args[0].equals("@p")) {
      BlockCommandSender bcs = (BlockCommandSender) sender;
      CommandBlock cb = (CommandBlock) bcs.getBlock().getState();
      Player p = PlayerHelper.getTargetedPlayer(cb.getBlock().getLocation());
      if (p == null) {
        sender.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER,
                serviceContext.getTranslationService().get(MessageKey.COMMAND_NO_PLAYER_IN_REACH)));
        return true;
      }

      spawn(p);
      return true;
    }

    if (args.length > 1) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    if (args.length == 1) {
      Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
          .getPlayer(args[0]);

      if (!serviceContext.getGroupService().isSenderAuthorized(sender, "mod")) {
        sender.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
        return true;
      }

      if (target == null) {
        sender.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
        return true;
      }

      spawn(target);
      return true;
    }

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

    spawn(p);
    return true;
  }

  /**
   * Teleports the given player to the spawn location of their current world.
   *
   * <p>Saves the player's current location as a back point before teleporting,
   * then sends a confirmation message including the world name.</p>
   *
   * @param p the player to teleport to spawn
   */
  public void spawn(Player p) {
    serviceContext.getBackService().saveBackPoint(p);

    Location coords = p.getWorld().getSpawnLocation();
    Location spawn = new Location(p.getWorld(), coords.getX(), coords.getY(), coords.getZ());

    p.teleport(spawn);
    p.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SPAWN, p.getWorld().getName()));
  }

  /**
   * Returns the sub-commands associated with this command construct.
   *
   * @return an empty array, as the spawn command has no sub-commands
   */
  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }
}
