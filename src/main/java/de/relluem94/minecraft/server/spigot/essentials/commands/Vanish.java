package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("vanish")
public class Vanish implements CommandConstruct {

  private final List<Player> isVanished = new ArrayList<>();
  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String @NotNull [] args) {
    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_VANISH));
      boolean canSee = !isVanished.contains(p);

      for (Player onlinePlayer : serviceContext.getPluginMetadataService().getPlugin().getServer()
          .getOnlinePlayers()) {
        if (canSee) {
          onlinePlayer.hidePlayer(RelluEssentials.getInstance(), p);
          isVanished.add(p);
        } else {
          onlinePlayer.showPlayer(RelluEssentials.getInstance(), p);
          isVanished.remove(p);
        }
      }

      p.sendMessage(serviceContext.getTranslationService().getWithPrefix(
          canSee ? MessageKey.COMMAND_VANISH_ENABLE : MessageKey.COMMAND_VANISH_DISABLE,
          p.getCustomName()));

      return true;
    }

    Player target = serviceContext.getPluginMetadataService().getPlugin().getServer()
        .getPlayer(args[0]);
    if (target == null) {
      p.sendMessage(serviceContext.getTranslationService()
          .get(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    target.sendMessage(
        serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_VANISH));

    boolean canSee = false;
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      if (onlinePlayer.canSee(target)) {
        onlinePlayer.hidePlayer(RelluEssentials.getInstance(), target);
      } else {
        onlinePlayer.showPlayer(RelluEssentials.getInstance(), target);
        canSee = true;
      }
    }

    p.sendMessage(serviceContext.getTranslationService().getWithPrefix(
        canSee ? MessageKey.COMMAND_VANISH_ENABLE : MessageKey.COMMAND_VANISH_DISABLE,
        target.getCustomName()));

    return true;
  }

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

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