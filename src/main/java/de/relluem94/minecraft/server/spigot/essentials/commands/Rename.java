package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;
import static de.relluem94.rellulib.utils.StringUtils.implode;
import static de.relluem94.rellulib.utils.StringUtils.replaceSymbols;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("rename")
public class Rename implements CommandConstruct {

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
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_RENAME_INFO));
      return true;
    }

    rename(p, args);
    return true;
  }

  private void rename(@NotNull Player p, String[] args) {
    String message = implode(0, args);
    message = replaceSymbols(replaceColor(message));
    ItemStack is = p.getInventory().getItemInMainHand();
    ItemMeta im = is.getItemMeta();
    if (!is.getType().equals(Material.AIR) && im != null) {
      im.setDisplayName(message);
      is.setItemMeta(im);
      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_RENAME));

    } else {
      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_RENAME_AIR));
    }
  }

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return List.of();
  }
}