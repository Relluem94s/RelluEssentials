package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("setGroup")
public class PermissionsGroup implements CommandConstruct {


  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String @NotNull [] args) {

    if (args.length < 2) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
      return true;
    }

    if (args.length > 2) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    OfflinePlayer target = PlayerHelper.getOfflinePlayer(args[0]);

    if (target == null) {
      sender.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    if (serviceContext.getPlayerService().getPlayerEntry(target.getPlayer()) == null) {
      sender.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    if (isPlayer(sender)) {
      Player p = (Player) sender;
      Optional<GroupEntry> groupEntry = serviceContext.getGroupService()
          .resolveAuthorizedGroup(p, args[1]);

      if (groupEntry.isEmpty()) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_SETGROUP_GROUP_NOT_FOUND, args[1]));
        return true;
      }

      serviceContext.getPlayerService().updateGroup(target, groupEntry.get());
      notifySenderAndTarget(sender, groupEntry.get(), target);
      return true;
    } else if (isCMDBlock(sender) || isConsole(sender)) {
      GroupEntry g = serviceContext.getGroupService().resolveGroupWithFallback(args[1]);
      serviceContext.getPlayerService().updateGroup(target, g);
      notifySenderAndTarget(sender, g, target);
      return true;
    }
    return false;
  }

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
      return new ArrayList<>();
    }

    if (strings.length > 2) {
      return new ArrayList<>();
    }

    if (strings.length == 1) {
      return TabCompleterHelper.getOnlinePlayers();
    }

    return TabCompleterHelper.getGroups(serviceContext.getGroupRegistry().getAll());
  }

  private void notifySenderAndTarget(@NotNull CommandSender sender, @NotNull GroupEntry g,
      @NotNull OfflinePlayer target) {
    String groupDisplayName = g.getPrefix() + g.getName();
    sender.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_SETGROUP, groupDisplayName, target.getName()));

    if (target.isOnline()) {
      Player onlineTarget = Bukkit.getPlayer(target.getUniqueId());
      if (onlineTarget != null) {
        onlineTarget.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_SETGROUP, groupDisplayName, target.getName()));
      }
    }
  }
}