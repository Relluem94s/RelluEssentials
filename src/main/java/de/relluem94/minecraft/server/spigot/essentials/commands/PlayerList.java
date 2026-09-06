package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for listing all online players visible to the sender.
 *
 * <p>Displays a header with the total player count followed by an entry for each
 * online player, including their group prefix, custom name, and group name. If the sender is a
 * {@link Player}, hidden players are excluded from the output.
 * </p>
 */
@CommandName("list")
public class PlayerList implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the {@link ServiceContext} required to access plugin services.
   *
   * @param context the service context providing access to translation, player, and plugin metadata
   *                services
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
   * Executes the player list command by sending the sender a header and an entry
   * for each visible online player.
   *
   * <p>If the sender is a {@link Player}, players that are not visible to the sender
   * via {@link Player#canSee(Player)} are skipped.
   * </p>
   *
   * @param sender  the entity that executed the command
   * @param command the command that was executed
   * @param label   the alias used to trigger the command
   * @param args    the arguments provided with the command
   * @return {@code true} to indicate the command was handled successfully
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {

    Collection<? extends Player> onlinePlayers = serviceContext.getPluginMetadataService()
        .getPlugin().getServer().getOnlinePlayers();
    sender.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_LIST_HEADER, onlinePlayers.size()));
    for (Player player : onlinePlayers) {
      if (sender instanceof Player p) {
        if (!p.canSee(player)) {
          continue;
        }
      }
      PlayerEntry pet = serviceContext.getPlayerService()

          .getPlayerEntry(player);
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_LIST_ENTRY, pet.getGroup().getPrefix(),
              pet.getCustomName(), pet.getGroup().getPrefix(), pet.getGroup().getName()));
    }

    return true;
  }

  /**
   * Provides tab-completion suggestions for the list command.
   *
   * @param commandSender the entity requesting tab-completion
   * @param command       the command being tab-completed
   * @param s             the alias used to trigger the command
   * @param strings       the current arguments provided by the sender
   * @return an empty list, as this command has no tab-completion suggestions
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return new ArrayList<>();
  }
}