package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PermissionHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("enderchest")
public class Enderchest implements CommandConstruct {

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (!isPlayer(sender)) {
      sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!PermissionHelper.isAuthorized(p, GroupRegistry.getGroup("vip").getId())) {
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      p.openInventory(p.getEnderChest());
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ENDERCHEST));
      return true;
    }

    if (Bukkit.getPlayer(args[0]) == null) {
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    Player target = Objects.requireNonNull(Bukkit.getPlayer(args[0])).getPlayer();

    if (target == null) {
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    if (!PermissionHelper.isAuthorized(p, GroupRegistry.getGroup("mod").getId())) {
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    p.openInventory(target.getEnderChest());
    p.sendMessage(
        languageHelper.getWithPrefix(MessageKey.COMMAND_ENDERCHEST_PLAYER, target.getCustomName()));
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!PermissionHelper.isAuthorized(commandSender, GroupRegistry.getGroup("mod").getId())) {
      return new ArrayList<>();
    }

    if (strings.length > 1) {
      return new ArrayList<>();
    }

    return TabCompleterHelper.getOnlinePlayers();
  }
}