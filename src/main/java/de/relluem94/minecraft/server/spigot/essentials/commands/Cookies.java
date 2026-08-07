package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("cookie")
public class Cookies implements CommandConstruct {

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

    if (!groupService.isSenderAuthorized(commandSender, "vip")) {
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

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (isCMDBlock(sender) && args.length == 1 && args[0].equals("@p")) {
      BlockCommandSender bcs = (BlockCommandSender) sender;
      CommandBlock cb = (CommandBlock) bcs.getBlock().getState();
      Player p = PlayerHelper.getTargetedPlayer(cb.getBlock().getLocation());
      if (p == null) {
        sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER,
            translationService.get(MessageKey.COMMAND_NO_PLAYER_IN_REACH)));
        return true;
      }

      getCookies(getCookie(p), p);
      return true;
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!groupService.isSenderAuthorized(p, "vip")) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      getCookies(getCookie(p), p);
      return true;
    }

    Player target = Bukkit.getPlayer(args[0]);
    if (target == null) {
      p.sendMessage(
          translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    p.sendMessage(
        translationService.get(MessageKey.COMMAND_COOKIES_PLAYER, target.getCustomName()));
    getCookies(getCookie(p), target);
    return true;
  }

  private void getCookies(ItemStack is, @NotNull Player p) {
    p.getWorld().dropItem(p.getLocation(), is);
    p.sendMessage(translationService.get(MessageKey.COMMAND_COOKIES, p.getCustomName()));
  }

  private @NotNull ItemStack getCookie(Player p) {
    ItemStack is = new ItemStack(Material.COOKIE, 1);
    ItemMeta im = is.getItemMeta();

    if (im == null) {
      return is;
    }

    im.setDisplayName(translationService.get(MessageKey.COMMAND_COOKIES_DISPLAYNAME));
    im.setLore(
        Arrays.asList(
            translationService.get(MessageKey.COMMAND_COOKIES_LORE_1,
                p.getCustomName()),
            translationService.get(MessageKey.COMMAND_COOKIES_LORE_3
            )
        )
    );
    is.setItemMeta(im);
    return is;
  }
}
