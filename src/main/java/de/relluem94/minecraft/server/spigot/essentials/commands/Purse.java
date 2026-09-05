package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_COINS;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.CoinHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Handles the /purse command for managing player coin purses.
 * Allows players to check their own balance or withdraw coins as physical items.
 * Moderators can additionally inspect the balance of other online players.
 */
@CommandName("purse")
public class Purse implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for this command to function.
   *
   * @param context the {@link ServiceContext} providing access to all necessary services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Executes the /purse command.
   * With the following behavior based on provided arguments:
   * <ul>
   *   <li>No arguments: displays the executing player's current purse balance.</li>
   *   <li>Player name as argument: displays the target player's balance
   *   (requires moderator permission).</li>
   *   <li>Integer as argument: withdraws the specified amount of coins from the player's purse
   *   and adds the corresponding coin item to the inventory.</li>
   * </ul>
   *
   * @param sender  the entity executing the command, must be a player
   * @param command the command that was executed
   * @param label   the alias used to trigger the command
   * @param args    optional arguments; either a target player name or a coin amount to withdraw
   * @return {@code true} in all cases to signal that the command was handled
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

    if (!serviceContext.getGroupService().isSenderAuthorized(sender, "user")) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PURSE_TOTAL, StringHelper.formatDouble(pe.getPurse())));
      return true;
    }

    Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getPlayer(args[0]);
    if (target != null) {
      if (serviceContext.getGroupService().isSenderAuthorized(sender, "mod")) {
        PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(target);
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_PURSE_TOTAL_OTHER, target.getCustomName(),
                StringHelper.formatDouble(pe.getPurse())));
      } else {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      }
      return true;
    }

    if (!TypeHelper.isInt(args[0])) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PURSE_TO_ITEM_VALUE_INVALID));
      return true;
    }

    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
    double purse = pe.getPurse();
    int coins = Math.abs(Integer.parseInt(args[0]));

    if (purse >= coins) {
      pe.setPurse(pe.getPurse() - coins);
      pe.setHasToBeUpdated(true);
      pe.setUpdatedBy(pe.getId());

      CustomItem coinItem = serviceContext.getItemService().find(
          new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
              PLUGIN_ITEM_NAMESPACE_COINS)).orElseThrow();

      p.getInventory().addItem(CoinHelper.buildCoinItem(coins, coinItem));
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PURSE_TO_ITEM, StringHelper.formatInt(coins)));
    } else {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PURSE_TO_ITEM_NOT_ENOUGH_MONEY));
    }
    return true;
  }

  /**
   * Returns the sub-commands associated with this command construct.
   *
   * @return an empty array, as the purse command has no sub-commands
   */
  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  /**
   * Provides tab-completion suggestions for the /purse command.
   * Returns the names of all currently online players when the sender has moderator permission
   * and exactly one argument is being typed. Returns an empty list otherwise.
   *
   * @param commandSender the entity requesting tab-completion
   * @param command the command for which completion is requested
   * @param s the alias used to trigger the command
   * @param strings the current argument tokens provided by the sender
   * @return a list of online player names, or an empty list if conditions are not met
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
      return List.of();
    }
    if (strings.length != 1) {
      return List.of();
    }

    return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
  }
}