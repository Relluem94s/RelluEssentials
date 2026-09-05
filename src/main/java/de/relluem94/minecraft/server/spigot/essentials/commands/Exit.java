package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_WORLD_LOBBY;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command handler for the {@code /exit} command.
 *
 * <p>When executed by the console, broadcasts a shutdown message, teleports all online players
 * to the lobby world, kicks them, and then shuts down the server.
 *
 * <p>When executed by a player, teleports the player to the lobby world and kicks them.
 */
@CommandName("exit")
public class Exit implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the {@link ServiceContext} used to access all required services.
   *
   * @param context the service context to inject
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
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
   * Handles the {@code /exit} command execution.
   *
   * <p>If the sender is the console, broadcasts a shutdown message, schedules all online players
   * to be teleported to the lobby world and kicked, then schedules a server shutdown.
   *
   * <p>If the sender is a player with the required {@code user} group permission, teleports
   * the player to the lobby world and kicks them.
   *
   * @param sender  the source of the command
   * @param command the command that was executed
   * @param label   the alias used
   * @param args    the arguments passed to the command
   * @return {@code true} in all cases to indicate the command was handled
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (isConsole(sender)) {
      serviceContext.getPluginMetadataService().getPlugin().getServer().broadcastMessage(
          serviceContext.getTranslationService().get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN));

      serviceContext.getSchedulerService().runTaskLater(
          () -> serviceContext.getPluginMetadataService().getPlugin().getServer().getOnlinePlayers()
              .forEach(op -> {
                serviceContext.getTeleportService().teleportWorld(op, PLUGIN_WORLD_LOBBY, true);
                op.kickPlayer(serviceContext.getTranslationService()
                    .get(MessageKey.COMMAND_EXIT_SERVER_SHUTTING_DOWN));
              }), 10L);

      serviceContext.getSchedulerService()
          .runTaskLater(serviceContext.getPluginMetadataService().getPlugin().getServer()::shutdown,
              20L);
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

    serviceContext.getTeleportService().teleportWorld(p, PLUGIN_WORLD_LOBBY, true);
    p.kickPlayer(serviceContext.getTranslationService().get(MessageKey.COMMAND_EXIT_KICK_MESSAGE));
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return new ArrayList<>();
  }
}
