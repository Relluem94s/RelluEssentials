package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("spawn")
public class Spawn implements CommandConstruct {

  private GroupService groupService;
  private TranslationService translationService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
    this.translationService = context.getTranslationService();
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!groupService.isSenderAuthorized(commandSender, "mod")) {
      return tabList;
    }

    if (strings.length > 1) {
      return tabList;
    }

    if (isPlayer(commandSender) || isConsole(commandSender)) {
      tabList.addAll(TabCompleterHelper.getOnlinePlayers());
      return tabList;
    }

    return tabList;
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String @NotNull [] args) {
    if (isCMDBlock(sender) && args.length == 1 && args[0].equals("@p")) {
      BlockCommandSender bcs = (BlockCommandSender) sender;
      CommandBlock cb = (CommandBlock) bcs.getBlock().getState();
      Player p = PlayerHelper.getTargetedPlayer(cb.getBlock().getLocation());
      if (p == null) {
        sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER,
            translationService.get(MessageKey.COMMAND_NO_PLAYER_IN_REACH)));
        return true;
      }

      spawn(p);
      return true;
    }

    if (args.length > 1) {
      sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    if (args.length == 1) {
      Player target = Bukkit.getPlayer(args[0]);

      if (!groupService.isSenderAuthorized(sender, "mod")) {
        sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
        return true;
      }

      if (target == null) {
        sender.sendMessage(
            translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
        return true;
      }

      spawn(target);
      return true;
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!groupService.isSenderAuthorized(p, "user")) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    spawn(p);
    return true;
  }

  public void spawn(Player p) {
    Back.addBackPoint(p);

    Location coords = p.getWorld().getSpawnLocation();
    Location spawn = new Location(p.getWorld(), coords.getX(), coords.getY(), coords.getZ());

    p.teleport(spawn);
    p.sendMessage(
        translationService.getWithPrefix(MessageKey.COMMAND_SPAWN, p.getWorld().getName()));
  }

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }
}
