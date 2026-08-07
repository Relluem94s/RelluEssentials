package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.MessageService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("where")
public class Where implements CommandConstruct {

  private GroupService groupService;
  private MessageService messageService;
  private TranslationService translationService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
    this.messageService = context.getMessageService();
    this.translationService = context.getTranslationService();
  }


  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String @NotNull [] args) {
    if (args.length > 0) {
      if (!groupService.isSenderAuthorized(sender, "mod")) {
        sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
        return true;
      }

      where(sender, args[0]);
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

    where(sender, p);
    return true;
  }

  private void where(CommandSender commandSender, String targetArg) {
    Player target = Bukkit.getPlayer(targetArg);
    if (target == null) {
      commandSender.sendMessage(
          translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, targetArg));
      return;
    }

    where(commandSender, target);
  }

  private void where(@NotNull CommandSender sender, @NotNull Player target) {
    sender.sendMessage(
        translationService.getWithPrefix(MessageKey.COMMAND_WHERE, target.getCustomName(),
            messageService.locationToString(target.getLocation())));
  }

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
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

    tabList.addAll(TabCompleterHelper.getOnlinePlayers());
    return tabList;
  }
}
