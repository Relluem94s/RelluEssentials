package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Command implementation for the marry system, allowing players to send marriage requests, accept
 * them, and divorce their partner. Handles pending request tracking, partner persistence, and
 * automatic permission sharing between married players via the protection service.
 */
@CommandName("marry")
public class Marry implements CommandConstruct {

  private final HashMap<Player, Player> marryAcceptList = new HashMap<>();
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

  @Override
  public CommandsEnum[] getCommands() {
    return Commands.values();
  }

  private void addMarryEntry(Player player, Player target) {
    if (serviceContext.getPlayerService().getPlayerEntry(player).getPartner() != null
        || serviceContext.getPlayerService().getPlayerEntry(target).getPartner() != null) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_MARRY_REQUEST_IS_MARRIED));
      return;
    }

    player.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_MARRY_SEND_REQUEST, target.getCustomName()));
    target.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_MARRY_RECEIVE_REQUEST, player.getCustomName()));

    marryAcceptList.put(target, player);
    serviceContext.getSchedulerService().runTaskLater(() -> {
      if (hasMarryEntry(target)) {
        player.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MARRY_REQUEST_EXPIRED));
        target.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MARRY_REQUEST_EXPIRED));
        removeMarryEntry(target);
      }
    }, 20 * 60 * 2L);
  }

  private boolean hasMarryEntry(Player target) {
    return marryAcceptList.containsKey(target);
  }

  private void removeMarryEntry(Player target) {
    marryAcceptList.remove(target);
  }

  private void marry(@NotNull Player player, @NotNull Player target) {
    target.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_MARRY_MARRIED, player.getCustomName()));
    player.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_MARRY_MARRIED, target.getCustomName()));

    PlayerEntry firstPlayer = serviceContext.getPlayerService()

        .getPlayerEntry(target);
    PlayerEntry secondPlayer = serviceContext.getPlayerService()

        .getPlayerEntry(player);

    PlayerPartnerEntry playerPartnerEntry = new PlayerPartnerEntry();
    playerPartnerEntry.setCreatedBy(firstPlayer.getId());
    playerPartnerEntry.setFirstPartnerId(firstPlayer.getId());
    playerPartnerEntry.setSecondPartnerId(secondPlayer.getId());

    serviceContext.getPlayerService().savePartner(playerPartnerEntry);
    playerPartnerEntry = serviceContext.getPlayerService().getPartner(firstPlayer);

    firstPlayer.setPartner(playerPartnerEntry);
    secondPlayer.setPartner(playerPartnerEntry);

    serviceContext.getProtectionService().getProtectionEntriesOwnedBy(firstPlayer.getId()).forEach(
        pre -> serviceContext.getProtectionActionService()
            .addRight(target, pre, secondPlayer.getId(), true));

    serviceContext.getProtectionService().getProtectionEntriesOwnedBy(secondPlayer.getId()).forEach(
        pre -> serviceContext.getProtectionActionService()
            .addRight(player, pre, firstPlayer.getId(), true));
  }

  private void divorce(@NotNull PlayerEntry pe) {
    PlayerPartnerEntry ppe = pe.getPartner();

    PlayerEntry secondPlayerEntry = serviceContext.getPlayerService()
        .getPlayerEntryByInternalId(
            ppe.getSecondPartnerId() != pe.getId() ? ppe.getSecondPartnerId()
                : ppe.getFirstPartnerId());

    if (pe.getUuid() == null) {
      return;
    }

    if (secondPlayerEntry == null) {
      return;
    }

    if (secondPlayerEntry.getUuid() == null) {
      return;
    }

    Player firstPlayer = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getPlayer(UUID.fromString(pe.getUuid()));
    OfflinePlayer secondOfflinePlayer = serviceContext.getPluginMetadataService().getPlugin()
        .getServer().getOfflinePlayer(UUID.fromString(secondPlayerEntry.getUuid()));

    if (firstPlayer != null && secondOfflinePlayer.getName() != null) {
      Player secondPlayer = serviceContext.getPluginMetadataService().getPlugin().getServer()
          .getPlayer(secondOfflinePlayer.getName());
      if (secondOfflinePlayer.isOnline() && secondPlayer != null) {
        firstPlayer.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MARRY_DIVORCED, secondPlayer.getDisplayName()));
        secondPlayer.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MARRY_DIVORCED, firstPlayer.getCustomName()));
      } else {
        firstPlayer.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MARRY_DIVORCED, secondOfflinePlayer.getName()));
      }

      ppe.setDeletedBy(pe.getId());
      pe.setPartner(null);
      secondPlayerEntry.setPartner(null);

      serviceContext.getPlayerService().deletePartner(ppe);

      Collection<ProtectionEntry> protectionEntryList = new ArrayList<>(
          serviceContext.getProtectionService().getAllProtectionEntries().values());

      for (ProtectionEntry pre : protectionEntryList) {
        if (pre.getCreatedBy() == pe.getId()) {
          serviceContext.getProtectionActionService()
              .removeRight(firstPlayer, pre, secondPlayerEntry.getId(), true);
        }

        if (pre.getCreatedBy() == secondPlayerEntry.getId()) {
          serviceContext.getProtectionActionService().removeRight(pre, pe.getId());
        }

      }
    }
  }

  /**
   * Handles execution of the marry command. Supports three interactions: sending a marriage request
   * to another player, accepting a pending request, and divorcing the current partner. Requires the
   * sender to be a player with the {@code vip} group.
   *
   * @param sender  the command sender, must be a player
   * @param command the executed command
   * @param label   the alias used to trigger the command
   * @param args    the command arguments; expects zero or one argument
   * @return {@code true} if the command was handled, {@code false} if the sender is not a player
   */
  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    Player p = null;
    if (isPlayer(sender)) {
      p = (Player) sender;
    }

    if (p == null) {
      return false;
    }

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "vip")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_MARRY_INFO, command.getName(), command.getName(),
              Commands.ACCEPT.getName(), command.getName(), Commands.DIVORCE.getName()));
      return true;
    }

    if (args.length == 1) {
      if (args[0].equalsIgnoreCase(Commands.ACCEPT.getName())) {
        if (hasMarryEntry(p)) {
          marry(p, marryAcceptList.get(p));
          removeMarryEntry(p);
          return true;
        }

        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MARRY_ACCEPT_NO_REQUEST));
        return true;
      }

      if (args[0].equalsIgnoreCase(Commands.DIVORCE.getName())) {
        PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
        if (serviceContext.getPlayerService().getPlayerEntry(p).getPartner() != null) {
          divorce(pe);
          return true;
        }

        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MARRY_DIVORCE_NOT_MARRIED));
        return true;
      }

      Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
          .getPlayer(args[0]);
      if (target == null) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
        return true;
      }

      if (target.getName().equalsIgnoreCase(p.getName())) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MARRY_SELF_MARRIAGE));
        return true;
      }

      addMarryEntry(p, target);

      return true;
    }

    p.sendMessage(
        serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
    return true;
  }

  /**
   * Provides tab completion for the marry command. Returns available sub-commands and online player
   * names for the first argument. Returns an empty list if the sender lacks the {@code user} group,
   * is not a player, or has already entered more than one argument.
   *
   * @param commandSender the sender requesting tab completion
   * @param command       the command being completed
   * @param s             the alias used
   * @param strings       the current arguments entered by the sender
   * @return a list of applicable tab completion suggestions
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

    tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
    tabList.addAll(TabCompleterHelper.getOnlinePlayers());

    return tabList;
  }

  /**
   * Defines the available sub-commands for the broadcast command.
   * Each entry represents a distinct broadcast mode.
   */
  @Getter
  public enum Commands implements CommandsEnum {

    ACCEPT("accept"), DIVORCE("divorce");

    private final String name;
    private final String[] subCommands;

    Commands(String name, String... subCommands) {
      this.name = name;
      this.subCommands = subCommands;
    }
  }
}