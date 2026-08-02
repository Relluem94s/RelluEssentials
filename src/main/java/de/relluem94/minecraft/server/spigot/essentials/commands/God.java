package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("god")
public class God implements CommandConstruct {

  private GroupService groupService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
  }

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {

    if (isCMDBlock(sender) || isConsole(sender)) {
      if (args.length < 1) {
        sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
        return true;
      }

      toggleGodMode(sender, args[0]);
      return true;
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!groupService.isSenderAuthorized(p, "mod")) {
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length > 0) {
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    toggleGodMode(p);
    return true;
  }

  private void toggleGodMode(@NotNull Player p) {
    p.sendMessage(!p.isInvulnerable()
        ? languageHelper.getWithPrefix(MessageKey.COMMAND_GOD_ON)
        : languageHelper.getWithPrefix(MessageKey.COMMAND_GOD_OFF));
    p.setInvulnerable(!p.isInvulnerable());
  }

  private void toggleGodMode(CommandSender sender, String targetName) {
    Player target = Bukkit.getPlayer(targetName);

    if (target == null) {
      sender.sendMessage(
          languageHelper.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, targetName));
      return;
    }

    toggleGodMode(target);
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!groupService.isSenderAuthorized(commandSender, "mod")) {
      return tabList;
    }

    if (isPlayer(commandSender)) {
      return tabList;
    }

    if (strings.length > 1) {
      return tabList;
    }

    tabList.addAll(TabCompleterHelper.getOnlinePlayers());

    return tabList;
  }
}