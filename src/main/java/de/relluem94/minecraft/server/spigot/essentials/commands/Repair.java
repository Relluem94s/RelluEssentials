package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("repair")
public class Repair implements CommandConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
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

    if (args.length == 0) {
      ItemStack item = p.getInventory().getItemInMainHand();
      ItemMeta im = item.getItemMeta();

      if (im instanceof Damageable dmg && dmg.hasDamage()) {
        dmg.setDamage(0);
        item.setItemMeta(im);
        p.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.COMMAND_REPAIR, item.getType().name()));
      } else {
        p.sendMessage(
            serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_CANNOT_REPAIR,
                item.getType().name()));
      }
    } else {
      Player target = Bukkit.getPlayer(args[0]);
      if (target == null) {
        p.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
        return true;
      }

      ItemStack item = target.getInventory().getItemInMainHand();
      ItemMeta im = item.getItemMeta();

      if (im instanceof Damageable dmg && dmg.hasDamage()) {
        dmg.setDamage(0);
        item.setItemMeta(im);
        p.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.COMMAND_REPAIR, item.getType().name()));
        target.sendMessage(
            serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_REPAIR_PLAYER,
                item.getType().name()));
      } else {
        p.sendMessage(
            serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_CANNOT_REPAIR,
                item.getType().name()));
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
