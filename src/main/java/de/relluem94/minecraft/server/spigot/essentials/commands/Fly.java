package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for toggling flight mode.
 *
 * <p>Allows authorized players to enable or disable flight mode for themselves.
 * Players with moderator privileges can additionally toggle flight mode for other online players.
 */
@CommandName("fly")
public class Fly implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for this command to operate.
   *
   * @param context the {@link ServiceContext} providing access to all required services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Returns the sub-commands associated with this command.
   *
   * @return an empty array as this command has no sub-commands
   */
  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  /**
   * Handles the execution of the fly command.
   *
   * <p>Toggles flight mode for the executing player if no arguments are provided.
   * If a target player name is given as an argument,
   * moderators can toggle flight mode for that player.
   *
   * @param sender  the entity that executed the command
   * @param command the command that was executed
   * @param label   the alias used to trigger the command
   * @param args    optional arguments; the first argument may be the name of a target player
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

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "vip")) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      flyMode(p);
      return true;
    }

    Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getPlayer(args[0]);

    if (target == null) {
      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    if (serviceContext.getGroupService().isSenderAuthorized(sender, "mod")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_FLYMODE, target.getCustomName(),
              !target.getAllowFlight() ? serviceContext.getTranslationService()
                  .get(MessageKey.COMMAND_FLYMODE_ACTIVATED)
                  : serviceContext.getTranslationService()
                      .get(MessageKey.COMMAND_FLYMODE_DEACTIVATED)));
      flyMode(target);
    } else {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));

    }
    return true;
  }

  private void flyMode(@NotNull Player p) {
    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
    pe.setFlying(!pe.isFlying());
    pe.setUpdatedBy(pe.getId());
    pe.setHasToBeUpdated(true);
    p.setAllowFlight(pe.isFlying());
    p.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_FLYMODE, p.getCustomName(),
            p.getAllowFlight() ? serviceContext.getTranslationService()
                .get(MessageKey.COMMAND_FLYMODE_ACTIVATED) : serviceContext.getTranslationService()
                .get(MessageKey.COMMAND_FLYMODE_DEACTIVATED)));
  }

  /**
   * Provides tab-completion suggestions for the fly command.
   *
   * <p>Returns a list of online player names as suggestions for the first argument,
   * but only if the sender has moderator privileges. Returns an empty list otherwise
   * or when more than one argument has already been entered.
   *
   * @param commandSender the entity requesting tab-completion
   * @param command       the command being tab-completed
   * @param s             the alias used to trigger the command
   * @param strings       the current arguments entered by the sender
   * @return a list of online player names, or an empty list if unauthorized or too many arguments
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