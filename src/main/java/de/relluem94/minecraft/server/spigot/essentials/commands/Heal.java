package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("heal")
public class Heal implements CommandConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
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
        sender.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
        return true;
      }

      heal(sender, args[0]);
      return true;
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length > 0) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    heal(p);
    return true;
  }

  private void heal(@org.jspecify.annotations.NonNull Player p) {
    p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.MAX_HEALTH)).getDefaultValue());
    p.setFoodLevel(20);
    p.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_HEAL));
  }

  private void heal(CommandSender sender, String targetName) {
    Player target = Bukkit.getPlayer(targetName);

    if (target == null) {
      sender.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, targetName));
      return;
    }

    heal(target);
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
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
