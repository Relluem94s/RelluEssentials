package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.OfflinePlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.rellulib.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for the {@code /sudo} command.
 *
 * <p>Allows authorized administrators to temporarily assume the identity of another player,
 * inheriting their group, inventory, homes, purse, and display name. The command also supports
 * dispatching arbitrary server commands via the console sender.
 *
 * <p>While in sudo mode, the executing player's original {@link PlayerEntry} is preserved in
 * {@link SudoManager#sudoers} and fully restored upon exiting sudo mode.
 */
@CommandName("sudo")
public class Sudo implements CommandConstruct {

  private ServiceContext serviceContext;

  /**
   * Exits the sudo mode for the given player and restores their original identity.
   *
   * <p>Saves the current world group inventory, restores the original {@link PlayerEntry} data
   * including group, custom name, homes, and purse, reloads the original world group inventory, and
   * removes the player from {@link SudoManager#sudoers}.
   *
   * @param p              the player who is currently in sudo mode and should exit it
   * @param serviceContext the service context used to access player, world group, and translation
   *                       services
   */
  public static void exitSudo(@NotNull Player p, ServiceContext serviceContext) {
    PlayerEntry tpe = SudoManager.sudoers.get(p.getUniqueId());
    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
    serviceContext.getWorldGroupService().saveWorldGroupInventoryForPlayer(p, true);
    pe.setId(tpe.getId());
    pe.setCustomName(tpe.getCustomName());
    pe.setGroup(tpe.getGroup());
    pe.setHomes(tpe.getHomes());
    pe.setPurse(tpe.getPurse());
    p.setCustomName(tpe.getGroup().getPrefix() + p.getName());
    if (tpe.getCustomName() != null) {
      p.setCustomName(tpe.getGroup().getPrefix() + tpe.getCustomName());
    }
    serviceContext.getWorldGroupService().loadWorldGroupInventoryForPlayer(p);
    SudoManager.sudoers.remove(p.getUniqueId());
    p.sendMessage(
        serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_SUDO_DEACTIVATED));
  }

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
   * Handles execution of the {@code /sudo} command.
   *
   * <p>Behavior depends on the provided arguments:
   * <ul>
   *   <li>If the first argument matches a known plugin command,
   *   it is dispatched via the console sender.</li>
   *   <li>If the player is already in sudo mode and no command is dispatched,
   *   sudo mode is exited.</li>
   *   <li>Otherwise, the player enters sudo mode as the specified target player.</li>
   * </ul>
   *
   * <p>Requires the sender to be an authorized player with the {@code admin} group permission.
   *
   * @param sender  the command sender, must be an in-game player
   * @param command the executed command
   * @param label   the alias used to trigger the command
   * @param args    the command arguments; the first argument is either a target player name or a
   *                command name
   * @return {@code true} in all cases to suppress default usage output
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (!serviceContext.getGroupService().isSenderAuthorized(sender, "admin")) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (args.length == 0) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
      return true;
    }

    if (RelluEssentials.getInstance().getCommand(args[0]) != null) {
      dispatchCommand(args);
      return true;
    }

    if (args.length != 1) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
      return true;
    }

    if (SudoManager.sudoers.containsKey(p.getUniqueId())) {
      exitSudo(p, serviceContext);
      return true;
    }

    OfflinePlayerEntry target = PlayerHelper.getOfflinePlayerByName(args[0]);
    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);

    if (target == null) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_SUDO_PLAYER_NOT_FOUND, args[0]));
      return true;
    }

    if (serviceContext.getPlayerService().getPlayerEntry(target.getId()) == null) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_SUDO_PLAYER_NOT_FOUND, args[0]));
      return true;
    }

    PlayerEntry tpe = serviceContext.getPlayerService().getPlayerEntry(target.getId());
    SudoManager.sudoers.put(p.getUniqueId(), new PlayerEntry(pe));
    serviceContext.getWorldGroupService().saveWorldGroupInventoryForPlayer(p, true);
    pe.setId(tpe.getId());
    pe.setCustomName(tpe.getCustomName());
    pe.setGroup(tpe.getGroup());
    pe.setHomes(tpe.getHomes());
    pe.setPurse(tpe.getPurse());
    p.setCustomName(tpe.getGroup().getPrefix() + target.getName());
    if (tpe.getCustomName() != null) {
      p.setCustomName(tpe.getGroup().getPrefix() + tpe.getCustomName());
    }
    serviceContext.getWorldGroupService().loadWorldGroupInventoryForPlayer(p);
    p.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SUDO_ACTIVATED,
            tpe.getGroup().getPrefix() + target.getName()));

    return true;
  }

  private void dispatchCommand(String[] args) {
    ConsoleCommandSender console = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getConsoleSender();
    serviceContext.getPluginMetadataService().getPlugin().getServer()
        .dispatchCommand(console, StringUtils.toString(args));
  }

  /**
   * Returns the sub-commands associated with this command.
   *
   * @return an empty array, as this command defines no sub-commands
   */
  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  /**
   * Provides tab completion suggestions for the {@code /sudo} command.
   *
   * <p>For the first argument, suggests all online player names as well as all registered command
   * names.
   * For the second argument, suggests all online player names only. Returns an empty list if the
   * sender is not an authorized in-game player.
   *
   * @param commandSender the sender requesting tab completion
   * @param command       the command being tab-completed
   * @param s             the alias used
   * @param strings       the current argument tokens entered by the sender
   * @return a list of tab completion suggestions, never {@code null}
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "admin")) {
      return tabList;
    }

    if (!isPlayer(commandSender)) {
      return tabList;
    }

    if (strings.length == 1) {
      tabList.addAll(TabCompleterHelper.getOnlinePlayers());
      tabList.addAll(serviceContext.getCommandService().getAllCommandNames());
      return tabList;
    }

    if (strings.length == 2) {
      tabList.addAll(TabCompleterHelper.getOnlinePlayers());
      return tabList;
    }

    return tabList;
  }
}