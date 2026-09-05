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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for repairing items held by a player.
 *
 * <p>When executed without arguments, repairs the item in the sender's main hand.
 * When executed with a player name as argument, repairs the item in that player's main hand.
 *
 * <p>Requires the sender to have at least the {@code mod} group permission.
 */
@CommandName("repair")
public class Repair implements CommandConstruct {

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
   * Executes the repair command for the sending player.
   *
   * <p>If no arguments are provided, the item in the sender's main hand is repaired.
   * If a player name is provided as the first argument, the item in that player's main hand is
   * repaired.
   *
   * @param sender  the entity that executed the command
   * @param command the command that was executed
   * @param label   the alias used to execute the command
   * @param args    optional arguments, where {@code args[0]} may specify a target player name
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
      ItemStack item = p.getInventory().getItemInMainHand();
      ItemMeta im = item.getItemMeta();

      if (im instanceof Damageable dmg && dmg.hasDamage()) {
        dmg.setDamage(0);
        item.setItemMeta(im);
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_REPAIR, item.getType().name()));
      } else {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_CANNOT_REPAIR, item.getType().name()));
      }
    } else {
      Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
          .getPlayer(args[0]);
      if (target == null) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
        return true;
      }

      ItemStack item = target.getInventory().getItemInMainHand();
      ItemMeta im = item.getItemMeta();

      if (im instanceof Damageable dmg && dmg.hasDamage()) {
        dmg.setDamage(0);
        item.setItemMeta(im);
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_REPAIR, item.getType().name()));
        target.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_REPAIR_PLAYER, item.getType().name()));
      } else {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_CANNOT_REPAIR, item.getType().name()));
      }
    }
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
   * Provides tab completion suggestions for the repair command.
   *
   * <p>Returns a list of online player names as suggestions for the first argument.
   * Returns an empty list if the sender is not a player, lacks {@code mod} group permission, or
   * more than one argument has already been provided.
   *
   * @param commandSender the entity requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias used
   * @param strings       the current arguments provided by the sender
   * @return a list of suggested player names, or an empty list if no suggestions are applicable
   */
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

    tabList.addAll(TabCompleterHelper.getOnlinePlayers());

    return tabList;
  }
}
