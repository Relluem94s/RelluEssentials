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
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation that sets the game mode of a player to {@link GameMode#CREATIVE}.
 *
 * <p>If an argument is provided, the command targets the specified online player.
 * Otherwise, the command targets the sender itself, requiring the sender to be a player.
 * Requires the sender to have the {@code mod} group authorization.</p>
 */
@CommandName("1")
public class GameModeCreative implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the {@link ServiceContext} into this command.
   *
   * @param context the service context providing access to all required services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Returns an empty array, as this command has no sub-commands.
   *
   * @return an empty {@link CommandsEnum} array
   */
  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  /**
   * Handles the execution of the creative game mode command.
   *
   * <p>If one argument is provided, the game mode of the specified target player is set to
   * {@link org.bukkit.GameMode#CREATIVE}. If no argument is provided, the sender itself is used as
   * the target, requiring the sender to be a {@link org.bukkit.entity.Player}.</p>
   *
   * @param sender  the entity that executed the command
   * @param command the command that was executed
   * @param label   the alias used to trigger the command
   * @param args    the arguments passed to the command
   * @return {@code true} in all cases to indicate that the command was handled
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
      @NonNull String label, String[] args) {
    if (!serviceContext.getGroupService().isSenderAuthorized(sender, "mod")) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 1) {
      Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
          .getPlayer(args[0]);

      if (target == null) {
        sender.sendMessage(serviceContext.getTranslationService()
            .get(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
        return true;
      }

      gameMode(target);
      return true;
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    gameMode((Player) sender);
    return true;
  }

  private void gameMode(@NotNull Player p) {
    p.setGameMode(GameMode.CREATIVE);
    p.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_GAMEMODE, p.getCustomName(),
            serviceContext.getTranslationService().get(MessageKey.COMMAND_GAMEMODE_CREATIVE)));
  }

  /**
   * Provides tab completion suggestions for the creative game mode command.
   *
   * <p>Returns a list of online player names as suggestions for the first argument.
   * Returns an empty list if the sender lacks {@code mod} authorization or more than one
   * argument is already present.</p>
   *
   * @param commandSender the entity requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias used to trigger the command
   * @param strings       the arguments currently entered by the sender
   * @return a list of matching online player names, or an empty list
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

    return TabCompleterHelper.getOnlinePlayers();
  }
}
