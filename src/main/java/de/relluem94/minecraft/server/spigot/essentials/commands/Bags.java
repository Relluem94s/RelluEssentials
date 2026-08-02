package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PermissionHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.rellulib.utils.TypeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("bags")
public class Bags implements CommandConstruct {

  private GroupService groupService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!PermissionHelper.isAuthorized(commandSender, GroupRegistry.getGroup("user").getId())) {
      return tabList;
    }

    if (!isPlayer(commandSender)) {
      return tabList;
    }

    if (strings.length > 1) {
      return tabList;
    }

    tabList.addAll(TabCompleterHelper.getBags((Player) commandSender));

    return tabList;
  }

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

    if (!groupService.isSenderAuthorized(commandSender, "user")) {
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length != 1) {
      p.openInventory(
          BagHelper.getBags(RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p)));
      return true;
    }

    BagTypeEntry bte;
    if (TypeUtils.isInt(args[0])) {
      bte = BagHelper.getBagTypeById(Integer.parseInt(args[0]));
    } else {
      bte = BagHelper.getBagTypeByName(args[0]);
    }

    if (bte != null) {
      PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p);
      if (BagHelper.hasBag(pe.getId(), bte.getId())) {
        p.openInventory(Objects.requireNonNull(BagHelper.getBag(bte.getId(), pe)));
      } else {
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_BAGS_NOT_FOUND, args[0]));
      }

    } else {
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_BAGS_NOT_FOUND, args[0]));
    }
    return true;
  }
}