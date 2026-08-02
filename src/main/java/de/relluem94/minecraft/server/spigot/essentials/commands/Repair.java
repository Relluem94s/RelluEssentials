package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("repair")
public class Repair implements CommandConstruct {

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

    if (!groupService.isSenderAuthorized(p, "mod")) {
      sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      ItemStack item = p.getInventory().getItemInMainHand();
      ItemMeta im = item.getItemMeta();

      if (im instanceof Damageable dmg && dmg.hasDamage()) {
        dmg.setDamage(0);
        item.setItemMeta(im);
        p.sendMessage(
            languageHelper.getWithPrefix(MessageKey.COMMAND_REPAIR, item.getType().name()));
      } else {
        p.sendMessage(
            languageHelper.getWithPrefix(MessageKey.COMMAND_CANNOT_REPAIR, item.getType().name()));
      }
    } else {
      Player target = Bukkit.getPlayer(args[0]);
      if (target == null) {
        p.sendMessage(
            languageHelper.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
        return true;
      }

      ItemStack item = target.getInventory().getItemInMainHand();
      ItemMeta im = item.getItemMeta();

      if (im instanceof Damageable dmg && dmg.hasDamage()) {
        dmg.setDamage(0);
        item.setItemMeta(im);
        p.sendMessage(
            languageHelper.getWithPrefix(MessageKey.COMMAND_REPAIR, item.getType().name()));
        target.sendMessage(
            languageHelper.getWithPrefix(MessageKey.COMMAND_REPAIR_PLAYER, item.getType().name()));
      } else {
        p.sendMessage(
            languageHelper.getWithPrefix(MessageKey.COMMAND_CANNOT_REPAIR, item.getType().name()));
      }
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
    List<String> tabList = new ArrayList<>();

    if (!groupService.isSenderAuthorized(commandSender, "mod")) {
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
