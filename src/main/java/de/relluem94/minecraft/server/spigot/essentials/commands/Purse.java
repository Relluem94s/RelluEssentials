package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.CoinHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PermissionHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Handles the /purse command for managing player coin purses. Allows players to check their balance
 * and withdraw coins as items. Moderators can additionally check the balance of other players.
 */
@CommandName("purse")
public class Purse implements CommandConstruct {

  private GroupService groupService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (!isPlayer(sender)) {
      sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!PermissionHelper.isAuthorized(sender, GroupRegistry.getGroup("user").getId())) {
      sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry()
          .getPlayerEntry(p.getUniqueId());
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PURSE_TOTAL,
          StringHelper.formatDouble(pe.getPurse())));
      return true;
    }

    Player target = Bukkit.getPlayer(args[0]);
    if (target != null) {
      if (PermissionHelper.isAuthorized(p, GroupRegistry.getGroup("mod").getId())) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry()
            .getPlayerEntry(target.getUniqueId());
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PURSE_TOTAL_OTHER,
            target.getCustomName(), StringHelper.formatDouble(pe.getPurse())));
      } else {
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      }
      return true;
    }

    if (!TypeHelper.isInt(args[0])) {
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PURSE_TO_ITEM_VALUE_INVALID));
      return true;
    }

    PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry()
        .getPlayerEntry(p.getUniqueId());
    double purse = pe.getPurse();
    int coins = Math.abs(Integer.parseInt(args[0]));

    if (purse >= coins) {
      pe.setPurse(pe.getPurse() - coins);
      pe.setHasToBeUpdated(true);
      pe.setUpdatedBy(pe.getId());

      p.getInventory().addItem(CoinHelper.buildCoinItem(coins));
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PURSE_TO_ITEM,
          StringHelper.formatInt(coins)));
    } else {
      p.sendMessage(
          languageHelper.getWithPrefix(MessageKey.COMMAND_PURSE_TO_ITEM_NOT_ENOUGH_MONEY));
    }
    return true;
  }

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!groupService.isSenderAuthorized(commandSender, "mod")) {
      return List.of();
    }
    if (strings.length != 1) {
      return List.of();
    }

    return Bukkit.getOnlinePlayers().stream()
        .map(Player::getName)
        .toList();
  }
}