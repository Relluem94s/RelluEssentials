package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CHAT_CONSOLE;
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
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for the /poke command. Allows authorized players to poke other online
 * players by playing effects and displaying a title.
 */
@CommandName("poke")
public class Poke implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Injects the service context required for this command to access services.
   *
   * @param context the {@link ServiceContext} providing access to all required services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Handles the execution of the /poke command. Validates sender permissions and target player
   * existence before poking the target.
   *
   * @param sender  the entity that executed the command
   * @param command the command that was executed
   * @param label   the alias of the command that was used
   * @param args    the arguments passed to the command, where {@code args[0]} is the target player
   *                name
   * @return {@code true} in all cases to indicate the command was handled
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (args.length == 0) {
      if (!serviceContext.getGroupService().isSenderAuthorized(sender, "vip")) {
        sender.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
        return true;
      }

      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_POKE));
      return true;
    }

    Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getPlayer(args[0]);
    if (target == null) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    if (!serviceContext.getGroupService().isSenderAuthorized(sender, "vip")) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    String senderName = isPlayer(sender) ? ((Player) sender).getDisplayName()
        : PLUGIN_NAME_CHAT_CONSOLE + sender.getName();
    poke(target);
    target.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_POKE_MESSAGE_TARGET, senderName));
    sender.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_POKE_MESSAGE_SENDER, target.getDisplayName()));
    return true;
  }

  private void poke(@NotNull Player target) {
    target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10F, 0F);
    target.getWorld().playEffect(target.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
    target.getWorld().playEffect(target.getLocation(), Effect.EXTINGUISH, 5);
    target.getWorld().playEffect(target.getLocation(), Effect.ENDERDRAGON_GROWL, 5);
    target.sendTitle(serviceContext.getTranslationService().get(MessageKey.COMMAND_POKE_TITLE),
        serviceContext.getTranslationService().get(MessageKey.COMMAND_POKE_SUBTITLE), 5, 80, 5);
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
   * Provides tab completion suggestions for the /poke command. Returns a list of online player
   * names when the sender is an authorized player and only one argument is being typed.
   *
   * @param commandSender the entity requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias of the command that was used
   * @param strings       the arguments currently entered by the sender
   * @return a list of online player names, or an empty list if the sender is unauthorized, not a
   *     player, or more than one argument is present
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "user")) {
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